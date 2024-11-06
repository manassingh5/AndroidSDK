package com.example.splash1;

import java.util.List;

public class ScheduleResponse {
    private String userId;
    private List<ScheduleDetailsResponse> schedules;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ScheduleDetailsResponse> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleDetailsResponse> schedules) {
        this.schedules = schedules;
    }
}
