package com.example.smartwastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button requestButton;
    private Button viewRequestsButton;
    private Button adminButton;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        welcomeTextView = findViewById(R.id.welcomeTextView);
        requestButton = findViewById(R.id.requestButton);
        viewRequestsButton = findViewById(R.id.viewRequestsButton);
        adminButton = findViewById(R.id.adminButton);
        logoutButton = findViewById(R.id.logoutButton);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            welcomeTextView.setText("Welcome, " + email);

            // Show admin button only for admin user
            if (email != null && email.equals("ss@gmail.com")) {
                adminButton.setVisibility(View.VISIBLE);
            } else {
                adminButton.setVisibility(View.GONE);
            }
        }

        requestButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RequestWasteActivity.class));
        });

        viewRequestsButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewRequestsActivity.class));
        });

        adminButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
