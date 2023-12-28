package com.example.nexashare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Profile extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    private ImageView img;
    CreatedFragment createdFragment = new CreatedFragment();
    JoinedFragment joinedFragment = new JoinedFragment();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView
                = findViewById(R.id.ridesNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.ridesNavigationView);

//        edit = findViewById(R.id.edit_profile);
//        Button edit = findViewById(R.id.edit_profile_btn);
//        img = findViewById(R.id.img);

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showEditDialog();
//            }
//        });


    }

    private void showEditDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Edit");
        LinearLayout linearLayout=new LinearLayout(this);
//        final EditText emailet= new EditText(this);
//
//        // write the email using which you registered
//        emailet.setText("Email");
//        emailet.setMinEms(16);
//        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        final EditText photo = new EditText(this);
//        photo.setHint("Select file");
//        photo.setMinEms(16);
//        linearLayout.addView(emailet);
//        linearLayout.addView(photo);

        final Button upload = new Button(this);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openFileChooser();
//                editPersonalDetails();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void editPersonalDetails(){

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.joined) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ridesFragment, joinedFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.created) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ridesFragment, createdFragment)
                    .commit();
            return true;
        }else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ridesFragment, createdFragment)
                    .commit();
        }
        return false;
    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            img.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child("profile_images")
                    .child("892630236");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle successful upload
                            // You can retrieve the download URL to save to a database or to display the image
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Use this uri to store in your database associated with the user
                                    // For example, you can use Firebase Firestore or Realtime Database
                                    // Update your user profile with this URL for future retrieval
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle unsuccessful uploads
                        }
                    });
        }
    }
}