//package com.example.splash1;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import java.io.ByteArrayOutputStream;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ProfileFragment extends Fragment {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private static final String TAG = "ProfileFragment";
//
//    private ImageView ivProfilePicture;
//    private EditText etUsername, etFirstName, etLastName, etPhone, etEmail, etCompany;
//    private RadioGroup radioGroupGender, radioGroupPlan, radioGroupPreferenceChannel;
//    private RadioButton radioMale, radioFemale, radioFree, radioBasic, radioStandard, radioPremium;
//    private RadioButton radioPreferenceChannelId1, radioPreferenceChannelId2, radioPreferenceChannelId3, radioPreferenceChannelId4, radioPreferenceChannelId5;
//    private CheckBox checkboxChannelId1, checkboxChannelId2, checkboxChannelId3, checkboxChannelId4, checkboxChannelId5;
//    private Button btnSaveProfile, btnChangePicture, btnEditProfile;
//
//    private Bitmap bitmap;
//    private SharedPrefManager sharedPrefManager;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        initializeViews(view);
//        sharedPrefManager = new SharedPrefManager(getActivity());
//
//        btnChangePicture.setOnClickListener(v -> openImagePicker());
//        btnEditProfile.setOnClickListener(v -> enableEditing(true));
//        btnSaveProfile.setOnClickListener(v -> saveProfile());
//
//        loadProfile();
//        return view;
//    }
//
//    private void initializeViews(View view) {
//        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
//        etUsername = view.findViewById(R.id.etUsername);
//        etFirstName = view.findViewById(R.id.etFirstName);
//        etLastName = view.findViewById(R.id.etLastName);
//        etPhone = view.findViewById(R.id.etPhone);
//        etEmail = view.findViewById(R.id.etEmail);
//        etCompany = view.findViewById(R.id.etCompany);
//
//        radioGroupGender = view.findViewById(R.id.radioGroupGender);
//        radioMale = view.findViewById(R.id.radioMale);
//        radioFemale = view.findViewById(R.id.radioFemale);
//
//        radioGroupPlan = view.findViewById(R.id.radioGroupPlan);
//        radioFree = view.findViewById(R.id.radioFree);
//        radioBasic = view.findViewById(R.id.radioBasic);
//        radioStandard = view.findViewById(R.id.radioStandard);
//        radioPremium = view.findViewById(R.id.radioPremium);
//
//        radioGroupPreferenceChannel = view.findViewById(R.id.radioGroupPreferenceChannel);
//        radioPreferenceChannelId1 = view.findViewById(R.id.radioPreferenceChannelId1);  // SMS
//        radioPreferenceChannelId2 = view.findViewById(R.id.radioPreferenceChannelId2);  // Twilio
//        radioPreferenceChannelId3 = view.findViewById(R.id.radioPreferenceChannelId3);  // WhatsApp
//        radioPreferenceChannelId4 = view.findViewById(R.id.radioPreferenceChannelId4);  // Instagram
//        radioPreferenceChannelId5 = view.findViewById(R.id.radioPreferenceChannelId5);  // Facebook
//        // Instagram
//
//        checkboxChannelId1 = view.findViewById(R.id.checkboxChannelId1); // SMS
//        checkboxChannelId2 = view.findViewById(R.id.checkboxChannelId2); // Twilio
//        checkboxChannelId3 = view.findViewById(R.id.checkboxChannelId3); // WhatsApp
//        checkboxChannelId4 = view.findViewById(R.id.checkboxChannelId4); // Facebook
//        checkboxChannelId5 = view.findViewById(R.id.checkboxTwilio); // Instagram
//
//        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
//        btnChangePicture = view.findViewById(R.id.btnChangePicture);
//        btnEditProfile = view.findViewById(R.id.btnEditProfile);
//    }
//
//    private String getUserId() {
//        String userId = sharedPrefManager.getUserId();
//        if (userId == null) {
//            Toast.makeText(getActivity(), "User ID not found. Please log in again.", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//            getActivity().finish();
//        }
//        Log.d(TAG, "Retrieved User ID: " + userId);
//        return userId;
//    }
//
//    private void loadProfile() {
//        String userId = getUserId();
//        if (userId == null) return;
//
//        Call<UserDetailResponse> call = RetrofitClient.getClient().create(ApiService.class).getUserDetailById(userId);
//        call.enqueue(new Callback<UserDetailResponse>() {
//            @Override
//            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    populateProfileFields(response.body());
//                } else {
//                    Toast.makeText(getActivity(), "Failed to load profile", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Response error: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), "Network failure", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Network error: ", t);
//            }
//        });
//    }
//
//    private void populateProfileFields(UserDetailResponse profile) {
//        etUsername.setText(profile.getUserName());
//        etFirstName.setText(profile.getFirstName());
//        etLastName.setText(profile.getLastName());
//        etPhone.setText(profile.getPhone());
//        etEmail.setText(profile.getEmail());
//        etCompany.setText(profile.getCompany());
//
//        // Set gender radio button
//        if (profile.getGenderId() == 1) {
//            radioMale.setChecked(true);
//        } else if (profile.getGenderId() == 2) {
//            radioFemale.setChecked(true);
//        }
//
//        setPlanRadioButton(profile.getPlanId());
//        setChannelCheckBoxes(profile.getChannelIds());
//        setPreferenceChannelRadioButton(profile.getPreferenceChannelIds());
//        loadProfilePicture(profile.getImageBase64());
//    }
//
//    private void setPlanRadioButton(int planId) {
//        switch (planId) {
//            case 1: radioFree.setChecked(true); break;
//            case 2: radioBasic.setChecked(true); break;
//            case 3: radioStandard.setChecked(true); break;
//            case 4: radioPremium.setChecked(true); break;
//        }
//    }
//
//    private void setChannelCheckBoxes(String channelIds) {
//        if (channelIds != null) {
//            String[] ids = channelIds.split(",");
//            for (String id : ids) {
//                switch (id) {
//                    case "1": checkboxChannelId1.setChecked(true); break; // SMS
//                    case "2": checkboxChannelId2.setChecked(true); break; // Twilio
//                    case "3": checkboxChannelId3.setChecked(true); break; // WhatsApp
//                    case "4": checkboxChannelId4.setChecked(true); break; // Facebook
//                    case "5": checkboxChannelId5.setChecked(true); break; // Instagram
//                }
//            }
//        }
//    }
//
//    private void setPreferenceChannelRadioButton(int preferenceChannelId) {
//        switch (preferenceChannelId) {
//            case 1: radioPreferenceChannelId1.setChecked(true); break;  // SMS
//            case 2: radioPreferenceChannelId2.setChecked(true); break;  // Twilio
//            case 3: radioPreferenceChannelId3.setChecked(true); break;  // WhatsApp
//            case 4: radioPreferenceChannelId4.setChecked(true); break;  // Facebook
//            case 5: radioPreferenceChannelId5.setChecked(true); break;  // Instagram
//        }
//    }
//
//    private void loadProfilePicture(String imageBase64) {
//        if (imageBase64 != null && !imageBase64.isEmpty()) {
//            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
//            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            ivProfilePicture.setImageBitmap(bitmap);
//        }
//    }
//
//    private void openImagePicker() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
//            try {
//                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
//                ivProfilePicture.setImageBitmap(bitmap);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void enableEditing(boolean enable) {
//        etUsername.setEnabled(enable);
//        etFirstName.setEnabled(enable);
//        etLastName.setEnabled(enable);
//        etPhone.setEnabled(enable);
//        etEmail.setEnabled(enable);
//        etCompany.setEnabled(enable);
//
//        radioGroupGender.setEnabled(enable);
//        radioGroupPlan.setEnabled(enable);
//        radioGroupPreferenceChannel.setEnabled(enable);
//
//        checkboxChannelId1.setEnabled(enable);
//        checkboxChannelId2.setEnabled(enable);
//        checkboxChannelId3.setEnabled(enable);
//        checkboxChannelId4.setEnabled(enable);
//        checkboxChannelId5.setEnabled(enable);
//
//        btnSaveProfile.setVisibility(enable ? View.VISIBLE : View.GONE);
//    }
//
//    private void saveProfile() {
//        if (validateInputs()) {
//            UserDetailRequest userDetailRequest = new UserDetailRequest();
//            populateUserDetailRequest(userDetailRequest);
//            updateProfile(userDetailRequest);
//        }
//    }
//
//    private boolean validateInputs() {
//        if (etUsername.getText().toString().isEmpty()) {
//            etUsername.setError("Username is required");
//            return false;
//        }
//        // Add more validations as needed
//        return true;
//    }
//
//    private void populateUserDetailRequest(UserDetailRequest request) {
//        request.setUserName(etUsername.getText().toString());
//        request.setFirstName(etFirstName.getText().toString());
//        request.setLastName(etLastName.getText().toString());
//        request.setPhone(etPhone.getText().toString());
//        request.setEmail(etEmail.getText().toString());
//        request.setCompany(etCompany.getText().toString());
//        request.setGenderId(getSelectedGenderId());  // Update genderId here
//        request.setPlanId(getSelectedPlanId());
//        request.setChannelIds(getSelectedChannelIds());
//        request.setPreferenceChannelIds(getSelectedPreferenceChannelId());
//        request.setImageBase64(bitmapToBase64(bitmap));
//    }
//
//    private int getSelectedGenderId() {
//        return radioMale.isChecked() ? 1 : 2;  // 1 for Male, 2 for Female
//    }
//
//    private int getSelectedPlanId() {
//        if (radioFree.isChecked()) return 1;
//        if (radioBasic.isChecked()) return 2;
//        if (radioStandard.isChecked()) return 3;
//        if (radioPremium.isChecked()) return 4;
//        return 0; // Or some default
//    }
//
//    private String getSelectedChannelIds() {
//        StringBuilder channelIds = new StringBuilder();
//        if (checkboxChannelId1.isChecked()) channelIds.append("1,"); // SMS
//        if (checkboxChannelId2.isChecked()) channelIds.append("2,"); // Twilio
//        if (checkboxChannelId3.isChecked()) channelIds.append("3,"); // WhatsApp
//        if (checkboxChannelId4.isChecked()) channelIds.append("4,"); // Facebook
//        if (checkboxChannelId5.isChecked()) channelIds.append("5,"); // Instagram
//        return channelIds.length() > 0 ? channelIds.substring(0, channelIds.length() - 1) : "";
//    }
//
//    private int getSelectedPreferenceChannelId() {
//        if (radioPreferenceChannelId1.isChecked()) return 1;  // SMS
//        if (radioPreferenceChannelId2.isChecked()) return 2;  // Twilio
//        if (radioPreferenceChannelId3.isChecked()) return 3;  // WhatsApp
//        if (radioPreferenceChannelId4.isChecked()) return 4;  // Facebook
//        if (radioPreferenceChannelId5.isChecked()) return 5;  // Instagram
//        return 0; // Or some default
//    }
//
//    private String bitmapToBase64(Bitmap bitmap) {
//        if (bitmap == null) return null;
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
//
//    private void updateProfile(UserDetailRequest updateUserProfile) {
//        String userId = getUserId();
//        Call<UserDetailResponse> call = RetrofitClient.getClient().create(ApiService.class).updateUserProfile(userId, updateUserProfile);
//        call.enqueue(new Callback<UserDetailResponse>() {
//            @Override
//            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                    populateProfileFields(response.body());
//                    enableEditing(false);
//                } else {
//                    Toast.makeText(getActivity(), "Profile update failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), "Network failure", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Network error: ", t);
//            }
//        });
//    }
//}



