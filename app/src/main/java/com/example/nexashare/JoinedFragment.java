package com.example.nexashare;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    ImageView back;
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
        back = view.findViewById(R.id.joinedBack);

        List<CreatedData> createdDataList = new ArrayList<>();

        Map<String, List<DocumentSnapshot>> documentsMap = new HashMap<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        // Retrieve documents from the "events" collection
        CollectionReference eventsCollectionRef = db.collection("events");
        CollectionReference ridesCollectionRef = db.collection("rides");

//        CollectionReference eventsCollectionRef = db.collection("events");
//        CollectionReference ridesCollectionRef = db.collection("rides");

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
                                                    for (DocumentSnapshot joinedUserDoc : joinedUsersTask.getResult()) {

                                                        Log.d("JoinedUsers", "User exists for pickup: " + pickupDoc.getId());
                                                        Log.d("JoinedUsers", "User exists for event: " + eventDoc.getId());
                                                        Log.d("JoinedUsers", "Event name: " + eventDoc.getString("eventName"));

                                                        //Event event = document.toObject(Event.class);
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

                                                } else {
                                                    // "joinedUser" doesn't exist in the subcollection for this pickup
                                                    // You can handle it here
                                                    Log.d("JoinedUsers", "User doesn't exist for pickup: " + pickupDoc.getId());
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
                                                                            Log.d("JoinedUsers", "UserId: " + joinedUsersTask2.getResult().isEmpty());
                                                                            if (joinedUsersTask2.isSuccessful() && !joinedUsersTask2.getResult().isEmpty()) {

                                                                                for (DocumentSnapshot joinedUserDoc : joinedUsersTask2.getResult()) {
                                                                                    JoinedData joinedData = new JoinedData();
                                                                                    joinedData.setDocumentId(rideDoc.getId());
                                                                                    joinedData.setType("ride");
                                                                                    joinedData.setName(rideDoc.getString("name"));
                                                                                    joinedData.setLocationOrSource(rideDoc.getString("source"));
                                                                                    joinedData.setPhoneNumberOrDestination(rideDoc.getString("destination"));
                                                                                    joinedDataList.add(joinedData);
                                                                                }

                                                                            } else {
                                                                                Log.d("JoinedUsers", "User doesn't exist for ride: " + rideDoc.getId());
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

//        eventsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot eventDoc : task.getResult()) {
//                        // Get a reference to the "pickups" subcollection for each "event" document
//                        CollectionReference pickupsCollectionRef = eventDoc.getReference().collection("pickups");
//
//                        // Retrieve documents from the "pickups" subcollection
//                        pickupsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> subCollectionTask) {
//                                if (subCollectionTask.isSuccessful()) {
//                                    for (DocumentSnapshot pickupDoc : subCollectionTask.getResult()) {
//                                        // Get a reference to the "joined users" subcollection for each "pickup" document
//                                        CollectionReference joinedUsersCollectionRef = pickupDoc.getReference().collection("joinedUsers");
//
//                                        // Query the "joined users" subcollection to check if the "joinedUser" exists
//                                        joinedUsersCollectionRef.whereEqualTo("joined_user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> joinedUsersTask) {
//                                                if (joinedUsersTask.isSuccessful() && !joinedUsersTask.getResult().isEmpty()) {
//                                                    for (DocumentSnapshot eventDoc : joinedUsersTask.getResult()) {
//
//                                                        Log.d("JoinedUsers", "User exists for pickup: " + pickupDoc.getId());
//                                                        Log.d("JoinedUsers", "User exists for event: " + eventDoc.getId());
//                                                        Log.d("JoinedUsers", "Event name: " + eventDoc.getString("eventName"));
//                                                        //                            Event event = document.toObject(Event.class);
////                                                    Event event = joinedUsersTask.toObject(Event.class);
//
//                                                        JoinedData joinedEventData = new JoinedData();
//                                                        joinedEventData.setDocumentId(eventDoc.getId());
//                                                        joinedEventData.setPickupDocumentId(pickupDoc.getId());
//                                                        joinedEventData.setType("event");
//                                                        joinedEventData.setName(eventDoc.getString("eventName"));
//                                                        joinedEventData.setLocationOrSource(eventDoc.getString("eventLocation"));
//                                                        joinedEventData.setPhoneNumberOrDestination(eventDoc.getString("organizerPhoneNumber"));
//                                                        joinedDataList.add(joinedEventData);
//                                                    }
//
//
//
//                                                } else {
//                                                    // "joinedUser" doesn't exist in the subcollection for this pickup
//                                                    // You can handle it here
//                                                    Log.d("JoinedUsers", "User doesn't exist for pickup: " + pickupDoc.getId());
//                                                    ridesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                            if (task.isSuccessful()) {
//                                                                for (DocumentSnapshot rideDoc : task.getResult()) {
//                                                                    // Get a reference to the "pickups" subcollection for each "event" document
//                                                                    CollectionReference joinedUsersCollectionRef = rideDoc.getReference().collection("joinedUsers");
//
//                                                                    joinedUsersCollectionRef.whereEqualTo("joined_user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<QuerySnapshot> joinedUsersTask2) {
//                                                                            Log.d("JoinedUsers", "UserId: " + joinedUsersTask2.getResult().isEmpty());
//                                                                            if (joinedUsersTask2.isSuccessful() && !joinedUsersTask2.getResult().isEmpty()) {
//
//                                                                                for (DocumentSnapshot rideDoc : joinedUsersTask2.getResult()) {
//                                                                                    JoinedData joinedData = new JoinedData();
//                                                                                    joinedData.setDocumentId(rideDoc.getId());
//                                                                                    joinedData.setType("ride");
//                                                                                    joinedData.setName(rideDoc.getString("name"));
//                                                                                    joinedData.setLocationOrSource(rideDoc.getString("source"));
//                                                                                    joinedData.setPhoneNumberOrDestination(rideDoc.getString("destination"));
//                                                                                    joinedDataList.add(joinedData);
//                                                                                }
//
//                                                                            } else {
//                                                                                Log.d("JoinedUsers", "User doesn't exist for ride: " + rideDoc.getId());
//                                                                            }
//                                                                            JoinedAdapter adapter = new JoinedAdapter(joinedDataList, getContext());
//                                                                            recyclerView.setAdapter(adapter);
//                                                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//                                                                        }
//                                                                    });
//                                                                }
//                                                            } else {
//                                                                Log.d("Firestore", "Error getting events documents: ", task.getException());
//                                                            }
//                                                        }
//                                                    });
//                                                }
//
//
//                                            }
//                                        });
//                                    }
//                                } else {
//                                    Log.d("Firestore", "Error getting pickups documents: ", subCollectionTask.getException());
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    Log.d("Firestore", "Error getting events documents: ", task.getException());
//                }
//            }
//        });


        return view;
    }



}