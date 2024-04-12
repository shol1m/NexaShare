package com.example.nexashare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nexashare.SingeRides.CreatedRidesHistory;
import com.example.nexashare.GroupRides.CreatedEventsHistory;

import android.util.Log;

public class CreatedFragment extends Fragment {

    TextView createdRides,createdEvents;
    CreatedRidesHistory createdRidesHistory = new CreatedRidesHistory();
    CreatedEventsHistory createdEventsHistory=new CreatedEventsHistory();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_created, container, false);

        createdRides=view.findViewById(R.id.single_created_arrow);
        createdEvents=view.findViewById(R.id.group_created_arrow);


        createdRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                try {
                    // Check if getParentFragmentManager() is the correct context, otherwise use getChildFragmentManager()
                    if (getParentFragmentManager() != null) {
                        transaction.replace(R.id.flFragment,createdRidesHistory );
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        // Log an error if getParentFragmentManager() returns null
                        Log.e("FragmentTransaction", "Parent Fragment Manager is null");
                    }
                } catch (Exception e) {
                    // Log any exception that might occur during the transaction
                    Log.e("FragmentTransaction", "Error during fragment transaction", e);
                }

            }
        });


        createdEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                try {
                    // Check if getParentFragmentManager() is the correct context, otherwise use getChildFragmentManager()
                    if (getParentFragmentManager() != null) {
                        transaction.replace(R.id.flFragment,createdEventsHistory );
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        // Log an error if getParentFragmentManager() returns null
                        Log.e("FragmentTransaction", "Parent Fragment Manager is null");
                    }
                } catch (Exception e) {
                    // Log any exception that might occur during the transaction
                    Log.e("FragmentTransaction", "Error during fragment transaction", e);
                }


            }
        });

        return view;
    }
}
//public class CreatedFragment extends Fragment {
//
//    TextView created;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_created, container, false);
//
//        String userId = MyData.userId;
//
//        RecyclerView recyclerView = view.findViewById(R.id.createdRecyclerview);
//        List<CreatedData> createdDataList = new ArrayList<>();
//
//// Reference to the Firestore database
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
//// Query for events created by the user
//        db.collection("events")
//                .whereEqualTo("userId", userId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Event event = document.toObject(Event.class);
//                            CreatedData createdData = new CreatedData();
//                            createdData.setDocumentId(document.getId());
//                            createdData.setType("event");
//                            createdData.setName(event.getEventName());
//                            createdData.setLocationOrSource(event.getEventLocation());
//                            createdData.setPhoneNumberOrDestination(event.getOrganizerPhoneNumber());
//                            createdDataList.add(createdData);
//                        }
//
//                        // After retrieving events, query for rides created by the user
//                        db.collection("rides")
//                                .whereEqualTo("userId", userId)
//                                .get()
//                                .addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
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
//        return view;
//    }
//}