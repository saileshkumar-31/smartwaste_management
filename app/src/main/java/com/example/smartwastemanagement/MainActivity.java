package com.example.smartwastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FloatingActionButton fabNewRequest;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottom_nav);
        fabNewRequest = findViewById(R.id.fab_new_request);

        setupRole();
        setupBottomNav();
        setupFab();
    }

    private void setupRole() {
        // Demo: false = user, true = admin
        isAdmin = false;
    }

    private void setupBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int itemId = item.getItemId();  // get int id

                        // REAL switch starts here
                        if (itemId == R.id.nav_home) {

                            // Stay on dashboard
                            return true;

                        } else if (itemId == R.id.nav_requests) {

                            if (isAdmin) {
                                startActivity(new Intent(MainActivity.this, AdminMapActivity.class));
                            } else {
                                startActivity(new Intent(MainActivity.this, ViewRequestsActivity.class));
                            }
                            return true;

                        } else if (itemId == R.id.nav_profile) {

                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                            return true;
                        }

                        return false;
                    }
                });
    }


    private void setupFab() {
        fabNewRequest.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RequestWasteActivity.class));
        });
    }
}
