package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Licenge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenge);
    }

    public void TpReg(View view) {
        Intent intent = new Intent(Licenge.this, RegisterActivity.class);
        startActivity(intent);
    }
}