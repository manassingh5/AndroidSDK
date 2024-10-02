package com.example.splash1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Step4Fragment extends Fragment {

    private TextView tvConfirmationMessage;
    private Button btnGoToChats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step4, container, false);

        tvConfirmationMessage = view.findViewById(R.id.tv_confirmation_message);
        btnGoToChats = view.findViewById(R.id.btn_go_to_chats);

        Bundle args = getArguments();
        if (args != null) {
            String channels = args.getString("channels", "None");
            String preferences = args.getString("preferences", "None");
            String plan = args.getString("plan", "None");
            tvConfirmationMessage.setText("Your preferences have been saved successfully.\n\nChannels: " + channels + "\nPreferences: " + preferences + "\nPlan: " + plan);
        }

        btnGoToChats.setOnClickListener(v -> {
            // Handle the "Go to My Chats" button click
        });

        return view;
    }
}
