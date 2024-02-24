package com.example.nexashare.GroupRides;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nexashare.CreatedFragment;
import com.example.nexashare.Models.MyData;
import com.example.nexashare.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JoinedEventActivity extends AppCompatActivity {
    public TextView eventName;
    public TextView eventLocation;
    public TextView organizerPhoneNumber;
    public TextView bookedSeats;
    public TextView pickupTime;
    public TextView pickupLocation;
    public ImageView back;
    static RecyclerView recyclerViewPassengers;
    String formattedTime;
    String selectedPickupLocation,eventId,pickupId;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    CreatedFragment createdFragment = new CreatedFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_event);

        back = findViewById(R.id.joinedEventBack);
        eventName = findViewById(R.id.joinedEventNameDetail);
        eventLocation = findViewById(R.id.joinedEventLocationDetail);
        organizerPhoneNumber = findViewById(R.id.organizerPhoneDetail);
        bookedSeats = findViewById(R.id.bookedSeatsDetail);
        pickupTime = findViewById(R.id.joinedPickupTimeDetail);
        pickupLocation = findViewById(R.id.joinedPickupLocationDetail);

        eventId = getIntent().getStringExtra("documentId");
        pickupId = getIntent().getStringExtra("pickupDocumentId");

        db.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, you can retrieve data
                        String eventNameString = documentSnapshot.getString("eventName");
                        String eventLocationString = documentSnapshot.getString("eventLocation");
                        String organizerPhoneNumberString = documentSnapshot.getString("organizerPhoneNumber");

//                        updateEventDetails();
                        eventName.setText("Event Name: " + eventNameString);
                        eventLocation.setText("Event Location: "+eventLocationString);
                        organizerPhoneNumber.setText("Organizer PhoneNumber: "+organizerPhoneNumberString);

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
        db.collection("events")
                .document(eventId)
                .collection("pickups")
                .document(pickupId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String pickupLocationString = documentSnapshot.getString("pickupLocation");
                        String pickupTimeString = documentSnapshot.getString("pickupTime");
//                        int BookedInt = documentSnapshot.getLong("availableSeats").intValue();

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
                        pickupLocation.setText(" "+pickupLocationString);
                        pickupTime.setText(" "+formattedTime);
//                        availableSeats.setText(" "+availableSeats);
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
                .document(eventId)
                .collection("pickups")
                .document(pickupId)
                .collection("joinedUsers")
                .document(MyData.userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, you can retrieve data
                        long bookedSeatsString = documentSnapshot.getLong("bookedSeats");
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