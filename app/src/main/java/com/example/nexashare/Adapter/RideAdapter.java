package com.example.nexashare.Adapter;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.FCM.APIService;
import com.example.nexashare.FCM.Client;
import com.example.nexashare.FCM.MyResponse;
import com.example.nexashare.FCM.Data;
import com.example.nexashare.FCM.NotificationSender;
import com.example.nexashare.FCM.FCMSend;
import com.example.nexashare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {
    private List<Ride> rides;
    public static String receiverToken;
    public static String userId ,rideId,source,destination;
    public static int seats;
    private static int selectedSeats;
    private OnRideClickListener rideClickListener;
    private APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

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

        holder.textViewDriverName.setText(ride.getName());
        holder.textViewPickup.setText("From " + ride.getSource() + " To " + ride.getDestination());
        holder.textViewSeats.setText("Seats: " + ride.getSeats());
        holder.textViewDateTime.setText(ride.getDate_and_time());
        userId = ride.getUserId();
        rideId = ride.getRideId();
        seats = ride.getSeats();
        source = ride.getSource();
        destination = ride.getDestination();

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
                    showPopup(view.getContext());

                }
            });
        }
    }


    public interface OnRideClickListener {
        void onRideClick(Ride ride);
    }

    private static void showPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select number of seats to book");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_seats_selected, null);
        builder.setView(view);

        NumberPicker numberPicker = view.findViewById(R.id.seatsNumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(seats); // Set the maximum value to the available seats

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedSeats = numberPicker.getValue();
                // Handle confirm button click
                if (selectedSeats > seats) {
                    // Handle the case where the user selects more seats than available
                    Toast.makeText(context, "Selected seats exceed available seats", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform actions with the selected number of seats
                    Toast.makeText(context, "Selected Seats: " + selectedSeats, Toast.LENGTH_SHORT).show();
                    // Call a method to handle further logic with the selected number of seats
                    handleSelectedSeats(context, selectedSeats);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel button click
            }
        });

        builder.show();
    }

    // Add this method to handle further logic with the selected pickup location
    private static void handleSelectedSeats(Context context,int selectedSeats) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Object receiverUserToken = documentSnapshot.get("fcmToken");
                            if (receiverUserToken != null) {
                                receiverToken = receiverUserToken.toString();
                                FCMSend.pushNotification(
                                        context,
                                        userId,
                                        receiverToken,
                                        "Request to join ride",
                                        MyData.name + " has requested to join your ride of "+ source + " To " + destination + " Seats: "+ selectedSeats
                                );
                                updateSeats();
                                Log.d("FIRESTORE_VALUE", "Token value: " + receiverUserToken.toString());
                            } else {
                                Log.d("FIRESTORE_VALUE", "Field 'fieldName' does not exist or is null");
                            }
                        } else {
                            Log.d("FIRESTORE_VALUE", "Document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failures
                        Log.e("FIRESTORE_VALUE", "Error getting value from Firestore", e);
                    }
                });

    }
    public static void updateSeats(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rides")
                .document(rideId) // Replace with the actual document ID
                .update("seats", seats - selectedSeats)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d("FIRESTORE_VALUE", "Seats has been updated from " + seats+ " to "+selectedSeats);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.d("FIRESTORE_VALUE", "Field to update seats");
                    }
                });
    }
}

//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Select number of seats to book");
//
//        // Convert List<String> to String[]
//        String[] pickupLocationsArray = pickupLocations.toArray(new String[0]);
//
//        // Set up the layout inflater for the custom view
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.popup_pickup_location, null);
//        builder.setView(view);
//
//        // Initialize the Spinner
//        Spinner pickupSpinner = view.findViewById(R.id.pickupSpinner);
//
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, pickupLocationsArray);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        pickupSpinner.setAdapter(adapter);
//
//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Handle confirm button click
//                String selectedPickupLocation = pickupSpinner.getSelectedItem().toString();
//                // Perform actions with the selected pickup location
//                Toast.makeText(context, "Selected Pickup Location: " + selectedPickupLocation, Toast.LENGTH_SHORT).show();
//                // Call a method to handle further logic with the selected pickup location
//
//                handleSelectedPickupLocation(context, selectedPickupLocation);
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Handle cancel button click
//
//            }
//        });
//
//        builder.show();
