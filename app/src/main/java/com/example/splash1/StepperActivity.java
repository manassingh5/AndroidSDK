//package com.example.splash1;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//
//import com.google.gson.Gson;
//
//import java.io.IOException;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class StepperActivity extends AppCompatActivity {
//
//    private ViewPager viewPager;
//    private StepperPagerAdapter pagerAdapter;
//    private Button btnNext, btnPrevious;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_stepper);
//
//        viewPager = findViewById(R.id.view_pager);
//        btnNext = findViewById(R.id.btn_next);
//        btnPrevious = findViewById(R.id.btn_previous);
//
//        pagerAdapter = new StepperPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        viewPager.setAdapter(pagerAdapter);
//
//        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                updateButtons(position);
//            }
//        });
//
//        btnNext.setOnClickListener(v -> {
//            int currentItem = viewPager.getCurrentItem();
//            if (validateCurrentStep(currentItem)) {
//                if (currentItem < pagerAdapter.getCount() - 1) {
//                    viewPager.setCurrentItem(currentItem + 1, true);
//                } else {
//                    // Collect data and make API call
//                    makeApiCall();
//                }
//            }
//        });
//
//        btnPrevious.setOnClickListener(v -> {
//            int currentItem = viewPager.getCurrentItem();
//            if (currentItem > 0) {
//                viewPager.setCurrentItem(currentItem - 1, true);
//            }
//        });
//
//        // Initialize buttons state
//        updateButtons(viewPager.getCurrentItem());
//    }
//
//    private void makeApiCall() {
//        String userId = "3e3f978b-ef9f-4f8a-b313-7081daf7d0f1"; // Replace with actual user ID
//
//        // Retrieve selected options from the adapter
//        String[] selectedChannels = pagerAdapter.getChannels();
//        String[] selectedPreferences = pagerAdapter.getPreferences();
//        String selectedPlan = pagerAdapter.getPlan();
//
//        // Map selections to IDs
//        String channelIds = mapChannelsToIds(selectedChannels);
//        String preferenceChannelIds = mapPreferencesToIds(selectedPreferences);
//        String planId = mapPlanToId(selectedPlan);
//
//        UserPreferenceRequest request = new UserPreferenceRequest();
//        request.setChannelIds(channelIds); // Pass IDs as a comma-separated string
//        request.setPreferenceChannelIds(preferenceChannelIds); // Pass IDs as a comma-separated string
//        request.setPlanId(planId); // Pass plan ID as a string
//
//        // Log request details
//        Log.d("StepperActivity", "Request body: " + new Gson().toJson(request));
//
//        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        Call<ApiResponse> call = apiService.updatePreferences(userId, request);
//
//        // Logging request details
//        Log.d("StepperActivity", "Sending request with userId: " + userId);
//
//        call.enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                if (response.isSuccessful()) {
//                    ApiResponse apiResponse = response.body();
//                    Toast.makeText(StepperActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("StepperActivity", "Response: " + new Gson().toJson(apiResponse));
//                } else {
//                    // Log the error body and headers
//                    try {
//                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
//                        Log.e("StepperActivity", "Error: " + response.message() + " Error Body: " + errorBody);
//                    } catch (IOException e) {
//                        Log.e("StepperActivity", "Error reading error body", e);
//                    }
//                    Toast.makeText(StepperActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//                Toast.makeText(StepperActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("StepperActivity", "Network error", t);
//            }
//        });
//    }
//
//
//    private String mapChannelsToIds(String[] selectedChannels) {
//        StringBuilder channelIds = new StringBuilder();
//        for (String channel : selectedChannels) {
//            switch (channel) {
//                case "WhatsApp":
//                    if (channelIds.length() > 0) channelIds.append(",");
//                    channelIds.append(2);
//                    break;
//                case "SMS":
//                    if (channelIds.length() > 0) channelIds.append(",");
//                    channelIds.append(1);
//                    break;
//                case "Instagram":
//                    if (channelIds.length() > 0) channelIds.append(",");
//                    channelIds.append(3);
//                    break;
//                case "Facebook":
//                    if (channelIds.length() > 0) channelIds.append(",");
//                    channelIds.append(4);
//                    break;
//                default:
//                    // Handle unexpected values
//                    Log.w("StepperActivity", "Unexpected channel value: " + channel);
//                    break;
//            }
//        }
//        return channelIds.toString();
//    }
//
//    private String mapPreferencesToIds(String[] selectedPreferences) {
//        StringBuilder preferenceIds = new StringBuilder();
//        for (String preference : selectedPreferences) {
//            switch (preference) {
//                case "WhatsApp":
//                    if (preferenceIds.length() > 0) preferenceIds.append(",");
//                    preferenceIds.append(2);
//                    break;
//                case "SMS":
//                    if (preferenceIds.length() > 0) preferenceIds.append(",");
//                    preferenceIds.append(1);
//                    break;
//                case "Instagram":
//                    if (preferenceIds.length() > 0) preferenceIds.append(",");
//                    preferenceIds.append(3);
//                    break;
//                case "Facebook":
//                    if (preferenceIds.length() > 0) preferenceIds.append(",");
//                    preferenceIds.append(4);
//                    break;
//                default:
//                    // Handle unexpected values
//                    Log.w("StepperActivity", "Unexpected preference value: " + preference);
//                    break;
//            }
//        }
//        return preferenceIds.toString();
//    }
//
//    private String mapPlanToId(String plan) {
//        switch (plan) {
//            case "Free":
//                return "1";
//            case "Basic":
//                return "2";
//            case "Standard":
//                return "3";
//            case "Premium":
//                return "4";
//            default:
//                return "0"; // Default or no selection
//        }
//    }
//    public void updateButtons(int position) {
//        btnPrevious.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
//        btnNext.setText(position == pagerAdapter.getCount() - 1 ? "Finish" : "Next");
//
//        // Update button color and state based on validation
//        boolean isValid = validateCurrentStep(position);
//        btnNext.setEnabled(isValid);
//        btnNext.setBackgroundColor(isValid ? Color.GREEN : Color.GRAY);
//    }
//
//    private boolean validateCurrentStep(int position) {
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + position);
//
//        if (fragment instanceof Step1Fragment) {
//            Step1Fragment step1 = (Step1Fragment) fragment;
//            if (!step1.isAnyChannelSelected()) {
//                showError("Please select at least one channel.");
//                return false;
//            }
//            pagerAdapter.setChannels(step1.getSelectedChannels());
//        } else if (fragment instanceof Step2Fragment) {
//            Step2Fragment step2 = (Step2Fragment) fragment;
//            if (!step2.isAnyPreferenceSelected()) {
//                showError("Please select at least one preference.");
//                return false;
//            }
//            pagerAdapter.setPreferences(step2.getSelectedPreferences());
//        } else if (fragment instanceof Step3Fragment) {
//            Step3Fragment step3 = (Step3Fragment) fragment;
//            if (!step3.isPlanSelected()) {
//                showError("Please select a plan.");
//                return false;
//            }
//            pagerAdapter.setPlan(step3.getSelectedPlan());
//        }
//
//        return true;
//    }
//
//    private void showError(String message) {
//        Toast.makeText(StepperActivity.this, message, Toast.LENGTH_SHORT).show();
//    }
//}


