package com.Zorvyn.FinanceApp.dto.response;



public class ApiResponse {

    private String message;

    private Boolean success;

    public ApiResponse() {}

    public ApiResponse(Boolean success , String message) {
        this.message = message;
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
