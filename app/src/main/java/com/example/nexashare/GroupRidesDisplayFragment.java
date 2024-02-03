package com.example.nexashare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nexashare.Models.Event;
import com.example.nexashare.Models.GroupRide;
import com.example.nexashare.Adapter.GroupRideAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.search.SearchView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class GroupRidesDisplayFragment extends Fragment {
    private List<GroupRide> groupRides;
    private GroupRideAdapter groupRideAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewRides;
    private ImageView notification;
    private SearchView search;
    Toolbar toolbar;
    Menu menu;
//    private List<Ride> rides;
    private List<Event> events;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_ride, container, false);

        recyclerViewRides = view.findViewById(R.id.recyclerView);
        notification = view.findViewById(R.id.notification);
//        search = view.findViewById(R.id.search);

        events = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        groupRideAdapter = new GroupRideAdapter(getContext(),events);
        recyclerViewRides.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewRides.setAdapter(groupRideAdapter);

//         Query Firestore for available rides and update the RecyclerView
        db.collection("events")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle the error
                        return;
                    }

                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        DocumentSnapshot document = documentChange.getDocument();
                        Event event = document.toObject(Event.class);
                        event.setId(document.getId());

                        switch (documentChange.getType()) {
                            case ADDED:
                                events.add(0, event);
                                break;
                            case MODIFIED:
                                int index = findRideIndex(event);
                                if (index >= 0) {
                                    events.set(index, event);
                                }
                                break;
                            case REMOVED:
                                int removeIndex = findRideIndex(event);
                                if (removeIndex >= 0) {
                                    events.remove(removeIndex);
                                }
                                break;
                        }
                    }
                    groupRideAdapter.notifyDataSetChanged();
                });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Notifications.class));
            }
        });

        return view;
    }

    private int findRideIndex(Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(event.getId())) {
                return i;
            }
        }
        return -1;
    }


}