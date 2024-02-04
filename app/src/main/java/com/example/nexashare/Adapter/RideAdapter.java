package com.example.nexashare.Adapter;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.FCM.APIService;
import com.example.nexashare.FCM.Client;
import com.example.nexashare.FCM.FCMSend;
import com.example.nexashare.Models.MyData;
import com.example.nexashare.Models.Ride;
import com.example.nexashare.R;
import com.example.nexashare.SingleRidesDescription;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {
    private List<Ride> rides;
    public static String formattedTime;
    public static String userId;
    public static int seats;
    private static Context context;
    private APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    public RideAdapter(Context context,List<Ride> rides) {
        this.context = context;
        this.rides = rides;
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

        holder.userId = ride.getUserId();


        String pickupTimeString = ride.getDate_and_time();

        SimpleDateFormat firestoreDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US);

        try {
            // Parse the Firestore date string into a Date object
            Date date = firestoreDateFormat.parse(pickupTimeString);

            // Define the desired format for the output
            SimpleDateFormat desiredDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

            // Format the Date object into the desired format
            formattedTime = desiredDateFormat.format(date);

            // Now 'formattedTime' contains the time in the desired format

        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing exception
        }

        holder.textViewDriverName.setText(ride.getName());
        holder.textViewPickup.setText(ride.getSource() + " To " + ride.getDestination());
        holder.textViewDateTime.setText(formattedTime);

        holder.rideId = ride.getRideId();

    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDriverName;
        public TextView textViewSeats;
        public TextView textViewPickup;
        public TextView textViewDateTime;
        public Button buttonSelectRide, joinRide;
        public String myName;
        public String userId;
        public String rideId;


        public RideViewHolder(View itemView) {
            super(itemView);
            textViewDriverName = itemView.findViewById(R.id.driverNameTxt);
            textViewPickup = itemView.findViewById(R.id.sourceTxt);
            textViewDateTime = itemView.findViewById(R.id.timeTxt);
            textViewSeats = itemView.findViewById(R.id.seatsTxt);
            joinRide = itemView.findViewById(R.id.joinRideBtn);
            joinRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Intent intent = new Intent(context, SingleRidesDescription.class);
                    intent.putExtra("rideId", rides.get(getAdapterPosition()).getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }


    public interface OnRideClickListener {
        void onRideClick(Ride ride);
    }

}
