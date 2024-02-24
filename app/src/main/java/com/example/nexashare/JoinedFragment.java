package com.example.nexashare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.Adapter.CreatedAdapter;
import com.example.nexashare.Adapter.JoinedAdapter;
import com.example.nexashare.Models.CreatedData;
import com.example.nexashare.Models.Event;
import com.example.nexashare.Models.JoinedData;
import com.example.nexashare.Models.MyData;
import com.example.nexashare.Models.Ride;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinedFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    int tasksCompleted = 0;
    List<JoinedData> joinedDataList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_joined, container, false);

        String userId = MyData.userId;

        recyclerView = view.findViewById(R.id.joinedRecyclerview);

        List<CreatedData> createdDataList = new ArrayList<>();

        Map<String, List<DocumentSnapshot>> documentsMap = new HashMap<>();

        // Retrieve documents from the "events" collection
        CollectionReference eventsCollectionRef = db.collection("events");
        CollectionReference ridesCollectionRef = db.collection("rides");

        eventsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot eventDoc : task.getResult()) {
                        // Get a reference to the "pickups" subcollection for each "event" document
                        CollectionReference pickupsCollectionRef = eventDoc.getReference().collection("pickups");

                        // Retrieve documents from the "pickups" subcollection
                        pickupsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> subCollectionTask) {
                                if (subCollectionTask.isSuccessful()) {
                                    for (DocumentSnapshot pickupDoc : subCollectionTask.getResult()) {
                                        // Get a reference to the "joined users" subcollection for each "pickup" document
                                        CollectionReference joinedUsersCollectionRef = pickupDoc.getReference().collection("joinedUsers");

                                        // Query the "joined users" subcollection to check if the "joinedUser" exists
                                        joinedUsersCollectionRef.whereEqualTo("joined_user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> joinedUsersTask) {
                                                if (joinedUsersTask.isSuccessful() && !joinedUsersTask.getResult().isEmpty()) {
                                                    for (DocumentSnapshot eventDoc : task.getResult()) {

                                                        Log.d("JoinedUsers", "User exists for pickup: " + pickupDoc.getId());
                                                        Log.d("JoinedUsers", "User exists for event: " + eventDoc.getId());
                                                        Log.d("JoinedUsers", "Event name: " + eventDoc.getString("eventName"));
                                                        //                            Event event = document.toObject(Event.class);
//                                                    Event event = joinedUsersTask.toObject(Event.class);

                                                        JoinedData joinedEventData = new JoinedData();
                                                        joinedEventData.setDocumentId(eventDoc.getId());
                                                        joinedEventData.setPickupDocumentId(pickupDoc.getId());
                                                        joinedEventData.setType("event");
                                                        joinedEventData.setName(eventDoc.getString("eventName"));
                                                        joinedEventData.setLocationOrSource(eventDoc.getString("eventLocation"));
                                                        joinedEventData.setPhoneNumberOrDestination(eventDoc.getString("organizerPhoneNumber"));
                                                        joinedDataList.add(joinedEventData);
                                                    }

                                                    ridesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (DocumentSnapshot rideDoc : task.getResult()) {
                                                                    // Get a reference to the "pickups" subcollection for each "event" document
                                                                    CollectionReference joinedUsersCollectionRef = rideDoc.getReference().collection("joinedUsers");

                                                                    joinedUsersCollectionRef.whereEqualTo("joined_user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> joinedUsersTask2) {
                                                                            if (joinedUsersTask2.isSuccessful() && !joinedUsersTask2.getResult().isEmpty()) {

                                                                                for (DocumentSnapshot rideDoc : task.getResult()) {
                                                                                    JoinedData joinedData = new JoinedData();
                                                                                    joinedData.setDocumentId(rideDoc.getId());
                                                                                    joinedData.setType("ride");
                                                                                    joinedData.setName(rideDoc.getString("name"));
                                                                                    joinedData.setLocationOrSource(rideDoc.getString("source"));
                                                                                    joinedData.setPhoneNumberOrDestination(rideDoc.getString("destination"));
                                                                                    joinedDataList.add(joinedData);
                                                                                }


                                                                            } else {
                                                                                Log.d("JoinedUsers", "User doesn't exist for pickup: " + rideDoc.getId());
                                                                            }
                                                                            JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
                                                                            recyclerView.setAdapter(adapter);
                                                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                                                        }
                                                                    });
                                                                }
                                                            } else {
                                                                Log.d("Firestore", "Error getting events documents: ", task.getException());
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    // "joinedUser" doesn't exist in the subcollection for this pickup
                                                    // You can handle it here
                                                    Log.d("JoinedUsers", "User doesn't exist for pickup: " + pickupDoc.getId());
                                                }


                                            }
                                        });
                                    }
                                } else {
                                    Log.d("Firestore", "Error getting pickups documents: ", subCollectionTask.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d("Firestore", "Error getting events documents: ", task.getException());
                }
            }
        });


        // Query to retrieve documents
