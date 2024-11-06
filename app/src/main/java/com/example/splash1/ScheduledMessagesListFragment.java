package com.example.splash1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.List;

public class ScheduledMessagesListFragment extends Fragment {
    private ScheduledMessagesViewModel viewModel;
    private ScheduledMessagesAdapter adapter;
    private RecyclerView recyclerView;
    private SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduled_messages_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewScheduledMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPrefManager = new SharedPrefManager(getContext());
        adapter = new ScheduledMessagesAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ScheduledMessagesViewModel.class);

        String userId = sharedPrefManager.getUserId(); // Retrieve user ID from SharedPreferences
        if (userId != null) {
            viewModel.fetchScheduledMessage(userId); // Fetch scheduled messages
            Log.d("Fetch", "User ID: " + userId);

            viewModel.getScheduleDetails().observe(getViewLifecycleOwner(), new Observer<List<ScheduleDetailsResponse>>() {
                @Override
                public void onChanged(List<ScheduleDetailsResponse> scheduleDetailsResponses) {
                    Log.d("ScheduleDetailsResponse", "Response: " + scheduleDetailsResponses);
                    if (scheduleDetailsResponses != null && !scheduleDetailsResponses.isEmpty()) {
                        adapter.updateData(scheduleDetailsResponses);
                    } else {
                        Toast.makeText(getContext(), "No scheduled messages found.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
