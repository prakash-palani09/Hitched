package com.example.get_hitched;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    private TextView textName, textLocation, textCity, textPrice, textDescription;
    private ImageView imageVenue;
    private RatingBar ratingBar;
    private Button buttonBookNow;

    private Venue venue;
    private Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize views
        textName = findViewById(R.id.textName);
        textLocation = findViewById(R.id.textLocation);
        textCity = findViewById(R.id.textCity);
        textPrice = findViewById(R.id.textPrice);
        textDescription = findViewById(R.id.textDescription);
        imageVenue = findViewById(R.id.imageVenue);
        ratingBar = findViewById(R.id.ratingBar);
        buttonBookNow = findViewById(R.id.buttonBookNow);

        // Check if the intent has data for Venue or Vendor
        if (getIntent().hasExtra("venue")) {
            venue = (Venue) getIntent().getSerializableExtra("venue");
            if (venue != null) {
                Log.d("DetailsActivity", "Venue: " + venue.getName());
                textName.setText(venue.getName());
                textLocation.setText("Location: " + venue.getLocation());
                textPrice.setText("₹" + venue.getPrice_per_plate() + "/plate");
                ratingBar.setRating((float) venue.getRating());
                Glide.with(this).load(venue.getImage_url()).into(imageVenue);

                // Set the click listener for the "Book Now" button
                buttonBookNow.setOnClickListener(v -> {
                    // Pass the venue data to BookingActivity
                    Intent intent = new Intent(DetailsActivity.this, BookingActivity.class);
                    intent.putExtra("venue_data", venue);  // Pass the venue object
                    startActivity(intent);
                });
            } else {
                Log.e("DetailsActivity", "Venue is null");
            }
        } else if (getIntent().hasExtra("vendor")) {
            vendor = (Vendor) getIntent().getSerializableExtra("vendor");
            if (vendor != null) {
                Log.d("DetailsActivity", "Vendor: " + vendor.getName());
                textName.setText(vendor.getName());
                textLocation.setText("Location: " + vendor.getType());
                textCity.setText("City: " + vendor.getCity());
                textPrice.setText("Contact: " + vendor.getContact());
                ratingBar.setRating((float) vendor.getRating());
                Glide.with(this).load(vendor.getImage_url()).into(imageVenue);

                buttonBookNow.setOnClickListener(v -> {
                    // Pass the vendor data to BookingActivity
                    Intent intent = new Intent(DetailsActivity.this, BookingActivity.class);
                    intent.putExtra("vendor_data", vendor);
                    startActivity(intent);
                });
            }
        }
    }
}
