//package com.example.splash1;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.Manifest;
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.telephony.SmsManager;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.card.MaterialCardView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import java.util.Calendar;
//
//public class ScheduleMessageFragment extends Fragment {
//
//    private TextView tvContactsSelected, charCount;
//    private EditText etMessage;
//    private CheckBox checkboxWhatsApp, checkboxSMS, checkboxTwilio;
//    private Spinner spinnerTemplate;
//    private MaterialButton btnSelectContacts;
//    private Button buttonPickDate, buttonAddEvent;
//    private int maxMessageLength = 160;
//    private TextView textViewSelectedDateTime;
//    private ContactViewModel contactViewModel;
//    private ApiService apiService;
//    private SmsManager smsManager;
//    private SharedPrefManager sharedPrefManager;
//    StringBuilder channelIds = new StringBuilder();
//    int templateId = 0;
//    private Calendar selectedDateTime;
//    public static final int IMAGE = 1;
//    public static final int MESSAGE = 2;
//    public static final int AUDIO_MESSAGE = 3;
//    public static final int CONTACT = 4;
//    public static final int LOCATION = 5;
//    public static final int MARKETING_TEMPLATE = 6;
//    public static final int CUSTOM_TEMPLATE = 7;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_schedule_message, container, false);
//
//
//        initializeViews(view);
//        setupListeners();
//        requestPermissions();
//        setupSpinner();
//
//        return view;
//    }
//
//
//    private void initializeViews(View view) {
//        tvContactsSelected = view.findViewById(R.id.tvContactsSelected);
//        etMessage = view.findViewById(R.id.et_message);
//        checkboxWhatsApp = view.findViewById(R.id.checkboxWhatsApp);
//        checkboxSMS = view.findViewById(R.id.checkboxSMS);
//        checkboxTwilio = view.findViewById(R.id.checkboxTwilio);
//        spinnerTemplate = view.findViewById(R.id.spinnerTemplate);
//        btnSelectContacts = view.findViewById(R.id.btnSelectContacts);
//        textViewSelectedDateTime = view.findViewById(R.id.textViewSelectedDateTime);
//        buttonPickDate = view.findViewById(R.id.buttonPickDate);
//        buttonAddEvent = view.findViewById(R.id.buttonAddEvent);
//
//        charCount = view.findViewById(R.id.char_count);
//    }
//
//
//
//    private void setupListeners() {
//
//        apiService = RetrofitClient.getClient().create(ApiService.class);
//        // Initialize ViewModel same as ContactsFragment
//        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
//
//        contactViewModel.getSelectedCount().observe(getViewLifecycleOwner(), count -> {
//            tvContactsSelected.setText("Selected Contacts: " + count);
//        });
//
//        sharedPrefManager = new SharedPrefManager(getActivity());
//
//        etMessage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                charCount.setText(String.valueOf(maxMessageLength - s.length()));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        btnSelectContacts.setOnClickListener(v -> {
//            FragmentManager fragmentManager = getParentFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, new ContactsFragment()); // Replace with ContactFragment
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        });
//
//        buttonPickDate.setOnClickListener(v -> openDateTimePicker());
//
//        buttonAddEvent.setOnClickListener(v -> {
//
//            String message = etMessage.getText().toString();
//            String userId = sharedPrefManager.getUserId();
//
//
//            // Check which checkboxes are selected and append their IDs to the StringBuilder
//            if (checkboxTwilio.isChecked()) {
//                channelIds.append("2,"); // Assuming 2 for Messenger
//            }
//            if (checkboxWhatsApp.isChecked()) {
//                channelIds.append("3,"); // Assuming 3 for WhatsApp
//            }
//
//// Remove the trailing comma if it's there
//            if (channelIds.length() > 0) {
//                channelIds.setLength(channelIds.length() - 1);
//            }
//
//            contactViewModel.getSelectedContact().observe(getViewLifecycleOwner(), contacts -> {
//                // Use the selected contacts to send SMS
//                if (contacts != null && !contacts.isEmpty()) {
//                    scheduleMessage(userId, contacts, channelIds.toString(), templateId, message);  // Pass parameters to API
//                }
//            });
//        });
//
//
//
//
//    }
//
//    private void requestPermissions() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
//        }
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
//        }
//    }
//
//    private void setupSpinner() {
//        List<String> templates = new ArrayList<>();
//        templates.add("Select Template");
//        templates.add("Hello World");
//        templates.add("Happy Diwali");
//        templates.add("Contact");
//        templates.add("Location");
//        templates.add("Marketing Template");
//        templates.add("Custom Template");
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, templates);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTemplate.setAdapter(adapter);
//
//
//        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//               /* if (position > 0) {
//                    // Set the selected template message
//                    etMessage.setText("Selected: " + templates.get(position));
//                }*/
//                switch (position) {
//                    case 1: // Image
//                        templateId = Template.HELLO_WORLD;
//                        break;
//                    case 2: // Message
//                        templateId = Template.HAPPY_DIWALI;
//                        break;
//                    case 3: // Audio Message
//                        templateId = Template.AUDIO_MESSAGE;
//                        break;
//                    case 4: // Contact
//                        templateId = Template.CONTACT;
//                        break;
//                    case 5: // Location
//                        templateId = Template.LOCATION;
//                        break;
//                    case 6: // Marketing Template
//                        templateId = Template.MARKETING_TEMPLATE;
//                        break;
//                    case 7: // Custom Template
//                        templateId = Template.CUSTOM_TEMPLATE;
//                        break;
//                }
//                etMessage.setText(" " + templates.get(position));
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                etMessage.setText("");
//            }
//        });
//    }
//
//    private void scheduleMessage(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {
//
//        List<Contact> contacts = new ArrayList<>();
//        for (Contact contact : contactList) {
//            contacts.add(new Contact(contact.getName(),contact.getNumber()));
//            Log.d("ContactNumbers", "Contact Name: " + contact.getName() + ", Phone Number: " + contact.getNumber());
//        }
//        Log.d("Template ID", "templateId " + templateId );
//        Log.d("Channels ID", "channelsId " + channelIds );
//        boolean isWhatsAppChecked = checkboxWhatsApp.isChecked();
//
//        boolean isTwilioChecked = checkboxTwilio.isChecked();
//
//        if (message.isEmpty()) {
//            // Show error: Message cannot be empty
//            Toast.makeText(getContext(), "Please enter message", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!isWhatsAppChecked   && !isTwilioChecked ) {
//            // Show error: At least one channel must be selected
//            Toast.makeText(getContext(), "Select atleast one channel", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String[] channels = channelIds.split(",");
//        // Add message sending logic here
//        for (String channelId : channels) {
//            switch (channelId) {
//                case "2":
//                    sendMessageToWhatsAppAndTwilio(userId, contactList, channelId, templateId, message);
//                    break;
//                case "3":
//                    sendMessageToWhatsAppAndTwilio(userId, contactList, channelId, templateId, message);
//                    break;
//                default:
//                    Log.e("sendMessage", "Please select any channels " + channelId);
//                    break;
//            }
//        }
//
//    }
//
//    private void sendMessageToWhatsAppAndTwilio(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {
//        // WhatsApp API integration logic here
//       /* List<Contact> contacts = new ArrayList<>();
//        for (Contact contact : contactList) {
//            contacts.add(new Contact(contact.getName(),contact.getNumber()));
//            Log.d("ContactNumbers", "Contact Name: " + contact.getName() + ", Phone Number: " + contact.getNumber());
//        }
//*/
//        SendMessageRequest messageRequest = new SendMessageRequest(userId,contactList, channelIds, templateId, message);
//
//        Call<Void> call = apiService.sendMessage(messageRequest);
//        //      Toast.makeText(getContext(), "Message Request" +messageRequest, Toast.LENGTH_SHORT).show();
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.d("messageResponse", "Message : " + response.message());
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Message sent successfully!", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    Toast.makeText(getContext(), "Failed to send message." +response.code(),  Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("Retrofit", "Error: " + t.getMessage());
//            }
//        });
//
//    }
//
//
//    private void sendSms(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {
//
//        smsManager = SmsManager.getDefault();
//        for (Contact contact : contactList) {
//            //  phoneNumber = phoneNumber.trim(); // Remove any leading/trailing spaces
//            try {
//                smsManager.sendTextMessage(contact.getNumber(), null, message, null, null);
//                Toast.makeText(getContext(), "SMS Sent to " + contact.getNumber(), Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                Toast.makeText(getContext(), "Failed to send SMS to " + contact.getNumber(), Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    private void openDateTimePicker() {
//        // Open Date Picker
//        final Calendar currentDate = Calendar.getInstance();
//        selectedDateTime = Calendar.getInstance();
//        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
//            selectedDateTime.set(year, month, dayOfMonth);
//
//            // Open Time Picker after date selection
//            new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
//                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                selectedDateTime.set(Calendar.MINUTE, minute);
//
//                // Display selected date and time
//                textViewSelectedDateTime.setText(selectedDateTime.getTime().toString());
//            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
//
//        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
//    }
//
//  /*  private void sendMessageToTwilio(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {
//        // Twilio API integration logic here
//        SendMessageRequest messageRequest = new SendMessageRequest(userId,contactList, channelIds, templateId, message);
//
//        Call<Void> call = apiService.sendMessage(messageRequest);
//        Toast.makeText(getContext(), "Message Request" +messageRequest, Toast.LENGTH_SHORT).show();
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.d("messageResponse", "Message : " + response.message());
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Message sent successfully!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Failed to send message." +response.code(),  Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("Retrofit", "Error: " + t.getMessage());
//            }
//        });
//    }*/
//
//    private void resetMessage() {
//        etMessage.setText("");
//        charCount.setText(String.valueOf(maxMessageLength));
//    }
//
//
//
//  /*  public String getSelectedChannelIds() {
//        List<String> channelIds = new ArrayList<>();
//
//        for (Channel channel : channels) {
//            if (channel.isActive()) {
//                channelIds.add(channel.getChannelId());
//            }
//        }
//
//        return TextUtils.join(",", channelIds); // Joins channel IDs with commas
//    }*/
//
//}