package com.example.splash1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepperActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private StepperPagerAdapter pagerAdapter;
    private Button btnNext, btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper);

        viewPager = findViewById(R.id.view_pager);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);

        pagerAdapter = new StepperPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateButtons(position);
            }
        });

        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (validateCurrentStep(currentItem)) {
                if (currentItem < pagerAdapter.getCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                } else {
                    // Collect data and make API call
                    makeApiCall();
                }
            }
        });

        btnPrevious.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true);
            }
        });

        // Initialize buttons state
        updateButtons(viewPager.getCurrentItem());
    }

    private void makeApiCall() {
        String userId = "3e3f978b-ef9f-4f8a-b313-7081daf7d0f1"; // Replace with actual user ID

        // Retrieve selected options from the adapter
        String[] selectedChannels = pagerAdapter.getChannels();
        String[] selectedPreferences = pagerAdapter.getPreferences();
        String selectedPlan = pagerAdapter.getPlan();

        // Map selections to IDs
        String channelIds = mapChannelsToIds(selectedChannels);
        String preferenceChannelIds = mapPreferencesToIds(selectedPreferences);
        String planId = mapPlanToId(selectedPlan);

        UserPreferenceRequest request = new UserPreferenceRequest();
        request.setChannelIds(channelIds); // Pass IDs as a comma-separated string
        request.setPreferenceChannelIds(preferenceChannelIds); // Pass IDs as a comma-separated string
        request.setPlanId(planId); // Pass plan ID as string

        // Log request details
        Log.d("StepperActivity", "Request body: " + new Gson().toJson(request));

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse> call = apiService.updatePreferences(userId, request);

        // Logging request details
        Log.d("StepperActivity", "Sending request with userId: " + userId);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isPreferenceSet()) {
                        Toast.makeText(StepperActivity.this, "Preferences saved successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(StepperActivity.this, "Failed to save preferences.", Toast.LENGTH_LONG).show();
                    }
                    Log.d("StepperActivity", "Response: " + new Gson().toJson(apiResponse));
                } else {
                    // Log the error body and headers
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("StepperActivity", "Error: " + response.message() + " Error Body: " + errorBody);
                        Toast.makeText(StepperActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e("StepperActivity", "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(StepperActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("StepperActivity", "Network error", t);
            }
        });
    }


