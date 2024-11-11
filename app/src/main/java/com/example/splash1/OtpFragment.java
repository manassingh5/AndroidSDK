package com.example.splash1;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment {

    private TextView tv_selected_contact, tv_resend_timer;
    private Button btn_verify, btn_resend, btnSelectContact, btn_send;
    private EditText[] otpEditTexts = new EditText[6];
    private EditText etContactName, etContactNumber;
    private CountDownTimer countDownTimer;
    private ContactViewModel contactViewModel;
    private Spinner countrySpinner;
    private String countryCode;
    private SharedPrefManager sharedPrefManager;
    private ApiService apiService;
    private String fullContactNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_otp, container, false);

        // Initialize Views

        tv_resend_timer = view.findViewById(R.id.tv_resend_timer);
        btn_verify = view.findViewById(R.id.btn_verify);
        btn_resend = view.findViewById(R.id.btn_resend);
        btn_send = view.findViewById(R.id.btn_send);
        etContactName = view.findViewById(R.id.et_contact_name);
        etContactNumber = view.findViewById(R.id.et_contact_number);

        otpEditTexts[0] = view.findViewById(R.id.otp_digit_1);
        otpEditTexts[1] = view.findViewById(R.id.otp_digit_2);
        otpEditTexts[2] = view.findViewById(R.id.otp_digit_3);
        otpEditTexts[3] = view.findViewById(R.id.otp_digit_4);
        otpEditTexts[4] = view.findViewById(R.id.otp_digit_5);
        otpEditTexts[5] = view.findViewById(R.id.otp_digit_6);
        countrySpinner = view.findViewById(R.id.spinner_country_code);


        setupOtpFields();
        setupListeners();

        // ViewModel to share contact data between fragments

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Initialize ViewModel same as ContactsFragment
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        /*contactViewModel.getSelectedCount().observe(getViewLifecycleOwner(), count -> {
            contactCounterTextView.setText("Selected Contacts: " + count);
        });*/

        return view;
    }

    private void sendOtp(String userId, List<Contact> contacts, String channelIds, Integer templateId, String message) {
        Log.d("userId", "user id: " + userId);
        List<Contact> contact1 = new ArrayList<>();
        for (Contact contact : contacts) {
            contact1.add(new Contact(contact.getName(),contact.getNumber()));
            Log.d("ContactNumbers", "Contact Name: " + contact.getName() + ", Phone Number: " + contact.getNumber());
        }
        SendMessageRequest messageRequest = new SendMessageRequest(userId, contacts, channelIds, templateId, message);

        Call<Void> call = apiService.sendMessage(messageRequest);
        //      Toast.makeText(getContext(), "Message Request" +messageRequest, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("messageResponse", "Message : " + response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getContext(), "Failed to send OTP." + response.code(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage());
            }
        });
    }

    private void setupOtpFields() {
        for ( int  i = 0; i < otpEditTexts.length - 1; i++) {
            final int index = i;
            otpEditTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpEditTexts.length - 1) {
                        otpEditTexts[index + 1].requestFocus(); // Move to the next EditText
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupListeners() {

        sharedPrefManager = new SharedPrefManager(getActivity());
        List<Country> countryList = new ArrayList<>();
     //   countryList.add(new Country("United States", "+1"));
        countryList.add(new Country("IND", "+91"));
    //    countryList.add(new Country("United Kingdom", "+44"));

        // Real-time validation with TextWatchers
        etContactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateName();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etContactNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhoneNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

// Retrieve selected country code
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                countryCode = selectedCountry.getCode(); // Use this code for further logic
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_send.setOnClickListener(v -> {
            btn_send.setEnabled(false);
            String message = "";
            String userId = sharedPrefManager.getUserId();
            String channelIds ="3";
            Integer templateId =3;


            String name = etContactName.getText().toString().trim();
            String number = etContactNumber.getText().toString().trim();

            fullContactNumber = countryCode + number;

        //    if (!name.isEmpty() && !number.isEmpty()) {
            if (validateName() && validatePhoneNumber()) {
                // Create a list with the single contact
                Contact contact = new Contact(name, fullContactNumber);
                List<Contact> contacts = new ArrayList<>();
                contacts.add(contact);

                // Pass the list to sendOtp
                sendOtp(userId, contacts, channelIds, templateId, message);
                startCountdown();
            } else {
                // Show an error if fields are empty
                Toast.makeText(getContext(), "Please enter both name and number", Toast.LENGTH_SHORT).show();
                btn_send.setEnabled(true);
            }

           /* contactViewModel.getSelectedContact().observe(getViewLifecycleOwner(), contacts -> {
                if (contacts != null && contacts.size() == 1) {
                    Contact contact = contacts.get(0);
              //      tv_selected_contact.setText(contact.getNumber());
                    sendOtp(userId, contacts, channelIds, templateId, message);
                }
                else {
                    Toast.makeText(getContext(), "Please select only one contact", Toast.LENGTH_SHORT).show();
                    btn_send.setEnabled(true);
                }
                });*/
        });

        btn_resend.setOnClickListener(v -> {
            btn_resend.setEnabled(false);
            String message = "";
            String userId = sharedPrefManager.getUserId();
            String channelIds ="3";
            Integer templateId =3;

            String name = etContactName.getText().toString().trim();
            String number = etContactNumber.getText().toString().trim();

            fullContactNumber = countryCode + number;

       //     if (!name.isEmpty() && !number.isEmpty()) {
            if (validateName() && validatePhoneNumber()) {
                // Create a list with the single contact
                Contact contact = new Contact(name, fullContactNumber);
                List<Contact> contacts = new ArrayList<>();
                contacts.add(contact);

                // Pass the list to sendOtp
                sendOtp(userId, contacts, channelIds, templateId, message);
                startCountdown();
            } else {
                // Show an error if fields are empty
                Toast.makeText(getContext(), "Please enter both name and number", Toast.LENGTH_SHORT).show();
                btn_resend.setEnabled(true);
            }

            /*contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
            contactViewModel.getSelectedContact().observe(getViewLifecycleOwner(), contacts -> {
                if (contacts != null && contacts.size() == 1) {
                    Contact contact = contacts.get(0);
          //          tv_selected_contact.setText(contact.getName());
                    sendOtp(userId, contacts, channelIds, templateId, message);
                    startCountdown();
                }
                else {
                    Toast.makeText(getContext(), "Please select only one contact", Toast.LENGTH_SHORT).show();
                    btn_send.setEnabled(true);
                }
            });*/
        });
        
        btn_verify.setOnClickListener(v -> {
            String userId = sharedPrefManager.getUserId();
            verifyOtp(userId);
        });
      //  btn_resend.setOnClickListener(v -> startCountdown());
     //   btnSelectContact.setOnClickListener(v -> openContactFragment());
    }

    private void verifyOtp(String userId) {
        StringBuilder otp = new StringBuilder();
        for (EditText editText : otpEditTexts) {
            otp.append(editText.getText().toString());
        }

        if (otp.length() == 6) {
            verifyOtpApiCall(userId, otp.toString());
       //     Toast.makeText(getContext(), "OTP Verified", Toast.LENGTH_SHORT).show();
            
        } else {
            Toast.makeText(getContext(), "Enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOtpApiCall(String userId, String otp) {
     //   String userId = sharedPrefManager.getUserId();
        Log.d("userId", "user Id : " +userId);
      //  VerifyOtpRequest request = new VerifyOtpRequest(userId, otp);

        Call<ResponseBody> call = apiService.verifyOtp(userId, otp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("messageResponse", "Message : " + response.message());
                // Stop the countdown when OTP verification completes, regardless of success or failure
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                // Enable the send button regardless of the verification result
                btn_send.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "OTP verified successfully", Toast.LENGTH_SHORT).show();
                    // Handle successful OTP verification (e.g., navigate to the next screen)
                } else {
                    Toast.makeText(getContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                // Enable the send button regardless of the verification result
                btn_send.setEnabled(true);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_resend_timer.setText("Resend in: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                tv_resend_timer.setText("You can resend now");
            }
        };
        countDownTimer.start();
    }
    // Method to validate Name and Phone Number
    private boolean validateName() {
        String name = etContactName.getText().toString().trim();
        if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
            etContactName.setError("Name should only contain alphabetic characters");
            return false;
        } else {
            etContactName.setError(null); // Clear error if valid
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String number = etContactNumber.getText().toString().trim();
        if (number.isEmpty() || number.length() != 10 || !number.matches("\\d+")) {
            etContactNumber.setError("Enter a valid 10-digit phone number");
            return false;
        } else {
            etContactNumber.setError(null); // Clear error if valid
            return true;
        }
    }

}
