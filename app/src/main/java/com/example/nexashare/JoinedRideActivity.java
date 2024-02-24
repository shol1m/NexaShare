package com.example.nexashare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nexashare.Models.MyData;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JoinedRideActivity extends AppCompatActivity {
    public TextView driverName;
    public TextView pickupLocation;
    public TextView dropoffLocation;
    public TextView driverPhoneNumber;
    public TextView bookedSeats;
    public TextView pickupTime;
    public ImageView back,whatsapp;
    static RecyclerView recyclerViewPassengers;
    String formattedTime;
    String selectedPickupLocation,rideId;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    CreatedFragment createdFragment = new CreatedFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_ride);

        back = findViewById(R.id.joinedRidesBack);
        driverName = findViewById(R.id.joinedRideDriverNameDetail);
        pickupLocation = findViewById(R.id.joinedRidePickupLocationDetail);
        dropoffLocation = findViewById(R.id.joinedRideDropoffLocationDetail);
        driverPhoneNumber = findViewById(R.id.joinedRideDriverPhoneDetail);
        bookedSeats = findViewById(R.id.joinedRideBookedSeats);
        pickupTime = findViewById(R.id.joinedRidePickupTimeDetail);
        whatsapp = findViewById(R.id.whatsappicon);

        rideId = getIntent().getStringExtra("documentId");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, createdFragment)
                        .commit();

            }
        });

        db.collection("rides")
                .document(rideId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, you can retrieve data
                        String driverNameString = documentSnapshot.getString("name");
                        String pickupLocationString = documentSnapshot.getString("source");
                        String dropoffLocationString = documentSnapshot.getString("destination");
                        String driverPhoneNumberString = documentSnapshot.getString("phone_number");
                        int availableSeatsInt = Math.toIntExact(documentSnapshot.getLong("seats"));
                        String pickupTimeString = documentSnapshot.getString("date_and_time");


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

//                        updateEventDetails();
                        driverName.setText("Driver Name: " + driverNameString);
                        pickupLocation.setText("Pickup Location: "+pickupLocationString);
                        dropoffLocation.setText("Dropoff Location: "+dropoffLocationString);
                        driverPhoneNumber.setText("Driver PhoneNumber: "+driverPhoneNumberString);
                        pickupTime.setText("Pickup Time: "+formattedTime);

                        // Update your UI or perform other actions with the data
                    } else {
                        // Document does not exist
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e(TAG, "Error fetching event details ", e);
                });
        db.collection("rides")
                .document(rideId)
                .collection("joinedUsers")
                .document(MyData.userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, you can retrieve data
                        Long bookedSeatsString = documentSnapshot.getLong("bookedSeats");
                        bookedSeats.setText("Booked seats: "+bookedSeatsString);
                        // Update your UI or perform other actions with the data
                    } else {
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e(TAG, "Error fetching event details ", e);
                });
    }
}