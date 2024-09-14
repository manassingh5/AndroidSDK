package com.example.splash1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessageActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private EditText etMessage;
    private Button btnSend;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnSend.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String message = etMessage.getText().toString().trim();

            if (message.length() > 160) {
                Toast.makeText(SendMessageActivity.this, "Message exceeds 160 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            sendMessage(phoneNumber, message);
        });
    }

    private void sendMessage(String phoneNumber, String message) {
        MessageRequest messageRequest = new MessageRequest(phoneNumber, message);

        apiService.sendMessage(messageRequest).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SendMessageActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SendMessageActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(SendMessageActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
