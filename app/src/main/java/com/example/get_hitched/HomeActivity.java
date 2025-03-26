package com.example.get_hitched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView weddingDetailsText, countdownText;
    private Button buttonAddWeddingDetails, buttonCatering, buttonWeddingTheme, buttonInvitations, buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        weddingDetailsText = findViewById(R.id.weddingDetailsText);
        countdownText = findViewById(R.id.weddingCountdownText);
        buttonAddWeddingDetails = findViewById(R.id.buttonPlanWedding);
        buttonCatering = findViewById(R.id.buttonCatering);
        buttonWeddingTheme = findViewById(R.id.buttonWeddingTheme);
        buttonInvitations = findViewById(R.id.buttonInvitations);
        buttonExit = findViewById(R.id.buttonExit);

        loadWeddingDetails();

        buttonAddWeddingDetails.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this,PlannerActivity.class)));
        buttonCatering.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CateringActivity.class)));
        buttonWeddingTheme.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, WeddingThemeActivity.class)));
        buttonInvitations.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, InvitationsActivity.class)));
        buttonExit.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
            auth.signOut();
            finish();
        });
    }

    private void loadWeddingDetails() {
        String userId = auth.getCurrentUser().getUid(); // Get logged-in user's UID
        DocumentReference docRef = db.collection("weddings").document(userId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("bride_name")) {
                String brideName = documentSnapshot.getString("bride_name");
                String groomName = documentSnapshot.getString("groom_name");
                String weddingDate = documentSnapshot.getString("wedding_date");
                String city = documentSnapshot.getString("city");

                String details = "Bride: " + brideName + "\nGroom: " + groomName +
                        "\nDate: " + weddingDate + "\nCity: " + city;
                weddingDetailsText.setText(details);

                calculateCountdown(weddingDate);
            } else {
                weddingDetailsText.setText("No wedding details found. Please add.");
                showWeddingDetailsPopup();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(HomeActivity.this, "Error loading data", Toast.LENGTH_SHORT).show()
        );
    }

    private void showWeddingDetailsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_wedding_details_dialog, null);
        builder.setView(dialogView);

        EditText editTextBride = dialogView.findViewById(R.id.editTextBrideName);
        EditText editTextGroom = dialogView.findViewById(R.id.editTextGroomName);
        EditText editTextDate = dialogView.findViewById(R.id.editTextWeddingDate);
        EditText editTextCity = dialogView.findViewById(R.id.editTextCity);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonSave.setOnClickListener(v -> {
            String brideName = editTextBride.getText().toString().trim();
            String groomName = editTextGroom.getText().toString().trim();
            String weddingDate = editTextDate.getText().toString().trim();
            String city = editTextCity.getText().toString().trim();

            if (brideName.isEmpty() || groomName.isEmpty() || weddingDate.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            saveWeddingDetails(brideName, groomName, weddingDate, city);
            dialog.dismiss();
        });
    }

    private void saveWeddingDetails(String bride, String groom, String date, String city) {
        String userId = auth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("weddings").document(userId);

        docRef.set(new WeddingDetails(bride, groom, date, city))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(HomeActivity.this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
                    loadWeddingDetails();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(HomeActivity.this, "Failed to save details", Toast.LENGTH_SHORT).show());
    }

    private void calculateCountdown(String weddingDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(weddingDate);
            Date today = new Date();
            long diff = date.getTime() - today.getTime();
            long daysRemaining = diff / (1000 * 60 * 60 * 24);

            if (daysRemaining >= 0) {
                countdownText.setText("Days until wedding: " + daysRemaining);
            } else {
                countdownText.setText("Wedding already happened!");
            }
        } catch (ParseException e) {
            countdownText.setText("Invalid date format!");
        }
    }

    public static class WeddingDetails {
        public String bride_name, groom_name, wedding_date, city;

        public WeddingDetails() {}

        public WeddingDetails(String bride_name, String groom_name, String wedding_date, String city) {
            this.bride_name = bride_name;
            this.groom_name = groom_name;
            this.wedding_date = wedding_date;
            this.city = city;
        }
    }
}
