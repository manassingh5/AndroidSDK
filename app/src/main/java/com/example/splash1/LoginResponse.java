package com.example.splash1;

public class LoginResponse {

    private String userId;

    private String message;
    private Integer status;

    public Integer getStatusCode() {
        return status;
    }


    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
