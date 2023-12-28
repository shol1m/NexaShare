package com.example.nexashare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Home homeFragment = new Home();
    CreateRideFragment createRideFragment = new CreateRideFragment();
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        String home = String.valueOf(R.id.home);
//        String create = String.valueOf(R.id.create);
//        String profile = String.valueOf(R.id.profile);
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
        } else if (itemId == R.id.profile) {
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
}