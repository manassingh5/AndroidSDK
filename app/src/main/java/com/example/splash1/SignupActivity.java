package com.example.splash1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etUserName, etPassword, etFirstName, etLastName, etEmail, etPhone, etCompany;
    private MaterialButton btnSignup;
    private TextView tvLoginPrompt;
    private RadioGroup genderRadioGroup;
    private ApiService apiService;
    private SharedPrefManager sharedPrefManager;
    private Dialog loadingDialog; // Custom loading dialog

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
        genderRadioGroup = findViewById(R.id.rg_gender);

        // Initialize ApiService and SharedPrefManager
        apiService = RetrofitClient.getClient().create(ApiService.class);
        sharedPrefManager = new SharedPrefManager(this); // Initialize SharedPrefManager

        // Set up the custom loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);  // Custom loading layout
        loadingDialog.setCancelable(false);  // Make it non-cancelable by tapping outside

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
        // Get user inputs
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        Integer genderId = getSelectedGender(); // Get selected gender
        String company = etCompany.getText().toString().trim();

        // Validate inputs
        if (!isValidUserName(userName)) return;
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

        // Show loading dialog
        loadingDialog.show();

        // Prepare the registration request
        RegisterRequest registerRequest = new RegisterRequest(userName, password, firstName, lastName, email, phone, genderId, company);
        Call<RegisterResponse> call = apiService.registerUser(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                // Dismiss loading dialog after response is received
                loadingDialog.dismiss();

                if (response.isSuccessful()) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        String userId = registerResponse.getUserId();
                        Toast.makeText(SignupActivity.this, "UserID: " + userId, Toast.LENGTH_LONG).show();
                        // Save the user ID in SharedPreferences
                        sharedPrefManager.saveUserId(userId);
                        Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        showError("Registration failed. Empty response from server.");
                    }
                } else {
                    showError("Registration failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Dismiss loading dialog on failure
                loadingDialog.dismiss();
                showError("Network error. Please check your connection and try again.");
            }
        });
    }

    private int getSelectedGender() {
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.rb_male) {
            return 1; // Male selected
        } else if (selectedGenderId == R.id.rb_female) {
            return 2; // Female selected
        }
        return 0; // No selection
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
        return !TextUtils.isEmpty(firstName) && firstName.matches("[a-zA-Z]+");
    }

    private boolean isValidLastName(String lastName) {
        return !TextUtils.isEmpty(lastName) && lastName.matches("[a-zA-Z]+");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$");
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etUserName.getText()) ||
                TextUtils.isEmpty(etPassword.getText()) ||
                TextUtils.isEmpty(etFirstName.getText()) ||
                TextUtils.isEmpty(etLastName.getText()) ||
                TextUtils.isEmpty(etEmail.getText()) ||
                TextUtils.isEmpty(etPhone.getText()) ||
                TextUtils.isEmpty(etCompany.getText())) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
