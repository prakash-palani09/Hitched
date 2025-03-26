package com.example.get_hitched;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CateringActivity extends AppCompatActivity {

    private EditText guestCountEditText, foodCostEditText;
    private TextView totalCostTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering);

        guestCountEditText = findViewById(R.id.editTextGuestCount);
        foodCostEditText = findViewById(R.id.editTextFoodCost);
        Button calculateCateringButton = findViewById(R.id.buttonCalculateCatering);
        totalCostTextView = findViewById(R.id.textViewTotalCost); // Initialize the TextView

        calculateCateringButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String guestCountString = guestCountEditText.getText().toString();
                String foodCostString = foodCostEditText.getText().toString();

                if (guestCountString.isEmpty() || foodCostString.isEmpty()) {
                    Toast.makeText(CateringActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int guestCount = Integer.parseInt(guestCountString);
                double foodCost = Double.parseDouble(foodCostString);

                double totalCateringCost = guestCount * foodCost;

                // Set the calculated cost to the TextView
                totalCostTextView.setText("Total Catering Cost: $" + totalCateringCost);
            }
        });
    }
}
