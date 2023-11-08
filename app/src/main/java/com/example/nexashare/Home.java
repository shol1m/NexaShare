package com.example.nexashare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nexashare.Adapter.Ride;
import com.example.nexashare.Adapter.RideAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    private RecyclerView recyclerViewRides;
    private RideAdapter rideAdapter;
    private List<Ride> rides;
    private FirebaseFirestore db;

    public Home() {
        // Required empty public constructor
    }


    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewRides = view.findViewById(R.id.recyclerViewRides);
        rides = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        rideAdapter = new RideAdapter(rides, new RideAdapter.OnRideClickListener() {
            @Override
            public void onRideClick(Ride ride) {
                // Handle the ride selection
            }
        });
        recyclerViewRides.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerViewRides.setAdapter(rideAdapter);

        // Query Firestore for available rides and update the RecyclerView
        db.collection("rides")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle the error
                        return;
                    }

                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        DocumentSnapshot document = documentChange.getDocument();
                        Ride ride = document.toObject(Ride.class);
                        ride.setId(document.getId());

                        switch (documentChange.getType()) {
                            case ADDED:
                                rides.add(0, ride);
                                break;
                            case MODIFIED:
                                int index = findRideIndex(ride);
                                if (index >= 0) {
                                    rides.set(index, ride);
                                }
                                break;
                            case REMOVED:
                                int removeIndex = findRideIndex(ride);
                                if (removeIndex >= 0) {
                                    rides.remove(removeIndex);
                                }
                                break;
                        }
                    }

                    rideAdapter.notifyDataSetChanged();
                });

        return view;
    }
    private int findRideIndex(Ride ride) {
        for (int i = 0; i < rides.size(); i++) {
            if (rides.get(i).getId().equals(ride.getId())) {
                return i;
            }
        }
        return -1;
    }
}