package com.example.splash1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ScheduledMessagesAdapter extends RecyclerView.Adapter<ScheduledMessagesAdapter.ViewHolder> {
    private List<ScheduleDetailsResponse> scheduleList;

    public ScheduledMessagesAdapter(List<ScheduleDetailsResponse> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scheduled_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleDetailsResponse schedule = scheduleList.get(position);
        holder.messageTextView.setText("Message: " + schedule.getMessage());
        holder.startDateTextView.setText("Scheduled for: " + formatDateTime(schedule.getStartDT()));
        holder.statusTextView.setText(schedule.isActive() ? "Active" : "Inactive");
    }

    private String formatDateTime(String dateTime) {
        return dateTime; // Format date if required
    }

    @Override
    public int getItemCount() {
        return scheduleList != null ? scheduleList.size() : 0;
    }

    public void updateData(List<ScheduleDetailsResponse> newScheduleList) {
        this.scheduleList = newScheduleList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView startDateTextView;
        TextView statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textViewMessage);
            startDateTextView = itemView.findViewById(R.id.textViewStartDate);
            statusTextView = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
