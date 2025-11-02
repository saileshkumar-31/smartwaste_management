package com.example.smartwastemanagement;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminRequestsActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private AdminRequestAdapter adapter;
    private List<WasteRequest> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_requests);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage All Requests");
        }

        // Initialize RecyclerView
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data (later you'll load from Firebase)
        loadSampleRequests();

        // Set up adapter
        adapter = new AdminRequestAdapter(this, requestList, new AdminRequestAdapter.OnRequestActionListener() {
            @Override
            public void onUpdateStatus(WasteRequest request) {
                // Handle status update
                updateRequestStatus(request);
            }
        });

        requestsRecyclerView.setAdapter(adapter);
    }

    private void loadSampleRequests() {
        requestList = new ArrayList<>();

        // Sample requests from different users
        // Parameters: requestId, userId, address, wasteType, description, status, timestamp
        requestList.add(new WasteRequest(
                "REQ001",
                "user1@gmail.com",
                "123 Main St, Bangalore",
                "Plastic & Paper",
                "Large amount of recyclable waste",
                "Pending",
                "Nov 2, 2025 10:30 AM"
        ));

        requestList.add(new WasteRequest(
                "REQ002",
                "user2@gmail.com",
                "456 Park Ave, Bangalore",
                "Organic Waste",
                "Kitchen waste from weekly cooking",
                "Processing",
                "Nov 1, 2025 2:15 PM"
        ));

        requestList.add(new WasteRequest(
                "REQ003",
                "user3@gmail.com",
                "789 Lake Rd, Bangalore",
                "Electronic Waste",
                "Old computer and monitor",
                "Completed",
                "Oct 28, 2025 9:00 AM"
        ));

        requestList.add(new WasteRequest(
                "REQ004",
                "user4@gmail.com",
                "321 Hill St, Bangalore",
                "Metal & Glass",
                "Glass bottles and metal cans",
                "Pending",
                "Nov 2, 2025 11:45 AM"
        ));
    }


    private void updateRequestStatus(WasteRequest request) {
        // Status options
        final String[] statusOptions = {"Pending", "Processing", "Completed", "Rejected"};

        // Create dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Update Request Status");
        builder.setItems(statusOptions, (dialog, which) -> {
            // Update the status based on selection
            String newStatus = statusOptions[which];
            request.setStatus(newStatus);
            adapter.notifyDataSetChanged();

            // Show confirmation
            android.widget.Toast.makeText(this,
                    "Status updated to: " + newStatus,
                    android.widget.Toast.LENGTH_SHORT).show();

            // In future: Update Firebase here
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
