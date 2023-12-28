package com.example.nexashare.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.Models.Notification;
import com.example.nexashare.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private OnItemClickListener listener;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView messageTextView;
        LinearLayout notificationLayout;

        public NotificationViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            notificationLayout = itemView.findViewById(R.id.notificationLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification currentNotification = notifications.get(position);
        holder.titleTextView.setText(currentNotification.getTitle());
        holder.messageTextView.setText(currentNotification.getMessage());

        // Change background color based on read state
        if (currentNotification.isRead()) {
            holder.notificationLayout.setBackgroundColor(R.color.light_grey);
        } else {
            holder.notificationLayout.setBackgroundColor(R.color.dark_grey);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
