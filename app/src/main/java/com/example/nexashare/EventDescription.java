package com.example.nexashare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nexashare.Adapter.Event;
import com.example.nexashare.Adapter.EventPickupDescriptionAdapter;
import com.example.nexashare.Adapter.EventPickupDetail;
import com.example.nexashare.Adapter.MyData;
import com.example.nexashare.FCM.APIService;
import com.example.nexashare.FCM.Client;
import com.example.nexashare.FCM.Data;
import com.example.nexashare.FCM.FCMSend;
import com.example.nexashare.FCM.MyResponse;
import com.example.nexashare.FCM.NotificationSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import static java.security.AccessController.getContext;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDescription extends AppCompatActivity {
    private TextView eventNameDetail, eventLocationDetail, organizerPhoneDetail, rideTypeDetail;
    private Button joinEvent;
    private RecyclerView recyclerViewPickups;
    private List<EventPickupDetail> pickupDetailsList;
    private EventPickupDetail eventPickupDetail;
    private static FirebaseFirestore db;
//    private static FirebaseFirestore db;
    public static String receiverToken;
    public static String eventName,EventPickupLocation;
    public static String userId;
    private APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        eventNameDetail = findViewById(R.id.eventNameDetail);
        eventLocationDetail = findViewById(R.id.eventLocationDetail);
        organizerPhoneDetail = findViewById(R.id.organizerPhoneDetail);
        rideTypeDetail = findViewById(R.id.rideTypeDetail);
        recyclerViewPickups = findViewById(R.id.recyclerViewPickups);
        joinEvent = findViewById(R.id.joinEventBtn);

        // Initialize RecyclerView
        pickupDetailsList = new ArrayList<>();
        EventPickupDescriptionAdapter eventPickupDescriptionAdapter = new EventPickupDescriptionAdapter(pickupDetailsList);
        recyclerViewPickups.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPickups.setAdapter(eventPickupDescriptionAdapter);

        // Fetch eventId from intent
        String eventId = getIntent().getStringExtra("eventId");
        Log.d(TAG,"EventId is: " + eventId);

            // Fetch event details from Firestore based on eventId
            FirebaseFirestore.getInstance().collection("events")
                    .document(eventId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Map Firestore data to your model class or use directly
                            Event event = documentSnapshot.toObject(Event.class);

                            // Update UI with event details
                            updateUI(event,eventId);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });

            joinEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPickupLocationPopup(EventDescription.this,eventId );

//                sendNotificationToUser(title, message, recipientDeviceToken);
                    Toast.makeText(view.getContext(), "Button clicked",Toast.LENGTH_SHORT).show();
                    Toast.makeText(view.getContext(), receiverToken,Toast.LENGTH_SHORT).show();
                }
            });
        }


    // Update UI with event details
    private void updateUI(Event event,String eventId) {
        TextView eventNameDetail = findViewById(R.id.eventNameDetail);
        TextView eventLocationDetail = findViewById(R.id.eventLocationDetail);
        TextView organizerPhoneDetail = findViewById(R.id.organizerPhoneDetail);
        TextView rideTypeDetail = findViewById(R.id.rideTypeDetail);

        // Set text in TextViews
        eventNameDetail.setText("Event Name: " + event.getEventName());
        eventLocationDetail.setText("Event Location: " + event.getEventLocation());
        organizerPhoneDetail.setText("Organizer's Phone: " + event.getOrganizerPhoneNumber());
        rideTypeDetail.setText("Ride Type: " + event.getRideType());
        userId = event.getUserId();

        eventName= event.getEventName();


        // TODO: Fetch and display pickups data in the RecyclerView
        Log.d(TAG,"EventId in updateUi() is: " + eventId);

        RecyclerView recyclerViewPickups = findViewById(R.id.recyclerViewPickups);

        // Fetch and display pickups data in the RecyclerView
        fetchAndDisplayPickups(eventId, recyclerViewPickups);
    }
    private void fetchAndDisplayPickups(String eventId, RecyclerView recyclerView) {

        FirebaseFirestore.getInstance().collection("events")
                .document(eventId)
                .collection("pickups")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Convert query result to list of EventPickupDetail objects
                    List<EventPickupDetail> pickupsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventPickupDetail pickup = document.toObject(EventPickupDetail.class);
                        pickupsList.add(pickup);
                    }

                    // Create and set adapter for RecyclerView
                    EventPickupDescriptionAdapter eventPickupDescriptionAdapter = new EventPickupDescriptionAdapter(pickupsList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(eventPickupDescriptionAdapter);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private static void showPickupLocationPopup(Context context, String eventId) {
        // Fetch pickup details for the selected event
        db = FirebaseFirestore.getInstance();
        db.collection("events")
                .document(eventId)
                .collection("pickups")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> pickupLocations = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            EventPickupDetail pickupDetail = document.toObject(EventPickupDetail.class);
                            pickupLocations.add(pickupDetail.getPickupLocation());
                        }
                        // Display the pickup locations in a popup with a Spinner
                        showPopup(context, pickupLocations);
                    } else {
                        // Handle errors
                        Toast.makeText(context, "Error fetching Pickup Locations", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static void showPopup(Context context, List<String> pickupLocations) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Pickup Location");

        // Convert List<String> to String[]
        String[] pickupLocationsArray = pickupLocations.toArray(new String[0]);

        // Set up the layout inflater for the custom view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_pickup_location, null);
        builder.setView(view);

        // Initialize the Spinner
        Spinner pickupSpinner = view.findViewById(R.id.pickupSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, pickupLocationsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        pickupSpinner.setAdapter(adapter);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle confirm button click
                String selectedPickupLocation = pickupSpinner.getSelectedItem().toString();
                // Perform actions with the selected pickup location
                Toast.makeText(context, "Selected Pickup Location: " + selectedPickupLocation, Toast.LENGTH_SHORT).show();
                // Call a method to handle further logic with the selected pickup location

                handleSelectedPickupLocation(context,selectedPickupLocation);
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
    private static void handleSelectedPickupLocation(Context context,String selectedPickupLocation) {
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
                                        MyData.name + " has requested to join your "+eventName+" Event from pickup Location "+selectedPickupLocation
                                );
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

//    private static void showPickupLocationPopup(Context context, String eventId) {
//        // Fetch pickup details for the selected event
//        db = FirebaseFirestore.getInstance();
//        db.collection("events")
//                .document(eventId)
//                .collection("pickups")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        List<String> pickupLocations = new ArrayList<>();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            EventPickupDetail pickupDetail = document.toObject(EventPickupDetail.class);
//                            pickupLocations.add(pickupDetail.getPickupLocation());
//                        }
//                        // Display the pickup locations in a popup
//                        showPopup(pickupLocations);
//                    } else {
//                        // Handle errors
//                        Toast.makeText(context, "Error fetching Pickup Locations", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//
//    private static void showPopup(List<String> pickupLocations) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Select Pickup Location");
//
//        // Convert List<String> to String[]
//        String[] pickupLocationsArray = pickupLocations.toArray(new String[0]);
//
//        builder.setItems(pickupLocationsArray, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String selectedPickupLocation = pickupLocations.get(which);
//                // Perform actions with the selected pickup location
//                Toast.makeText(context, "Selected Pickup Location: " + selectedPickupLocation, Toast.LENGTH_SHORT).show();
//                // Add further logic as needed
//            }
//        });
//
//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Handle confirm button click
//                // You can add further logic here
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Handle cancel button click
//            }
//        });
//
//        builder.show();
//    }
}