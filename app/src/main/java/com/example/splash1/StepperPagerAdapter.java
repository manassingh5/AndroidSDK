// StepperPagerAdapter.java
package com.example.splash1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StepperPagerAdapter extends FragmentPagerAdapter {
    private String channels;
    private int preferences;
    private int plan;

    public StepperPagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Step1Fragment();
            case 1:
                return new Step2Fragment();
            case 2:
                return new Step3Fragment();
            case 3:
                Step4Fragment step4Fragment = new Step4Fragment();
                Bundle args = new Bundle();
                args.putString("channels", channels);
                args.putInt("preference", preferences);
                args.putInt("plan", plan);
                step4Fragment.setArguments(args);
                return step4Fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public void setPreferences(int preferences) {
        this.preferences = preferences;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public String getChannels() {
        return channels;
    }

    public int getPreferences() {
        return preferences;
    }

    public int getPlan() {
        return plan;
    }
}