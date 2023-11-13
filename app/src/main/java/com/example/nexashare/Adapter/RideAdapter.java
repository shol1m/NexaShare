package com.example.nexashare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nexashare.R;

import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    private List<Ride> rides;
    private OnRideClickListener rideClickListener;

    public RideAdapter(List<Ride> rides, OnRideClickListener onRideClickListener) {
        this.rides = rides;
        this.rideClickListener = rideClickListener;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ride_item, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride ride = rides.get(position);

        holder.textViewDriverName.setText(ride.getDriverName());
        holder.textViewPickup.setText(ride.getSource());
        holder.textViewDropoff.setText(ride.getDestination());
        holder.textViewDateTime.setText(ride.getDate_and_time().toString());

        holder.buttonSelectRide.setOnClickListener(v -> {
            if (rideClickListener != null) {
                rideClickListener.onRideClick(ride);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDriverName;
        public TextView textViewDropoff;
        public TextView textViewPickup;
        public TextView textViewDateTime;
        public Button buttonSelectRide;

        public RideViewHolder(View itemView) {
            super(itemView);
            textViewDriverName = itemView.findViewById(R.id.driverNameTxt);
            textViewDropoff = itemView.findViewById(R.id.destinationTxt);
            textViewPickup = itemView.findViewById(R.id.sourceTxt);
            textViewDateTime = itemView.findViewById(R.id.timeTxt);
            buttonSelectRide = itemView.findViewById(R.id.buttonSelectRide);
        }
    }

    public interface OnRideClickListener {
        void onRideClick(Ride ride);
    }
}
