package com.example.splash1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LogoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void logoutUser() {
        // Ensure the activity context is available
        if (getActivity() == null) return;

        // Clear user session data
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());
       // Ensure this method exists

        // Show a Toast message
        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to the login screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish(); // Finish the current activity
    }
}
