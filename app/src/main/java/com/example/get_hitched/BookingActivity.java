package com.example.get_hitched;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    private TextView textName, textLocation, textPrice, textSelectedDate;
    private ImageView imageVenue;
    private RatingBar ratingBar;
    private Button buttonConfirmBooking, buttonSelectDate;

    private Venue venue;
    private Vendor vendor;

    private int year, month, dayOfMonth;

    // Firestore instance
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        textName = findViewById(R.id.textName);
        textLocation = findViewById(R.id.textLocation);
        textPrice = findViewById(R.id.textPrice);
        textSelectedDate = findViewById(R.id.textViewSelectedDate);  // TextView to display selected date
        imageVenue = findViewById(R.id.imageVenue);
        ratingBar = findViewById(R.id.ratingBar);
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);  // Button to trigger DatePicker

        // Get current date
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Check if the Intent contains data for Venue or Vendor
        if (getIntent().hasExtra("venue_data")) {
            venue = (Venue) getIntent().getSerializableExtra("venue_data");
            if (venue != null) {
                textName.setText(venue.getName());
                textLocation.setText("Location: " + venue.getLocation());
                textPrice.setText("₹" + venue.getPrice_per_plate() + "/plate");
                ratingBar.setRating((float) venue.getRating());
                Glide.with(this).load(venue.getImage_url()).into(imageVenue);
            }
        } else if (getIntent().hasExtra("vendor_data")) {
            vendor = (Vendor) getIntent().getSerializableExtra("vendor_data");
            if (vendor != null) {
                textName.setText(vendor.getName());
                textLocation.setText("Location: " + vendor.getType());
                textPrice.setText("Contact: " + vendor.getContact());
                ratingBar.setRating((float) vendor.getRating());
                Glide.with(this).load(vendor.getImage_url()).into(imageVenue);
            }
        }

        // Set the click listener for the "Pick Date" button
        buttonSelectDate.setOnClickListener(v -> {
            // Open DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (view, year, month, dayOfMonth) -> {
                        // Update the selected date in textView
                        textSelectedDate.setText("Selected Date: " + dayOfMonth + "/" + (month + 1) + "/" + year);

                        // Enable the Confirm button
                        buttonConfirmBooking.setEnabled(true);
                    },
                    year, month, dayOfMonth);

            // Show the date picker dialog
            datePickerDialog.show();
        });

        // Handle booking confirmation
        buttonConfirmBooking.setOnClickListener(v -> {
            // Get the selected date
            String selectedDate = textSelectedDate.getText().toString();

            // If no date was selected, show a message or handle the error
            if (selectedDate.equals("No date selected")) {
                Toast.makeText(BookingActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
            } else {
                // Get the current user ID from FirebaseAuth
                String userId = mAuth.getCurrentUser().getUid();  // Get the logged-in user's UID

                // Save the booking information to Firestore
                Booking booking = new Booking(
                        venue != null ? venue.getName() : vendor.getName(),
                        venue != null ? venue.getLocation() : vendor.getType(),
                        venue != null ? "₹" + venue.getPrice_per_plate() + "/plate" : "Contact: " + vendor.getContact(),
                        selectedDate,
                        userId
                );

                // Save to Firestore
                mFirestore.collection("Bookings")
                        .add(booking)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(BookingActivity.this, "Booking Confirmed", Toast.LENGTH_SHORT).show();
                            finish();  // Close the activity after booking
                        })
                        .addOnFailureListener(e -> Toast.makeText(BookingActivity.this, "Failed to confirm booking", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
