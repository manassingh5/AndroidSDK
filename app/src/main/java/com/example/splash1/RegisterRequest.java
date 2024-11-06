package com.example.splash1;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("UserName")
    private String userName;

    @SerializedName("Password")
    private String password;

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("Email")
    private String email;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("GenderId")
    private Integer genderId;

    @SerializedName("Company")
    private String company;

    public RegisterRequest(String userName, String password, String firstName, String lastName, String email, String phone, Integer genderId, String company) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.genderId = genderId;
        this.company = company;
    }
}
