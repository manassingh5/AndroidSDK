package com.example.splash1;

public class ApiResponse {
    private String message;
    private int statusCode;
    private String userId;
    private boolean isPreferenceSet; // Use camelCase for field names

    // Getters and Setters
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPreferenceSet() { // Correct method name for boolean field
        return isPreferenceSet;
    }

    public void setPreferenceSet(boolean preferenceSet) {
        this.isPreferenceSet = preferenceSet;
    }
}
