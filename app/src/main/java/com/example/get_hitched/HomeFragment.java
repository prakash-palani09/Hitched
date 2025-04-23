package com.example.get_hitched;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HomeFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DatabaseReference dbRef;
    private TextView weddingDetailsText, countdownText, locationText;
    private VendorAdapter vendorAdapter;
    private List<Vendor> vendorList;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        weddingDetailsText = view.findViewById(R.id.weddingDetailsText);
        countdownText = view.findViewById(R.id.weddingCountdownText);
        locationText = view.findViewById(R.id.locationText); // TextView to display the address

        Button buttonPlanWedding = view.findViewById(R.id.buttonPlanWedding);
        Button buttonCatering = view.findViewById(R.id.buttonCatering);
        Button buttonWeddingTheme = view.findViewById(R.id.buttonWeddingTheme);
        Button buttonInvitations = view.findViewById(R.id.buttonInvitations);
        RecyclerView vendorRecyclerView = view.findViewById(R.id.rv_vendors);

        vendorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vendorList = new ArrayList<>();
        vendorAdapter = new VendorAdapter(vendorList, getContext());
        vendorRecyclerView.setAdapter(vendorAdapter);

        loadWeddingDetails();
        loadTopVendors();

        // Check location permissions and fetch location if granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();  // Fetch the current location
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        buttonPlanWedding.setOnClickListener(v -> startActivity(new Intent(getActivity(), PlannerActivity.class)));
        buttonCatering.setOnClickListener(v -> startActivity(new Intent(getActivity(), CateringActivity.class)));
        buttonWeddingTheme.setOnClickListener(v -> startActivity(new Intent(getActivity(), WeddingThemeActivity.class)));
        buttonInvitations.setOnClickListener(v -> startActivity(new Intent(getActivity(), InvitationsActivity.class)));

        return view;
    }

    // Method to get the current location using FusedLocationProviderClient
    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            getAddressFromCoordinates(latitude, longitude); // Fetch address using the coordinates
                        } else {
                            Toast.makeText(getContext(), "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to get the address from latitude and longitude using Geocoder
    private void getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();

                String fullAddress = addressLine + ", " + city + ", " + state + ", " + country;
                locationText.setText("Location: " + fullAddress);
            } else {
                Toast.makeText(getContext(), "No address found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to get address", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load wedding details
    private void loadWeddingDetails() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DocumentReference docRef = firestore.collection("weddings").document(userId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String partnerName = documentSnapshot.getString("partner_name");
                String weddingDate = documentSnapshot.getString("wedding_date");

                String details = "Name: " + name + "\nPartner's Name: " + partnerName;
                weddingDetailsText.setText(details);

                calculateCountdown(weddingDate);
            } else {
                weddingDetailsText.setText("No wedding details found.");
                showWeddingDetailsDialog(); // show popup
            }
        });
    }

    private void showWeddingDetailsDialog() {
        WeddingDetailsDialog dialog = new WeddingDetailsDialog(() -> {
            loadWeddingDetails();
        });
        dialog.setCancelable(false);
        dialog.show(getChildFragmentManager(), "weddingDetailsDialog");
    }

    // Method to calculate countdown
    private void calculateCountdown(String weddingDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(weddingDate);
            assert date != null;
            long diff = date.getTime() - new Date().getTime();
            long daysRemaining = diff / (1000 * 60 * 60 * 24);

            countdownText.setText(daysRemaining >= 0
                    ? "Days until wedding: " + daysRemaining
                    : "Wedding already happened!");
        } catch (ParseException e) {
            countdownText.setText("Invalid date format");
        }
    }

    private void loadTopVendors() {
        dbRef = FirebaseDatabase.getInstance().getReference("vendors");

        if (dbRef != null) {
            dbRef.limitToFirst(5).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    vendorList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Vendor vendor = snap.getValue(Vendor.class);
                        if (vendor != null) {
                            vendorList.add(vendor);
                        }
                    }
                    vendorAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load vendors: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Firebase database reference is null", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation(); // Fetch location if permission granted
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
