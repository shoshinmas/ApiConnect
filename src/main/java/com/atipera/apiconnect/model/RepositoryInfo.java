package com.atipera.apiconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryInfo {

    private String name;
    private Owner owner;
    private boolean fork;
    private String branchesUrl;
    private List<BranchInfo> branches;
    private String username;

    public RepositoryInfo(String name, Owner owner, boolean fork, String branchesUrl) {
        this.name = name;
        this.owner = owner;
        this.fork = fork;
        this.branchesUrl = branchesUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public String getBranchesUrl() {
        return branchesUrl;
    }

    public void setBranchesUrl(String branchesUrl) {
        this.branchesUrl = branchesUrl;
    }

    public List<BranchInfo> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchInfo> branches) {
        this.branches = branches;
    }

    public void setOwnerLogin(String username) {
        this.username = username;
    }
}
