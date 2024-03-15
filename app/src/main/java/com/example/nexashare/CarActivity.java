package com.example.nexashare;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nexashare.Models.MyData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CarActivity extends AppCompatActivity {

    EditText model,make,plate;
    Button Submit;
    MyData data=new MyData();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

//        model=findViewById(R.id.name_car_model_edt);
//        make=findViewById(R.id.car_make_edt);
//        plate=findViewById(R.id.car_plate_edt);
//        Submit=findViewById(R.id.submit_btn);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Model = model.getText().toString();
                String Make = make.getText().toString();
                String Plate=plate.getText().toString();
                String userId = MyData.userId;


                Map<String, Object> carData = new HashMap<>();
                carData.put("userId",userId);
                carData.put("model",Model);
                carData.put("make",Make);
                carData.put("plate",Plate);

                db.collection("users").document(MyData.userId).collection("Cars").document(MyData.userId)
                        .set(carData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot for create rides successfully written!");
                                Toast.makeText(CarActivity.this, "Car Information submitted successfully", Toast.LENGTH_SHORT).show();
                                model.setText("");
                                make.setText("");
                                plate.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document for create rides", e);
                                Toast.makeText(CarActivity.this, "Error submitting car information", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });



    }
}