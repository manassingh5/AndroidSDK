package com.example.splash1;

public class UserDetailRequest {

    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String company;
    private int genderId; // "1" for male, "2" for female (as an integer)
    private String channelIds; // Comma-separated string of channel IDs
    private int preferenceChannelIds; // Single integer for preference channel
    private int planId;
    private String imageBase64;
    private String userId; // Assuming this is a String, but can be Integer or Long if numeric

    // Default constructor
    public UserDetailRequest() {}

    // Parameterized constructor
    public UserDetailRequest(String userName, String firstName, String lastName, String phone,
                             String email, String company, int genderId, String channelIds,
                             int preferenceChannelIds, int planId, String imageBase64,
                             String userId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.genderId = genderId;
        this.channelIds = channelIds;
        this.preferenceChannelIds = preferenceChannelIds;
        this.planId = planId;
        this.imageBase64 = imageBase64;
        this.userId = userId;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

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

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
