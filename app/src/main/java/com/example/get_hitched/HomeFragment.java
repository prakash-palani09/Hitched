package com.example.get_hitched;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HomeFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DatabaseReference dbRef;
    private TextView weddingDetailsText, countdownText;
    private VendorAdapter vendorAdapter;
    private List<Vendor> vendorList;
    private RecyclerView recyclerViewBookings;
    private List<Booking> bookingList;
    private BookingAdapter bookingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("vendors");

        weddingDetailsText = view.findViewById(R.id.weddingDetailsText);
        countdownText = view.findViewById(R.id.weddingCountdownText);
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

        buttonPlanWedding.setOnClickListener(v -> startActivity(new Intent(getActivity(), PlannerActivity.class)));
        buttonCatering.setOnClickListener(v -> startActivity(new Intent(getActivity(), CateringActivity.class)));
        buttonWeddingTheme.setOnClickListener(v -> startActivity(new Intent(getActivity(), WeddingThemeActivity.class)));
        buttonInvitations.setOnClickListener(v -> startActivity(new Intent(getActivity(), InvitationsActivity.class)));
        recyclerViewBookings = view.findViewById(R.id.rv_bookings);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, requireContext());
        recyclerViewBookings.setAdapter(bookingAdapter);
        loadBookings();

        return view;



    }

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
        dbRef.limitToFirst(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vendorList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Vendor vendor = snap.getValue(Vendor.class);
                    vendorList.add(vendor);
                }
                vendorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load vendors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBookings() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();

        firestore.collection("Bookings")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    bookingList.clear();
                    for (DocumentSnapshot doc : querySnapshots) {
                        Booking booking = doc.toObject(Booking.class);
                        bookingList.add(booking);
                    }
                    bookingAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
                });
    }



}
