package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class netCheckerAact extends AppCompatActivity {
    private ImageView wlanimg,wlanimg2;
    private TextView nettest;
    private ProgressBar netconnetcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_checker_aact);
        wlanimg = findViewById(R.id.wlaninmg);
        wlanimg2 = findViewById(R.id.wlaninmg2);
        netconnetcion = findViewById(R.id.netconnetcion);
        nettest = findViewById(R.id.nettest);
        netconnetcion.setVisibility(View.GONE);
        nettest.setVisibility(View.GONE);
    }
    public void lefts(View view) {
        finish();
    }

    public void attempt2(View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wlanimg.setVisibility(View.GONE);
                wlanimg2.setVisibility(View.GONE);
                netconnetcion.setVisibility(View.VISIBLE);
                nettest.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wlanimg.setVisibility(View.GONE);
                        wlanimg2.setVisibility(View.GONE);

                        ConnectivityManager connectivityManager = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            Toast toast1 = Toast.makeText(getApplicationContext(),
                                    "Успешное подключение",Toast.LENGTH_LONG);
                            netconnetcion.setVisibility(View.GONE);
                            wlanimg.setVisibility(View.VISIBLE);
                            wlanimg2.setVisibility(View.VISIBLE);
                            nettest.setVisibility(View.GONE);
                            toast1.show();
                            Intent intent = new Intent(netCheckerAact.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast toast2 = Toast.makeText(getApplicationContext(),
                                    "Нет подключения\n Повт попытку",Toast.LENGTH_LONG);
                            netconnetcion.setVisibility(View.GONE);
                            wlanimg.setVisibility(View.VISIBLE);
                            wlanimg2.setVisibility(View.VISIBLE);
                            nettest.setVisibility(View.GONE);
                            toast2.show();
                        }
                    }
                }, 2000);

            }
        }, 400);


    }
}