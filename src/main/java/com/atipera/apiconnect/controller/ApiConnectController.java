package com.atipera.apiconnect.controller;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.RepositoryInfo;
import com.atipera.apiconnect.service.GitHubService;
import com.atipera.apiconnect.service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class ApiConnectController {

    private final GitHubService gitHubService;
    private final JsonService jsonService;

    @Autowired
    public ApiConnectController(GitHubService gitHubService, JsonService jsonService) {
        this.gitHubService = gitHubService;
        this.jsonService = jsonService;
    }

    @GetMapping("/user/{username}/repositories")
    public ResponseEntity<?> getRepositories(@PathVariable String username) {
        try {
            List<RepositoryInfo> repositories = gitHubService.getRepositories(username);
            String xmlPath = jsonService.saveToJsonAndConvertToXml(repositories);
            return ResponseEntity.ok(Map.of("xmlFilePath", xmlPath));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", 500, "message", "An error occurred"));
        }
    }

    @GetMapping("/user/{username}/xml")
    public ResponseEntity<?> getXml(@PathVariable String username) {
        try {
            String xmlContent = jsonService.getXml(username);
            return ResponseEntity.ok(xmlContent);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}

