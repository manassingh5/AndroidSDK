package com.example.splash1;

import java.util.List;
public class ScheduleDetailsResponse {
    private String scheduleId;
    private boolean isActive;
    private int contactCount;
    private String channelIds;
    private int templateId;
    private String message;
    private String startDT;
    private String createdDT;
    private String updatedDT;

    // Getters and Setters
    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getContactCount() {
        return contactCount;
    }

    public void setContactCount(int contactCount) {
        this.contactCount = contactCount;
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

    public String getStartDT() {
        return startDT;
    }

    public void setStartDT(String startDT) {
        this.startDT = startDT;
    }

    public String getCreatedDT() {
        return createdDT;
    }

    public void setCreatedDT(String createdDT) {
        this.createdDT = createdDT;
    }

    public String getUpdatedDT() {
        return updatedDT;
    }

    public void setUpdatedDT(String updatedDT) {
        this.updatedDT = updatedDT;
    }
}
