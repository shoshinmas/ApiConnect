package com.atipera.apiconnect.model;

public class BranchInfo {
    private String name;
    private Commit commit;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public String getLastCommitSha() {
        return commit.getSha();
    }

    // Inner class for Commit
    public static class Commit {
        private String sha;

        public String getSha() {
            return sha;
        }

        public void setSha(String sha) {
            this.sha = sha;
        }
    }
}
