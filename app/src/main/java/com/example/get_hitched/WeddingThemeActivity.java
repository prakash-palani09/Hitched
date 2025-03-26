package com.example.get_hitched;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class WeddingThemeActivity extends AppCompatActivity {

    private EditText inputColors;
    private Spinner inputVenue, inputSeason;
    private TextView themeSuggestionText;
    private Button btnGetTheme;
    private ProgressBar progressBar; // ProgressBar to show loading state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_theme);

        inputColors = findViewById(R.id.inputColors);
        inputVenue = findViewById(R.id.inputVenue);
        inputSeason = findViewById(R.id.inputSeason);
        themeSuggestionText = findViewById(R.id.themeSuggestionText);
        btnGetTheme = findViewById(R.id.btnGetTheme);
        progressBar = findViewById(R.id.progressBar); // Initialize your ProgressBar

        btnGetTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                String colors = inputColors.getText().toString();
                String venue = inputVenue.getSelectedItem().toString();
                String season = inputSeason.getSelectedItem().toString();

                // Validate inputs
                if (colors.isEmpty()) {
                    Toast.makeText(WeddingThemeActivity.this, "Please enter your color preferences", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });
    }
}

    // Method to get AI-based theme suggestions using Gemini API
