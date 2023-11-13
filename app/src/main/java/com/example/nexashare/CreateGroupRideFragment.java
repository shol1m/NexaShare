package com.example.nexashare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nexashare.Helper.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CreateGroupRideFragment extends Fragment {
    private EditText destination, source, phone, carType,seats,dateTime;
    private Button createRideBtn;
    private String formattedDateTime;
    private Date selectedDateTime;
    private FirebaseHelper firebaseHelper;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group_ride, container, false);

        phone = view.findViewById(R.id.phone_create_edt);
        source = view.findViewById(R.id.source_edt);
        destination = view.findViewById(R.id.destination_edt);
        seats = view.findViewById(R.id.seats_edt);
        carType = view.findViewById(R.id.car_type_edt);
        dateTime = view.findViewById(R.id.dateTime_Edt);
        createRideBtn = view.findViewById(R.id.create_ride_btn);
        firebaseHelper = new FirebaseHelper();

        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePickerDialog();
            }
        });

        createRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Source = source.getText().toString();
                String Destination = destination.getText().toString();
                String Phone = phone.getText().toString();
                String CarType = carType.getText().toString();
                int Seats = Integer.parseInt(seats.getText().toString());
                String rideType = "Group";

                Map<String, Object> rideData = new HashMap<>();
                rideData.put("rideType", rideType);
                rideData.put("source", Source);
                rideData.put("destination", Destination);
                rideData.put("phone_number", Phone);
                rideData.put("date_and_time", String.valueOf(selectedDateTime));
                rideData.put("cartType", CarType);
                rideData.put("seats", Seats);

                db.collection("rides").document()
                        .set(rideData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot for create rides successfully written!");
                                Toast.makeText(getContext(),"Ride Created successfully",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document for create rides", e);
                                Toast.makeText(getContext(),"Error Creating Rides,check your Internet",Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        return view;
    }
    private void showDateTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Show DatePickerDialog for date selection
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        showTimePickerDialog(c);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog(final Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show TimePickerDialog for time selection
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        selectedDateTime = calendar.getTime();

                        // Format the selected date and time
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                        formattedDateTime = dateFormat.format(selectedDateTime);

                        // Set the formatted date and time to the EditText
//                        dateTimeEdt.setText(formattedDateTime);
                    }
                },
                hour, minute, false);

        timePickerDialog.show();
    }
}