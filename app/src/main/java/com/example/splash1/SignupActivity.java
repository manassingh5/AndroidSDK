package com.example.splash1;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etUserName, etPassword, etFirstName, etLastName, etEmail, etPhone, etCompany;
    private Button btnSignup;
    private TextView tvLoginPrompt;
    private ApiService apiService;
    private RadioGroup genderRadioGroup;
    int gender = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etFirstName = findViewById(R.id.et_firstname);
        etLastName = findViewById(R.id.et_lastname);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etCompany = findViewById(R.id.et_company);
        btnSignup = findViewById(R.id.btn_signup);
        tvLoginPrompt = findViewById(R.id.tv_login_prompt);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        apiService = RetrofitClient.getClient().create(ApiService.class);


        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
       // int gender = 0; // Default or no selection
        if (selectedGenderId == R.id.radioMale) {
            gender = 1; // Male selected
        } else if (selectedGenderId == R.id.radioFemale) {
            gender = 2; // Female selected
        }

        btnSignup.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        tvLoginPrompt.setOnClickListener(v -> {
            // Navigate to LoginActivity
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }

    private void registerUser() {
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        Integer genderId =gender;
        String company = etCompany.getText().toString().trim();

        if (!isValidUserName(userName)) {
            return;
        }
        if (!isValidFirstName(firstName)) {
            Toast.makeText(this, "Invalid First Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidLastName(lastName)) {
            Toast.makeText(this, "Invalid Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters long and contain one uppercase letter, one lowercase letter, one digit, and one special character.", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest(userName, password, firstName, lastName, email, phone, genderId, company);
        Call<RegisterResponse> call = apiService.registerUser(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    String userId = registerResponse.getUserId();
                        String message = registerResponse.getMessage();
                        Toast.makeText(SignupActivity.this, "UserID: " + userId + ", Message: " + message, Toast.LENGTH_LONG).show();
                        // Registration successful, handle response if needed
                         Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    /* else {
                        showError("Registration failed. Please try again.");
                    }*/
                } else {
                    showError("Registration failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                showError("Network error. Please check your connection and try again.");
            }
        });
    }

    private boolean isValidUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            etUserName.setError("Username cannot be empty");
            etUserName.requestFocus();
            return false;
        }
        if (userName.length() < 3 || userName.length() > 15) {
            etUserName.setError("Username must be between 3 and 15 characters");
            etUserName.requestFocus();
            return false;
        }
        if (!userName.matches("[a-zA-Z0-9._-]+")) {
            etUserName.setError("Username can only contain letters, numbers, dots, underscores, and hyphens");
            etUserName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidPhoneNumber(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidFirstName(String firstName) {
        return !TextUtils.isEmpty(firstName) && Pattern.matches("[a-zA-Z]+", firstName);
    }

    private boolean isValidLastName(String lastName) {
        return !TextUtils.isEmpty(lastName) && Pattern.matches("[a-zA-Z]+", lastName);
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etUserName.getText().toString()) ||
                TextUtils.isEmpty(etPassword.getText().toString()) ||
                TextUtils.isEmpty(etFirstName.getText().toString()) ||
                TextUtils.isEmpty(etLastName.getText().toString()) ||
                TextUtils.isEmpty(etEmail.getText().toString()) ||
                TextUtils.isEmpty(etPhone.getText().toString()) ||
                TextUtils.isEmpty(etCompany.getText().toString())) {

            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}