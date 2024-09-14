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

    private CheckBox cbWhatsApp, cbSms, cbInstagram, cbFacebook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step1, container, false);

        cbWhatsApp = view.findViewById(R.id.cb_whatsapp);
        cbSms = view.findViewById(R.id.cb_sms);
        cbInstagram = view.findViewById(R.id.cb_instagram);
        cbFacebook = view.findViewById(R.id.cb_facebook);


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

        return view;
    }

    public boolean isAnyChannelSelected() {
        return cbWhatsApp.isChecked() || cbSms.isChecked() || cbInstagram.isChecked() || cbFacebook.isChecked();
    }

    public String getSelectedChannels() {
        StringBuilder channels = new StringBuilder();
        if (cbWhatsApp.isChecked()) channels.append("WhatsApp ");
        if (cbSms.isChecked()) channels.append("SMS ");
        if (cbInstagram.isChecked()) channels.append("Instagram ");
        if (cbFacebook.isChecked()) channels.append("Facebook ");
        return channels.toString().trim();
    }
}
