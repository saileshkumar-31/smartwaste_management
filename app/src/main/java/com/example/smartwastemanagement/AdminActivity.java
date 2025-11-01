package com.example.smartwastemanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminRequestAdapter adapter;
    private List<WasteRequest> requestList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference("requests");

        recyclerView = findViewById(R.id.adminRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        adapter = new AdminRequestAdapter(requestList, this::showStatusDialog);
        recyclerView.setAdapter(adapter);

        loadAllRequests();
    }

    private void loadAllRequests() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String requestId = snapshot.getKey();
                    String userId = snapshot.child("userId").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String wasteType = snapshot.child("wasteType").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String timestamp = snapshot.child("timestamp").getValue(String.class);

                    WasteRequest request = new WasteRequest(requestId, userId, address,
                            wasteType, description, status, timestamp);
                    requestList.add(request);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Error loading requests", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStatusDialog(WasteRequest request) {
        String[] statuses = {"Pending", "In Progress", "Completed", "Cancelled"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status for: " + request.getWasteType());
        builder.setItems(statuses, (dialog, which) -> {
            String newStatus = statuses[which];
            updateRequestStatus(request.getRequestId(), newStatus);
        });
        builder.show();
    }

    private void updateRequestStatus(String requestId, String newStatus) {
        databaseReference.child(requestId).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Status updated to: " + newStatus, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show());
    }
}
