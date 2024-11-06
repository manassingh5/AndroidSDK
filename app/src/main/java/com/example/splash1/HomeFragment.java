package com.example.splash1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        TextView welcomeMessage = view.findViewById(R.id.welcome_message);
        Button btnStartStepper = view.findViewById(R.id.btn_start_stepper);

        // Initialize SharedPrefManager
        sharedPrefManager = new SharedPrefManager(requireContext());

        // Retrieve the username from SharedPrefManager
//        String username = sharedPrefManager.getUsername();
//        welcomeMessage.setText("Welcome, " + username + "!");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");
        welcomeMessage.setText("Welcome, " + username + "!");

        // Check if 'isPreferenceSet' is true or false
        boolean isPreferenceSet = sharedPrefManager.getIsPreferenceSet();

        // If 'isPreferenceSet' is true, hide the button; otherwise, show it
        if (isPreferenceSet) {
            btnStartStepper.setVisibility(View.GONE);
        } else {
            btnStartStepper.setVisibility(View.VISIBLE);
        }

        // Set the click listener for the button
        btnStartStepper.setOnClickListener(v -> openStepperActivity());
    }

    private void openStepperActivity() {
        Intent intent = new Intent(getActivity(), StepperActivity.class);
        startActivity(intent);
    }
}