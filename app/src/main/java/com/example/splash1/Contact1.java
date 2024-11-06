package com.example.splash1;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact1 implements Parcelable {
    private String phoneNumber;

    // Constructor
    public Contact1(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setter
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Parcelable implementation
    protected Contact1(Parcel in) {
        phoneNumber = in.readString();
    }

    public static final Creator<Contact1> CREATOR = new Creator<Contact1>() {
        @Override
        public Contact1 createFromParcel(Parcel in) {
            return new Contact1(in);
        }

        @Override
        public Contact1[] newArray(int size) {
            return new Contact1[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phoneNumber);
    }
}
