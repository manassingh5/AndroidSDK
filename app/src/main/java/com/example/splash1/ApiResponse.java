package com.example.splash1;

public class ApiResponse {
    private String Message;
    private int StatusCode;
    private String UserId;
    private boolean isPreferenceSet; // This field should be included if your API returns it

    // Getters and Setters
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public boolean isPreferenceSet() {
        return isPreferenceSet;
    }

    public void setPreferenceSet(boolean preferenceSet) {
        isPreferenceSet = preferenceSet;
    }
}
