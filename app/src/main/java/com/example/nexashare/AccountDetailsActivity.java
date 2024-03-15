package com.example.nexashare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.nexashare.Models.MyData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.SetOptions;

public class AccountDetailsActivity extends AppCompatActivity {
    TextView nameTxt,emailTxt,phoneTxt,modelTxt,makeTxt,plateTxt;
    TextView editName,editEmail,editPhone,editModel,editMake,editPlate;
    String email,name,phone,make,model,plate;
    boolean modelExist,makeExist,plateExist;
    private ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        nameTxt = findViewById(R.id.name);
        emailTxt = findViewById(R.id.email);
        phoneTxt = findViewById(R.id.phone);
        modelTxt = findViewById(R.id.model);
        makeTxt = findViewById(R.id.make);
        plateTxt = findViewById(R.id.plate);
        editName = findViewById(R.id.name_edit);
        editEmail = findViewById(R.id.email_edit);
        editPhone = findViewById(R.id.phone_edit);
        editModel = findViewById(R.id.model_edit);
        editMake = findViewById(R.id.make_edit);
        editPlate = findViewById(R.id.plate_edit);

        db.collection("users")
                .document(MyData.userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // DocumentSnapshot exists, retrieve data
                            name = document.getString("name");
                            email = document.getString("email");
                            phone = document.getString("phone_number");

                            nameTxt.setText(name);
                            emailTxt.setText(email);
                            phoneTxt.setText(phone);

                        } else {
                            // Document does not exist
                            Log.e("FirestoreData", "No such document");
                        }
                    } else {
                        // Task failed with an exception
                        Log.d("FirestoreData", "Task failed: " + task.getException());
                    }
                });
        db.collection("users")
                .document(MyData.userId)
                .collection("cars")
                .document(MyData.userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            // DocumentSnapshot exists, retrieve data
                            model = document.getString("model");
                            make = document.getString("make");
                            plate = document.getString("plate");

                            modelTxt.setText(model);
                            makeTxt.setText(make);
                            plateTxt.setText(plate);

                            modelExist = !TextUtils.isEmpty(model);
                            makeExist = !TextUtils.isEmpty(make);
                            plateExist = !TextUtils.isEmpty(plate);

                        } else {
                            // Document does not exist
                            Log.e("FirestoreData", "No such document");
                        }
                    } else {
                        // Task failed with an exception
                        Log.d("FirestoreData", "Task failed: " + task.getException());
                    }
                });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditNamePopupDialog(AccountDetailsActivity.this);
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditEmailPopupDialog(AccountDetailsActivity.this);
            }
        });
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditPhonePopupDialog(AccountDetailsActivity.this,phone);
            }
        });
        editModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditModelPopupDialog(AccountDetailsActivity.this,modelExist);
            }
        });
        editMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditMakePopupDialog(AccountDetailsActivity.this,makeExist);
            }
        });
        editPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditPlatePopupDialog(AccountDetailsActivity.this,plateExist);
            }
        });
    }
    private void showEditNamePopupDialog(Context context){
        // Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Name");

        // Create a LinearLayout to hold EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditText
        final EditText editText = new EditText(context);
        layout.addView(editText);

        // Set the LinearLayout as the AlertDialog's view
        builder.setView(layout);
        editText.setText(name);

        // Add buttons to the AlertDialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Submit button click
                String inputText = editText.getText().toString();
                updateName(inputText);
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showEditEmailPopupDialog(Context context){
        // Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Email");

        // Create a LinearLayout to hold EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditText
        final EditText editText = new EditText(context);
        layout.addView(editText);

        // Set the LinearLayout as the AlertDialog's view
        builder.setView(layout);
        editText.setText(email);

        // Add buttons to the AlertDialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Submit button click
                String inputText = editText.getText().toString();
                updateEmail(inputText);
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showEditPhonePopupDialog(Context context,String phone) {
        // Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Phone Number");

        // Create a LinearLayout to hold EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditText
        final EditText editText = new EditText(context);
        layout.addView(editText);

        // Set the LinearLayout as the AlertDialog's view
        builder.setView(layout);
        editText.setText(phone);

        // Add buttons to the AlertDialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Submit button click
                String inputText = editText.getText().toString();
//                Toast.makeText(AccountDetailsActivity.this, "Entered Text: " + inputText, Toast.LENGTH_SHORT).show();
                SendOTP(inputText);
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showEditModelPopupDialog(Context context,boolean exist){
        // Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (exist) {
            builder.setTitle("Update Model");
        }else{
            builder.setTitle("Add Model");
        }
        // Create a LinearLayout to hold EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditText
        final EditText editText = new EditText(context);
        layout.addView(editText);

        // Set the LinearLayout as the AlertDialog's view
        builder.setView(layout);
        editText.setText(model);

        // Add buttons to the AlertDialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Submit button click
                String inputText = editText.getText().toString();
                updateModel(inputText,exist);
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showEditMakePopupDialog(Context context,boolean exist){
        // Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (exist) {
            builder.setTitle("Update Make");
        }else{
            builder.setTitle("Add Make");
        }
        // Create a LinearLayout to hold EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditText
        final EditText editText = new EditText(context);
        layout.addView(editText);

        // Set the LinearLayout as the AlertDialog's view
        builder.setView(layout);
        editText.setText(make);

        // Add buttons to the AlertDialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Submit button click
                String inputText = editText.getText().toString();
                updateMake(inputText,exist);
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showEditPlatePopupDialog(Context context,boolean exist){
        // Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (exist) {
            builder.setTitle("Update Plate");
        }else{
            builder.setTitle("Add Plate");
        }
        // Create a LinearLayout to hold EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditText
        final EditText editText = new EditText(context);
        layout.addView(editText);

        // Set the LinearLayout as the AlertDialog's view
        builder.setView(layout);
        editText.setText(plate);

        // Add buttons to the AlertDialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Submit button click
                String inputText = editText.getText().toString();
                updatePlate(inputText,exist);
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updateName(String name){
        db.collection("users")
                .document(MyData.userId)
                .update("name",name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FIRESTORE_VALUE","Name updated successfully");
                        Toast.makeText(AccountDetailsActivity.this, "Name updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FIRESTORE_VALUE","Could not update name : "+e);
                    }
                });
    }
    private void updateEmail(String email){}
    private void updateModel(String model, boolean exist){
        if (exist){
            db.collection("users")
                    .document(MyData.userId)
                    .collection("cars")
                    .document(MyData.userId)
                    .update("model",model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FIRESTORE_VALUE","Model updated successfully");
                            Toast.makeText(AccountDetailsActivity.this, "Model updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FIRESTORE_VALUE","Could not update name : "+e);
                        }
                    });
        }else{
            Log.e("FIRESTORE_VALUE","Model does not exist: ");
            Map<String, Object> modelData = new HashMap<>();
            modelData.put("model",model);
            db.collection("users")
                    .document(MyData.userId)
                    .collection("cars")
                    .document(MyData.userId)
                    .set(modelData, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FIRESTORE_VALUE","Model updated successfully");
                            Toast.makeText(AccountDetailsActivity.this, "Model updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FIRESTORE_VALUE","Could not update name : "+e);
                        }
                    });
        }
    }
    private void updateMake(String make, boolean exist){
        if (exist){
            db.collection("users")
                    .document(MyData.userId)
                    .collection("cars")
                    .document(MyData.userId)
                    .update("make",make)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FIRESTORE_VALUE","Make updated successfully");
                            Toast.makeText(AccountDetailsActivity.this, "Make updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FIRESTORE_VALUE","Could not update Make : "+e);
                        }
                    });
        }else{
            Map<String, Object> makeData = new HashMap<>();
            makeData.put("make",make);
            db.collection("users")
                    .document(MyData.userId)
                    .collection("cars")
                    .document(MyData.userId)
                    .set(makeData, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FIRESTORE_VALUE","Make updated successfully");
                            Toast.makeText(AccountDetailsActivity.this, "Make added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FIRESTORE_VALUE","Could not update make : "+e);
                        }
                    });
        }
    }
    private void updatePlate(String plate, boolean exist){
        if (exist){
            db.collection("users")
                    .document(MyData.userId)
                    .collection("cars")
                    .document(MyData.userId)
                    .update("plate",plate)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FIRESTORE_VALUE","Plate updated successfully");
                            Toast.makeText(AccountDetailsActivity.this, "Plate updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FIRESTORE_VALUE","Could not update plate : "+e);
                        }
                    });
        }else{
            Map<String, Object> plateData = new HashMap<>();
            plateData.put("plate",plate);
            db.collection("users")
                    .document(MyData.userId)
                    .collection("cars")
                    .document(MyData.userId)
                    .set(plateData, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FIRESTORE_VALUE","Plate updated successfully");
                            Toast.makeText(AccountDetailsActivity.this, "Plate added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FIRESTORE_VALUE","Could not update plate : "+e);
                        }
                    });
        }
    }
    private void SendOTP(String phone) {
    // Show progress dialog
    showProgressDialog("Sending OTP...");

    PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+254" + phone,
            60,
            TimeUnit.SECONDS,
            AccountDetailsActivity.this,
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    // Hide progress dialog
                    hideProgressDialog();
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    // Hide progress dialog
                    hideProgressDialog();
                    Toast.makeText(AccountDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    // Hide progress dialog
                    hideProgressDialog();
                    // Open the OTP input dialog when code is sent
                    showOTPInputDialog(verificationId);
                }
            }
    );
}
    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(AccountDetailsActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void showOTPInputDialog(String verificationId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailsActivity.this);
        builder.setTitle("Enter OTP");

        View viewInflated = LayoutInflater.from(AccountDetailsActivity.this).inflate(R.layout.dialog_otp_input, null, false);

        final EditText inputOtp1 = viewInflated.findViewById(R.id.otp_code1);
        final EditText inputOtp2 = viewInflated.findViewById(R.id.otp_code2);
        final EditText inputOtp3 = viewInflated.findViewById(R.id.otp_code3);
        final EditText inputOtp4 = viewInflated.findViewById(R.id.otp_code4);
        final EditText inputOtp5 = viewInflated.findViewById(R.id.otp_code5);
        final EditText inputOtp6 = viewInflated.findViewById(R.id.otp_code6);

        builder.setView(viewInflated);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the OTP code entered by the user
                String code = inputOtp1.getText().toString() +
                        inputOtp2.getText().toString() +
                        inputOtp3.getText().toString() +
                        inputOtp4.getText().toString() +
                        inputOtp5.getText().toString() +
                        inputOtp6.getText().toString();

                // Verify the OTP code using Firebase
                verifyOTP(verificationId, code);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private void verifyOTP(String verificationId, String code) {
        showProgressDialog("Verifying OTP...");
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            db.collection("users")
                                    .document(MyData.userId)
                                    .update("phone_number",phone)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AccountDetailsActivity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                                            Log.d("FIRESTORE_VALUE","Phone number updated successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("FIRESTORE_VALUE","Could not update phone number: "+e);
                                        }
                                    });

                        } else {
                            // Handle verification failure
                            Toast.makeText(AccountDetailsActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
