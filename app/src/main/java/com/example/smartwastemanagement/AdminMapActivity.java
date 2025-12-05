package com.example.smartwastemanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Default camera so map is not blank
        LatLng city = new LatLng(13.0827, 80.2707); // change to your city if you want
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 12f));

        loadWastePins();
    }

    private void loadWastePins() {
        FirebaseDatabase.getInstance().getReference("waste_requests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (mMap == null) return;

                        mMap.clear();
                        LatLng first = null;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Double lat = child.child("lat").getValue(Double.class);
                            Double lng = child.child("lng").getValue(Double.class);
                            String desc = child.child("description").getValue(String.class);

                            if (lat != null && lng != null) {
                                LatLng pos = new LatLng(lat, lng);
                                if (first == null) first = pos;
                                mMap.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title(desc == null ? "Waste request" : desc));
                            }
                        }

                        if (first != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first, 14f));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) { }
                });
    }
}
