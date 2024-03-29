package com.example.nexashare;

import static com.example.nexashare.Models.MyData.userId;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nexashare.Models.MyData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Objects;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ProfileFragment extends Fragment{
    LinearLayout notifications,joinedRides,createdRides,accountDetails,deleteAccount,logout;
    TextView username,greetings;
    ImageView profile;
    String myEmail,myName;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        notifications = view.findViewById(R.id.notifications);
        joinedRides = view.findViewById(R.id.joined);
        createdRides = view.findViewById(R.id.created);
        username = view.findViewById(R.id.username);
        greetings = view.findViewById(R.id.greetings);
        logout = view.findViewById(R.id.logout);
        accountDetails = view.findViewById(R.id.account_details);
        profile = view.findViewById(R.id.profileEdit);

        storage = FirebaseStorage.getInstance();

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Determine the greeting message based on the time
        String greetingMessage;
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greetingMessage = "Good Morning";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greetingMessage = "Good Afternoon";
        } else {
            greetingMessage = "Good Evening";
        }

        username.setText(MyData.name);
        greetings.setText(greetingMessage);

        JoinedFragment joinedFragment = new JoinedFragment();
        CreatedFragment createdFragment = new CreatedFragment();
        PhoneVerification phoneVerification = new PhoneVerification();

        StorageReference storageRef = storage.getReference().child("profile_pictures").child(userId);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Load the image into the ImageView using Glide
                Glide.with(getContext())
                        .load(uri)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
                                .error(R.drawable.baseline_person_off_24) // Error image if download fails
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching to always load the latest image
                        )
                        .into(profile);// Disable caching to always load the latest image
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                e.printStackTrace();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications.setBackgroundColor(getContext().getColor(R.color.light_grey));
                startActivity(new Intent(getContext(), Notifications.class));
            }
        });
        joinedRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                try {
                    // Check if getParentFragmentManager() is the correct context, otherwise use getChildFragmentManager()
                    if (getParentFragmentManager() != null) {
                        transaction.replace(R.id.flFragment, joinedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        // Log an error if getParentFragmentManager() returns null
                        Log.e("FragmentTransaction", "Parent Fragment Manager is null");
                    }
                } catch (Exception e) {
                    // Log any exception that might occur during the transaction
                    Log.e("FragmentTransaction", "Error during fragment transaction", e);
                }
            }
        });
        createdRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                try {
                    // Check if getParentFragmentManager() is the correct context, otherwise use getChildFragmentManager()
                    if (getParentFragmentManager() != null) {
                        transaction.replace(R.id.flFragment, createdFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        // Log an error if getParentFragmentManager() returns null
                        Log.e("FragmentTransaction", "Parent Fragment Manager is null");
                    }
                } catch (Exception e) {
                    // Log any exception that might occur during the transaction
                    Log.e("FragmentTransaction", "Error during fragment transaction", e);
                }
            }
        });
        accountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                startActivity(new Intent(getContext(), AccountDetailsActivity.class));

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),StartActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            // Image successfully selected from gallery
            Uri selectedImageUri = data.getData();
            uploadProfilePicture(selectedImageUri);
        }
    }

    private void uploadProfilePicture(Uri filePath) {
        if (filePath != null) {
//            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            StorageReference storageRef = storage.getReference().child("profile_pictures").child(userId);

            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Profile picture uploaded successfully
                            Log.d("STORAGE","Profile picture uploaded successfully");
                            Toast.makeText(getContext(), "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle unsuccessful uploads
                            Log.e("STORAGE","Failed to upload profile picture "+e);
                            Toast.makeText(getContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No file selected
            Log.e("STORAGE","No file selected");
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}