//        db.collection("events")
////                .whereArrayContains()
//                .whereEqualTo("pickups.joinedUsers.joined_user", userId)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        // Iterate through the documents
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                            // Get the value of the eventName field
//                            String eventName = document.getString("eventName");
//
//                            // Check if documentsMap already contains a list for this eventName
//                            if (!documentsMap.containsKey(eventName)) {
//                                // If not, create a new list
//                                documentsMap.put(eventName, new ArrayList<>());
//                            }
//
//                            // Add the document to the list for this eventName
//                            documentsMap.get(eventName).add(document);
//                        }
//
//                        // Now you have all documents grouped by eventName
//                        // You can access them like this:
//                        for (Map.Entry<String, List<DocumentSnapshot>> entry : documentsMap.entrySet()) {
//                            String eventName = entry.getKey();
//                            List<DocumentSnapshot> documents = entry.getValue();
//                            // Do whatever you want with the documents
//                            // For example, print out the event name and number of documents
//                            System.out.println("Event Name: " + eventName);
//                            System.out.println("Number of Documents: " + documents.size());
//                        }
//                    }
//                });


//        db.collection("events").document()
//                .collection("pickups").document()
//                .collection("joinedUsers")
////                .whereEqualTo("userId", userId)
//                .whereEqualTo("joined_user" , userId)
////                .whereArrayContains("joinedUsers", userId)
////                .whereEqualTo("joinedUsers." + userId, true) // Check if the user ID exists in the joinedUsers subcollection
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Map<String, Object> data = document.getData();
//                            if (data != null) {
//                                for (String fieldName : data.keySet()) {
//                                    Object value = data.get(fieldName);
//                                    System.out.println("Field Name: " + fieldName + ", Value: " + value);
//                                }
//                            }
//                        }
//
//                        // After retrieving joined events, query for joined rides
//                        db.collection("rides")
//                                .document()
//                                .collection("joinedUsers")
////                .whereEqualTo("userId", userId)
//                                .whereEqualTo("joined_user" , userId)
//                                .get()
//                                .addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                            Map<String, Object> data = document.getData();
//                                            if (data != null) {
//                                                for (String fieldName : data.keySet()) {
//                                                    Object value = data.get(fieldName);
//                                                    System.out.println("Field Name: " + fieldName + ", Value: " + value);
//                                                }
//                                            }
//                                        for (QueryDocumentSnapshot document : task1.getResult()) {
//                                            Ride ride = document.toObject(Ride.class);
//                                            CreatedData createdData = new CreatedData();
//                                            createdData.setDocumentId(document.getId());
//                                            createdData.setType("ride");
//                                            createdData.setName(ride.getName());
//                                            createdData.setLocationOrSource(ride.getSource());
//                                            createdData.setPhoneNumberOrDestination(ride.getDestination());
//                                            createdDataList.add(createdData);
//                                        }
//
//                                        // Set up the RecyclerView with the combined items
//                                        CreatedAdapter adapter = new CreatedAdapter(createdDataList, getContext());
//                                        recyclerView.setAdapter(adapter);
//                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                    } else {
//                                        // Handle errors for rides query
//                                        Log.e("FIRESTORE_DATA", "Error getting rides: ", task1.getException());
//                                    }
//                                });
//                    } else {
//                        // Handle errors for events query
//                        Log.e("FIRESTORE_DATA", "Error getting events: ", task.getException());
//                    }
//                });
//
//        db.collection("events")
//                .whereEqualTo("joined_user", userId)
////                .document()
////                .collection("pickup")
//////                .whereEqualTo("pickups.joinedUsers." + userId, true) // Check if user ID exists in joinedUsers subcollection
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Event event = document.toObject(Event.class);
//                            JoinedData joinedData = new JoinedData();
//                            joinedData.setDocumentId(document.getId());
//                            joinedData.setType("event");
//                            joinedData.setName(event.getEventName());
//                            joinedData.setLocationOrSource(event.getEventLocation());
//                            joinedData.setPhoneNumberOrDestination(event.getOrganizerPhoneNumber());
//                            joinedDataList.add(joinedData);
//                        }
//
//                        // After retrieving joined events, query for joined rides
//                        db.collection("rides")
//                                .whereEqualTo("joined_user", userId)
////                                .whereEqualTo("joinedUsers." + userId, true) // Check if user ID exists in joinedUsers subcollection
//                                .get()
//                                .addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
//                                        for (QueryDocumentSnapshot document : task1.getResult()) {
//                                            Ride ride = document.toObject(Ride.class);
//                                            JoinedData joinedData = new JoinedData();
//                                            joinedData.setDocumentId(document.getId());
//                                            joinedData.setType("ride");
//                                            joinedData.setName(document.getString("name"));
//                                            joinedData.setLocationOrSource(ride.getSource());
//                                            joinedData.setPhoneNumberOrDestination(ride.getDestination());
//                                            joinedDataList.add(joinedData);
//                                        }
//
//                                        // Set up the RecyclerView with the combined items
//                                        JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
//                                        recyclerView.setAdapter(adapter);
//                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                    } else {
//                                        // Handle errors for rides query
//                                        Log.e("FIRESTORE_DATA", "Error getting joined rides: ", task1.getException());
//                                    }
//                                });
//                    } else {
//                        // Handle errors for events query
//                        Log.e("FIRESTORE_DATA", "Error getting joined events: ", task.getException());
//                    }
//                });

