package com.example.get_hitched;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class PlannerActivity extends AppCompatActivity {

    private RecyclerView locationRecyclerView, dressRecyclerView, menDressRecyclerView;
    private EditText guestCountEditText;
    private Button calculateButton, cateringButton;
    private TextView welcomeText, estimatedCostTextView;
    private SpinnerItem selectedLocation, selectedDress, selectedMenDress;
    private static final String PREFS_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        initializeViews();
        setWelcomeMessage();
        setupRecyclerViews();
        setupListeners();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcomeText);
        locationRecyclerView = findViewById(R.id.locationRecyclerView);
        dressRecyclerView = findViewById(R.id.dressRecyclerView);
        menDressRecyclerView = findViewById(R.id.menDressRecyclerView);
        guestCountEditText = findViewById(R.id.editTextGuestCount);
        calculateButton = findViewById(R.id.buttonCalculate);
        cateringButton = findViewById(R.id.buttonCatering);
        estimatedCostTextView = findViewById(R.id.textViewEstimatedCost);
    }

    private void setWelcomeMessage() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = preferences.getString("username", "User");
        welcomeText.setText("Welcome, " + username + "!");
    }

    private void setupRecyclerViews() {
        List<SpinnerItem> locations = new ArrayList<>();
        locations.add(new SpinnerItem("Beach", 500));
        locations.add(new SpinnerItem("Garden", 300));
        locations.add(new SpinnerItem("Hall", 700));
        locations.add(new SpinnerItem("Home", 1000));

        List<SpinnerItem> brideDresses = new ArrayList<>();
        brideDresses.add(new SpinnerItem("Simple Dress", 200));
        brideDresses.add(new SpinnerItem("Elegant Dress", 500));
        brideDresses.add(new SpinnerItem("Designer Dress", 1000));
        brideDresses.add(new SpinnerItem("Custom Dress", 800));

        List<SpinnerItem> groomDresses = new ArrayList<>();
        groomDresses.add(new SpinnerItem("Classic Suit", 300));
        groomDresses.add(new SpinnerItem("Elegant Suit", 600));
        groomDresses.add(new SpinnerItem("Designer Wear", 400));
        groomDresses.add(new SpinnerItem("Custom Suit", 200));

        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menDressRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CustomAdapter locationAdapter = new CustomAdapter(this, locations, item -> {
            selectedLocation = item;
            Toast.makeText(this, "Selected Location: " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        CustomAdapter dressAdapter = new CustomAdapter(this, brideDresses, item -> {
            selectedDress = item;
            Toast.makeText(this, "Selected Bride Dress: " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        CustomAdapter menDressAdapter = new CustomAdapter(this, groomDresses, item -> {
            selectedMenDress = item;
            Toast.makeText(this, "Selected Groom Dress: " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        locationRecyclerView.setAdapter(locationAdapter);
        dressRecyclerView.setAdapter(dressAdapter);
        menDressRecyclerView.setAdapter(menDressAdapter);
    }

    private void setupListeners() {
        calculateButton.setOnClickListener(v -> calculateCost());
    }

    private void calculateCost() {
        String guestCountString = guestCountEditText.getText().toString();

        if (guestCountString.isEmpty() || selectedLocation == null || selectedDress == null || selectedMenDress == null) {
            Toast.makeText(this, "Please select all options and enter guest count", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int guestCount = Integer.parseInt(guestCountString);
            if (guestCount <= 0) {
                Toast.makeText(this, "Guest count must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }

            double estimatedCost = calculateWeddingCost(selectedLocation, selectedDress, selectedMenDress, guestCount);
            String costDetails = "Selected Location: " + selectedLocation.getName() + "\n" +
                    "Selected Bride Dress: " + selectedDress.getName() + "\n" +
                    "Selected Groom Dress: " + selectedMenDress.getName() + "\n" +
                    "Base Cost: $1000\n" +
                    "Location Cost: $" + selectedLocation.getCost() + "\n" +
                    "Bride Dress Cost: $" + selectedDress.getCost() + "\n" +
                    "Groom Dress Cost: $" + selectedMenDress.getCost() + "\n" +
                    "Accommodation Cost per person: $50\n" +
                    "Total Guest Count: " + guestCount + "\n" +
                    "Estimated Cost: $" + estimatedCost;

            estimatedCostTextView.setText(costDetails);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid guest count", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateWeddingCost(SpinnerItem location, SpinnerItem dress, SpinnerItem menDress, int guestCount) {
        double baseCost = 1000;
        double locationCost = location.getCost();
        double dressCost = dress.getCost();
        double menDressCost = menDress.getCost();
        return baseCost + locationCost + dressCost + menDressCost + (guestCount * 50);
    }
}
