//package com.example.splash1;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText etUsername, etPassword;
//    private Button btn_login, btn_signup;
//    private ApiService apiService;
//    private SharedPrefManager sharedPrefManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        // Initialize views
//        etUsername = findViewById(R.id.et_username);
//        etPassword = findViewById(R.id.et_password);
//        btn_login = findViewById(R.id.btn_login);
//        btn_signup = findViewById(R.id.btn_signup);
//
//        // Initialize ApiService and SharedPrefManager
//        apiService = RetrofitClient.getClient().create(ApiService.class);
//        sharedPrefManager = new SharedPrefManager(this);
//
//        // Set up click listener for login button
//        btn_login.setOnClickListener(v -> loginUser());
//
//        // Set up click listener for signup button
//        btn_signup.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    private void loginUser() {
//        String userName = etUsername.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        LoginRequest loginRequest = new LoginRequest(userName, password);
//        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    LoginResponse loginResponse = response.body();
//                    if (loginResponse.getUserId() != null) {
//                        // Save all relevant user data in SharedPreferences
//                        sharedPrefManager.saveUserId(loginResponse.getUserId());
//                        sharedPrefManager.saveUsername(loginResponse.getUsername());
//                        sharedPrefManager.setIsPreferenceSet(loginResponse.isPreferenceSet());
//
//                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//}

package com.example.splash1;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btn_login, btn_signup;
    private ApiService apiService;
    private SharedPrefManager sharedPrefManager;
    private Dialog loadingDialog;  // Custom loading dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getIsLoggedIn()) {
            // If logged in, go to HomeActivity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return; // Stop further execution
        }

        setContentView(R.layout.activity_login);

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        // Initialize ApiService and SharedPrefManager
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Set up the custom loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);  // Custom loading layout
        loadingDialog.setCancelable(false);  // Make it non-cancelable by tapping outside

        // Set up click listener for login button
        btn_login.setOnClickListener(v -> loginUser());

        // Set up click listener for signup button
        btn_signup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the custom loading dialog
        loadingDialog.show();

        LoginRequest loginRequest = new LoginRequest(userName, password);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Dismiss the loading dialog once the response is received
                loadingDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getUserId() != null) {
                        // Save all relevant user data in SharedPreferences
                        sharedPrefManager.saveUserId(loginResponse.getUserId());
                        sharedPrefManager.saveUsername(loginResponse.getUsername());
                        sharedPrefManager.setIsPreferenceSet(loginResponse.isPreferenceSet());

                        sharedPrefManager.setIsLoggedIn(true); // Mark as logged in
                        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", userName);
                        // editor.putString("UserID" , userId);
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Dismiss the loading dialog if there's a failure
                loadingDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
