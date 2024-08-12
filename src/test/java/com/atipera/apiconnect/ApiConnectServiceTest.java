package com.atipera.apiconnect;

import com.atipera.apiconnect.service.ApiConnectService;
import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.BranchInfo;
import com.atipera.apiconnect.model.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiConnectServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiConnectService gitHubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRepositories_validUser_returnsNonForkedRepositories() {
        String username = "validUser";
        String repositoriesUrl = "https://api.github.com/users/" + username + "/repos";

        RepositoryInfo[] mockRepositories = {
                createRepository("Repo1", false, "https://api.github.com/repos/validUser/repo1/branches{/branch}"),
                createRepository("Repo2", true, "https://api.github.com/repos/validUser/repo2/branches{/branch}"),
        };

        BranchInfo[] mockBranches = {
                createBranch("main", "abc123"),
                createBranch("develop", "def456")
        };

        when(restTemplate.getForObject(repositoriesUrl, RepositoryInfo[].class))
                .thenReturn(mockRepositories);
        when(restTemplate.getForObject("https://api.github.com/repos/validUser/repo1/branches", BranchInfo[].class))
                .thenReturn(mockBranches);

        List<RepositoryInfo> repositories = gitHubService.getRepositories(username);

        assertEquals(1, repositories.size());
        assertEquals("Repo1", repositories.get(0).getName());
        assertEquals("validUser", repositories.get(0).getOwnerLogin());
        assertEquals(2, repositories.get(0).getBranches().size());
        assertEquals("main", repositories.get(0).getBranches().get(0).getName());
        assertEquals("abc123", repositories.get(0).getBranches().get(0).getLastCommitSha());
    }

    @Test
    void getRepositories_invalidUser_throwsNotFoundException() {
        String username = "invalidUser";
        String repositoriesUrl = "https://api.github.com/users/" + username + "/repos";

        when(restTemplate.getForObject(repositoriesUrl, RepositoryInfo[].class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            gitHubService.getRepositories(username);
        });

        assertEquals("GitHub user 'invalidUser' not found", exception.getMessage());
    }

    private RepositoryInfo createRepository(String name, boolean isFork, String branchesUrl) {
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setName(name);
        repositoryInfo.setFork(isFork);
        repositoryInfo.setBranchesUrl(branchesUrl);
        repositoryInfo.setOwnerLogin("validUser");
        return repositoryInfo;
    }

    private BranchInfo createBranch(String name, String sha) {
        BranchInfo branchInfo = new BranchInfo();
        BranchInfo.Commit commit = new BranchInfo.Commit();
        commit.setSha(sha);
        branchInfo.setName(name);
        branchInfo.setCommit(commit);
        return branchInfo;
    }
}

