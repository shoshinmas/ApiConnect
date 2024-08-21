package com.atipera.apiconnect;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.Owner;
import com.atipera.apiconnect.model.RepositoryInfo;
import com.atipera.apiconnect.service.GitHubService;
import com.atipera.apiconnect.service.JsonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiConnectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @MockBean
    private JsonService jsonService;

    @Test
    void getRepositories_existingUser_returnsXmlFilePath() throws Exception {
        String username = "shoshinmas";
        List<RepositoryInfo> mockRepositories = List.of(
                new RepositoryInfo("repo1", new Owner("shoshinmas"), false, "https://api.github.com/repos/shoshinmas/repo1/branches{/branch}")
        );
        when(gitHubService.getRepositories(username)).thenReturn(mockRepositories);
        when(jsonService.saveToJsonAndConvertToXml(mockRepositories)).thenReturn("/path/to/xml/file.xml");

        mockMvc.perform(get("/api/user/{username}/repositories", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.xmlFilePath").value("/path/to/xml/file.xml"));
    }

    @Test
    void getRepositories_nonExistingUser_returns404() throws Exception {
        String username = "nonExistingUser";
        when(gitHubService.getRepositories(username)).thenThrow(new NotFoundException("GitHub user '" + username + "' not found"));

        mockMvc.perform(get("/api/user/{username}/repositories", username))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("GitHub user '" + username + "' not found"));
    }

    @Test
    void getXml_existingUser_returnsXmlContent() throws Exception {
        String username = "shoshinmas";
        String mockXml = "<repositories><repository>repo1</repository></repositories>";
        when(jsonService.getXml(username)).thenReturn(mockXml);

        mockMvc.perform(get("/api/user/{username}/xml", username))
                .andExpect(status().isOk())
                .andExpect(content().xml(mockXml));
    }
}

