package com.example.splash1;

public class UpdateMessageCountRequest {
    private String userId; // or whatever identifier you use
    private int messageCount;

    public UpdateMessageCountRequest(String userId, int messageCount) {
        this.userId = userId;
        this.messageCount = messageCount;
    }

    // Getters and setters (if needed)
}

