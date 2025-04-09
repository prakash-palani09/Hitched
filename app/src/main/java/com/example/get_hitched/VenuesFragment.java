package com.example.get_hitched;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.*;

import java.util.*;
import com.google.android.gms.location.FusedLocationProviderClient;
public class VenuesFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;

    private GridView gridView;
    private RecyclerView recyclerView;
    private List<Vendor> allVendors = new ArrayList<>();
    private List<Venue> allVenues = new ArrayList<>();
    private List<Object> filteredList = new ArrayList<>();
    private List<String> vendorTypes = Arrays.asList("All", "Photographer", "Decorator", "Caterer", "DJ", "Florist", "Makeup Artist", "Venue");

    private DatabaseReference vendorRef;
    private DatabaseReference venueRef;
    private int dataLoadedCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venues, container, false);

        gridView = view.findViewById(R.id.grid_vendor_types);
        recyclerView = view.findViewById(R.id.rv_combined_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                vendorTypes
        );
        gridView.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        vendorRef = FirebaseDatabase.getInstance().getReference("vendors");
        venueRef = FirebaseDatabase.getInstance().getReference("venues");

        loadAllData();

        gridView.setOnItemClickListener((AdapterView<?> parent, View v, int position, long id) -> {
            String selectedType = vendorTypes.get(position);
            filterByType(selectedType);
        });

        return view;
    }

    private void loadAllData() {
        dataLoadedCount = 0;

        vendorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allVendors.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Vendor vendor = snap.getValue(Vendor.class);
                    allVendors.add(vendor);
                }
                checkAndShow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load vendors", Toast.LENGTH_SHORT).show();
            }
        });

        venueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allVenues.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Venue venue = snap.getValue(Venue.class);
                    allVenues.add(venue);
                }
                checkAndShow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load venues", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndShow() {
        dataLoadedCount++;
        if (dataLoadedCount == 2) {
            filterByType("All");
        }
    }

    private void filterByType(String type) {
        filteredList.clear();

        if (type.equals("All")) {
            filteredList.addAll(allVendors);
            filteredList.addAll(allVenues);
        } else if (type.equals("Venue")) {
            filteredList.addAll(allVenues);
        } else {
            for (Vendor vendor : allVendors) {
                if (vendor.type != null && vendor.type.equalsIgnoreCase(type)) {
                    filteredList.add(vendor);
                }
            }
        }

        if (!filteredList.isEmpty()) {
            Object firstItem = filteredList.get(0);
            if (firstItem instanceof Vendor) {
                recyclerView.setAdapter(new VendorAdapter((List<Vendor>) (List<?>) filteredList, getContext()));
            } else if (firstItem instanceof Venue) {
                recyclerView.setAdapter(new VenueAdapter((List<Venue>) (List<?>) filteredList, getContext()));
            }
        } else {
            recyclerView.setAdapter(null);
        }
    }
}