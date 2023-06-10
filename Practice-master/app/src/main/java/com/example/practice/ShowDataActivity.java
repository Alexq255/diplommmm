package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowDataActivity extends AppCompatActivity {
private TextView fullop,shortop,twStartDate,twEndDate,nazvanie;
private ImageView imgBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        init();
        getIntentMain();

    }
    private void init(){
        nazvanie = findViewById(R.id.Nazvanie);
        twStartDate = findViewById(R.id.twStartDate);
        twEndDate = findViewById(R.id.twEndDate);
        fullop= findViewById(R.id.fullop);
        shortop = findViewById(R.id.shortop);
        imgBD = findViewById(R.id.imgTovar);


    }
    private void getIntentMain(){
        Intent i = getIntent();
        if (i!=null){
            Picasso.get().load(i.getStringExtra("user_image_id")).into(imgBD);
            fullop.setText(i.getStringExtra("user_fullOp"));
            shortop.setText(i.getStringExtra("user_shortOp"));
            nazvanie.setText(i.getStringExtra("user_nazvanie"));
            twStartDate.setText(i.getStringExtra("user_dateStart"));
            twEndDate.setText(i.getStringExtra("user_dateEnd"));

        }
    }

    public void Send(View view) {
        Uri number = Uri.parse("tel:88005553535");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);



    }

    public void goBack(View view) {
        Intent intent = new Intent(ShowDataActivity.this, ReadActivity.class);
        startActivity(intent);
    }
}