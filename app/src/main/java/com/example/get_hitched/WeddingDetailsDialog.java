package com.example.get_hitched;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WeddingDetailsDialog extends DialogFragment {

    private EditText editTextName, editTextPartner, editTextDate, editTextCity;
    private Button buttonSave;
    private Runnable onSavedCallback;

    public WeddingDetailsDialog(Runnable onSavedCallback) {
        this.onSavedCallback = onSavedCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.activity_wedding_details_dialog);

        editTextName = dialog.findViewById(R.id.editTextName);
        editTextPartner = dialog.findViewById(R.id.editTextPartnerName);
        editTextDate = dialog.findViewById(R.id.editTextWeddingDate);
        editTextCity = dialog.findViewById(R.id.editTextCity);
        buttonSave = dialog.findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> saveWeddingDetails());

        return dialog;
    }

    private void saveWeddingDetails() {
        String name = editTextName.getText().toString().trim();
        String partnerName = editTextPartner.getText().toString().trim();
        String weddingDate = editTextDate.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();

        if (name.isEmpty() || partnerName.isEmpty() || weddingDate.isEmpty() || city.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, String> weddingData = new HashMap<>();
        weddingData.put("name", name);
        weddingData.put("partner_name", partnerName);
        weddingData.put("wedding_date", weddingDate);
        weddingData.put("city", city);

        db.collection("weddings").document(userId).set(weddingData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Wedding details saved!", Toast.LENGTH_SHORT).show();
                    if (onSavedCallback != null) onSavedCallback.run();
                    dismiss();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error saving data", Toast.LENGTH_SHORT).show()
                );
    }
}
