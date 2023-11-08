package com.example.nexashare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nexashare.Helper.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CreateRideFragment extends Fragment {
    private EditText editTextDriverName, editTextSource, editTextDestination, editTextDateTime;
    private Button buttonCreateRide;
    private FirebaseHelper firebaseHelper;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_create_ride, container, false);

        editTextDriverName = view.findViewById(R.id.editTextDriverName);
        editTextSource = view.findViewById(R.id.editTextSource);
        editTextDestination = view.findViewById(R.id.editTextDestination);
        editTextDateTime = view.findViewById(R.id.editTextDateTime);
        buttonCreateRide = view.findViewById(R.id.buttonCreateRide);
        firebaseHelper = new FirebaseHelper();

        buttonCreateRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverName = editTextDriverName.getText().toString();
                String source = editTextSource.getText().toString();
                String destination = editTextDestination.getText().toString();
//                String dateTime = editTextDateTime.getText().toString();

                Map<String, Object> rideData = new HashMap<>();
                rideData.put("driverName", driverName);
                rideData.put("source", source);
                rideData.put("destination", destination);
//                rideData.put("dateTime", dateTime);

//                db.collection("rides")
//                        .add(rideData)
//                        .addOnCompleteListener(new OnCompleteListener() {
//                            @Override
//                            public void onComplete(Task task) {
//                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "DocumentSnapshot successfully written!");
//                                    // Data added successfully
//                                } else {
//                                    Log.d(TAG, "DocumentSnapshot not successfully written!");
//                                    // Error occurred while adding data
//                                }
//                            }
//                        });
                db.collection("rides").document()
                        .set(rideData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}