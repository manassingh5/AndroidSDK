package com.example.splash1;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleMessageFragment extends Fragment {

    private EditText messageContent;
    private Spinner spinnerChannels, spinnerTemplates;
    private Button btnPickDate, btnPickTime, btnSchedule;
    private Switch toggleSwitch;
    private Calendar scheduledTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_message, container, false);

        messageContent = view.findViewById(R.id.etMessageContent);
        spinnerChannels = view.findViewById(R.id.spinnerChannels);
        spinnerTemplates = view.findViewById(R.id.spinnerTemplates);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        btnPickTime = view.findViewById(R.id.btnPickTime);
        btnSchedule = view.findViewById(R.id.btnSchedule);
        toggleSwitch = view.findViewById(R.id.toggleSwitch);

        btnPickDate.setOnClickListener(v -> showDatePickerDialog());
        btnPickTime.setOnClickListener(v -> showTimePickerDialog());
        btnSchedule.setOnClickListener(v -> scheduleMessage());

        return view;
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            if (scheduledTime == null) {
                scheduledTime = Calendar.getInstance();
            }
            scheduledTime.set(year1, month1, dayOfMonth);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute1) -> {
            if (scheduledTime == null) {
                scheduledTime = Calendar.getInstance();
            }
            scheduledTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            scheduledTime.set(Calendar.MINUTE, minute1);
        }, hour, minute, DateFormat.is24HourFormat(requireContext()));
        timePickerDialog.show();
    }

    private void scheduleMessage() {
        String message = messageContent.getText().toString();
        int channelID = spinnerChannels.getSelectedItemPosition();
        int templateID = spinnerTemplates.getSelectedItemPosition();
        boolean isOn = toggleSwitch.isChecked();

        if (scheduledTime != null && !message.isEmpty()) {
            ScheduledMessage scheduledMessage = new ScheduledMessage(
                    new ArrayList<>(), new ArrayList<>(), templateID, scheduledTime, message, isOn
            );

            // Convert the ScheduledMessage to JSON
            String scheduledMessageJson = new Gson().toJson(scheduledMessage);

            // Save to SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("ScheduledMessages", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("message_" + System.currentTimeMillis(), scheduledMessageJson);
            editor.apply();

            // Set up the alarm
            Intent intent = new Intent(requireContext(), MessageSenderReceiver.class);
            intent.putExtra("message", scheduledMessageJson); // Send JSON string

            PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduledTime.getTimeInMillis(), pendingIntent);
            }

            Toast.makeText(requireContext(), "Message scheduled!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }
}
