package com.example.practice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class AdmOrderChanger extends AppCompatActivity {
    private TextView status2,twcategory22,fullprice22,edStatus2,twnazvanie14,twItogPrice22,idZapis,twWarranty22,twdescription22,twadress2,twdatetime2,twiduser2,twCountl22;
    private ImageView imgTovar1;
    private CheckBox CheckBoxOrd;
    private String ORDERTOVAR = "Orders",dateText,timeText;
    private DatabaseReference mBase;
    private DatabaseReference Cbbase;
    private FirebaseAuth mAuth;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    private Button OrderZakaz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_order_changer);
        init();

    }
    private void init(){
        twadress2=findViewById(R.id.twadress2);
        twCountl22=findViewById(R.id.twCountl22);
        twdatetime2=findViewById(R.id.twdatetime2);
        twdescription22=findViewById(R.id.twdescription22);
        idZapis=findViewById(R.id.idZapis);
        twiduser2=findViewById(R.id.twiduser2);
        twItogPrice22=findViewById(R.id.twItogPrice22);
        twnazvanie14=findViewById(R.id.twnazvanie14);
        twWarranty22=findViewById(R.id.twWarranty22);
        edStatus2=findViewById(R.id.edStatus2);
        fullprice22=findViewById(R.id.fullprice22);
        twcategory22 = findViewById(R.id.twcategory22);

        status2=findViewById(R.id.status2);

        getIntentMain();

    }



    private void getIntentMain(){
        Intent isa = getIntent();
        if (isa!=null){
            twadress2.setText(isa.getStringExtra("Orders_adress"));
            twCountl22.setText(isa.getStringExtra("Orders_CountLn"));
            twdatetime2.setText(isa.getStringExtra("Orders_datetime"));
            twdescription22.setText(isa.getStringExtra("Orders_description"));
            idZapis.setText(isa.getStringExtra("Orders_id"));
            twiduser2.setText(isa.getStringExtra("Orders_idUser"));
            twItogPrice22.setText(isa.getStringExtra("Orders_itogPrice"));
            twnazvanie14.setText(isa.getStringExtra("Orders_nazvanie"));
            status2.setText(isa.getStringExtra("Orders_status"));
            twWarranty22.setText(isa.getStringExtra("Orders_warranty"));
            twcategory22.setText(isa.getStringExtra("Orders_Category"));




        }
    }

    public void ChangedStatus(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdmOrderChanger.this);
        builder.setTitle("Изменить статус")
                .setCancelable(false)
                .setIcon(R.drawable.cart)
                .setMessage("Выберите один из доступных статусов, он будет изменен в таблице БД, не забудьте нажать сохранить перед выходом!")
                .setPositiveButton("Отменен",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                edStatus2.setText("Отменен");
                                Toast.makeText(AdmOrderChanger.this,"Успех",Toast.LENGTH_SHORT).show();

                            }

                        })
                .setNegativeButton("Доставляется",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                edStatus2.setText("Доставляется");
                                Toast.makeText(AdmOrderChanger.this,"Успех",Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNeutralButton("Доставлен",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                edStatus2.setText("Доставлен");
                                Toast.makeText(AdmOrderChanger.this,"Успех",Toast.LENGTH_SHORT).show();
                            }
                        });

        AlertDialog alert =builder.create();
        alert.show();
    }

    public void SvAllData(View view) {

saveData();
    }
    private void saveData()
    {
        String id = mBase.push().getKey();
        String nazvanie = twnazvanie14.getText().toString();
        String description = twdescription22.getText().toString();
        String fullPrice = fullprice22.getText().toString();
        String warranty = twWarranty22.getText().toString();

        String adress = twadress2.getText().toString();
        String countLn = twCountl22.getText().toString();
        String idUser= twiduser2.getText().toString();
        String dateTime = twdatetime2.getText().toString();
        String ItogPrice = twItogPrice22.getText().toString();
        String category = twcategory22.getText().toString();
        String Status ="Новый заказ";


        OrderClass Cart = new OrderClass(id,nazvanie,description,fullPrice,warranty,category,adress,countLn,dateTime,idUser,ItogPrice,Status);
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(description)&&!TextUtils.isEmpty(fullPrice)){
            if (id != null)mBase.child(id).setValue(Cart);


        }else{
            Toast.makeText(AdmOrderChanger.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }
    public void BackToAdmin(View view) {
        Intent is = new Intent(AdmOrderChanger.this, Glavnaya.class);
        startActivity(is);
    }



}
