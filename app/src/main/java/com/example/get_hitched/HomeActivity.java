package com.example.get_hitched;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load HomeFragment as first screen
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_venues) {
                selectedFragment = new VenuesFragment();
            } else if (id == R.id.nav_bookings) {
                selectedFragment = new BookingsFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });
    }
}


