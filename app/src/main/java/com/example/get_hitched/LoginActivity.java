package com.example.get_hitched;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText inputMobile;
    Button getOTP;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        countryCodePicker = findViewById(R.id.login_countrycode);
        inputMobile = findViewById(R.id.login_mobile_number);
        getOTP = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(inputMobile);
        getOTP.setOnClickListener((v)->{
            if(!countryCodePicker.isValidFullNumber()){
                inputMobile.setError("Phone number not valid");
                return;
            }
            Intent intent=new Intent(LoginActivity.this,login_otp.class);
            intent.putExtra("mobile",countryCodePicker.getFullNumberWithPlus().replace(" ",""));
            startActivity(intent);
        });
    }

}