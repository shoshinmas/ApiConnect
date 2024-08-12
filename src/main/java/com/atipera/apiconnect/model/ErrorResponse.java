package com.atipera.apiconnect.model;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public abstract class ErrorResponse {

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public abstract HttpStatusCode getStatusCode();

    public abstract ProblemDetail getBody();
}
