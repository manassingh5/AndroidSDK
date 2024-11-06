package com.example.splash1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import java.util.ArrayList;

public class ContactViewModel extends ViewModel {


    private final MutableLiveData<List<Contact>> selectedContact = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> selectedCount = new MutableLiveData<>();
    private MutableLiveData<String> userId = new MutableLiveData<>();

    public void setUserId(String id) {
        userId.setValue(id);
    }

    public LiveData<String> getUserId() {
        return userId;
    }
    public void setSelectedCount(int count) {
        selectedCount.setValue(count);
    }

    public LiveData<Integer> getSelectedCount() {
        return selectedCount;
    }


    public LiveData<List<Contact>> getSelectedContact() {
        return selectedContact;
    }

    public void addContact(List<Contact> contacts) {
        List<Contact> currentContacts = selectedContact.getValue();
        if (currentContacts != null) {

            for (Contact contact : contacts) {
                // Add contact only if it's not already selected
                if (!currentContacts.contains(contact)) {
                    currentContacts.add(contact);
                }
            }
            // Update the LiveData
            selectedContact.setValue(currentContacts);
        }
    }

    // Method to remove a contact
    public void removeContact(Contact contact) {
        List<Contact> currentContacts = selectedContact.getValue();
        if (currentContacts != null && currentContacts.contains(contact)) {
            currentContacts.remove(contact);
            selectedContact.setValue(currentContacts);
        }
    }

    // Method to clear all selected contacts
    public void clearContacts() {
        List<Contact> currentContacts = selectedContact.getValue();
        if (currentContacts != null) {
            currentContacts.clear();
            selectedContact.setValue(currentContacts);
        }
    }
}