//// ProfileFragment.java
package com.example.splash1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "ProfileFragment";

    private ImageView ivProfilePicture;
    private EditText etUsername, etFirstName, etLastName, etPhone, etEmail, etCompany;
    private RadioGroup radioGroupGender, radioGroupPlan, radioGroupPreferenceChannel;
    private RadioButton radioMale, radioFemale, radioFree, radioBasic, radioStandard, radioPremium;
    private RadioButton radioPreferenceChannelId1, radioPreferenceChannelId2, radioPreferenceChannelId3, radioPreferenceChannelId4, radioPreferenceChannelId5;
    private CheckBox checkboxChannelId1, checkboxChannelId2, checkboxChannelId3, checkboxChannelId4, checkboxChannelId5;
    private Button btnSaveProfile, btnChangePicture, btnEditProfile;

    private Bitmap bitmap;
    private SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(view);
        sharedPrefManager = new SharedPrefManager(getActivity());

        btnChangePicture.setOnClickListener(v -> openImagePicker());
        btnEditProfile.setOnClickListener(v -> enableEditing(true));
        btnSaveProfile.setOnClickListener(v -> saveProfile());

        loadProfile();
        return view;
    }

    private void initializeViews(View view) {
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        etUsername = view.findViewById(R.id.etUsername);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etCompany = view.findViewById(R.id.etCompany);

        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);

        radioGroupPlan = view.findViewById(R.id.radioGroupPlan);
        radioFree = view.findViewById(R.id.radioFree);
        radioBasic = view.findViewById(R.id.radioBasic);
        radioStandard = view.findViewById(R.id.radioStandard);
        radioPremium = view.findViewById(R.id.radioPremium);

        radioGroupPreferenceChannel = view.findViewById(R.id.radioGroupPreferenceChannel);
        radioPreferenceChannelId1 = view.findViewById(R.id.radioPreferenceChannelId1);  // SMS
        radioPreferenceChannelId2 = view.findViewById(R.id.radioPreferenceChannelId2);  // Twilio
        radioPreferenceChannelId3 = view.findViewById(R.id.radioPreferenceChannelId3);  // WhatsApp
        radioPreferenceChannelId4 = view.findViewById(R.id.radioPreferenceChannelId4);  // Instagram
        radioPreferenceChannelId5 = view.findViewById(R.id.radioPreferenceChannelId5);  // Facebook

        checkboxChannelId1 = view.findViewById(R.id.checkboxChannelId1); // SMS
        checkboxChannelId2 = view.findViewById(R.id.checkboxChannelId2); // Twilio
        checkboxChannelId3 = view.findViewById(R.id.checkboxChannelId3); // WhatsApp
        checkboxChannelId4 = view.findViewById(R.id.checkboxChannelId4); // Instagram
        checkboxChannelId5 = view.findViewById(R.id.checkboxChannelId5); // Facebook

        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnChangePicture = view.findViewById(R.id.btnChangePicture);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
    }

    private String getUserId() {
        String userId = sharedPrefManager.getUserId();
        if (userId == null) {
            Toast.makeText(getActivity(), "User ID not found. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        Log.d(TAG, "Retrieved User ID: " + userId);
        return userId;
    }

    private void loadProfile() {
        String userId = getUserId();
        if (userId == null) return;

        Call<UserDetailResponse> call = RetrofitClient.getClient().create(ApiService.class).getUserDetailById(userId);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateProfileFields(response.body());
                } else {
                    Toast.makeText(getActivity(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Network failure", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error: ", t);
            }
        });
    }

    private void populateProfileFields(UserDetailResponse profile) {
        etUsername.setText(profile.getUserName());
        etFirstName.setText(profile.getFirstName());
        etLastName.setText(profile.getLastName());
        etPhone.setText(profile.getPhone());
        etEmail.setText(profile.getEmail());
        etCompany.setText(profile.getCompany());

        if (profile.getGenderId() == 1) {
            radioMale.setChecked(true);
        } else if (profile.getGenderId() == 2) {
            radioFemale.setChecked(true);
        }

        setPlanRadioButton(profile.getPlanId());
        setChannelCheckBoxes(profile.getChannelIds());
        setPreferenceChannelRadioButton(profile.getPreferenceChannelIds());
        loadProfilePicture(profile.getImageBase64());
    }

    private void setPlanRadioButton(int planId) {
       /* switch (planId) {
            case 1: radioFree.setChecked(true); break;
            case 2: radioBasic.setChecked(true); break;
            case 3: radioStandard.setChecked(true); break;
            case 4: radioPremium.setChecked(true); break;
        }*/
        switch (planId) {
            case 1:
                radioFree.setChecked(true);
                radioBasic.setEnabled(false);
                radioStandard.setEnabled(false);
                radioPremium.setEnabled(false);
                break;
            case 2:
                radioBasic.setChecked(true);
                radioFree.setEnabled(false);
                radioStandard.setEnabled(false);
                radioPremium.setEnabled(false);
                break;
            case 3:
                radioStandard.setChecked(true);
                radioFree.setEnabled(false);
                radioBasic.setEnabled(false);
                radioPremium.setEnabled(false);
                break;
            case 4:
                radioPremium.setChecked(true);
                radioFree.setEnabled(false);
                radioBasic.setEnabled(false);
                radioStandard.setEnabled(false);
                break;
        }
    }

    private void setChannelCheckBoxes(String channelIds) {
        if (channelIds != null) {
            String[] ids = channelIds.split(",");
            for (String id : ids) {
                switch (id) {
                    case "1": checkboxChannelId1.setChecked(true); break; // SMS
                    case "2": checkboxChannelId2.setChecked(true); break; // Twilio
                    case "3": checkboxChannelId3.setChecked(true); break; // WhatsApp
                    case "4": checkboxChannelId4.setChecked(true); break; // Instagram
                    case "5": checkboxChannelId5.setChecked(true); break; // Facebook
                }
            }
        }
    }

    private void setPreferenceChannelRadioButton(int preferenceChannelId) {
       /* switch (preferenceChannelId) {
            case 1: radioPreferenceChannelId1.setChecked(true); break;  // SMS
            case 2: radioPreferenceChannelId2.setChecked(true); break;  // Twilio
            case 3: radioPreferenceChannelId3.setChecked(true); break;  // WhatsApp
            case 4: radioPreferenceChannelId4.setChecked(true); break;  // Instagram
            case 5: radioPreferenceChannelId5.setChecked(true); break;  // Facebook
        }*/
        switch (preferenceChannelId) {
            case 1:
                radioPreferenceChannelId1.setChecked(true);
                radioPreferenceChannelId2.setEnabled(false);
                radioPreferenceChannelId3.setEnabled(false);
                radioPreferenceChannelId4.setEnabled(false);
                radioPreferenceChannelId5.setEnabled(false);
                break;
            case 2:
                radioPreferenceChannelId1.setChecked(false);
                radioPreferenceChannelId2.setEnabled(true);
                radioPreferenceChannelId3.setEnabled(false);
                radioPreferenceChannelId4.setEnabled(false);
                radioPreferenceChannelId5.setEnabled(false);
                break;
            case 3:
                radioPreferenceChannelId1.setChecked(false);
                radioPreferenceChannelId2.setEnabled(false);
                radioPreferenceChannelId3.setEnabled(true);
                radioPreferenceChannelId4.setEnabled(false);
                radioPreferenceChannelId5.setEnabled(false);
                break;
            case 4:
                radioPreferenceChannelId1.setChecked(false);
                radioPreferenceChannelId2.setEnabled(false);
                radioPreferenceChannelId3.setEnabled(false);
                radioPreferenceChannelId4.setEnabled(true);
                radioPreferenceChannelId5.setEnabled(false);
                break;
            case 5:
                radioPreferenceChannelId1.setChecked(false);
                radioPreferenceChannelId2.setEnabled(false);
                radioPreferenceChannelId3.setEnabled(false);
                radioPreferenceChannelId4.setEnabled(false);
                radioPreferenceChannelId5.setEnabled(true);
                break;
        }
    }

    private void loadProfilePicture(String imageBase64) {
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivProfilePicture.setImageBitmap(bitmap);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                ivProfilePicture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableEditing(boolean enable) {
        etUsername.setEnabled(enable);
        etFirstName.setEnabled(enable);
        etLastName.setEnabled(enable);
        etPhone.setEnabled(enable);
        etEmail.setEnabled(enable);
        etCompany.setEnabled(enable);

        radioGroupGender.setEnabled(enable);
        radioGroupPlan.setEnabled(enable);
        radioGroupPreferenceChannel.setEnabled(enable);

        checkboxChannelId1.setEnabled(enable);
        checkboxChannelId2.setEnabled(enable);
        checkboxChannelId3.setEnabled(enable);
        checkboxChannelId4.setEnabled(enable);
        checkboxChannelId5.setEnabled(enable);

        btnSaveProfile.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void saveProfile() {
        if (validateInputs()) {
            UserDetailRequest userDetailRequest = new UserDetailRequest();
            populateUserDetailRequest(userDetailRequest);
            updateProfile(userDetailRequest);
        }
    }

    private boolean validateInputs() {
        if (etUsername.getText().toString().isEmpty()) {
            etUsername.setError("Username is required");
            return false;
        }
        // Add more validations as needed
        return true;
    }

    private void populateUserDetailRequest(UserDetailRequest request) {
        request.setUserName(etUsername.getText().toString());
        request.setFirstName(etFirstName.getText().toString());
        request.setLastName(etLastName.getText().toString());
        request.setPhone(etPhone.getText().toString());
        request.setEmail(etEmail.getText().toString());
        request.setCompany(etCompany.getText().toString());
        request.setGenderId(getSelectedGenderId());
        request.setPlanId(getSelectedPlanId());
        request.setChannelIds(getSelectedChannelIds());
        request.setPreferenceChannelIds(getSelectedPreferenceChannelId());
        request.setImageBase64(bitmapToBase64(bitmap));
    }

    private int getSelectedGenderId() {
        return radioMale.isChecked() ? 1 : 2;  // 1 for Male, 2 for Female
    }

    private int getSelectedPlanId() {
        if (radioFree.isChecked()) return 1;
        if (radioBasic.isChecked()) return 2;
        if (radioStandard.isChecked()) return 3;
        if (radioPremium.isChecked()) return 4;
        return 0; // Or some default
    }

    private String getSelectedChannelIds() {
        StringBuilder channelIds = new StringBuilder();
        if (checkboxChannelId1.isChecked()) channelIds.append("1,"); // SMS
        if (checkboxChannelId2.isChecked()) channelIds.append("2,"); // Twilio
        if (checkboxChannelId3.isChecked()) channelIds.append("3,"); // WhatsApp
        if (checkboxChannelId4.isChecked()) channelIds.append("4,"); // Instagram
        if (checkboxChannelId5.isChecked()) channelIds.append("5,"); // Facebook
        return channelIds.length() > 0 ? channelIds.substring(0, channelIds.length() - 1) : "";
    }

    private int getSelectedPreferenceChannelId() {
        if (radioPreferenceChannelId1.isChecked()) return 1;  // SMS
        if (radioPreferenceChannelId2.isChecked()) return 2;  // Twilio
        if (radioPreferenceChannelId3.isChecked()) return 3;  // WhatsApp
        if (radioPreferenceChannelId4.isChecked()) return 4;  // Instagram
        if (radioPreferenceChannelId5.isChecked()) return 5;  // Facebook
        return 0; // Or some default
    }

    private String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void updateProfile(UserDetailRequest updateUserProfile) {
        String userId = getUserId();
        Call<UserDetailResponse> call = RetrofitClient.getClient().create(ApiService.class).updateUserProfile(userId, updateUserProfile);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    populateProfileFields(response.body());
                    enableEditing(false);
                } else {
                    Toast.makeText(getActivity(), "Profile update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Network failure", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error: ", t);
            }
        });
    }
}
