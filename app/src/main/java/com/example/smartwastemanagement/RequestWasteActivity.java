package com.example.smartwastemanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestWasteActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    private static final int LOCATION_REQUEST = 101;

    private ImageView imageView;
    private EditText descEditText;
    private Button submitBtn;

    private Bitmap capturedImage;
    private FusedLocationProviderClient fusedLocationClient;
    private double userLat = 0, userLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_waste);

        imageView = findViewById(R.id.imageView);
        descEditText = findViewById(R.id.descEditText);
        submitBtn = findViewById(R.id.submitBtn);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        imageView.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

        submitBtn.setOnClickListener(v -> submitRequest());

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST
            );
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLat = location.getLatitude();
                userLng = location.getLongitude();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            capturedImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(capturedImage);
        }
    }

    private void submitRequest() {
        if (capturedImage == null) {
            Toast.makeText(this, "Take a photo first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userLat == 0 && userLng == 0) {
            Toast.makeText(this, "Wait for GPS location", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "anonymous";

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imagePath = "waste_requests/" + userId + "_" + timestamp + ".jpg";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageData = baos.toByteArray();

        storageRef.putBytes(imageData).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnSuccessListener(uri ->
                        saveRequestToDatabase(userId, uri.toString())
                )
        ).addOnFailureListener(e ->
                Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void saveRequestToDatabase(String userId, String imageUrl) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("waste_requests");
        String requestId = ref.push().getKey();

        Map<String, Object> request = new HashMap<>();
        request.put("userId", userId);
        request.put("description", descEditText.getText().toString());
        request.put("imageUrl", imageUrl);
        request.put("lat", userLat);
        request.put("lng", userLng);
        request.put("status", "pending");
        request.put("timestamp", System.currentTimeMillis());

        ref.child(requestId).setValue(request).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Request submitted with GPS!", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST) {
            getCurrentLocation();
        }
    }
}
