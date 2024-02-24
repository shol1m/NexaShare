package com.example.nexashare.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.Models.EventPickupDetail;
import com.example.nexashare.R;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

public class EventPickupDescriptionAdapter extends RecyclerView.Adapter<EventPickupDescriptionAdapter.PickupViewHolder> {
    private List<EventPickupDetail> pickupsList;

    public EventPickupDescriptionAdapter(List<EventPickupDetail> pickupsList) {
        this.pickupsList = pickupsList;
    }

    @NonNull
    @Override
    public PickupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_pickup_item, parent, false);
        return new PickupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickupViewHolder holder, int position) {
        EventPickupDetail pickup = pickupsList.get(position);

        // Set pickup details in the ViewHolder
        holder.locationTextView.setText("Location: " + pickup.getPickupLocation());
        holder.timeTextView.setText("Time: " + pickup.getPickupTime());
        holder.seatsTextView.setText("Available Seats: " + pickup.getAvailableSeats());
    }

    @Override
    public int getItemCount() {
        return pickupsList.size();
    }

    static class PickupViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;
        TextView timeTextView;
        TextView seatsTextView;

        PickupViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.event_pickup_location_txt);
            timeTextView = itemView.findViewById(R.id.event_pickup_time_txt);
            seatsTextView = itemView.findViewById(R.id.event_pickup_available_seats_txt);
//            locationTextView = itemView.findViewById(R.id.locationTextView);
//            timeTextView = itemView.findViewById(R.id.timeTextView);
//            seatsTextView = itemView.findViewById(R.id.seatsTextView);
        }
    }

}
