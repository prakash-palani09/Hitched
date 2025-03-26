package com.example.get_hitched;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class WeddingDetailsDialog extends DialogFragment {

    private EditText editTextBride, editTextGroom, editTextDate, editTextCity;
    private Button buttonSave;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.activity_wedding_details_dialog);

        editTextBride = dialog.findViewById(R.id.editTextBrideName);
        editTextGroom = dialog.findViewById(R.id.editTextGroomName);
        editTextDate = dialog.findViewById(R.id.editTextWeddingDate);
        editTextCity = dialog.findViewById(R.id.editTextCity);
        buttonSave = dialog.findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> saveWeddingDetails());

        return dialog;
    }

    private void saveWeddingDetails() {
        String brideName = editTextBride.getText().toString().trim();
        String groomName = editTextGroom.getText().toString().trim();
        String weddingDate = editTextDate.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();

        if (brideName.isEmpty() || groomName.isEmpty() || weddingDate.isEmpty() || city.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> weddingData = new HashMap<>();
        weddingData.put("bride_name", brideName);
        weddingData.put("groom_name", groomName);
        weddingData.put("wedding_date", weddingDate);
        weddingData.put("city", city);

        db.collection("weddings").document("user_wedding").set(weddingData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Wedding details saved!", Toast.LENGTH_SHORT).show();
                    dismiss(); // Close the dialog
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error saving data", Toast.LENGTH_SHORT).show()
                );
    }
}