//    private void navigateToHomeFragment() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment homeFragment = new HomeFragment();
//        fragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, homeFragment) // Make sure R.id.fragment_container is your container ID
//                .addToBackStack(null) // Optional: add to back stack if you want the user to be able to navigate back
//                .commit();
//    }

    private String mapChannelsToIds(String[] selectedChannels) {
        StringBuilder channelIds = new StringBuilder();
        for (String channel : selectedChannels) {
            switch (channel) {
                case "WhatsApp":
                    if (channelIds.length() > 0) channelIds.append(",");
                    channelIds.append(2);
                    break;
                case "SMS":
                    if (channelIds.length() > 0) channelIds.append(",");
                    channelIds.append(1);
                    break;
                case "Instagram":
                    if (channelIds.length() > 0) channelIds.append(",");
                    channelIds.append(3);
                    break;
                case "Facebook":
                    if (channelIds.length() > 0) channelIds.append(",");
                    channelIds.append(4);
                    break;
                default:
                    // Handle unexpected values
                    Log.w("StepperActivity", "Unexpected channel value: " + channel);
                    break;
            }
        }
        return channelIds.toString();
    }

    private String mapPreferencesToIds(String[] selectedPreferences) {
        StringBuilder preferenceIds = new StringBuilder();
        for (String preference : selectedPreferences) {
            switch (preference) {
                case "WhatsApp":
                    if (preferenceIds.length() > 0) preferenceIds.append(",");
                    preferenceIds.append(2);
                    break;
                case "SMS":
                    if (preferenceIds.length() > 0) preferenceIds.append(",");
                    preferenceIds.append(1);
                    break;
                case "Instagram":
                    if (preferenceIds.length() > 0) preferenceIds.append(",");
                    preferenceIds.append(3);
                    break;
                case "Facebook":
                    if (preferenceIds.length() > 0) preferenceIds.append(",");
                    preferenceIds.append(4);
                    break;
                default:
                    // Handle unexpected values
                    Log.w("StepperActivity", "Unexpected preference value: " + preference);
                    break;
            }
        }
        return preferenceIds.toString();
    }

    private String mapPlanToId(String plan) {
        switch (plan) {
            case "Free":
                return "1";
            case "Basic":
                return "2";
            case "Standard":
                return "3";
            case "Premium":
                return "4";
            default:
                return "0"; // Default or no selection
        }
    }

    public void updateButtons(int position) {
        btnPrevious.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
        btnNext.setText(position == pagerAdapter.getCount() - 1 ? "Finish" : "Next");

        // Update button color and state based on validation
        boolean isValid = validateCurrentStep(position);
        btnNext.setEnabled(isValid);
        btnNext.setBackgroundColor(isValid ? Color.GREEN : Color.GRAY);
    }

    private boolean validateCurrentStep(int position) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + position);

        if (fragment instanceof Step1Fragment) {
            Step1Fragment step1 = (Step1Fragment) fragment;
            if (!step1.isAnyChannelSelected()) {
                showError("Please select at least one channel.");
                return false;
            }
            pagerAdapter.setChannels(step1.getSelectedChannels());
        } else if (fragment instanceof Step2Fragment) {
            Step2Fragment step2 = (Step2Fragment) fragment;
            if (!step2.isAnyPreferenceSelected()) {
                showError("Please select at least one preference.");
                return false;
            }
            pagerAdapter.setPreferences(step2.getSelectedPreferences());
        } else if (fragment instanceof Step3Fragment) {
            Step3Fragment step3 = (Step3Fragment) fragment;
            if (!step3.isPlanSelected()) {
                showError("Please select a plan.");
                return false;
            }
            pagerAdapter.setPlan(step3.getSelectedPlan());
        }

        return true;
    }

    private void showError(String message) {
        Toast.makeText(StepperActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
