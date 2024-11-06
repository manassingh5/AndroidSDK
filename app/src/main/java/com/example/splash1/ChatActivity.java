package com.example.splash1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get the message details from the intent
        Intent intent = getIntent();
        String messageBody = intent.getStringExtra("MESSAGE_BODY");
        String fromNumber = intent.getStringExtra("FROM_NUMBER");
        String toNumber = intent.getStringExtra("TO_NUMBER");
        String channelId = intent.getStringExtra("CHANNEL_ID");
        String channelName = intent.getStringExtra("CHANNEL_NAME");

        // Example: set up TextViews to display the message
        TextView messageBodyTextView = findViewById(R.id.messageBodyTextView);
        TextView fromNumberTextView = findViewById(R.id.fromNumberTextView);
        TextView toNumberTextView = findViewById(R.id.toNumberTextView);
        TextView channelIdTextView = findViewById(R.id.channelIdTextView);
        TextView channelNameTextView = findViewById(R.id.channelNameTextView);
        // Add other TextViews as needed

        messageBodyTextView.setText(messageBody);
        fromNumberTextView.setText(fromNumber);
        toNumberTextView.setText(toNumber);
        channelIdTextView.setText(channelId);
        channelNameTextView.setText(channelName);
        // Set other TextViews accordingly
    }
}
