package com.example.splash1;

import java.util.List;

public class ScheduledMessage {

    private String channelIds;
    private int templateId;
    private String message;
    private String startDT;  // Scheduled Date-Time in String format
    private List<Contact> contacts;

    // Constructor
    public ScheduledMessage(List<Contact> contacts, String channelIds, int templateId, String message, String startDT) {
        this.channelIds = channelIds;
        this.templateId = templateId;
        this.message = message;
        this.startDT = startDT;
        this.contacts = contacts;
    }

    // Getters and setters
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

    public String getStartDT() {
        return startDT;
    }

    public void setStartDT(String startDT) {
        this.startDT = startDT;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "ScheduledMessage{" +
                "channelIds='" + channelIds + '\'' +
                ", templateId=" + templateId +
                ", message='" + message + '\'' +
                ", startDT='" + startDT + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
