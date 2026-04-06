package com.Zorvyn.FinanceApp.dto.response;

import java.time.LocalDateTime;

public class ErrorResponse {

    private boolean success;
    private String error;
    private String message;
    private int statusCode;
    private String path;
    private LocalDateTime timestamp;

    public ErrorResponse(){}

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public ErrorResponse(boolean success, String error, String message, int statusCode, String path,
            LocalDateTime timestamp) {
        this.success = success;
        this.error = error;
        this.message = message;
        this.statusCode = statusCode;
        this.path = path;
        this.timestamp = timestamp;
    }

}
