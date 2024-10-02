package com.example.splash1;


public class UserPreferenceRequest {
    private String channelIds;
    private String preferenceChannelIds;
    private String planId;

    // Getters and Setters
    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public String getPreferenceChannelIds() {
        return preferenceChannelIds;
    }

    public void setPreferenceChannelIds(String preferenceChannelIds) {
        this.preferenceChannelIds = preferenceChannelIds;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
