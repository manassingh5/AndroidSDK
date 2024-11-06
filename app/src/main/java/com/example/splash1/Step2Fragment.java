// Step2Fragment.java
package com.example.splash1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Step2Fragment extends Fragment {

    private RadioGroup rgPreferences;
    private RadioButton rbWhatsAppPref, rbSmsPref, rbInstagramPref, rbFacebookPref, rbTwilioPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step2, container, false);

        rgPreferences = view.findViewById(R.id.rg_preferences);
        rbWhatsAppPref = view.findViewById(R.id.rb_whatsapp_pref);
        rbSmsPref = view.findViewById(R.id.rb_sms_pref);
        rbInstagramPref = view.findViewById(R.id.rb_instagram_pref);
        rbFacebookPref = view.findViewById(R.id.rb_facebook_pref);
        rbTwilioPref = view.findViewById(R.id.rb_twilio_pref);

        rgPreferences.setOnCheckedChangeListener((group, checkedId) -> {
            StepperActivity activity = (StepperActivity) getActivity();
            if (activity != null) {
                activity.updateButtons(1);
            }
        });

        return view;
    }

    public int getSelectedPreference() {
        int selectedId = rgPreferences.getCheckedRadioButtonId();

        if (selectedId == R.id.rb_sms_pref) {
            return 1;
        } else if (selectedId == R.id.rb_twilio_pref) {
            return 2;
        } else if (selectedId == R.id.rb_whatsapp_pref) {
            return 3;
        } else if (selectedId == R.id.rb_instagram_pref) {
            return 4;
        } else if (selectedId == R.id.rb_facebook_pref) {
            return 5;
        } else {
            return -1;
        }
    }
}