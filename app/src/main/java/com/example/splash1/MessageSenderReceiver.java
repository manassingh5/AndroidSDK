package com.example.splash1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;

public class MessageSenderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String messageJson = intent.getStringExtra("message");
        ScheduledMessage scheduledMessage = new Gson().fromJson(messageJson, ScheduledMessage.class);

        if (scheduledMessage != null && scheduledMessage.isOn()) {
            // Handle sending the message (e.g., show a notification or send an SMS)
            Toast.makeText(context, "Message Sent: " + scheduledMessage.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
