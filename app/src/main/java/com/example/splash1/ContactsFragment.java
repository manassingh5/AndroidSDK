package com.example.splash1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView contactsRecyclerView;
    private TextView SelectedContactsCounter;
  //  private List<String> selectedContactNumbers =new ArrayList<>();
    private List<Contact> contactsList = new ArrayList<>();
    private ContactViewModel contactViewModel;
    private CheckBox selectAllCheckBox;
    private ContactsAdapter contactAdapter;
    private Button nxtBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // Initialize ViewModel
        // changes related to see the contacts counter in mychat layout
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        nxtBtn = view.findViewById(R.id.nextButton);
        SelectedContactsCounter =view.findViewById(R.id.selected_contacts_counter);

       // setupRecyclerView(view);
        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsList = fetchContactsFromPhone();
        contactAdapter = new ContactsAdapter(contactsList, contactViewModel);
     //   ContactsAdapter  contactsAdapter = new ContactsAdapter(contactsList, selectedContactNumbers, SelectedContactsCounter);
        contactsRecyclerView.setAdapter(contactAdapter);

        // to select all the contacts through checkbox code
        selectAllCheckBox = view.findViewById(R.id.selectAllCheckBox);
        selectAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contactViewModel.selectAllContacts(isChecked, contactsList.size());
            contactAdapter.notifyDataSetChanged();
        });

          /*  contactViewModel.setIsSelectAll(isChecked );
            if (isChecked) {
                contactViewModel.setSelectedContacts(new ArrayList<>(contactsList));
            } else {
                contactViewModel.setSelectedContacts(new ArrayList<>());
            }
            contactAdapter.notifyDataSetChanged();
        });*/

        contactViewModel.getSelectedContacts().observe(getViewLifecycleOwner(), selectedContacts -> {
            SelectedContactsCounter.setText("Selected Contacts: " + selectedContacts.size());
        });

        // Below code is to search the contacts
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // We don't handle submit, just search on text change
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Call filter method in adapter to filter the list based on user input
                ContactsAdapter  contactsAdapter = new ContactsAdapter(contactsList,contactViewModel);
                contactsAdapter.filter(newText);
                return true;
            }
        });
        // Handle "Next" button click to move to MyChatFragment
       /* nextButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(MyContactFragment.this)
                    .navigate(R.id.action_myContactFragment_to_myChatFragment);
        });*/
        nxtBtn.setOnClickListener(v -> openMyChatFragment());
        
        return view;
    }

    private void openMyChatFragment() {
        Intent intent = new Intent(getActivity(), MyChatsFragment.class);
        startActivity(intent);
    }

   /* private List<Contact> getContacts() {
        List<Contact> contactsList = new ArrayList<>();

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(new Contact(name, phoneNumber));

            }
            cursor.close();
        }
        return new ArrayList<>();
    }*/

/*    private void setupRecyclerView(View view) {
        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load contacts from phone (implement your contact fetching logic here)
        List<Contact> contactsList = fetchContactsFromPhone();
        // Setup adapter with contact list and selection logic

        ContactsAdapter  contactsAdapter = new ContactsAdapter(contactsList, selectedContactNumbers, SelectedContactsCounter);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }*/

    private List<Contact> fetchContactsFromPhone() {
        // Implement logic to fetch contacts from the phone and return a list of Contact objects
        // Each Contact object should have a name and phone number

        List<Contact> contactsList = new ArrayList<>();

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(new Contact(name, phoneNumber));

            }
            cursor.close();
        }
        return contactsList;
        //   return new ArrayList<>();
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
