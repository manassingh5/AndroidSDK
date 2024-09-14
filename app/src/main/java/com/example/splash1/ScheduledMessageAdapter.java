package com.example.splash1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduledMessageAdapter extends RecyclerView.Adapter<ScheduledMessageAdapter.ViewHolder> {

    private List<ScheduledMessage> messages;
    private Context context;

    public ScheduledMessageAdapter(List<ScheduledMessage> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scheduled_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduledMessage message = messages.get(position);
//        holder.timeTextView.setText(message.getFormattedTime()); // Ensure this method exists
        holder.messageTextView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView messageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
