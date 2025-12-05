package com.example.smartwastemanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use existing fragment_profile.xml as the profile screen
        setContentView(R.layout.fragment_profile);
    }
}
