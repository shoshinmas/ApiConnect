package com.atipera.apiconnect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.Owner;
import com.atipera.apiconnect.model.RepositoryInfo;
import com.atipera.apiconnect.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.List;

class GitHubServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gitHubService = new GitHubService(restTemplate, "https://api.github.com/users/{username}/repos");
    }

    @Test
    void getRepositories_existingUser_returnsRepositories() {
        String username = "shoshinmas";
        RepositoryInfo[] mockRepositories = new RepositoryInfo[]{
                new RepositoryInfo("repo1", new Owner("shoshinmas"), false, "https://api.github.com/repos/shoshinmas/repo1/branches{/branch}")
        };

        when(restTemplate.getForObject("https://api.github.com/users/shoshinmas/repos", RepositoryInfo[].class))
                .thenReturn(mockRepositories);

        List<RepositoryInfo> repositories = gitHubService.getRepositories(username);

        assertNotNull(repositories);
        assertEquals(1, repositories.size());
        assertEquals("repo1", repositories.get(0).getName());
        assertFalse(repositories.get(0).isFork());
    }

    @Test
    void getRepositories_nonExistingUser_throwsNotFoundException() {
        String username = "nonExistingUser";

        when(restTemplate.getForObject("https://api.github.com/users/nonExistingUser/repos", RepositoryInfo[].class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found"));

        assertThrows(NotFoundException.class, () -> {
            gitHubService.getRepositories(username);
        });
    }
}