//package com.example.splash1;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.Manifest;
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.google.android.material.button.MaterialButton;
//
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ScheduleMessageFragment extends Fragment {
//
//    private TextView tvContactsSelected, charCount, textViewSelectedDateTime;
//    private EditText etMessage;
//    private CheckBox checkboxWhatsApp, checkboxSMS, checkboxTwilio;
//    private Spinner spinnerTemplate;
//    private MaterialButton btnSelectContacts;
//    private Button buttonPickDate, buttonAddEvent;
//    private int maxMessageLength = 160;
//    private ContactViewModel contactViewModel;
//    private ApiService apiService;
//    private SharedPrefManager sharedPrefManager;
//    private StringBuilder channelIds = new StringBuilder();
//    private int templateId = 0;
//    private Calendar selectedDateTime;
//
//    public static final int HELLO_WORLD = 1;
//    public static final int HAPPY_DIWALI = 2;
//    public static final int AUDIO_MESSAGE = 3;
//    public static final int CONTACT = 4;
//    public static final int LOCATION = 5;
//    public static final int MARKETING_TEMPLATE = 6;
//    public static final int CUSTOM_TEMPLATE = 7;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_schedule_message, container, false);
//
//        initializeViews(view);
//        setupListeners();
//        requestPermissions();
//        setupSpinner();
//
//        return view;
//    }
//
//    private void initializeViews(View view) {
//        tvContactsSelected = view.findViewById(R.id.tvContactsSelected);
//        etMessage = view.findViewById(R.id.et_message);
//        checkboxWhatsApp = view.findViewById(R.id.checkboxWhatsApp);
//        checkboxSMS = view.findViewById(R.id.checkboxSMS);
//        checkboxTwilio = view.findViewById(R.id.checkboxTwilio);
//        spinnerTemplate = view.findViewById(R.id.spinnerTemplate);
//        btnSelectContacts = view.findViewById(R.id.btnSelectContacts);
//        textViewSelectedDateTime = view.findViewById(R.id.textViewSelectedDateTime);
//        buttonPickDate = view.findViewById(R.id.buttonPickDate);
//        buttonAddEvent = view.findViewById(R.id.buttonAddEvent);
//        charCount = view.findViewById(R.id.char_count);
//
//        sharedPrefManager = new SharedPrefManager(getActivity());
//    }
//
//    private String getUserId() {
//        String userId = sharedPrefManager.getUserId();
//        if (userId == null) {
//            Toast.makeText(getActivity(), "User ID not found. Please log in again.", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//            getActivity().finish();
//        }
//        Log.d("TAG", "Retrieved User ID: " + userId);
//        return userId;
//    }
//
//    private void setupListeners() {
//        apiService = RetrofitClient.getClient().create(ApiService.class);
//        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
//
//        contactViewModel.getSelectedCount().observe(getViewLifecycleOwner(), count -> {
//            tvContactsSelected.setText("Selected Contacts: " + count);
//        });
//
//        etMessage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                charCount.setText(String.valueOf(maxMessageLength - s.length()));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        btnSelectContacts.setOnClickListener(v -> {
//            FragmentManager fragmentManager = getParentFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, new ContactsFragment());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        });
//
//        buttonPickDate.setOnClickListener(v -> openDateTimePicker());
//
//        buttonAddEvent.setOnClickListener(v -> {
//            String message = etMessage.getText().toString();
//
//            String startDT = getIso8601Timestamp();
//
//
//            if (checkboxTwilio.isChecked()) {
//                channelIds.append("2,");
//            }
//            if (checkboxWhatsApp.isChecked()) {
//                channelIds.append("3,");
//            }
//            if (channelIds.length() > 0) {
//                channelIds.setLength(channelIds.length() - 1); // Remove last comma
//            }
//
//            contactViewModel.getSelectedContact().observe(getViewLifecycleOwner(), contacts -> {
//                if (contacts != null && !contacts.isEmpty()) {
//                    scheduleMessage(contacts, channelIds.toString(), templateId, message,startDT);
//                }
//            });
//        });
//    }
//
//    private void requestPermissions() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
//        }
//    }
//
//    private void setupSpinner() {
//        List<String> templates = new ArrayList<>();
//        templates.add("Select Template");
//        templates.add("Hello World");
//        templates.add("Happy Diwali");
//        templates.add("Contact");
//        templates.add("Location");
//        templates.add("Marketing Template");
//        templates.add("Custom Template");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, templates);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTemplate.setAdapter(adapter);
//
//        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 1:
//                        templateId = HELLO_WORLD;
//                        break;
//                    case 2:
//                        templateId = HAPPY_DIWALI;
//                        break;
//                    case 3:
//                        templateId = CONTACT;
//                        break;
//                    case 4:
//                        templateId = LOCATION;
//                        break;
//                    case 5:
//                        templateId = MARKETING_TEMPLATE;
//                        break;
//                    case 6:
//                        templateId = CUSTOM_TEMPLATE;
//                        break;
//                }
//                etMessage.setText(templates.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                etMessage.setText("");
//            }
//        });
//    }
//
//    private void scheduleMessage(List<Contact> contactList, String channelIds, Integer templateId, String message, String startDT) {
//        if (TextUtils.isEmpty(message)) {
//            Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!checkboxWhatsApp.isChecked() && !checkboxTwilio.isChecked()) {
//            Toast.makeText(getContext(), "Select at least one channel", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Get the scheduled date-time as a string
//      //  String startDT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(selectedDateTime.getTime());
//
//        // Create the ScheduledMessage object
//        ScheduledMessage scheduledMessage = new ScheduledMessage(contactList,channelIds, templateId, message, startDT );
//        String userId = getUserId();
//        // Schedule the message using your API
//        Call<ScheduleIdResponse> call = apiService.scheduleMessage(userId, scheduledMessage); // Pass userId and scheduledMessage
//        call.enqueue(new Callback<ScheduleIdResponse>() {
//            @Override
//            public void onResponse(Call<ScheduleIdResponse> call, Response<ScheduleIdResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d("messageResponse", "Message : " + response.message());
//                    ScheduleIdResponse scheduleIdResponse = response.body();
//                    Toast.makeText(getContext(), "Message Scheduled: " + scheduleIdResponse.getScheduleId(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Failed to schedule message", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ScheduleIdResponse> call, Throwable t) {
//                Log.d("ERROR", "Error On Failure : " + t.getMessage());
//                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    private void openDateTimePicker() {
//        Calendar currentDate = Calendar.getInstance();
//
//        DatePickerDialog datePicker = new DatePickerDialog(getContext(), (view, year, month, day) -> {
//            TimePickerDialog timePicker = new TimePickerDialog(getContext(), (timeView, hourOfDay, minute) -> {
//                selectedDateTime = Calendar.getInstance();
//                selectedDateTime.set(year, month, day, hourOfDay, minute);
//                updateDateTimeText();
//            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
//            timePicker.show();
//        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
//
//        datePicker.show();
//    }
//
//    private void updateDateTimeText() {
//        String formattedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(selectedDateTime.getTime());
//        textViewSelectedDateTime.setText("Selected Date & Time: " + formattedDateTime);
//    }
//
//    public String getIso8601Timestamp() {
//        return DateTimeFormatter.ISO_INSTANT.format(Instant.now()); // or Instant.ofEpochMilli(yourTime)
//    }
//
//}

package com.example.splash1;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleMessageFragment extends Fragment {

    private TextView tvContactsSelected, charCount, textViewSelectedDateTime;
    private EditText etMessage;
    private CheckBox checkboxWhatsApp, checkboxSMS, checkboxTwilio;
    private Spinner spinnerTemplate;
    private MaterialButton btnSelectContacts;
    private Button buttonPickDate, buttonAddEvent, buttonScheduleMessage;
    private int maxMessageLength = 160;
    private ContactViewModel contactViewModel;
    private ApiService apiService;
    private SharedPrefManager sharedPrefManager;
    private StringBuilder channelIds = new StringBuilder();
    private int templateId = 0;
    private Calendar selectedDateTime;

    public static final int HELLO_WORLD = 1;
    public static final int HAPPY_DIWALI = 2;
    public static final int AUDIO_MESSAGE = 3;
    public static final int CONTACT = 4;
    public static final int LOCATION = 5;
    public static final int MARKETING_TEMPLATE = 6;
    public static final int CUSTOM_TEMPLATE = 7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_message, container, false);

        initializeViews(view);
        setupListeners();
        requestPermissions();
        setupSpinner();

        return view;
    }

    private void initializeViews(View view) {
        tvContactsSelected = view.findViewById(R.id.tvContactsSelected);
        etMessage = view.findViewById(R.id.et_message);
        checkboxWhatsApp = view.findViewById(R.id.checkboxWhatsApp);
        checkboxSMS = view.findViewById(R.id.checkboxSMS);
        checkboxTwilio = view.findViewById(R.id.checkboxTwilio);
        spinnerTemplate = view.findViewById(R.id.spinnerTemplate);
        btnSelectContacts = view.findViewById(R.id.btnSelectContacts);
        textViewSelectedDateTime = view.findViewById(R.id.textViewSelectedDateTime);
        buttonPickDate = view.findViewById(R.id.buttonPickDate);
        buttonAddEvent = view.findViewById(R.id.buttonAddEvent);
        charCount = view.findViewById(R.id.char_count);
        buttonScheduleMessage = view.findViewById(R.id.buttonScheduleMessage);
        sharedPrefManager = new SharedPrefManager(getActivity());
    }

    private String getUserId() {
        String userId = sharedPrefManager.getUserId();
        if (userId == null) {
            Toast.makeText(getActivity(), "User ID not found. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        Log.d("TAG", "Retrieved User ID: " + userId);
        return userId;
    }

    private void setupListeners() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        contactViewModel.getSelectedCount().observe(getViewLifecycleOwner(), count -> {
            tvContactsSelected.setText("Selected Contacts: " + count);
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCount.setText(String.valueOf(maxMessageLength - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSelectContacts.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ContactsFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        buttonPickDate.setOnClickListener(v -> openDateTimePicker());

        buttonAddEvent.setOnClickListener(v -> {
            String message = etMessage.getText().toString();
            String startDT = getIso8601Timestamp();

            if (checkboxTwilio.isChecked()) {
                channelIds.append("2,");
            }
            if (checkboxWhatsApp.isChecked()) {
                channelIds.append("3,");
            }
            if (channelIds.length() > 0) {
                channelIds.setLength(channelIds.length() - 1); // Remove last comma
            }

            contactViewModel.getSelectedContact().observe(getViewLifecycleOwner(), contacts -> {
                if (contacts != null && !contacts.isEmpty()) {
                    scheduleMessage(contacts, channelIds.toString(), templateId, message, startDT);
                }
            });
        });

        // Schedule message button click listener
        buttonScheduleMessage.setOnClickListener(v -> {
            // Open ScheduledMessagesListFragment
            ScheduledMessagesListFragment scheduledMessagesListFragment = new ScheduledMessagesListFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, scheduledMessagesListFragment) // Ensure your container ID is correct
                    .addToBackStack(null) // Add this transaction to the back stack
                    .commit(); // Commit the transaction
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
    }

    private void setupSpinner() {
        List<String> templates = new ArrayList<>();
        templates.add("Select Template");
        templates.add("Hello World");
        templates.add("Happy Diwali");
        templates.add("Contact");
        templates.add("Location");
        templates.add("Marketing Template");
        templates.add("Custom Template");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, templates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemplate.setAdapter(adapter);

        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        templateId = HELLO_WORLD;
                        break;
                    case 2:
                        templateId = HAPPY_DIWALI;
                        break;
                    case 3:
                        templateId = CONTACT;
                        break;
                    case 4:
                        templateId = LOCATION;
                        break;
                    case 5:
                        templateId = MARKETING_TEMPLATE;
                        break;
                    case 6:
                        templateId = CUSTOM_TEMPLATE;
                        break;
                }
                etMessage.setText(templates.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etMessage.setText("");
            }
        });
    }

    private void scheduleMessage(List<Contact> contactList, String channelIds, Integer templateId, String message, String startDT) {
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkboxWhatsApp.isChecked() && !checkboxTwilio.isChecked()) {
            Toast.makeText(getContext(), "Select at least one channel", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the ScheduledMessage object
        ScheduledMessage scheduledMessage = new ScheduledMessage(contactList, channelIds, templateId, message, startDT);
        String userId = getUserId();

        // Schedule the message using your API
        Call<ScheduleIdResponse> call = apiService.scheduleMessage(userId, scheduledMessage); // Pass userId and scheduledMessage
        call.enqueue(new Callback<ScheduleIdResponse>() {
            @Override
            public void onResponse(Call<ScheduleIdResponse> call, Response<ScheduleIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("messageResponse", "Message : " + response.message());
                    ScheduleIdResponse scheduleIdResponse = response.body();
                    Toast.makeText(getContext(), "Message Scheduled: " + scheduleIdResponse.getScheduleId(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error scheduling message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleIdResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, hourOfDay, minute1) -> {
                selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year1, month1, dayOfMonth, hourOfDay, minute1);
                updateDateTimeTextView(selectedDateTime);
            }, hour, minute, true);
            timePickerDialog.show();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateDateTimeTextView(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        textViewSelectedDateTime.setText(sdf.format(calendar.getTime()));
    }

    private String getIso8601Timestamp() {
        Instant instant = selectedDateTime.toInstant();
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }
}
