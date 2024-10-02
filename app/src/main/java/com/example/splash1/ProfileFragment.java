package com.example.splash1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
    private EditText etChannelId, etPlanId, etPreferenceChannelId;
    private Button btnSaveProfile, btnChangePicture, btnEditProfile;
    private Bitmap bitmap;
    private String userId;

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
        etChannelId = view.findViewById(R.id.etChannelId);
        etPlanId = view.findViewById(R.id.etPlanId);
        etPreferenceChannelId = view.findViewById(R.id.etPreferenceChannelId);

        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnChangePicture = view.findViewById(R.id.btnChangePicture);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnChangePicture.setOnClickListener(v -> openFileChooser());
        btnSaveProfile.setOnClickListener(v -> saveUserProfile());
        btnEditProfile.setOnClickListener(v -> toggleEditMode());

        userId = "3e3f978b-ef9f-4f8a-b313-7081daf7d0f1"; // Replace with actual user ID
        fetchUserDetails(userId);

        return view;
    }

    private void toggleEditMode() {
        boolean isEditing = btnEditProfile.getText().toString().equals("Edit");

        etUsername.setEnabled(isEditing);
        etFirstName.setEnabled(isEditing);
        etLastName.setEnabled(isEditing);
        etPhone.setEnabled(isEditing);
        etGender.setEnabled(isEditing);
        etEmail.setEnabled(isEditing);
        etCompany.setEnabled(isEditing);
        etCountry.setEnabled(isEditing);
        etChannelId.setEnabled(isEditing);
        etPlanId.setEnabled(isEditing);
        etPreferenceChannelId.setEnabled(isEditing);

        if (isEditing) {
            btnEditProfile.setText("Cancel");
            btnSaveProfile.setVisibility(View.VISIBLE);
        } else {
            btnEditProfile.setText("Edit");
            btnSaveProfile.setVisibility(View.GONE);
        }
    }

    private void saveUserProfile() {
        UserProfile userProfile = new UserProfile();

        if (!TextUtils.isEmpty(etUsername.getText())) userProfile.setUserName(etUsername.getText().toString());
        if (!TextUtils.isEmpty(etFirstName.getText())) userProfile.setFirstName(etFirstName.getText().toString());
        if (!TextUtils.isEmpty(etLastName.getText())) userProfile.setLastName(etLastName.getText().toString());
        if (!TextUtils.isEmpty(etPhone.getText())) userProfile.setPhone(etPhone.getText().toString());
        if (!TextUtils.isEmpty(etEmail.getText())) userProfile.setEmail(etEmail.getText().toString());
        if (!TextUtils.isEmpty(etCompany.getText())) userProfile.setCompany(etCompany.getText().toString());
        if (!TextUtils.isEmpty(etChannelId.getText())) userProfile.setChannelIds(etChannelId.getText().toString());
        if (!TextUtils.isEmpty(etPlanId.getText())) userProfile.setPlanId(Integer.parseInt(etPlanId.getText().toString()));
        if (!TextUtils.isEmpty(etPreferenceChannelId.getText())) userProfile.setPreferenceChannelIds(Integer.parseInt(etPreferenceChannelId.getText().toString()));

        if (bitmap != null) {
            String imageBase64 = encodeImageToBase64(bitmap);
            userProfile.setImageBase64(imageBase64);
        }

        userProfile.setUserId(userId);

        Call<UserResponse> call = apiService.updateUserProfile(userId, userProfile);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    toggleEditMode(); // Exit edit mode after saving
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("ProfileFragment", "Failure: " + t.getMessage());
                Toast.makeText(getActivity(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleErrorResponse(Response<UserResponse> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("ProfileFragment", "Error: " + errorBody);
            Toast.makeText(getActivity(), "Failed to update profile. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("ProfileFragment", "Error parsing error response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fetchUserDetails(String userId) {
        Call<UserDetailResponse> call = apiService.getUserDetailById(userId);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (response.isSuccessful()) {
                    UserDetailResponse userDetails = response.body();
                    populateUserDetails(userDetails);
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUserDetails(UserDetailResponse userDetails) {
        etUsername.setText(userDetails.getUserName());
        etFirstName.setText(userDetails.getFirstName());
        etLastName.setText(userDetails.getLastName());
        etPhone.setText(userDetails.getPhone());
        etGender.setText(userDetails.getGender());
        etEmail.setText(userDetails.getEmail());
        etCompany.setText(userDetails.getCompany());
        etCountry.setText(userDetails.getCountry());
        etChannelId.setText(userDetails.getChannelId());
        etPlanId.setText(String.valueOf(userDetails.getPlanId()));
        etPreferenceChannelId.setText(String.valueOf(userDetails.getPreferenceChannelId()));

        if (userDetails.getImageBase64() != null && !userDetails.getImageBase64().isEmpty()) {
            byte[] decodedString = Base64.decode(userDetails.getImageBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivProfilePicture.setImageBitmap(decodedByte);
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                Log.e("ProfileFragment", "Error loading image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}


//package com.example.splash1;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ProfileFragment extends Fragment {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    private ImageView ivProfilePicture;
//    private EditText etUsername, etFirstName, etLastName, etPhone, etGender, etEmail, etCompany, etCountry;
//    private EditText etChannelId, etPlanId, etPreferenceChannelId; // New fields
//    private Button btnSaveProfile, btnChangePicture, btnEditProfile;
//    private Bitmap bitmap;
//    private String userId; // User ID for fetching and updating profile
//
//    private ApiService apiService;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
//        etUsername = view.findViewById(R.id.etUsername);
//        etFirstName = view.findViewById(R.id.etFirstName);
//        etLastName = view.findViewById(R.id.etLastName);
//        etPhone = view.findViewById(R.id.etPhone);
//        etGender = view.findViewById(R.id.etGender);
//        etEmail = view.findViewById(R.id.etEmail);
//        etCompany = view.findViewById(R.id.etCompany);
//        etCountry = view.findViewById(R.id.etCountry);
//        etChannelId = view.findViewById(R.id.etChannelId); // Initialize new fields
//        etPlanId = view.findViewById(R.id.etPlanId); // Initialize new fields
//        etPreferenceChannelId = view.findViewById(R.id.etPreferenceChannelId); // Initialize new fields
//
//        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
//        btnChangePicture = view.findViewById(R.id.btnChangePicture);
//        btnEditProfile = view.findViewById(R.id.btnEditProfile);
//
//        // Initialize Retrofit service
//        apiService = RetrofitClient.getClient().create(ApiService.class);
//
//        // Retrieve user ID from shared preferences
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        userId = sharedPreferences.getString("userId", null);
//
//        if (userId != null) {
//            btnChangePicture.setOnClickListener(v -> openFileChooser());
//            btnSaveProfile.setOnClickListener(v -> saveUserProfile());
//            btnEditProfile.setOnClickListener(v -> toggleEditMode());
//
//            fetchUserDetails(userId);
//        } else {
//            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
//        }
//
//        return view;
//    }
//
//    private void toggleEditMode() {
//        boolean isEditing = btnEditProfile.getText().toString().equals("Edit");
//
//        etUsername.setEnabled(isEditing);
//        etFirstName.setEnabled(isEditing);
//        etLastName.setEnabled(isEditing);
//        etPhone.setEnabled(isEditing);
//        etGender.setEnabled(isEditing);
//        etEmail.setEnabled(isEditing);
//        etCompany.setEnabled(isEditing);
//        etCountry.setEnabled(isEditing);
//        etChannelId.setEnabled(isEditing); // Toggle new fields
//        etPlanId.setEnabled(isEditing); // Toggle new fields
//        etPreferenceChannelId.setEnabled(isEditing); // Toggle new fields
//
//        if (isEditing) {
//            btnEditProfile.setText("Cancel");
//            btnSaveProfile.setVisibility(View.VISIBLE);
//        } else {
//            btnEditProfile.setText("Edit");
//            btnSaveProfile.setVisibility(View.GONE);
//        }
//    }
//
//    private void saveUserProfile() {
//        if (userId == null) {
//            Toast.makeText(getActivity(), "User ID not found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        UserProfile userProfile = new UserProfile();
//
//        // Set only non-empty fields
//        if (!TextUtils.isEmpty(etUsername.getText())) userProfile.setUsername(etUsername.getText().toString());
//        if (!TextUtils.isEmpty(etFirstName.getText())) userProfile.setFirstName(etFirstName.getText().toString());
//        if (!TextUtils.isEmpty(etLastName.getText())) userProfile.setLastName(etLastName.getText().toString());
//        if (!TextUtils.isEmpty(etPhone.getText())) userProfile.setPhone(etPhone.getText().toString());
//        if (!TextUtils.isEmpty(etGender.getText())) userProfile.setGender(etGender.getText().toString());
//        if (!TextUtils.isEmpty(etEmail.getText())) userProfile.setEmail(etEmail.getText().toString());
//        if (!TextUtils.isEmpty(etCompany.getText())) userProfile.setCompany(etCompany.getText().toString());
//        if (!TextUtils.isEmpty(etCountry.getText())) userProfile.setCountry(etCountry.getText().toString());
//        if (!TextUtils.isEmpty(etChannelId.getText())) userProfile.setChannelId(etChannelId.getText().toString());
//        if (!TextUtils.isEmpty(etPlanId.getText())) userProfile.setPlanId(etPlanId.getText().toString());
//        if (!TextUtils.isEmpty(etPreferenceChannelId.getText())) userProfile.setPreferenceChannelId(etPreferenceChannelId.getText().toString());
//
//        if (bitmap != null) {
//            String imageBase64 = encodeImageToBase64(bitmap);
//            userProfile.setImageBase64(imageBase64);
//        }
//
//        // Set userId in profile
//        userProfile.setUserId(userId);
//
//        Call<UserResponse> call = apiService.updateUserProfile(userId, userProfile); // Correct API call
//        call.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                    toggleEditMode(); // Exit edit mode after saving
//                } else {
//                    handleErrorResponse(response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                Log.e("ProfileFragment", "Failure: " + t.getMessage());
//                Toast.makeText(getActivity(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void handleErrorResponse(Response<UserResponse> response) {
//        try {
//            String errorBody = response.errorBody().string();
//            Log.e("ProfileFragment", "Error: " + errorBody);
//            Toast.makeText(getActivity(), "Failed to update profile. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            Log.e("ProfileFragment", "Error parsing error response: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void fetchUserDetails(String userId) {
//        Call<UserDetailResponse> call = apiService.getUserDetailById(userId);
//        call.enqueue(new Callback<UserDetailResponse>() {
//            @Override
//            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
//                if (response.isSuccessful()) {
//                    UserDetailResponse userDetails = response.body();
//                    populateUserDetails(userDetails);
//                } else {
//                    Toast.makeText(getActivity(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void populateUserDetails(UserDetailResponse userDetails) {
//        etUsername.setText(userDetails.getUserName());
//        etFirstName.setText(userDetails.getFirstName());
//        etLastName.setText(userDetails.getLastName());
//        etPhone.setText(userDetails.getPhone());
//        etGender.setText(userDetails.getGender());
//        etEmail.setText(userDetails.getEmail());
//        etCompany.setText(userDetails.getCompany());
//        etCountry.setText(userDetails.getCountry());
//        etChannelId.setText(userDetails.getChannelId()); // Populate new fields
//        etPlanId.setText(userDetails.getPlanId()); // Populate new fields
//        etPreferenceChannelId.setText(userDetails.getPreferenceChannelId()); // Populate new fields
//
//        // Decode base64 image string if available
//        if (userDetails.getImageBase64() != null && !userDetails.getImageBase64().isEmpty()) {
//            byte[] decodedString = Base64.decode(userDetails.getImageBase64(), Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            ivProfilePicture.setImageBitmap(decodedByte);
//        }
//    }
//
//    private String encodeImageToBase64(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
//
//    private void openFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//                ivProfilePicture.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                Log.e("ProfileFragment", "Error loading image: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
