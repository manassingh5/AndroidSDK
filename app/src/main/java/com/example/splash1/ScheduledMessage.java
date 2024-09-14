package com.example.splash1;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class ScheduledMessage implements Serializable {
    private List<Object> channels;
    private List<Object> templates;
    private int templateID;
    private Calendar scheduledTime;
    private String message;
    private boolean isOn;

    // Constructor
    public ScheduledMessage(List<Object> channels, List<Object> templates, int templateID, Calendar scheduledTime, String message, boolean isOn) {
        this.channels = channels;
        this.templates = templates;
        this.templateID = templateID;
        this.scheduledTime = scheduledTime;
        this.message = message;
        this.isOn = isOn;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Getter for isOn
    public boolean isOn() {
        return isOn;
    }
}
