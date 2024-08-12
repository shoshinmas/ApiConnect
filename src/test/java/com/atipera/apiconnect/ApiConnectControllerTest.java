package com.atipera.apiconnect;



import com.atipera.apiconnect.controller.ApiConnectController;
import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.ErrorResponse;
import com.atipera.apiconnect.model.RepositoryInfo;
import com.atipera.apiconnect.service.ApiConnectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiConnectController.class)
class ApiConnectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiConnectService gitHubService;

    @Test
    void getRepositories_existingUser_returnsRepositories() throws Exception {
        String username = "validUser";
        RepositoryInfo repository = new RepositoryInfo();
        repository.setName("Repo1");
        repository.setOwnerLogin(username);
        repository.setBranches(Collections.emptyList());

        when(gitHubService.getRepositories(username)).thenReturn(List.of(repository));

        mockMvc.perform(get("/api/github/repositories/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Repo1"))
                .andExpect(jsonPath("$[0].ownerLogin").value("validUser"));
    }

    @Test
    void getRepositories_nonExistingUser_returns404() throws Exception {
        String username = "invalidUser";

        when(gitHubService.getRepositories(username))
                .thenThrow(new NotFoundException("GitHub user '" + username + "' not found"));

        mockMvc.perform(get("/api/github/repositories/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("GitHub user 'invalidUser' not found"));
    }
}

