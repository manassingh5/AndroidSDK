package com.example.splash1;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
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
    private OnContactSelectListener onContactSelectListener;
    private boolean isSelectAllMode = false; // Flag to disable individual check updates in select-all mode

    public ContactsAdapter(List<Contact> contactsList, OnContactSelectListener onContactSelectListener) {
        this.contactsList = contactsList;
        this.onContactSelectListener = onContactSelectListener;
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
        holder.contactPhone.setText(contact.getNumber());
        // Temporarily disable listener during binding to avoid incorrect counter updates
        holder.contactCheckBox.setOnCheckedChangeListener(null);
        holder.contactCheckBox.setChecked(contact.isSelected());

        // Handle individual contact selection
        holder.contactCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isSelectAllMode) {
                contact.setSelected(isChecked);
                onContactSelectListener.onContactSelected(contact ,isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void selectAll(boolean isSelected) {
        isSelectAllMode = true; // Disable individual checkbox handling
        for (Contact contact : contactsList) {
            contact.setSelected(isSelected);
        }
        notifyDataSetChanged();
        isSelectAllMode = false; // Re-enable after updating
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone;
        private CheckBox contactCheckBox;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhoneNumber);
            contactCheckBox = itemView.findViewById(R.id.contactCheckBox);
        }
    }

    public interface OnContactSelectListener {
        void onContactSelected(Contact contact,boolean isSelected);
    }

    /*public void filter(String text) {
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
    }*/
}
