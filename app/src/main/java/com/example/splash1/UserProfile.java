package com.example.splash1;


public class UserProfile {
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private String email;
    private String company;
    private String country;
    private String profilePicture;

    public UserProfile(String username, String firstName, String lastName, String phone, String gender,
                       String email, String company, String country, String profilePicture) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.company = company;
        this.country = country;
        this.profilePicture = profilePicture;
    }

    // Getters and setters
}