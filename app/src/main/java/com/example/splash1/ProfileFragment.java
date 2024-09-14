package com.example.splash1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfilePicture;
    private EditText etUsername, etFirstName, etLastName, etPhone, etGender, etEmail, etCompany, etCountry;
    private Button btnSaveProfile, btnChangePicture;
    private Bitmap bitmap;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        etUsername = view.findViewById(R.id.etUsername);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhone = view.findViewById(R.id.etPhone);
        etGender = view.findViewById(R.id.etGender);
        etEmail = view.findViewById(R.id.etEmail);
        etCompany = view.findViewById(R.id.etCompany);
        etCountry = view.findViewById(R.id.etCountry);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnChangePicture = view.findViewById(R.id.btnChangePicture);

        // Initialize Retrofit service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnChangePicture.setOnClickListener(v -> openFileChooser());
        btnSaveProfile.setOnClickListener(v -> saveUserProfile());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ivProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserProfile() {
        String username = etUsername.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String company = etCompany.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(company) || TextUtils.isEmpty(country)) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert image to base64 string
        String imageBase64 = "";
        if (bitmap != null) {
            imageBase64 = encodeImageToBase64(bitmap);
        }

        // Create a user profile object
        UserProfile userProfile = new UserProfile(username, firstName, lastName, phone, gender, email, company, country, imageBase64);

        // Call the API to save the profile
        Call<UserResponse> call = apiService.updateUserProfile(userProfile);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}