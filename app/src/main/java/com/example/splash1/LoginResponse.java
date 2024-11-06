package com.example.splash1;

public class LoginResponse {
    private String userId;
    private String token;
    private String message;
    private String username;
    private boolean isPreferenceSet;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPreferenceSet() {
        return isPreferenceSet;
    }

    public void setPreferenceSet(boolean preferenceSet) {
        this.isPreferenceSet = preferenceSet;
    }
}