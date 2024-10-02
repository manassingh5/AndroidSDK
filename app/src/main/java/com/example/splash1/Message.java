package com.example.splash1;
public class Message {
    private String content;
    private String mobileNumber;

    public Message(String content, String mobileNumber) {
        this.content = content;
        this.mobileNumber = mobileNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}