package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        final EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetOtp= findViewById(R.id.btnVerifyс);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        buttonGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputMobile.getText().toString().trim().isEmpty()){
                    Toast.makeText(SendOtpActivity.this,"Введите номер",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonGetOtp.setVisibility(View.VISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber("+7"+inputMobile.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        SendOtpActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(view.GONE);
                        buttonGetOtp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(view.GONE);
                        buttonGetOtp.setVisibility(View.VISIBLE);
                        Toast.makeText(SendOtpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       progressBar.setVisibility(View.GONE);
                       progressBar.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getApplicationContext(), VerifyActivity.class);
                        intent.putExtra("Номер", inputMobile.getText().toString());
                        intent.putExtra("verificationId",verificationId);
                        startActivity(intent);
                    }
                });


            }
        });
    }
}