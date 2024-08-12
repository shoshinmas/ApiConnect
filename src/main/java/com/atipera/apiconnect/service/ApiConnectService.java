package com.atipera.apiconnect.service;

import com.atipera.apiconnect.exception.NotFoundException;
import com.atipera.apiconnect.model.BranchInfo;
import com.atipera.apiconnect.model.RepositoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiConnectService {

    private final RestTemplate restTemplate;

    public ApiConnectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepositoryInfo> getRepositories(String username) {
        String repositoriesUrl = "https://api.github.com/users/" + username + "/repos";
        List<RepositoryInfo> repositories = new ArrayList<>();

        try {
            // Fetch the list of repositories for the given user
            RepositoryInfo[] response = restTemplate.getForObject(repositoriesUrl, RepositoryInfo[].class);

            if (response != null) {
                for (RepositoryInfo repo : response) {
                    if (!repo.isFork()) {  // Filter out forked repositories
                        String branchesUrl = repo.getBranchesUrl().replace("{/branch}", "");
                        BranchInfo[] branches = restTemplate.getForObject(branchesUrl, BranchInfo[].class);

                        if (branches != null) {
                            repo.setBranches(List.of(branches));
                        }

                        repositories.add(repo);
                    }
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("GitHub user '" + username + "' not found");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch repositories from GitHub: " + e.getMessage());
        }

        return repositories;
    }
}
