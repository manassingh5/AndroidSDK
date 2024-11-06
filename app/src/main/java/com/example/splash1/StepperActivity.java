// StepperActivity.java
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
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper);

        sharedPrefManager = new SharedPrefManager(this);

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

        updateButtons(viewPager.getCurrentItem());
    }

    private void makeApiCall() {
        String userId = sharedPrefManager.getUserId();

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }

        String channelIds = pagerAdapter.getChannels();
        int preferenceChannelIds = pagerAdapter.getPreferences();
        int planId = pagerAdapter.getPlan();

        UserPreferenceRequest request = new UserPreferenceRequest();
        request.setChannelIds(channelIds);
        request.setPreferenceChannelIds(preferenceChannelIds);
        request.setPlanId(planId);

        Log.d("StepperActivity", "Request body: " + new Gson().toJson(request));

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse> call = apiService.updatePreferences(userId, request);

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

    public void updateButtons(int position) {
        btnPrevious.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
        btnNext.setText(position == pagerAdapter.getCount() - 1 ? "Finish" : "Next");

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
            int selectedPreference = step2.getSelectedPreference();
            if (selectedPreference == -1) {
                showError("Please select a preference.");
                return false;
            }
            pagerAdapter.setPreferences(selectedPreference);
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