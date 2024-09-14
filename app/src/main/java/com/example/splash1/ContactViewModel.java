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
   private final MutableLiveData<HashMap<Integer, Boolean>> selectedContacts = new MutableLiveData<>(new HashMap<>());

    // To get the state of selected contacts
    public LiveData<HashMap<Integer, Boolean>> getSelectedContacts() {
        return selectedContacts;
    }

    /*public LiveData<List<Contact>> getSelectedContacts() {
        return selectedContacts;
    }
*/
    // Select or deselect all contacts
    public void selectAllContacts(boolean isSelected, int totalContacts) {
        HashMap<Integer, Boolean> currentSelections = selectedContacts.getValue();
        if (currentSelections != null) {
            for (int i = 0; i < totalContacts; i++) {
                currentSelections.put(i, isSelected);
            }
            selectedContacts.setValue(currentSelections);
        }
    }

    // Update contact selection state
    public void setContactSelected(int position, boolean isSelected) {
        HashMap<Integer, Boolean> currentSelections = selectedContacts.getValue();
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
    /*public void setSelectedContacts(List<Contact> contacts) {
        selectedContacts.setValue(contacts);
    }*/

   /* public LiveData<Boolean> getIsSelectAll() {
        return isSelectAll;
    }*/

/*    public void setIsSelectAll(boolean selectAll) {
        isSelectAll.setValue(selectAll);
    }*/
// Get a count of selected contacts
public int getSelectedContactsCount() {
    HashMap<Integer, Boolean> currentSelections = selectedContacts.getValue();
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

