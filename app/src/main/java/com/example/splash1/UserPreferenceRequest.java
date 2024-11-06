
package com.example.splash1;

public class UserPreferenceRequest {
    private String channelIds;
    private int preferenceChannelIds;
    private int planId;

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public int getPreferenceChannelIds() {
        return preferenceChannelIds;
    }

    public void setPreferenceChannelIds(int preferenceChannelIds) {
        this.preferenceChannelIds = preferenceChannelIds;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }
}