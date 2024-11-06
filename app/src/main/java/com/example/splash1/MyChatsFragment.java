package com.example.splash1;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyChatsFragment extends Fragment {

    private TextView tvContactsSelected, charCount;
    private EditText etMessage;
    private CheckBox checkboxWhatsApp, checkboxSMS, checkboxTwilio;
    private Spinner spinnerTemplate;
    private MaterialButton btnSelectContacts, btnSend, btnReset, btnSchedule;
    private int maxMessageLength = 160;
    private EditText messageInput;
    private TextView messageCount;
    private int currentMessageCount = 0;
    private ContactViewModel contactViewModel;
    private ApiService apiService;
    private SmsManager smsManager;
    private SharedPrefManager sharedPrefManager;
    StringBuilder channelIds = new StringBuilder();
    private HashMap<String, Integer> templateIdMap;
    private Integer templateId;
    public static final int IMAGE = 1;
    public static final int MESSAGE = 2;
    public static final int AUDIO_MESSAGE = 3;
    public static final int CONTACT = 4;
    public static final int LOCATION = 5;
    public static final int MARKETING_TEMPLATE = 6;
    public static final int CUSTOM_TEMPLATE = 7;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_chats, container, false);


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
        btnSend = view.findViewById(R.id.btnSend);
        btnReset = view.findViewById(R.id.btnReset);
    //    btnSchedule = view.findViewById(R.id.btnSchedule);
        charCount = view.findViewById(R.id.char_count);
    }

    private void setupListeners() {

        apiService = RetrofitClient.getClient().create(ApiService.class);
        // Initialize ViewModel same as ContactsFragment
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        contactViewModel.getSelectedCount().observe(getViewLifecycleOwner(), count -> {
            tvContactsSelected.setText("Selected Contacts: " + count);
        });

        sharedPrefManager = new SharedPrefManager(getActivity());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");
        String userId = sharedPreferences.getString("userId","0");
        // Retrieve the message count from SharedPreferences
        currentMessageCount = sharedPreferences.getInt("messageCount", 0);
        messageCount.setText("Message Sent: " + currentMessageCount);

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
            fragmentTransaction.replace(R.id.fragment_container, new ContactsFragment()); // Replace with ContactFragment
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        btnSend.setOnClickListener(v -> {

            String message = etMessage.getText().toString();
          //  String userId = sharedPrefManager.getUserId();


            // Check which checkboxes are selected and append their IDs to the StringBuilder
            if (checkboxSMS.isChecked()) {
                channelIds.append("1,"); // Assuming 1 for SMS
            }
            if (checkboxTwilio.isChecked()) {
                channelIds.append("2,"); // Assuming 2 for Messenger
            }
            if (checkboxWhatsApp.isChecked()) {
                channelIds.append("3,"); // Assuming 3 for WhatsApp
            }

// Remove the trailing comma if it's there
            if (channelIds.length() > 0) {
                channelIds.setLength(channelIds.length() - 1);
            }

            contactViewModel.getSelectedContact().observe(getViewLifecycleOwner(), contacts -> {
                // Use the selected contacts to send SMS
                if (contacts != null && !contacts.isEmpty()) {
                    sendMessage(userId, contacts, channelIds.toString(), templateId, message);  // Pass parameters to API
                }
            });
        });

        btnReset.setOnClickListener(v -> resetMessage());

    //    btnSchedule.setOnClickListener(v -> openScheduleFragment());
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
        }
    }

    private void setupSpinner() {
        // Define a mapping for template names to IDs
        HashMap<String, Integer> templateIdMap = new HashMap<>();
        templateIdMap.put("Hello World", 1);
        templateIdMap.put("Happy Diwali", 2);
        templateIdMap.put("Contact", 3);
        templateIdMap.put("Location", 4);
        templateIdMap.put("Marketing Template", 5);
        templateIdMap.put("Custom Template", 6);

        // Initialize templates list with options
        List<String> templates = new ArrayList<>();
        templates.add("Select Template");
        templates.add("Hello World");
        templates.add("Happy Diwali");
        templates.add("Contact");
        templates.add("Location");
        templates.add("Marketing Template");
        templates.add("Custom Template");

        // Set up the spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, templates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemplate.setAdapter(adapter);

        // Set up item selected listener
        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTemplate = (String) parent.getItemAtPosition(position);
                 templateId = templateIdMap.get(selectedTemplate);

                /*if (templateId != null) {
                    // Set the template ID and message based on the selection
                    etMessage.setText(" " + templateId);
                } else {
                    etMessage.setText(""); // Clear message for "Select Template"
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                templateId = null; // Clear message when no item is selected
            }
        });
    }


    private void sendMessage(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {

        List<Contact> contacts = new ArrayList<>();
        for (Contact contact : contactList) {
            contacts.add(new Contact(contact.getName(),contact.getNumber()));
            Log.d("ContactNumbers", "Contact Name: " + contact.getName() + ", Phone Number: " + contact.getNumber());
        }
        Log.d("Template ID", "templateId " + templateId );
        Log.d("Channels ID", "channelsId " + channelIds );
        boolean isWhatsAppChecked = checkboxWhatsApp.isChecked();
        boolean isSmsChecked = checkboxSMS.isChecked();
        boolean isTwilioChecked = checkboxTwilio.isChecked();

        if (message.isEmpty()) {
            // Show error: Message cannot be empty
            Toast.makeText(getContext(), "Please enter message", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isWhatsAppChecked && !isSmsChecked && !isTwilioChecked ) {
            // Show error: At least one channel must be selected
            Toast.makeText(getContext(), "Select atleast one channel", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] channels = channelIds.split(",");
        // Add message sending logic here
        for (String channelId : channels) {
            switch (channelId) {
                case "1":
                    // Call sendSms if SMS (1) is selected
                    sendSms(userId, contactList, channelId, templateId, message);
                    break;
                case "2":
                    sendMessageToWhatsAppAndTwilio(userId, contactList, channelId, templateId, message);
                    break;
                case "3":
                    sendMessageToWhatsAppAndTwilio(userId, contactList, channelId, templateId, message);
                    break;
                default:
                    Log.e("sendMessage", "Please select any channels " + channelId);
                    break;
            }
        }

    }

    private void sendMessageToWhatsAppAndTwilio(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {
        // WhatsApp API integration logic here
       /* List<Contact> contacts = new ArrayList<>();
        for (Contact contact : contactList) {
            contacts.add(new Contact(contact.getName(),contact.getNumber()));
            Log.d("ContactNumbers", "Contact Name: " + contact.getName() + ", Phone Number: " + contact.getNumber());
        }
*/
        SendMessageRequest messageRequest = new SendMessageRequest(userId, contactList, channelIds, templateId, message);

        Call<Void> call = apiService.sendMessage(messageRequest);
        //      Toast.makeText(getContext(), "Message Request" +messageRequest, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("messageResponse", "Message : " + response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Message sent successfully!", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getContext(), "Failed to send message." + response.code(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage());
            }
        });
    }

        private int updateMessageCount(int count) {
            currentMessageCount += count; // Update the current message count
            messageCount.setText("Message Sent: " +currentMessageCount); // Update the UI

            // Save the updated message count to SharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("messageCount", currentMessageCount);
            editor.apply();

            return currentMessageCount;
        }


        private void updateMessageCountInAzure(String userId, int count) {
            // Retrieve user ID (or however you identify the user)

            UpdateMessageCountRequest request = new UpdateMessageCountRequest(userId, count);
            //ApiService apiService = ApiService.getClient().create(ApiService.class);

            Call<ResponseBody> call = apiService.updateMessageCount(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Handle successful response
                        Log.d("UpdateMessageCount", "Message count updated successfully.");
                    } else {
                        // Handle errors (e.g., 4xx, 5xx responses)
                        Log.e("UpdateMessageCount", "Failed to update message count: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle failure
                    Log.e("UpdateMessageCount", "API call failed: " + t.getMessage());
                }
            });
        }



    private void sendSms(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {

        smsManager = SmsManager.getDefault();
        for (Contact contact : contactList) {
            //  phoneNumber = phoneNumber.trim(); // Remove any leading/trailing spaces
            try {
                smsManager.sendTextMessage(contact.getNumber(), null, message, null, null);
                Toast.makeText(getContext(), "SMS Sent to " + contact.getNumber(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to send SMS to " + contact.getNumber(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

  /*  private void sendMessageToTwilio(String userId ,List<Contact> contactList, String channelIds, Integer templateId, String message) {
        // Twilio API integration logic here
        SendMessageRequest messageRequest = new SendMessageRequest(userId,contactList, channelIds, templateId, message);

        Call<Void> call = apiService.sendMessage(messageRequest);
        Toast.makeText(getContext(), "Message Request" +messageRequest, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("messageResponse", "Message : " + response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Message sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to send message." +response.code(),  Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage());
            }
        });
    }*/

    private void resetMessage() {
        etMessage.setText("");
        charCount.setText(String.valueOf(maxMessageLength));
    }

    private void openScheduleFragment() {
        // Logic to open the schedule fragment
    }

  /*  public String getSelectedChannelIds() {
        List<String> channelIds = new ArrayList<>();

        for (Channel channel : channels) {
            if (channel.isActive()) {
                channelIds.add(channel.getChannelId());
            }
        }

        return TextUtils.join(",", channelIds); // Joins channel IDs with commas
    }*/

}