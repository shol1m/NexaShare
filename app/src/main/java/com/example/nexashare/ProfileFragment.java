package com.example.nexashare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nexashare.Models.MyData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment{
    LinearLayout notifications,joinedRides,createdRides,accountDetails,deleteAccount,logout;
    TextView username,email;
    String myEmail,myName;
    private FirebaseAuth mAuth;
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
        email = view.findViewById(R.id.email);
        logout = view.findViewById(R.id.logout);
//
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        myEmail = currentUser.getEmail();

        username.setText(MyData.name);
        email.setText(MyData.email);

        JoinedFragment joinedFragment = new JoinedFragment();
        CreatedFragment createdFragment = new CreatedFragment();
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

}