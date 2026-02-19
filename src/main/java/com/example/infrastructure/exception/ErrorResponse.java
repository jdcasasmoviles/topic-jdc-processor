package com.example.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String error;
    private String message;
    private String path;
    private Integer status;
    private Instant timestamp;

    public ErrorResponse() {}

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public static ErrorResponse of(String error, String message) {
        return new ErrorResponse(error, message);
    }

    public static ErrorResponse of(String error, String message, String path, Integer status) {
        ErrorResponse response = new ErrorResponse(error, message);
        response.path = path;
        response.status = status;
        return response;
    }

    // Getters and Setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}