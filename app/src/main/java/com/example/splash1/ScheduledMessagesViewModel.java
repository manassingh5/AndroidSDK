package com.example.splash1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ScheduledMessagesViewModel extends ViewModel {
    private final MutableLiveData<List<ScheduleDetailsResponse>> scheduleDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<List<ScheduleDetailsResponse>> getScheduleDetails() {
        return scheduleDetailsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchScheduledMessage(String userId) {
        isLoading.setValue(true);
        Log.d("ScheduledMessagesViewModel", "Fetching scheduled messages for userId: " + userId);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ScheduleResponse> call = apiService.getScheduledMessages(userId);

        call.enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ScheduledMessagesViewModel", "API Response Code: " + response.code());
                    List<ScheduleDetailsResponse> schedules = response.body().getSchedules();
                    scheduleDetailsLiveData.postValue(schedules != null ? schedules : new ArrayList<>());
                } else {
                    Log.e("ScheduledMessagesViewModel", "Error Response Code: " + response.code());
                    scheduleDetailsLiveData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                isLoading.postValue(false);
                Log.e("ScheduledMessagesViewModel", "API failure: " + t.getMessage());
                scheduleDetailsLiveData.postValue(new ArrayList<>());
            }
        });
    }
}
