package com.example.splash1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduledMessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduledMessageAdapter adapter;
    private List<ScheduledMessage> scheduledMessages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduled_messages, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load messages
        loadScheduledMessages();

        adapter = new ScheduledMessageAdapter(scheduledMessages, requireContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadScheduledMessages() {
        SharedPreferences prefs = requireContext().getSharedPreferences("ScheduledMessages", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Map<String, ?> allEntries = prefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getValue() instanceof String) {
                String json = (String) entry.getValue();
                ScheduledMessage message = gson.fromJson(json, ScheduledMessage.class);
                scheduledMessages.add(message);
            }
        }
    }
}
