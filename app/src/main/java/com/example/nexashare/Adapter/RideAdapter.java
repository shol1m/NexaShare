package com.example.nexashare.Adapter;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.FCM.APIService;
import com.example.nexashare.FCM.Client;
import com.example.nexashare.FCM.MyResponse;
import com.example.nexashare.FCM.Data;
import com.example.nexashare.FCM.NotificationSender;
import com.example.nexashare.FCM.FCMSend;
import com.example.nexashare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {
    private List<Ride> rides;
    public String receiverToken,userId;
    private OnRideClickListener rideClickListener;
    private APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    public RideAdapter(List<Ride> rides, OnRideClickListener onRideClickListener) {
        this.rides = rides;
        this.rideClickListener = rideClickListener;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ride_item, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride ride = rides.get(position);

        holder.textViewDriverName.setText(ride.getName());
        holder.textViewPickup.setText("From "+ride.getSource() + " To "+ride.getDestination());
        holder.textViewSeats.setText("Seats: " + ride.getSeats());
        holder.textViewDateTime.setText(ride.getDate_and_time());
        userId = ride.getUserId();

    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDriverName;
        public TextView textViewSeats;
        public TextView textViewPickup;
        public TextView textViewDateTime;
        public Button buttonSelectRide,joinRide;
        public String myName;

        public RideViewHolder(View itemView) {
            super(itemView);
            textViewDriverName = itemView.findViewById(R.id.driverNameTxt);
            textViewPickup = itemView.findViewById(R.id.sourceTxt);
            textViewDateTime = itemView.findViewById(R.id.timeTxt);
            textViewSeats = itemView.findViewById(R.id.seatsTxt);
            joinRide = itemView.findViewById(R.id.joinRideBtn);
            joinRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = "Notification Title";
                    String message = "Notification Message";
                    String recipientDeviceToken = "fuhfTgwWQ2iC5KYli4S_h9:APA91bEVdSSb_BS8kGMX1U1xeBiOnNCPNc_hjBA50oaJmVtDrojViu5izHT77FmFoqyWakLvSR1zyiJ0TAnwHpJaIj04kk-BOjK8dmoupc1o-vm6-lyzES_Z8DKZn-LIKH5aKcz7k9Z8";

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
                                                    view.getContext(),
                                                    userId,
                                                    receiverToken,
                                                        "Request to join ride",
                                                    MyData.name + " has requested to join your ride"
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

//                sendNotificationToUser(title, message, recipientDeviceToken);
                    Toast.makeText(view.getContext(), "Button clicked",Toast.LENGTH_SHORT).show();
                    Toast.makeText(view.getContext(), receiverToken,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);

        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    MyResponse responseBody = response.body();
                    if (responseBody != null && responseBody.success == 1) {
                        // Notification sent successfully
                        Log.d(TAG, "Notification sent successfully" + data + " to " + usertoken);
                        // You might perform further actions here upon successful sending
                    } else {
                        // Error in sending notification
                        Log.e(TAG, "Failed to send notification");
                    }
                } else {
                    if (response.code() == 401) {
                        // Unauthorized - handle authentication issues
                        Log.e(TAG, "Failed to send notification. Unauthorized access");
                        // You might redirect the user to log in again or refresh authentication tokens
                    } else {
                        // Handle non-successful responses (other server errors)
                        Log.e(TAG, "Failed to send notification. Server error: " + response.code() + data + " to " + usertoken);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                // Network or unexpected errors
                Log.e(TAG, "Failed to send notification. Error: " + t.getMessage());
            }
        });
    }


    public interface OnRideClickListener {
        void onRideClick(Ride ride);
    }

    public void updateToken(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseMessaging.getInstance().getToken().getResult();

        if (firebaseUser != null && refreshToken != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a data object with the token
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("fcmToken", refreshToken);

            // Update the token under the user's document in the "users" collection
            db.collection("users")
                    .document(firebaseUser.getUid())
                    .set(tokenMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Token updated successfully
                            Log.d("TOKEN_UPDATE", "Token updated in Firestore");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            Log.e("TOKEN_UPDATE", "Error updating token in Firestore", e);
                        }
                    });
        }
    }

}

