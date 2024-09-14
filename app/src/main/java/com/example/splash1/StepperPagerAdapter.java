package com.example.splash1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StepperPagerAdapter extends FragmentPagerAdapter {

    private String channels = "";
    private String preferences = "";
    private String plan = "";

    public StepperPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Step1Fragment();
        } else if (position == 1) {
            return new Step2Fragment();
        } else if (position == 2) {
            return new Step3Fragment();
        } else {
            Step4Fragment step4Fragment = new Step4Fragment();
            Bundle args = new Bundle();
            args.putString("channels", channels);
            args.putString("preferences", preferences);
            args.putString("plan", plan);
            step4Fragment.setArguments(args);
            return step4Fragment;
        }
    }

    @Override
    public int getCount() {
        return 4; // Number of steps
    }

    public void setChannels(String channels) {
        this.channels = channels != null ? channels : "";
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences != null ? preferences : "";
    }

    public void setPlan(String plan) {
        this.plan = plan != null ? plan : "";
    }

    public String[] getChannels() {
        return channels.split(","); // Convert comma-separated string to array
    }

    public String[] getPreferences() {
        return preferences.split(","); // Convert comma-separated string to array
    }

    public String getPlan() {
        return plan;
    }
}
