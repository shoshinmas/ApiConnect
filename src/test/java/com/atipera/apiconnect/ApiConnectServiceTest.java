package com.atipera.apiconnect;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.RepositoryInfo;
import com.atipera.apiconnect.service.ApiConnectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ApiConnectServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiConnectService apiConnectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void getRepositories_invalidUser_throwsNotFoundException() {
        String username = "invalidUser";
        String repositoriesUrl = "https://api.github.com/users/" + username + "/repos";

        when(restTemplate.getForObject(repositoriesUrl, RepositoryInfo[].class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            apiConnectService.getRepositories(username);
        });

        assertEquals("GitHub user 'invalidUser' not found", exception.getMessage());
    }
}
