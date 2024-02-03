package com.example.nexashare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.Models.Passenger;
import com.example.nexashare.R;

import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.ViewHolder> {
    private List<Passenger> passengersList;
    private static Context context;

    public PassengerAdapter(List<Passenger> passengersList) {
        this.passengersList = passengersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Passenger passenger = passengersList.get(position);
        holder.passengerNameTextView.setText(passenger.getName());
        holder.bookedSeatsTextView.setText("Booked Seats: " + passenger.getBookedSeats());
//        if(!passenger.isConfirmed()){
////            holder.passengerLinearLayout.setBackgroundColor(context.getColor(R.color.light_grey));
//            holder.passengerLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
//        }else{
//            holder.passengerLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
//        }
    }

    @Override
    public int getItemCount() {
        return passengersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView passengerNameTextView;
        private TextView bookedSeatsTextView;
        private TextView confirmedTextView;
        LinearLayout passengerLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerNameTextView = itemView.findViewById(R.id.passengerNameDetail);
            bookedSeatsTextView = itemView.findViewById(R.id.bookedSeatsDetail);
            passengerLinearLayout = itemView.findViewById(R.id.passengerLinearaLyout);
        }
    }
}