//        db.collection("events")
//                .document()
//                .collection("pickups")
//                .document()
//                .collection("joinedUsers")
//                .whereEqualTo(FieldPath.documentId(), userId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Event event = document.toObject(Event.class);
//                            JoinedData joinedData = new JoinedData();
//                            joinedData.setDocumentId(document.getId());
//                            joinedData.setType("event");
//                            joinedData.setName((String) document.get("name"));
////                            joinedData.setLocationOrSource(event.getEventLocation());
////                            joinedData.setPhoneNumberOrDestination(event.getOrganizerPhoneNumber());
//                            joinedDataList.add(joinedData);
//                        }
//                        JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
//                        recyclerView.setAdapter(adapter);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        // After retrieving events, query for rides created by the user
//                        db.collection("rides")
//                                .whereEqualTo(FieldPath.documentId(), userId)
//                                .get()
//                                .addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
//                                        for (QueryDocumentSnapshot document : task1.getResult()) {
//                                            Ride ride = document.toObject(Ride.class);
//                                            JoinedData joinedData = new JoinedData();
//                                            joinedData.setDocumentId(document.getId());
//                                            joinedData.setType("ride");
//                                            joinedData.setName(ride.getName());
//                                            joinedData.setLocationOrSource(ride.getSource());
//                                            joinedData.setPhoneNumberOrDestination(ride.getDestination());
//                                            joinedDataList.add(joinedData);
//                                        }
//
//                                        // Set up the RecyclerView with the combined items
//                                        JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
//                                        recyclerView.setAdapter(adapter);
//                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                    } else {
//                                        // Handle errors for rides query
//                                        Log.e("FIRESTORE_DATA", "Error getting rides: ", task1.getException());
//                                    }
//                                });
//                    } else {
//                        // Handle errors for events query
//                        Log.e("FIRESTORE_DATA", "Error getting events: ", task.getException());
//                    }
//                });

//        db.collection("events")
//                .whereEqualTo("pickups.joinedUsers." + userId, true)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                        // Retrieve event details
//                        String eventName = document.getString("eventName");
//                        String eventLocation = document.getString("eventLocation");
//
//                        // Retrieve pickup details
//                        List<Map<String, Object>> pickups = (List<Map<String, Object>>) document.get("pickups");
//                        for (Map<String, Object> pickup : pickups) {
//                            String pickupId = (String) pickup.get("pickupId");
//                            String pickupLocation = (String) pickup.get("pickupLocation");
//                            Timestamp pickupTime = (Timestamp) pickup.get("pickupTime");
//
//                            // Print or use the retrieved data as needed
//                            System.out.println("Event Name: " + eventName);
//                            System.out.println("Event Location: " + eventLocation);
//                            System.out.println("Pickup ID: " + pickupId);
//                            System.out.println("Pickup Location: " + pickupLocation);
//                            System.out.println("Pickup Time: " + pickupTime);
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                    System.err.println("Error getting events: " + e.getMessage());
//                });

