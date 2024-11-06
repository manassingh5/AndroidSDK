// Step1Fragment.java
package com.example.splash1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Step1Fragment extends Fragment {

    private CheckBox cbWhatsApp, cbSms, cbInstagram, cbFacebook, cbTwilio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step1, container, false);

        cbWhatsApp = view.findViewById(R.id.cb_whatsapp);
        cbSms = view.findViewById(R.id.cb_sms);
        cbInstagram = view.findViewById(R.id.cb_instagram);
        cbFacebook = view.findViewById(R.id.cb_facebook);
        cbTwilio = view.findViewById(R.id.cb_twilio);

        View.OnClickListener updateButtonListener = v -> {
            StepperActivity activity = (StepperActivity) getActivity();
            if (activity != null) {
                activity.updateButtons(0);
            }
        };

        cbWhatsApp.setOnClickListener(updateButtonListener);
        cbSms.setOnClickListener(updateButtonListener);
        cbInstagram.setOnClickListener(updateButtonListener);
        cbFacebook.setOnClickListener(updateButtonListener);
        cbTwilio.setOnClickListener(updateButtonListener);

        return view;
    }

    public boolean isAnyChannelSelected() {
        return cbWhatsApp.isChecked() || cbSms.isChecked() || cbInstagram.isChecked() || cbFacebook.isChecked() || cbTwilio.isChecked();
    }

    public String getSelectedChannels() {
        StringBuilder channels = new StringBuilder();
        if (cbSms.isChecked()) channels.append("1");
        if (cbTwilio.isChecked()) {
            if (channels.length() > 0) channels.append(",");
            channels.append("2");
        }
        if (cbWhatsApp.isChecked()) {
            if (channels.length() > 0) channels.append(",");
            channels.append("3");  // Changed from 2 to 3 for WhatsApp
        }
        if (cbInstagram.isChecked()) {
            if (channels.length() > 0) channels.append(",");
            channels.append("4");
        }
        if (cbFacebook.isChecked()) {
            if (channels.length() > 0) channels.append(",");
            channels.append("5");
        }
        return channels.toString();
    }
}
