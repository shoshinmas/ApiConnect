package com.atipera.apiconnect.service;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.RepositoryInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    private final String githubApiUrl;

    @Autowired
    public GitHubService(RestTemplate restTemplate, @Value("${github.api.url}") String githubApiUrl) {
        this.restTemplate = restTemplate;
        this.githubApiUrl = githubApiUrl;
    }

    public List<RepositoryInfo> getRepositories(String username) {
        String repositoriesUrl = githubApiUrl.replace("{username}", username);
        try {
            RepositoryInfo[] repositories = restTemplate.getForObject(repositoriesUrl, RepositoryInfo[].class);

            if (repositories == null || repositories.length == 0) {
                throw new NotFoundException("GitHub user '" + username + "' not found");
            }

            return Arrays.stream(repositories)
                    .filter(repo -> !repo.isFork())
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("GitHub user '" + username + "' not found");
            } else {
                throw new RuntimeException("Failed to fetch repositories from GitHub", e);
            }
        }
    }
}
