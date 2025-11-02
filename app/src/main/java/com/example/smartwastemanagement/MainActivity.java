package com.example.smartwastemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FirebaseAuth mAuth;
    private static final String ADMIN_EMAIL = "ss@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigation = findViewById(R.id.bottomNavigation);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isAdmin = currentUser != null && ADMIN_EMAIL.equals(currentUser.getEmail());

        // Set up bottom navigation listener
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_request) {
                if (isAdmin) {
                    // Admin: Show all requests management
                    startActivity(new Intent(MainActivity.this, AdminRequestsActivity.class));
                } else {
                    // Regular user: Create new request
                    startActivity(new Intent(MainActivity.this, RequestWasteActivity.class));
                }
                return true;
            } else if (itemId == R.id.nav_menu) {
                loadFragment(new MenuFragment());
                return true;
            }
            return false;
        });

        // Load home fragment by default
        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    // Check if current user is admin
    public static boolean isAdmin(FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();
        return user != null && ADMIN_EMAIL.equals(user.getEmail());
    }

    // ============== HOME FRAGMENT ==============
    public static class HomeFragment extends Fragment {

        private TextView userEmailText;
        private FirebaseAuth mAuth;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            mAuth = FirebaseAuth.getInstance();
            userEmailText = view.findViewById(R.id.userEmailText);

            // Display user email
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                userEmailText.setText(user.getEmail());
            }

            return view;
        }
    }

    // ============== MENU FRAGMENT (with Requests + Profile + Logout) ==============
    public static class MenuFragment extends Fragment {

        private Button viewRequestsBtn, editProfileBtn, saveProfileBtn, logoutBtn;
        private LinearLayout profileDisplayLayout, profileEditLayout;
        private TextView displayName, displayEmail, displayMobile, displayAddress;
        private EditText editName, editMobile, editAddress;
        private FirebaseAuth mAuth;
        private SharedPreferences prefs;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_menu, container, false);

            mAuth = FirebaseAuth.getInstance();
            prefs = getActivity().getSharedPreferences("UserProfile", getActivity().MODE_PRIVATE);

            boolean isAdmin = MainActivity.isAdmin(mAuth);

            // Initialize views
            viewRequestsBtn = view.findViewById(R.id.viewRequestsBtn);
            editProfileBtn = view.findViewById(R.id.editProfileBtn);
            saveProfileBtn = view.findViewById(R.id.saveProfileBtn);
            logoutBtn = view.findViewById(R.id.logoutBtn);

            profileDisplayLayout = view.findViewById(R.id.profileDisplayLayout);
            profileEditLayout = view.findViewById(R.id.profileEditLayout);

            displayName = view.findViewById(R.id.displayName);
            displayEmail = view.findViewById(R.id.displayEmail);
            displayMobile = view.findViewById(R.id.displayMobile);
            displayAddress = view.findViewById(R.id.displayAddress);

            editName = view.findViewById(R.id.editName);
            editMobile = view.findViewById(R.id.editMobile);
            editAddress = view.findViewById(R.id.editAddress);

            // Display user email
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                displayEmail.setText(user.getEmail());
            }

            // Load saved profile data
            loadProfileData();

            // For Admin: Hide mobile and address, show only name
            if (isAdmin) {
                displayMobile.setVisibility(View.GONE);
                displayAddress.setVisibility(View.GONE);
                view.findViewById(R.id.mobileLabel).setVisibility(View.GONE);
                view.findViewById(R.id.addressLabel).setVisibility(View.GONE);
                editProfileBtn.setVisibility(View.GONE);
            }

            // View All Requests Button
            viewRequestsBtn.setOnClickListener(v -> {
                if (isAdmin) {
                    startActivity(new Intent(getActivity(), AdminRequestsActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), ViewRequestsActivity.class));
                }
            });

            // Edit Profile Button
            editProfileBtn.setOnClickListener(v -> {
                showEditMode();
            });

            // Save Profile Button
            saveProfileBtn.setOnClickListener(v -> {
                saveProfileData();
            });

            // Logout Button
            logoutBtn.setOnClickListener(v -> {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });

            return view;
        }

        private void loadProfileData() {
            String name = prefs.getString("name", "Not set");
            String mobile = prefs.getString("mobile", "Not set");
            String address = prefs.getString("address", "Not set");

            displayName.setText(name);
            displayMobile.setText(mobile);
            displayAddress.setText(address);
        }

        private void showEditMode() {
            profileDisplayLayout.setVisibility(View.GONE);
            profileEditLayout.setVisibility(View.VISIBLE);

            editName.setText(displayName.getText().toString().equals("Not set") ? "" : displayName.getText().toString());
            editMobile.setText(displayMobile.getText().toString().equals("Not set") ? "" : displayMobile.getText().toString());
            editAddress.setText(displayAddress.getText().toString().equals("Not set") ? "" : displayAddress.getText().toString());
        }

        private void saveProfileData() {
            String name = editName.getText().toString().trim();
            String mobile = editMobile.getText().toString().trim();
            String address = editAddress.getText().toString().trim();

            if (name.isEmpty() || mobile.isEmpty() || address.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", name);
            editor.putString("mobile", mobile);
            editor.putString("address", address);
            editor.apply();

            displayName.setText(name);
            displayMobile.setText(mobile);
            displayAddress.setText(address);

            profileEditLayout.setVisibility(View.GONE);
            profileDisplayLayout.setVisibility(View.VISIBLE);

            Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
