package com.atipera.apiconnect.controller;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.ErrorResponse;
import com.atipera.apiconnect.service.ApiConnectService;
import com.atipera.apiconnect.model.RepositoryInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class ApiConnectController {

    private final ApiConnectService gitHubService;

    public ApiConnectController(ApiConnectService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/repositories/{username}")
    public ResponseEntity<List<RepositoryInfo>> getRepositories(@PathVariable String username) {
        List<RepositoryInfo> repositories = gitHubService.getRepositories(username);
        return new ResponseEntity<>(repositories, HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<com.atipera.apiconnect.model.ErrorResponse> handleNotFound(NotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(404, e.getMessage()) {
            @Override
            public HttpStatusCode getStatusCode() {
                return null;
            }

            @Override
            public ProblemDetail getBody() {
                return null;
            }
        }, HttpStatus.NOT_FOUND);
    }
}
