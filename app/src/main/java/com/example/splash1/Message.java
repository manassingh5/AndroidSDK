package com.example.splash1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String body;
    private String fromNumber;
    private String toNumber;
    private int channelId;
    private String channelName;
    private String createdDT;

    // Constructor, getters, and setters
    public Message(String body, String fromNumber, String toNumber, int channelId, String channelName, String createdDT) {
        this.body = body;
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
        this.channelId = channelId;
        this.channelName = channelName;
        this.createdDT = createdDT;
    }

    public String getBody() {
        return body;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public int getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getCreatedDT() {
        return createdDT;
    }
}

