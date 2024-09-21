package com.example.splash1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<Contact> contactsList; // List of contacts fetched from the phone
    private ContactViewModel contactViewModel;
    private HashMap<Integer, Boolean> checkedStates = new HashMap<>();

   public ContactsAdapter(List<Contact> contactsList, ContactViewModel contactViewModel) {
        this.contactsList = contactsList;
       this.contactViewModel = contactViewModel;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactsList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhoneNumber());

        // Get the current selection state from ViewModel for the contact at this position
        Boolean isSelected = contactViewModel.isContactSelected(position);
        // Remove the listener temporarily to avoid triggering it when updating the checked state
        holder.contactCheckBox.setOnCheckedChangeListener(null);
        // Set the checkbox state based on the selected status
        if (isSelected != null) {
            holder.contactCheckBox.setChecked(isSelected);
        } else {
            holder.contactCheckBox.setChecked(false); // Default unchecked if not found
        }

        // Re-attach listener to update the selection state when user interacts
        holder.contactCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // When checkbox is checked/unchecked, update the ViewModel with the new state
            contactViewModel.setContactSelected(position, isChecked);
        });
    }


       /*

      //  holder.contactCheckBox.setChecked(contact.isSelected());
        // Observe the selected contacts from contactViewModel
        contactViewModel.getSelectedContacts().observe((LifecycleOwner) holder.itemView.getContext(), selectedContacts -> {
            Boolean isChecked = selectedContacts.get(position);
            holder.contactCheckBox.setChecked(isChecked != null && isChecked);
        });

        // Update selected state in ViewModel when checkbox changes
        holder.contactCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contactViewModel.setContactSelected(position, isChecked);
            /*contact.setSelected(isChecked);
            if (isChecked) {
                contactViewModel.setContactSelected(position, isChecked);
            } else {
                contactViewModel.clearSelectedContacts();
            }
        });
      //  holder.bind(contact, contactViewModel);


    }*/

 /*   @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactsList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhoneNumber());

        holder.itemView.setOnClickListener(v -> {
            String phoneNumber = contact.getPhoneNumber();
            if (selectedContacts.contains(contact.getPhoneNumber())) {
                selectedContacts.remove(phoneNumber);
                v.setBackgroundColor(Color.WHITE); // Unselect
            } else {
                selectedContacts.add(phoneNumber);
                v.setBackgroundColor(Color.LTGRAY); // Select
            }
          //  updateCounter();
        });
    }*/

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    /*private void updateCounter() {
        counterTextView.setText("Selected: " + selectedContacts.size());
    }*/

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone;
        private CheckBox contactCheckBox;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhoneNumber);
            contactCheckBox = itemView.findViewById(R.id.contactCheckBox);
        }

      /*  public void bind(Contact contact, ContactViewModel contactViewModel) {
            contactName.setText(contact.getName());
            contactPhone.setText(contact.getPhoneNumber());
            contactViewModel.getSelectedContacts().observe((LifecycleOwner) itemView.getContext(), selectedContacts -> {
              //  contactCheckBox.setChecked(contact.isSelected());
                contactCheckBox.setChecked(selectedContacts.contains(contact));
            });

            contactCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
               // contact.setSelected(isChecked);
                List<Contact> selectedContacts = new ArrayList<>(contactViewModel.getSelectedContacts().getValue());
                if (isChecked) {
                    selectedContacts.add(contact);
                } else {
                    selectedContacts.remove(contact);
                }
                contactViewModel.setSelectedContacts(selectedContacts);
            });

            contactViewModel.getIsSelectAll().observe((LifecycleOwner) itemView.getContext(), isSelectAll -> {
                if (isSelectAll) {
                    contactCheckBox.setChecked(true);
                }
            });
        }*/
    }

    public void filter(String text) {
        List<Contact> filteredList = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            // If search text is empty, restore full contact list
            filteredList.addAll(contactsList);
        } else {
            for (Contact contact : contactsList) {
                if (contact.getName().toLowerCase().contains(text.toLowerCase()) ||
                        contact.getPhoneNumber().contains(text)) {
                    filteredList.add(contact);
                }
            }
        }
        updateList(filteredList);
    }
    public void updateList(List<Contact> filteredList) {
        contactsList.clear();
        contactsList.addAll(filteredList);
        notifyDataSetChanged(); // Refresh RecyclerView
    }
}
