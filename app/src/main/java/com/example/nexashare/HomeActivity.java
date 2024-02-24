package com.example.nexashare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nexashare.GroupRides.CreateGroupRideFragment;
import com.example.nexashare.GroupRides.GroupRidesDisplayFragment;
import com.example.nexashare.SingeRides.CreateRideFragment;
import com.example.nexashare.SingeRides.SingleRidesDisplayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    Home homeFragment = new Home();
    SingleRidesDisplayFragment singleRidesDisplayFragment = new SingleRidesDisplayFragment();
    GroupRidesDisplayFragment groupRidesDisplayFragment = new GroupRidesDisplayFragment();
    CreateRideFragment createRideFragment = new CreateRideFragment();
    CreateGroupRideFragment createGroupRideFragment = new CreateGroupRideFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationView);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, homeFragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.create) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, createRideFragment)
                    .commit();
            return true;
        }else if (itemId == R.id.create_group_ride) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, createGroupRideFragment)
                    .commit();
            return true;
        }else if (itemId == R.id.profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .commit();
;
            return true;
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .commit();
        }
        return false;

    }


}