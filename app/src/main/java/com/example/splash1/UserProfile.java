package com.example.splash1;
public class UserProfile {
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String company;
    private String gender;
    private int planId;
    private int preferenceChannelId;
    private String imageBase64;

    // Parameterized constructor
    public UserProfile(String username, String firstName, String lastName, String phone,
                       String email, String company, String gender, int planId,
                       int preferenceChannelId, String imageBase64) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.gender = gender;
        this.planId = planId;
        this.preferenceChannelId = preferenceChannelId;
        this.imageBase64 = imageBase64;
    }

    // Default constructor
    public UserProfile() {}

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getPreferenceChannelId() {
        return preferenceChannelId;
    }

    public void setPreferenceChannelId(int preferenceChannelId) {
        this.preferenceChannelId = preferenceChannelId;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
