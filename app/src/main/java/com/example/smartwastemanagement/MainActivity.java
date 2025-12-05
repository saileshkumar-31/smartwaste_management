package com.example.smartwastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

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

    // Decide if current user is admin or normal user
    private void setupRole() {
        String adminEmail = "admin.smartwaste@gmail.com";   // fixed admin account
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            isAdmin = currentEmail != null && currentEmail.equals(adminEmail);
        } else {
            isAdmin = false;
        }
    }

    private void setupBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.nav_home) {
                            // stay on home (current layout)
                            return true;
                        } else if (id == R.id.nav_requests) {
                            if (isAdmin) {
                                // Admin: open map with all user pins
                                Intent intent = new Intent(MainActivity.this, AdminMapActivity.class);
                                startActivity(intent);
                            } else {
                                // Normal user: open old request list screen
                                Intent intent = new Intent(MainActivity.this, ViewRequestsActivity.class);
                                startActivity(intent);
                            }
                            return true;
                        } else if (id == R.id.nav_profile) {
                            // Open ProfileActivity (uses fragment_profile.xml)
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
    }

    private void setupFab() {
        fabNewRequest.setOnClickListener(v -> {
            // Both admin and user create new request (demo mode)
            Intent intent = new Intent(MainActivity.this, RequestWasteActivity.class);
            startActivity(intent);
        });
    }
}
