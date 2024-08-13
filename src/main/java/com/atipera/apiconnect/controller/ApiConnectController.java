package com.atipera.apiconnect.controller;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.RepositoryInfo;
import com.atipera.apiconnect.service.ApiConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class ApiConnectController {

    private final ApiConnectService apiConnectService;

    @Autowired
    public ApiConnectController(ApiConnectService apiConnectService) {
        this.apiConnectService = apiConnectService;
    }

    @GetMapping("/{username}/repositories")
    public ResponseEntity<?> getRepositories(@PathVariable String username) {
        try {
            List<RepositoryInfo> repositories = apiConnectService.getRepositories(username);
            return ResponseEntity.ok(repositories);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}
