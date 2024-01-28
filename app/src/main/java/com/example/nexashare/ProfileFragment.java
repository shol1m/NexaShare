package com.example.nexashare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment{
    LinearLayout notifications,joinedRides,createdRides,accountDetails,deleteAccount,logout;
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
        logout = view.findViewById(R.id.logout);
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
                joinedRides.setBackgroundColor(getContext().getColor(R.color.light_grey));
                startActivity(new Intent(getContext(), JoinedFragment.class));
            }
        });
        createdRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createdRides.setBackgroundColor(getContext().getColor(R.color.light_grey));
                startActivity(new Intent(getContext(), CreatedFragment.class));
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