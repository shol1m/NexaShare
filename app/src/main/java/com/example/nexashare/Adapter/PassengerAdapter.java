package com.example.nexashare.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.Helper.Whatsapp;
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

        if(!passenger.isConfirmed()){
            holder.confirmedTextView.setText("Not Confirmed");
            holder.confirm.setText("Deny");
        }else{
            holder.passengerLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            holder.confirmedTextView.setText("Confirmed");
            holder.confirm.setBackgroundResource(R.drawable.baseline_whatsapp_24);
        }

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Whatsapp.sendMessageToWhatsApp();

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Test","Item has been clicked");
            }
        });
    }
    @Override
    public int getItemCount() {
        return passengersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView passengerNameTextView;
        private TextView bookedSeatsTextView;
        private TextView confirmedTextView;
        private Button confirm;
        private ImageView whatsapp;
        LinearLayout passengerLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerNameTextView = itemView.findViewById(R.id.passengerNameDetail);
            bookedSeatsTextView = itemView.findViewById(R.id.bookedSeatsDetail);
            passengerLinearLayout = itemView.findViewById(R.id.passengerLinearaLyout);
            confirmedTextView = itemView.findViewById(R.id.confirmedDetail);
            confirm = itemView.findViewById(R.id.confirm);
            whatsapp = itemView.findViewById(R.id.whatsappicon);
        }
    }
}