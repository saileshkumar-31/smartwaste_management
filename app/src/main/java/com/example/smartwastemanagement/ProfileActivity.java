package com.example.smartwastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);   // uses fragment_profile.xml

        TextView nameText = findViewById(R.id.nameText);
        TextView emailText = findViewById(R.id.emailText);

        nameText.setText("John Doe");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            emailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        } else {
            emailText.setText("demo@email.com");
        }

        Button editProfileBtn = findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(v -> {
            // demo only – edit screen not implemented
            // startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        Button myRequestsBtn = findViewById(R.id.myRequestsBtn);
        myRequestsBtn.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, ViewRequestsActivity.class))
        );

        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                auth.signOut();
            }

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
