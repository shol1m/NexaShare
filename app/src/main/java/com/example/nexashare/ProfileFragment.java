package com.example.nexashare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    CreatedFragment createdFragment = new CreatedFragment();
    JoinedFragment joinedFragment = new JoinedFragment();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bottomNavigationView
                = view.findViewById(R.id.bottomNavigationView);

//        bottomNavigationView.setOnNavigationItemSelectedListener();
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationView);
        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.joined) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, joinedFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.created) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, createdFragment)
                    .commit();
            return true;
        }else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, createdFragment)
                    .commit();
        }
        return false;
    }
}