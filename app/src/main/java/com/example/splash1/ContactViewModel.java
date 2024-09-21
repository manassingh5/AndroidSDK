package com.example.splash1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactViewModel extends ViewModel {
   /* private final MutableLiveData<List<Contact>> selectedContacts = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isSelectAll = new MutableLiveData<>(false);
*/
   // A HashMap to store contact positions and their selected states
   private MutableLiveData<HashMap<Integer, Boolean>> selectedContacts = new MutableLiveData<>(new HashMap<>());

    // To get the state of selected contacts
    public LiveData<HashMap<Integer, Boolean>> getSelectedContacts() {
        return selectedContacts;
    }
    public HashMap<Integer, Boolean> currentSelections = selectedContacts.getValue();

    // Get the current selection state for a specific contact

    public Boolean isContactSelected(int position) {
        if (currentSelections != null) {
            return currentSelections.get(position);
        }
        return false; // Default is unselected
    }
    // Select or deselect all contacts
    public void selectAllContacts(boolean isSelected, int totalContacts) {
        if (currentSelections != null) {
            for (int i = 0; i < totalContacts; i++) {
                currentSelections.put(i, isSelected);
            }
            selectedContacts.setValue(currentSelections);
        }
    }

    // Update contact selection state
    public void setContactSelected(int position, boolean isSelected) {
        if (currentSelections != null) {
            currentSelections.put(position, isSelected);
            selectedContacts.setValue(currentSelections);  // Update LiveData
        }
    }

    public void clearSelectedContacts() {
        if (selectedContacts.getValue() != null) {
            selectedContacts.getValue().clear();
            selectedContacts.setValue(selectedContacts.getValue());
        }
    }

     // Get a count of selected contacts
    public int getSelectedContactsCount() {
    if (currentSelections == null) return 0;
    int count = 0;
    for (boolean isSelected : currentSelections.values()) {
        if (isSelected) {
            count++;
        }
    }
    return count;
}
}

