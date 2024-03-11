package com.example.nexashare.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.AccountDetailsActivity;
import com.example.nexashare.Helper.Whatsapp;
import com.example.nexashare.Models.Passenger;
import com.example.nexashare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.ViewHolder> {
    private List<Passenger> passengersList;
    private static Context context;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PassengerAdapter(List<Passenger> passengersList,Context context) {
        this.passengersList = passengersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Passenger passenger = passengersList.get(position);
        holder.passengerNameTextView.setText(passenger.getName());
        holder.bookedSeatsTextView.setText("Booked Seats: " + passenger.getBookedSeats());

        if(!passenger.isConfirmed()){
            holder.confirmedTextView.setText("Not Confirmed");
            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (passenger.getType().equals("event")){
                        db.collection("events")
                                .document(passenger.getDocumentId())
                                .collection("pickups")
                                .document(passenger.getPickupId()).collection("joinedUsers")
                                .document(passenger.getUserId())
                                .update("confirmed",true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Passenger confirmed Successfully", Toast.LENGTH_SHORT).show();
                                        Log.d("FIRESTORE_VALUE","Passenger confirmed Successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("FIRESTORE_VALUE","Could not update confirmed: "+e);
                                    }
                                });
                    }else {
                        db.collection("rides")
                                .document(passenger.getDocumentId())
                                .collection("joinedUsers")
                                .document(passenger.getUserId())
                                .update("confirmed",true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Passenger confirmed Successfully", Toast.LENGTH_SHORT).show();
                                        Log.d("FIRESTORE_VALUE","Passenger confirmed Successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("FIRESTORE_VALUE","Could not update confirmed: "+e);
                                    }
                                });
                    }

                }
            });
            holder.deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int bookedSeats = passenger.getBookedSeats();
                    if (passenger.getType().equals("event")){
                        db.collection("events")
                                .document(passenger.getDocumentId())
                                .collection("pickups")
                                .document(passenger.getPickupId()).collection("joinedUsers")
                                .document(passenger.getUserId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(context, "Passenger confirmed Successfully", Toast.LENGTH_SHORT).show();
                                        Log.d("FIRESTORE_VALUE","Passenger denied Successfully");
                                        db.collection("events")
                                                .document(passenger.getDocumentId())
                                                .collection("pickups")
                                                .document(passenger.getPickupId())
                                                .update("availableSeats", FieldValue.increment(bookedSeats))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "Seats updated Successfully", Toast.LENGTH_SHORT).show();
                                                        Log.d("FIRESTORE_VALUE","Seats updated Successfully");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("FIRESTORE_VALUE","Could not update seats: "+e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("FIRESTORE_VALUE","Could not update confirmed: "+e);
                                    }
                                });
                    }else {
                        db.collection("rides")
                                .document(passenger.getDocumentId())
                                .collection("joinedUsers")
                                .document(passenger.getUserId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Passenger confirmed Successfully", Toast.LENGTH_SHORT).show();
                                        Log.d("FIRESTORE_VALUE","Passenger confirmed Successfully");
                                        db.collection("rides")
                                                .document(passenger.getDocumentId())
                                                .update("availableSeats", FieldValue.increment(bookedSeats))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "Seats updated Successfully", Toast.LENGTH_SHORT).show();
                                                        Log.d("FIRESTORE_VALUE","Seats updated Successfully");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("FIRESTORE_VALUE","Could not update seats: "+e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("FIRESTORE_VALUE","Could not update confirmed: "+e);
                                    }
                                });
                    }
                }
            });
//            holder.confirm.setText("Deny");
        }else{
//            holder.passengerLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            holder.confirmedTextView.setText("Confirmed");
            holder.confirm.setVisibility(View.GONE);
            holder.deny.setVisibility(View.GONE);
//            holder.confirm.setBackgroundResource(R.drawable.baseline_whatsapp_24);
        }

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Whatsapp.sendMessageToWhatsApp();
                db.collection("users")
                        .document(passenger.getUserId())
                        .get()
                        .addOnSuccessListener(task -> {
                            String phone=task.getString("phone_number");
                            Whatsapp.sendMessageToWhatsApp(phone," ",context);
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FIRESTORE_VALUE","Could not retrieve phone number: "+e);
                            }
                        });

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Test","Item has been clicked");
            }
        });
    }
    @Override
    public int getItemCount() {
        return passengersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView passengerNameTextView;
        private TextView bookedSeatsTextView;
        private TextView confirmedTextView;
        private Button confirm,deny;
        private ImageView whatsapp;
        LinearLayout passengerLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerNameTextView = itemView.findViewById(R.id.passengerNameDetail);
            bookedSeatsTextView = itemView.findViewById(R.id.bookedSeatsDetail);
            passengerLinearLayout = itemView.findViewById(R.id.passengerLinearaLyout);
            confirmedTextView = itemView.findViewById(R.id.confirmedDetail);
            confirm = itemView.findViewById(R.id.confirm);
            deny = itemView.findViewById(R.id.deny);
            whatsapp = itemView.findViewById(R.id.whatsappicon);
        }
    }
}