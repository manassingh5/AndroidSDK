package com.example.splash1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    private static final String USERNAME_KEY = "username_key";

    public static HomeFragment newInstance(String username) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView welcomeMessage = view.findViewById(R.id.welcome_message);
        Button btnStartStepper = view.findViewById(R.id.btn_start_stepper);
        Button btnSendMessage = view.findViewById(R.id.btn_send_message);

        // Retrieve the username from arguments
        String username = getArguments() != null ? getArguments().getString(USERNAME_KEY) : "User";
        welcomeMessage.setText("Welcome, " + username + "!");

//        btnStartStepper.setOnClickListener(v -> openStepperFragment());
        btnSendMessage.setOnClickListener(v -> openSendMessageActivity());
        btnStartStepper.setOnClickListener(v -> openStepperActivity());
    }

//    private void openStepperFragment() {
//        FragmentManager fragmentManager = getParentFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        StepperFragment stepperFragment = new StepperFragment();
//        fragmentTransaction.replace(R.id.fragment_container, stepperFragment);
//        fragmentTransaction.addToBackStack(null); // Optional: adds the transaction to the back stack
//        fragmentTransaction.commit();
//    }
private void openStepperActivity() {
    Intent intent = new Intent(getActivity(), StepperActivity.class);
    startActivity(intent);
}

    private void openSendMessageActivity() {
        Intent intent = new Intent(getActivity(), SendMessageActivity.class);
        startActivity(intent);
    }
}
