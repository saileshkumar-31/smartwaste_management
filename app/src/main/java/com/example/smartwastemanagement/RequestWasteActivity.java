package com.example.smartwastemanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RequestWasteActivity extends AppCompatActivity {

    private EditText addressEditText, descriptionEditText;
    private Spinner wasteTypeSpinner;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_waste);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("requests");

        // Initialize views
        addressEditText = findViewById(R.id.addressEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        wasteTypeSpinner = findViewById(R.id.wasteTypeSpinner);
        submitButton = findViewById(R.id.submitButton);

        // Setup spinner
        String[] wasteTypes = {
                "Organic",
                "Plastic",
                "Paper/Cardboard",
                "Metal/Cans",
                "Glass/Bottles",
                "Electronic Waste (E-waste)",
                "Hazardous Waste",
                "Construction/Demolition",
                "Textile/Clothing",
                "Mixed/General Waste"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wasteTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wasteTypeSpinner.setAdapter(adapter);

        // Submit button
        submitButton.setOnClickListener(v -> submitRequest());
    }

    private void submitRequest() {
        String address = addressEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String wasteType = wasteTypeSpinner.getSelectedItem().toString();

        if (address.isEmpty()) {
            addressEditText.setError("Address required");
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        String requestId = databaseReference.push().getKey();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        HashMap<String, String> request = new HashMap<>();
        request.put("userId", userId);
        request.put("address", address);
        request.put("wasteType", wasteType);
        request.put("description", description);
        request.put("status", "Pending");
        request.put("timestamp", timestamp);

        databaseReference.child(requestId).setValue(request)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
