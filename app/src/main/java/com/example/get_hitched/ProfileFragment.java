package com.example.get_hitched;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private EditText etName, etPartnerName, etCity, etWeddingDate, etEmail;
    private Button buttonEdit, buttonSave, buttonLogout;
    private ImageView profileImage;
    private TextView tvName, tvEmail;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Firebase instances
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // UI elements
        etName = view.findViewById(R.id.et_name);
        etPartnerName = view.findViewById(R.id.et_partner_name);
        etCity = view.findViewById(R.id.et_city);
        etWeddingDate = view.findViewById(R.id.et_wedding_date);
        etEmail = view.findViewById(R.id.et_email);

        buttonEdit = view.findViewById(R.id.button_edit);
        buttonSave = view.findViewById(R.id.button_save);
        buttonLogout = view.findViewById(R.id.button_logout);

        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        profileImage = view.findViewById(R.id.image_profile);

        disableEditing();
        loadProfileData();

        buttonEdit.setOnClickListener(v -> enableEditing());
        buttonSave.setOnClickListener(v -> saveProfileData());
        buttonLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            // You can add Intent to redirect to login screen if needed
        });

        return view;
    }

    private void loadProfileData() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        String email = auth.getCurrentUser().getEmail();
        tvEmail.setText(email);
        etEmail.setText(email);

        firestore.collection("weddings").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String partner = documentSnapshot.getString("partner_name");
                        String city = documentSnapshot.getString("city");
                        String weddingDate = documentSnapshot.getString("wedding_date");

                        etName.setText(name);
                        etPartnerName.setText(partner);
                        etCity.setText(city);
                        etWeddingDate.setText(weddingDate);
                        tvName.setText(name);
                    } else {
                        Toast.makeText(getContext(), "No profile data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error loading profile", Toast.LENGTH_SHORT).show()
                );
    }

    private void saveProfileData() {
        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("name", etName.getText().toString().trim());
        data.put("partner_name", etPartnerName.getText().toString().trim());
        data.put("city", etCity.getText().toString().trim());
        data.put("wedding_date", etWeddingDate.getText().toString().trim());

        firestore.collection("weddings").document(userId)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                    disableEditing();
                    tvName.setText(etName.getText().toString().trim());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error saving profile", Toast.LENGTH_SHORT).show()
                );
    }

    private void enableEditing() {
        isEditing = true;
        etName.setEnabled(true);
        etPartnerName.setEnabled(true);
        etCity.setEnabled(true);
        etWeddingDate.setEnabled(true);
        buttonSave.setVisibility(View.VISIBLE);
    }

    private void disableEditing() {
        isEditing = false;
        etName.setEnabled(false);
        etPartnerName.setEnabled(false);
        etCity.setEnabled(false);
        etWeddingDate.setEnabled(false);
        buttonSave.setVisibility(View.GONE);
    }
}
