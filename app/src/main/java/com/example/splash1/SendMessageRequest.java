package com.example.splash1;

import java.util.List;

public class SendMessageRequest {
    private String userId;
    private List<Contact> contacts;
    private String channelIds;
    private int templateId;
    private String message;


    // Constructor
    public SendMessageRequest(String userId , List<Contact> contacts, String channelIds, int templateId, String message) {
        this.userId = userId;
        this.contacts = contacts;
        this.channelIds = channelIds;
        this.templateId = templateId;
        this.message = message;

    }

    // Getters and setters
    public String  getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
