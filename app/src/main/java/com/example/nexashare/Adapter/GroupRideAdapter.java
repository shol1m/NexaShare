package com.example.nexashare.Adapter;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.EventDescription;
import com.example.nexashare.FCM.APIService;
import com.example.nexashare.FCM.Client;
import com.example.nexashare.Models.Event;
import com.example.nexashare.Models.EventPickupDetail;
import com.example.nexashare.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupRideAdapter extends RecyclerView.Adapter<GroupRideAdapter.GroupRideViewHolder> {
    private static List<Event> events;
    private List<EventPickupDetail> eventPickupDetails;
    private static FirebaseFirestore db;
    public static String receiverToken;
    public static String userId;
    private APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    private static Context context; // Add a context variable

    public GroupRideAdapter(Context context, List<Event> groupRides) {
        this.context = context;
        this.events = groupRides;
    }
//    public GroupRideAdapter(List<Event> groupRides) {
//        this.events = groupRides;
//    }

    @NonNull
    @Override
    public GroupRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_ride_item, parent, false);
        return new GroupRideViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GroupRideViewHolder holder, int position) {
        Event event = events.get(position);

        holder.eventNameTextView.setText("Event Name: " + event.getEventName());
        holder.pickupLocationTextView.setText("Event Location: " + event.getEventLocation());

        // Fetch pickup details for the current event
        fetchPickupDetails(event, holder);

        userId = event.getUserId();
    }

    // Fetch pickup details for the given event
    private void fetchPickupDetails(Event event, GroupRideViewHolder holder) {
        db = FirebaseFirestore.getInstance();
        db.collection("events")
                .document(event.getId())  // Use the event's ID to get the document
                .collection("pickups")   // Navigate to the "pickups" collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<EventPickupDetail> pickupDetails = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert each document to EventPickupDetail
                            EventPickupDetail pickupDetail = document.toObject(EventPickupDetail.class);
                            pickupDetails.add(pickupDetail);
                        }

                        // Display pickup details if available
                        if (!pickupDetails.isEmpty()) {
                            EventPickupDetail firstPickup = pickupDetails.get(0); // Assuming you're displaying the first pickup for simplicity
                            holder.pickupLocationTextView.setText("Pickup Location: " + firstPickup.getPickupLocation());
                            // Add more details as needed
                        } else {
                            holder.pickupLocationTextView.setText("No Pickup Details Available");
                        }
                    } else {
                        // Handle errors
                        holder.pickupLocationTextView.setText("Error fetching Pickup Details");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class GroupRideViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;
        TextView pickupLocationTextView;
        Button joinEvent;
        GroupRideViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTxt);
            pickupLocationTextView = itemView.findViewById(R.id.eventLocationTxt);
            joinEvent=itemView.findViewById(R.id.joinEventBtn);

            Event event= new Event();

            joinEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    showPickupLocationPopup(context,events.get(getAdapterPosition()));


                    Intent intent = new Intent(context, EventDescription.class);
                    intent.putExtra("eventId", events.get(getAdapterPosition()).getId());
                    view.getContext().startActivity(intent);

                    Log.d(TAG,"EventId before intent is: " + events.get(getAdapterPosition()).getId());


                }
            });
        }
    }


}
