package com.example.splash1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ContactsFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView contactsRecyclerView;
    private TextView SelectedContactsCounter;
    private List<Contact> contactsList = new ArrayList<>();
    private ContactViewModel contactViewModel;
    private CheckBox selectAllCheckBox;
    private ContactsAdapter contactAdapter;
    private Button nxtBtn;
    private int selectedCount = 0;
    private List<Contact> filteredContactList;
    private static final int READ_CONTACTS_PERMISSION_CODE = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_CODE);
        }
        else{
            // Permission is granted, proceed with accessing contacts
            fetchContactsFromPhone();
        }

        // Initialize ViewModel
        // changes related to see the contacts counter in mychat layout
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        nxtBtn = view.findViewById(R.id.nextButton);
        SelectedContactsCounter =view.findViewById(R.id.selected_contacts_counter);

        // setupRecyclerView(view);
        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);

        contactsList = fetchContactsFromPhone();
        filteredContactList = new ArrayList<>(contactsList);
        contactAdapter = new ContactsAdapter(filteredContactList, (contact, isSelected) -> {
            if (isSelected) {
                contactViewModel.addContact(Collections.singletonList(contact));  // Add the selected contact
            } else {
                contactViewModel.removeContact(contact);  // Remove if deselected
            }
            updateCounter(isSelected);
        });

        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsRecyclerView.setAdapter(contactAdapter);

        // to select all the contacts through checkbox code
        selectAllCheckBox = view.findViewById(R.id.selectAllCheckBox);
        selectAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contactAdapter.selectAll(isChecked);
            selectedCount = isChecked ? contactsList.size() : 0;
            updateSelectedCountText();
            contactViewModel.setSelectedCount(selectedCount);  // Update ViewModel
        });

        // Below code is to search the contacts
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterContacts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);  // Filter contacts as the user types
                return true;
            }
        });

        // Handle "Next" button click to move to MyChatFragment
        nxtBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new MyChatsFragment()); // Replace with ContactFragment
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });


        return view;
     }
        @Override
        public void onResume() {
            super.onResume();
            // Clear selected contacts when returning to ContactsFragment
            contactViewModel.clearContacts();
            resetSelection();
        }


    private void filterContacts(String query) {
        filteredContactList.clear();  // Clear the current filtered list
        if (query.isEmpty()) {
            // If query is empty, show all contacts
            filteredContactList.addAll(contactsList);
        } else {
            // Convert query to lowercase for case-insensitive search
            String lowerCaseQuery = query.toLowerCase();
            for (Contact contact : contactsList) {
                // Check if contact name or phone number contains the query
                if (contact.getName().toLowerCase().contains(lowerCaseQuery) ||
                        contact.getNumber().contains(lowerCaseQuery)) {
                    filteredContactList.add(contact);  // Add matching contact to filtered list
                }
            }
        }
        contactAdapter.notifyDataSetChanged();
    }

 /*   @Override
    public void onResume() {
        super.onResume();
        // Reset selected contacts and counter when returning to MyContactFragment
        resetSelection();
    }*/

    // Method to reset contact selection and counter
    private void resetSelection() {
        selectedCount = 0;  // Reset counter
        contactViewModel.setSelectedCount(selectedCount);  // Reset the ViewModel counter
        contactAdapter.selectAll(false);  // Deselect all contacts
        updateSelectedCountText();  // Update UI with the reset counter
        selectAllCheckBox.setChecked(false);  // Uncheck "Select All" checkbox
    }

    private void updateCounter(boolean isSelected) {
        if (isSelected) {
            selectedCount++;
        } else if (selectedCount > 0) {  // Prevent counter going negative
            selectedCount--;
        }
        updateSelectedCountText();
        contactViewModel.setSelectedCount(selectedCount);  // Update ViewModel
    }

    private void updateSelectedCountText() {
        SelectedContactsCounter.setText("Selected: " + selectedCount);
    }  

   /* private List<Contact> fetchContactsFromPhone() {
        // Implement logic to fetch contacts from the phone and return a list of Contact objects
        // Each Contact object should have a name and phone number
        String countryCode = "+91";
        List<Contact> contactsList = new ArrayList<>();
        // HashSet to track unique phone numbers
        HashSet<String> phoneNumbersSet = new HashSet<>();
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
               // contactsList.add(new Contact(name, phoneNumber));
                // Normalize phone number to avoid duplicates with different formats
                phoneNumber = phoneNumber.replaceAll("\\s", "").replaceAll("-", "");

                //  Validate phone number length and Only add contact if phone number is not already in the HashSet
                if (phoneNumber.length() <= 10 && !phoneNumbersSet.contains(phoneNumber)) {
                    phoneNumbersSet.add(phoneNumber);
                    String fullPhoneNumber = countryCode + phoneNumber;
                    contactsList.add(new Contact(name, fullPhoneNumber));
                }

            }
            cursor.close();
        }
        return contactsList;
        //   return new ArrayList<>();
    }*/

    private List<Contact> fetchContactsFromPhone() {
    //    String countryCode = "+91"; // Set your desired country code
        List<Contact> contactsList = new ArrayList<>();
        HashSet<String> uniquePhoneNumbers = new HashSet<>(); // Tracks unique phone numbers with name

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            );

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(
                            cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    );
                    String phoneNumber = cursor.getString(
                            cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    );

                    // Normalize the phone number by removing spaces, dashes, and parentheses
                    phoneNumber = phoneNumber.replaceAll("\\s", "").replaceAll("-", "")
                            .replaceAll("\\(", "").replaceAll("\\)", "");

                    // Create a unique key by combining name and phone number
                    String uniqueKey = name + phoneNumber;

                    // Check if the phone number and name combination is unique
                    if (!uniquePhoneNumbers.contains(uniqueKey)) {
                        uniquePhoneNumbers.add(uniqueKey);
                     //   String fullPhoneNumber = countryCode + phoneNumber;
                        contactsList.add(new Contact(name, phoneNumber));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure the cursor is closed
            }
        }

        return contactsList;
    }


    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                fetchContactsFromPhone();
            } else {
                // Permission was denied, you can show a message to the user
                Toast.makeText(getContext(), "Contact permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadContactList() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                // Handle file selection
                String fileName = getFileName(uri);
                Toast.makeText(getContext(), "Selected file: " + fileName, Toast.LENGTH_SHORT).show();
                // Add logic to read and process the contact list from the file
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    //         result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
