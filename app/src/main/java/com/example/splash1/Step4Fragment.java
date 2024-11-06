// Step4Fragment.java
package com.example.splash1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Step4Fragment extends Fragment {

    private TextView tvConfirmationMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step4, container, false);

        tvConfirmationMessage = view.findViewById(R.id.tv_confirmation_message);

        Bundle args = getArguments();
        if (args != null) {
            String channels = args.getString("channels", "None");
            int preference = args.getInt("preference", 0);
            int plan = args.getInt("plan", 0);

            String preferenceString = getPreferenceString(preference);
            String planString = getPlanString(plan);

            tvConfirmationMessage.setText("Your preferences have been saved successfully.\n\n" +
                    "Channels: " + channels + "\nPreference: " + preferenceString + "\nPlan: " + planString);
        }

        return view;
    }

    private String getPreferenceString(int preference) {
        switch (preference) {
            case 1: return "SMS";
            case 2: return "Twilio";
            case 3: return "WhatsApp";
            case 4: return "Instagram";

            case 5: return "Facebook";
            default: return "None";
        }
    }

    private String getPlanString(int plan) {
        switch (plan) {
            case 1: return "Free";
            case 2: return "Basic";
            case 3: return "Standard";
            case 4: return "Premium";
            default: return "None";
        }
    }
}
