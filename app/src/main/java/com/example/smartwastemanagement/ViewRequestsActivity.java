package com.example.smartwastemanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<WasteRequest> requestList;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("requests");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        loadRequests();
    }

    private void loadRequests() {
        String currentUserId = mAuth.getCurrentUser().getUid();

        databaseReference.orderByChild("userId").equalTo(currentUserId)
                .addValueEventListener(new ValueEventListener() {
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
                        Toast.makeText(ViewRequestsActivity.this, "Error loading requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
