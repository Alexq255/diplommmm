package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);
        onBackPressed();
    }
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Запрещено!",Toast.LENGTH_SHORT).show();
    }

    public void buy(View view) {
        Intent intent = new Intent(BanActivity.this, PayActivity.class);
        startActivity(intent);
    }
}