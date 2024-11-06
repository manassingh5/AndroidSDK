package com.example.splash1;

import java.util.List;

public class ScheduleMessageRequest {
    private String userId;
    private List<Contact> contacts;
    private String channelIds;
    private Integer templateId;
    private String message;
    private long scheduledTime;

    // Constructor
    public ScheduleMessageRequest(String userId, List<Contact> contacts, String channelIds, Integer templateId, String message, long scheduledTime) {
        this.userId = userId;
        this.contacts = contacts;
        this.channelIds = channelIds;
        this.templateId = templateId;
        this.message = message;
        this.scheduledTime = scheduledTime;
    }

    // Getters and Setters
    public String getUserId() {
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

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
