package com.example.splash1;

public class UserDetailResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private int genderId; // Use int for gender, "1" for male, "2" for female
    private String email;
    private String company;
    private String country;
    private String imageBase64;
    private String channelIds; // Comma-separated string of channel IDs
    private int planId;
    private int preferenceChannelIds;

    // Default constructor
    public UserDetailResponse() {}

    // Parameterized constructor
    public UserDetailResponse(String userName, String firstName, String lastName, String phone,
                              int genderId, String email, String company, String country,
                              String imageBase64, String channelIds, int planId,
                              int preferenceChannelIds) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.genderId = genderId;
        this.email = email;
        this.company = company;
        this.country = country;
        this.imageBase64 = imageBase64;
        this.channelIds = channelIds;
        this.planId = planId;
        this.preferenceChannelIds = preferenceChannelIds;
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

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelId(String channelId) {
        this.channelIds = channelId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getPreferenceChannelIds() {
        return preferenceChannelIds;
    }

    public void setPreferenceChannelId(int preferenceChannelIds) {
        this.preferenceChannelIds = preferenceChannelIds;
    }
}