//        db.collection("events")
//                .whereArrayContains("pickups.joinedUsers", userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            joinedDataList.clear();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Event event = document.toObject(Event.class);
//                                JoinedData joinedData = new JoinedData();
//                                joinedData.setDocumentId(document.getId());
//                                joinedData.setType("event");
//                                joinedData.setName(event.getEventName());
//                                joinedData.setLocationOrSource(event.getEventLocation());
//                                joinedData.setPhoneNumberOrDestination(event.getOrganizerPhoneNumber());
//                                joinedDataList.add(joinedData);
//                            }
//                            JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
//                            recyclerView.setAdapter(adapter);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
////                            adapter.notifyDataSetChanged();
//                        } else {
//                            Log.d(TAG, "Error getting joined rides: ", task.getException());
//                        }
//                    }
//                });

//        db.collection("events")
//                .whereEqualTo("pickups.joinedUsers." + userId + ".confirmed", true)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.e("FIRESTORE_DATA", "no error getting rides: ");
//
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Event event = document.toObject(Event.class);
//                            JoinedData joinedData = new JoinedData();
//                            joinedData.setDocumentId(document.getId());
//                            Log.e("FIRESTORE_DATA", (String) document.get("name"));
//                            Log.e("FIRESTORE_DATA", document.getId());
//                            joinedData.setType("event");
//                            joinedData.setName(event.getEventName());
//                            joinedData.setLocationOrSource(event.getEventLocation());
//                            joinedData.setPhoneNumberOrDestination(event.getOrganizerPhoneNumber());
//                            joinedDataList.add(joinedData);
//                            Log.e("FIRESTORE_DATA", joinedData.getName());
//                        }
//
//                        // After retrieving events, query for rides joined by the user
//                        db.collection("rides")
//                                .whereEqualTo("joinedUsers." + userId + ".confirmed", true)
//                                .get()
//                                .addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
//                                        for (QueryDocumentSnapshot document : task1.getResult()) {
//                                            Ride ride = document.toObject(Ride.class);
//                                            JoinedData joinedData = new JoinedData();
//                                            joinedData.setDocumentId(document.getId());
//                                            joinedData.setType("ride");
////                                            joinedData.setName(ride.getDrivername());
////                                            joinedData.setLocationOrSource(ride.getSource());
////                                            joinedData.setPhoneNumberOrDestination(ride.getPhoneNumber());
//                                            joinedDataList.add(joinedData);
//                                        }
//
//                                        // Set up the RecyclerView with the combined items
//                                        JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
//                                        recyclerView.setAdapter(adapter);
//                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                    } else {
//                                        // Handle errors for rides query
//                                        Log.e("FIRESTORE_DATA", "Error getting rides: ", task1.getException());
//                                    }
//                                });
//                    } else {
//                        // Handle errors for events query
//                        Log.e("FIRESTORE_DATA", "Error getting events: ", task.getException());
//                    }
//                });

//        db.collection("events").document().collection("joinedUsers")
//                .whereEqualTo(FieldPath.documentId(), userId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Event event = document.toObject(Event.class);
//                            JoinedData joinedData = new JoinedData();
//                            joinedData.setDocumentId(document.getId());
//                            joinedData.setType("event");
//                            joinedData.setName(event.getEventName());
//                            joinedData.setLocationOrSource(event.getEventLocation());
//                            joinedData.setPhoneNumberOrDestination(event.getOrganizerPhoneNumber());
//                            joinedDataList.add(joinedData);
//                        }
//
//                        // After retrieving events, query for rides created by the user
//                        db.collection("rides").document().collection("joinedUsers")
//                                .whereEqualTo("userId", userId)
//                                .get()
//                                .addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
//                                        for (QueryDocumentSnapshot document : task1.getResult()) {
//                                            Ride ride = document.toObject(Ride.class);
//                                            JoinedData joinedData = new JoinedData();
//                                            joinedData.setDocumentId(document.getId());
//                                            joinedData.setType("ride");
//                                            joinedData.setName(ride.getName());
//                                            joinedData.setLocationOrSource(ride.getSource());
//                                            joinedData.setPhoneNumberOrDestination(ride.getDestination());
//                                            joinedDataList.add(joinedData);
//                                        }
//
//                                        // Set up the RecyclerView with the combined items
//                                        JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
//                                        recyclerView.setAdapter(adapter);
//                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                    } else {
//                                        // Handle errors for rides query
//                                        Log.e("FIRESTORE_DATA", "Error getting rides: ", task1.getException());
//                                    }
//                                });
//                    } else {
//                        // Handle errors for events query
//                        Log.e("FIRESTORE_DATA", "Error getting events: ", task.getException());
//                    }
//                });
        return view;
    }



}