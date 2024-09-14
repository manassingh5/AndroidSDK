package com.example.splash1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.CircularArray;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyChatsFragment extends Fragment {

    private ContactViewModel contactViewModel;
    private static final int MAX_CHAR_LIMIT = 160;
    private EditText messageInput;
    private TextView charCount;
    private TextView selectedContactCounter;
    private Button btnWhatsApp;
    private Button btnText;
    private Spinner spinnerTemplates;
    private RadioGroup radioGroupTemplates;
    private RadioButton rbTemplate;
    private Button btnSend;
    private Button btnReset;
    private Button btnSchedule;
    private Button buttonSendTemplate;
    private ApiService apiService;


//    private Button btnSelectContacts; // New button for contact selection


    private SmsManager smsManager;
    private static final int SMS_PERMISSION_CODE = 2;
    private static final int READ_CONTACTS_PERMISSION_CODE = 3;
    private  List<Contact> contactList =new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_my_chats, container, false);

        // Initialize views
        selectedContactCounter = view.findViewById(R.id.tvContactsSelected);
        messageInput = view.findViewById(R.id.et_message);
        charCount = view.findViewById(R.id.char_count);
        btnWhatsApp = view.findViewById(R.id.btnWhatsApp);
        btnText = view.findViewById(R.id.btnText);
        spinnerTemplates = view.findViewById(R.id.spinnerTemplates);
        radioGroupTemplates = view.findViewById(R.id.radioGroupTemplates);
        rbTemplate = view.findViewById(R.id.rbTemplate);
        btnSend = view.findViewById(R.id.btnSend);
        btnReset = view.findViewById(R.id.btnReset);
        btnSchedule = view.findViewById(R.id.btnSchedule);
        buttonSendTemplate = view.findViewById(R.id.buttonSendTemplate);
    //    btnSelectContacts = view.findViewById(R.id.btnSelectContacts); // Initialize the new button

        // Request permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_CODE);
        }

         apiService = RetrofitClient.getClient().create(ApiService.class);
        // Initialize ViewModel same as ContactsFragment
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        contactViewModel.getSelectedContacts().observe(getViewLifecycleOwner(), selectedContacts -> {
            int selectedCount = contactViewModel.getSelectedContactsCount();
            selectedContactCounter.setText("Selected Contacts: " + selectedCount);
        });

        // Observe the selected contacts list and update the counter
        /*contactViewModel.getSelectedContacts().observe(getViewLifecycleOwner(), selectedContacts -> {
            selectedContactCounter.setText("Selected Contacts: " + selectedContacts.size());
        });*/

        // Set initial character count
        updateCharCount(MAX_CHAR_LIMIT);

        // Set spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.template_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemplates.setAdapter(adapter);

        buttonSendTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTemplateMessage();
            }
        });

        // Handle button selections
        btnWhatsApp.setOnClickListener(v -> {
            spinnerTemplates.setVisibility(View.VISIBLE);
            radioGroupTemplates.setVisibility(View.VISIBLE);
            btnWhatsApp.setBackgroundColor(Color.parseColor("#ADD8E6")); // Light Blue
            btnText.setBackgroundColor(Color.TRANSPARENT); // Reset color
        });

        btnText.setOnClickListener(v -> {
            spinnerTemplates.setVisibility(View.GONE);
            radioGroupTemplates.setVisibility(View.GONE);
            btnText.setBackgroundColor(Color.parseColor("#ADD8E6")); // Light Blue
            btnWhatsApp.setBackgroundColor(Color.TRANSPARENT); // Reset color
        });

        btnSend.setOnClickListener(v -> {
            contactList = getSelectedContactsFromViewModel();
       // contactList = contactViewModel.getSelectedContacts().getValue();
            String message = messageInput.getText().toString();
            if (contactList != null && !contactList.isEmpty()) {
                sendSmsToMultipleContacts(contactList, message);
            }
        });

        btnReset.setOnClickListener(v -> {
            messageInput.setText("");
            updateCharCount(MAX_CHAR_LIMIT);
        });

        btnSchedule.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ScheduleMessageFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

       /* btnSelectContacts.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ContactsFragment()); // Replace with ContactFragment
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
*/
        // Handle spinner selection
        spinnerTemplates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle template selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        return view;
    }

    private void sendTemplateMessage() {
        contactList = getSelectedContactsFromViewModel();
        if (contactList.isEmpty()) {
            Log.e("", "Phone number is empty.");
            List<Contact> contactList = new ArrayList<>();
            contactList.add(new Contact("+1234567890", "Hello, this is a test message!"));
            contactList.add(new Contact("+0987654321", "Another test message!"));

// Create the request body
           /* ContactRequest contactRequest = new ContactRequest(contactList);
            TemplateMessageRequest templateMessageRequest = new TemplateMessageRequest(contactList);

            Call<ResponseBody> call = apiService.sendTemplateMessage(templateMessageRequest);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Handle success
                        Log.d("API", "Contacts sent successfully");
                    } else {
                        // Handle failure
                        Log.e("API", "Failed to send contacts");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle error
                    Log.e("API", "Error occurred: " + t.getMessage());
                }
            });*/
        }
    }



    private List<Contact> getSelectedContactsFromViewModel() {
        List<Contact> selectedContacts = new ArrayList<>();
        HashMap<Integer, Boolean> selectedMap = contactViewModel.getSelectedContacts().getValue();

        if (selectedMap != null) {
            for (Integer position : selectedMap.keySet()) {
                if (selectedMap.get(position)) {  // Check if the contact is selected
                    selectedContacts.add(contactList.get(position));  // Add selected contact to the list
                }
            }
        }
        return selectedContacts;
    }

    private void sendSmsToMultipleContacts(List<Contact> phoneNumbers, String message) {
        smsManager = SmsManager.getDefault();
        /*for (String phoneNumber : phoneNumbers) {*/
        for (Contact contact : phoneNumbers) {
          //  phoneNumber = phoneNumber.trim(); // Remove any leading/trailing spaces
            try {
                smsManager.sendTextMessage(contact.getPhoneNumber(), null, message, null, null);
                Toast.makeText(getContext(), "SMS Sent to " + contact.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to send SMS to " + contact.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

  /*  private void setSelectedContacts(String contacts) {
        selectedContacts.setText("Selected Contacts: " + contacts);
        // Assuming contact numbers are set separately for sending messages
        selectedContactNumbers.clear();
        // Example numbers, replace with actual numbers from selected contacts
        selectedContactNumbers.add("+1234567890");
        selectedContactNumbers.add("+0987654321");
    }*/

    private void updateCharCount(int count) {
        charCount.setText(count + " characters left");
        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remaining = MAX_CHAR_LIMIT - s.length();
                charCount.setText(remaining + " characters left");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
