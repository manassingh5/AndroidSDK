package com.example.splash1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private OnMessageClickListener listener;

    public interface OnMessageClickListener {
        void onMessageClick(Message message);
    }

    public MessageAdapter(List<Message> messageList, OnMessageClickListener listener) {
        this.messageList = messageList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.body.setText(String.valueOf(message.getBody()));
        holder.fromNumber.setText(String.valueOf(message.getFromNumber()));
        holder.toNumber.setText(String.valueOf(message.getToNumber()));
        holder.channelId.setText(String.valueOf(message.getChannelId()));
        holder.channelName.setText(String.valueOf(message.getChannelName()));
        holder.createdDT.setText(String.valueOf(message.getCreatedDT()));

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            listener.onMessageClick(message);
            Log.d("MessageAdapter", "Clicked on message from: " + message.getFromNumber());
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView body, fromNumber, toNumber, channelId, channelName, createdDT;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.text_body);
            fromNumber = itemView.findViewById(R.id.text_fromNumber);
            toNumber = itemView.findViewById(R.id.text_toNumber);
            channelId = itemView.findViewById(R.id.text_channelId);
            channelName = itemView.findViewById(R.id.text_channelName);
            createdDT = itemView.findViewById(R.id.text_createdDT);
        }
    }
}
