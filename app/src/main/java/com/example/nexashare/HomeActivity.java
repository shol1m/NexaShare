package com.example.nexashare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        Home.OnCardClickListener {

    BottomNavigationView bottomNavigationView;
    Home homeFragment = new Home();
    RidesFragment ridesFragment = new RidesFragment();
    GroupRideFragment groupRideFragment = new GroupRideFragment();
    CreateRideFragment createRideFragment = new CreateRideFragment();
    CreateGroupRideFragment createGroupRideFragment = new CreateGroupRideFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment.setOnCardClickListener(this);
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationView);
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
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.flFragment, profileFragment)
//                    .commit();
            startActivity(new Intent(HomeActivity.this, Profile.class));
//            Intent profile = new Intent(HomeActivity.this,Profile.class);
//            StartActivity(profile);
            return true;
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .commit();
        }
        return false;

    }

    @Override
    public void onCard1Clicked() {
        // Handle the click for card 1
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, ridesFragment)
                .commit();
    }

    @Override
    public void onCard2Clicked() {
        // Handle the click for card 2
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, groupRideFragment)
                .commit();
    }


}