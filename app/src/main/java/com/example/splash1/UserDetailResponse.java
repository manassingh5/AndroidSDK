package com.example.splash1;

public class UserDetailResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private String email;
    private String company;
    private String country;
    private String imageBase64;
    private String channelId;
    private int planId;
    private int preferenceChannelId;

    // Default constructor
    public UserDetailResponse() {}

    // Parameterized constructor
    public UserDetailResponse(String userName, String firstName, String lastName, String phone,
                              String gender, String email, String company, String country,
                              String imageBase64, String channelId, int planId,
                              int preferenceChannelId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.company = company;
        this.country = country;
        this.imageBase64 = imageBase64;
        this.channelId = channelId;
        this.planId = planId;
        this.preferenceChannelId = preferenceChannelId;
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public int getPreferenceChannelId() { return preferenceChannelId; }
    public void setPreferenceChannelId(int preferenceChannelId) { this.preferenceChannelId = preferenceChannelId; }
}
