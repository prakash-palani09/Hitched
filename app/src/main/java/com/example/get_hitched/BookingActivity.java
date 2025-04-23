package com.example.get_hitched;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textName = findViewById(R.id.textName);
        textLocation = findViewById(R.id.textLocation);
        textPrice = findViewById(R.id.textPrice);
        textSelectedDate = findViewById(R.id.textViewSelectedDate);
        imageVenue = findViewById(R.id.imageVenue);
        ratingBar = findViewById(R.id.ratingBar);
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

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

        buttonSelectDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format and set selected date
                        String formattedDate = "Selected Date: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        textSelectedDate.setText(formattedDate);

                        // Enable the confirm button
                        buttonConfirmBooking.setEnabled(true);
                    },
                    year, month, dayOfMonth
            );

            // 🔐 Prevent selection of past dates
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            datePickerDialog.show();
        });


        buttonConfirmBooking.setOnClickListener(v -> {
            String selectedDate = textSelectedDate.getText().toString();

            if (selectedDate.equals("No date selected")) {
                Toast.makeText(BookingActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
            } else {
                String userId = mAuth.getCurrentUser().getUid();

                Booking booking = new Booking(
                        venue != null ? venue.getName() : vendor.getName(),
                        venue != null ? venue.getLocation() : vendor.getType(),
                        venue != null ? "₹" + venue.getPrice_per_plate() + "/plate" : "Contact: " + vendor.getContact(),
                        selectedDate,
                        userId
                );

                mFirestore.collection("Bookings")
                        .add(booking)
                        .addOnSuccessListener(documentReference -> {
                            String title = venue != null ? venue.getName() : vendor.getName();
                            showBookingNotification(title);  // 🔔 Show local notification
                            Toast.makeText(BookingActivity.this, "Booking Confirmed", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(BookingActivity.this, "Failed to confirm booking", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showBookingNotification(String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "hitched_channel",
                    "Booking Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifies user when a booking is confirmed");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "hitched_channel")
                .setSmallIcon(R.mipmap.icon)  // Make sure you have this icon
                .setContentTitle("Booking Confirmed!")
                .setContentText("Your booking at " + title + " is confirmed.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(1001, builder.build());
    }
}
