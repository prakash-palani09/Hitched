package com.example.get_hitched;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Booking> bookingList;
    private BookingAdapter bookingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        // Initialize RecyclerView and set LayoutManager
        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list of bookings and the adapter
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, getContext());
        recyclerView.setAdapter(bookingAdapter);

        // Load bookings from Firestore
        loadBookings();

        return view;
    }

    // Method to load bookings from Firestore
    private void loadBookings() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();

        firestore.collection("Bookings")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    // Clear the list before adding new data
                    bookingList.clear();
                    for (DocumentSnapshot doc : querySnapshots) {
                        // Convert Firestore document to Booking object
                        Booking booking = doc.toObject(Booking.class);
                        if (booking != null) {
                            // Add booking to the list
                            bookingList.add(booking);
                        }
                    }
                    // Notify the adapter to update the view
                    bookingAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Show error message if data load fails
                    Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
                });
    }
}
