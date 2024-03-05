package com.example.nexashare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nexashare.Adapter.CreatedAdapter;
import com.example.nexashare.Models.CreatedData;
import com.example.nexashare.Models.Event;
import com.example.nexashare.Models.MyData;
import com.example.nexashare.Models.Ride;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class CreatedFragment extends Fragment {

    TextView created;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_created, container, false);

        String userId = MyData.userId;

        RecyclerView recyclerView = view.findViewById(R.id.createdRecyclerview);
        List<CreatedData> createdDataList = new ArrayList<>();

// Reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();


// Query for events created by the user
        db.collection("events")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            CreatedData createdData = new CreatedData();
                            createdData.setDocumentId(document.getId());
                            createdData.setType("event");
                            createdData.setName(event.getEventName());
                            createdData.setLocationOrSource(event.getEventLocation());
                            createdData.setPhoneNumberOrDestination(event.getOrganizerPhoneNumber());
                            createdDataList.add(createdData);
                        }

                        // After retrieving events, query for rides created by the user
                        db.collection("rides")
                                .whereEqualTo("userId", userId)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task1.getResult()) {
                                            Ride ride = document.toObject(Ride.class);
                                            CreatedData createdData = new CreatedData();
                                            createdData.setDocumentId(document.getId());
                                            createdData.setType("ride");
                                            createdData.setName(ride.getName());
                                            createdData.setLocationOrSource(ride.getSource());
                                            createdData.setPhoneNumberOrDestination(ride.getDestination());
                                            createdDataList.add(createdData);
                                        }

                                        // Set up the RecyclerView with the combined items
                                        CreatedAdapter adapter = new CreatedAdapter(createdDataList, getContext());
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    } else {
                                        // Handle errors for rides query
                                        Log.e("FIRESTORE_DATA", "Error getting rides: ", task1.getException());
                                    }
                                });
                    } else {
                        // Handle errors for events query
                        Log.e("FIRESTORE_DATA", "Error getting events: ", task.getException());
                    }
                });

        return view;
    }
}