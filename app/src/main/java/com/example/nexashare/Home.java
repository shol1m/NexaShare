package com.example.nexashare;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.nexashare.Adapter.Ride;
import com.example.nexashare.Adapter.RideAdapter;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {
    private ImageView notification;
    private CardView singles,groups;
    RidesFragment ridesFragment = new RidesFragment();
    GroupRideFragment groupRideFragment = new GroupRideFragment();
    public Home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        notification= view.findViewById(R.id.notification);
        singles= view.findViewById(R.id.singles);
        groups= view.findViewById(R.id.groups);

        singles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                try {
                    // Check if getParentFragmentManager() is the correct context, otherwise use getChildFragmentManager()
                    if (getParentFragmentManager() != null) {
                        transaction.replace(R.id.flFragment, ridesFragment);
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
        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                try {
                    // Check if getParentFragmentManager() is the correct context, otherwise use getChildFragmentManager()
                    if (getParentFragmentManager() != null) {
                        transaction.replace(R.id.flFragment, groupRideFragment);
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

        return view;
    }



}