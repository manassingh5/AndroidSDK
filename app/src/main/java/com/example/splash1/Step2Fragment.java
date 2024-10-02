package com.example.splash1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Step2Fragment extends Fragment {

    private CheckBox cbWhatsAppPref, cbSmsPref, cbInstagramPref, cbFacebookPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step2, container, false);

        cbWhatsAppPref = view.findViewById(R.id.cb_whatsapp_pref);
        cbSmsPref = view.findViewById(R.id.cb_sms_pref);
        cbInstagramPref = view.findViewById(R.id.cb_instagram_pref);
        cbFacebookPref = view.findViewById(R.id.cb_facebook_pref);


        View.OnClickListener updateButtonListener = v -> {
            StepperActivity activity = (StepperActivity) getActivity();
            if (activity != null) {
                activity.updateButtons(1);
            }
        };

        cbWhatsAppPref.setOnClickListener(updateButtonListener);
        cbSmsPref.setOnClickListener(updateButtonListener);
        cbInstagramPref.setOnClickListener(updateButtonListener);
        cbFacebookPref.setOnClickListener(updateButtonListener);

        return view;
    }

    public boolean isAnyPreferenceSelected() {
        return cbWhatsAppPref.isChecked() || cbSmsPref.isChecked() || cbInstagramPref.isChecked() || cbFacebookPref.isChecked();
    }

    public String getSelectedPreferences() {
        StringBuilder preferences = new StringBuilder();
        if (cbWhatsAppPref.isChecked()) preferences.append("WhatsApp ");
        if (cbSmsPref.isChecked()) preferences.append("SMS ");
        if (cbInstagramPref.isChecked()) preferences.append("Instagram ");
        if (cbFacebookPref.isChecked()) preferences.append("Facebook ");
        return preferences.toString().trim();
    }
}
