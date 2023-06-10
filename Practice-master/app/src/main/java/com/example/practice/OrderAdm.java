package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderAdm extends AppCompatActivity {

    private TextView twnazvanie1, twdescription1, twfullprice1, ShopCounter1, twCategory1, twWarranty1, twCountln, twAdress, datas, userid, OrderPrice;
    private ImageView imgTovar1;
    private CheckBox CheckBoxOrd;
    private String ORDERTOVAR = "Orders", dateText, timeText;
    private DatabaseReference mBase;
    private DatabaseReference Cbbase;
    private FirebaseAuth mAuth;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    private Button OrderZakaz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_adm);
        init();
        getIntentMain();
        userid.setVisibility(View.GONE);
        Button bg = (Button) OrderZakaz;
        bg.setEnabled(false);
        userid.setText("-NJ-fi--PU_evGT3OSuJ");
        twCountln.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (OrderPrice.equals("")) {

                } else {
                    Computations();
                }

            }


            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

    private void init() {
        CheckBoxOrd = findViewById(R.id.checkBoxOrd);
        twnazvanie1 = findViewById(R.id.twNazavanie1);
        twdescription1 = findViewById(R.id.twdescription1);
        twfullprice1 = findViewById(R.id.twfullprice1);
        twCategory1 = findViewById(R.id.twCategory1);
        twWarranty1 = findViewById(R.id.twWarranty1);
        twCountln = findViewById(R.id.twCountLn);
        twAdress = findViewById(R.id.twAdress);
        OrderPrice = findViewById(R.id.OrderPrice);
        datas = findViewById(R.id.datas);
        userid = findViewById(R.id.userid);
        OrderZakaz = findViewById(R.id.OrderZakaz);
        imgTovar1 = findViewById(R.id.imgTovar1);
        mBase = FirebaseDatabase.getInstance().getReference(ORDERTOVAR);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        Cbbase = FirebaseDatabase.getInstance().getReference();
        Date currentDate = new Date();
// Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
// Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        datas.setText(dateText + timeText);


        CheckBoxOrd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (CheckBoxOrd.isChecked()) {
                    Button bg = (Button) OrderZakaz;
                    bg.setEnabled(true);

                } else {
                    Button bg = (Button) OrderZakaz;
                    bg.setEnabled(false);

                }
            }
        });


    }

    private void Computations() {

        int a = Integer.parseInt(twfullprice1.getText().toString());

        int b = Integer.parseInt(twCountln.getText().toString());
        int c = a * b;
        OrderPrice.setText(c + "$");

    }

    private void getIntentMain() {
        Intent intent = getIntent();
        if (intent != null) {
            Picasso.get().load(intent.getStringExtra("Cart_imgTovar")).into(imgTovar1);
            twdescription1.setText(intent.getStringExtra("Cart_description"));
            twnazvanie1.setText(intent.getStringExtra("Cart_nazvanie"));
            twfullprice1.setText(intent.getStringExtra("Cart_fullprice"));
            twWarranty1.setText(intent.getStringExtra("Cart_warranty"));
            twCategory1.setText(intent.getStringExtra("Cart_Category"));


        }
    }


    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imgTovar1.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "MyImg");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                saveData();

            }


        });

    }

    private void saveData() {
        String id = mBase.push().getKey();
        String nazvanie = twnazvanie1.getText().toString();
        String description = twdescription1.getText().toString();
        String fullPrice = twfullprice1.getText().toString();
        String warranty = twWarranty1.getText().toString();
        String category = twCategory1.getText().toString();
        String adress = twAdress.getText().toString();
        String countLn = twCountln.getText().toString();
        String idUser = userid.getText().toString();
        String dateTime = datas.getText().toString();
        String ItogPrice = OrderPrice.getText().toString();
        String Status = "Новый заказ";


        OrderClass Cart = new OrderClass(id, nazvanie, description, fullPrice, warranty, category, adress, countLn, dateTime, idUser, ItogPrice, Status);
        if (!TextUtils.isEmpty(nazvanie) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(fullPrice)) {
            if (id != null) mBase.child(id).setValue(Cart);


        } else {
            Toast.makeText(OrderAdm.this, "Возможно некоторые поля пустые!", Toast.LENGTH_SHORT).show();
        }
    }

    public void OrderZakaz(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderAdm.this);
        builder.setTitle("Оформить заказ?")
                .setCancelable(false)
                .setIcon(R.drawable.shopicon)
                .setMessage("После нажатия кнопки OK, вы не сможете изменить свой заказ, если у вас есть вопросы вы можете нам позвонить нажав Позвонить ")
                .setPositiveButton("Оформить заказ",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                uploadImage();
                                Toast.makeText(OrderAdm.this, "Заказ на выбранные вами позиции успешно оформлен", Toast.LENGTH_SHORT).show();

                            }

                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.cancel();
                            }
                        }).setNeutralButton("Позвонить",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri number = Uri.parse("tel:88005553535");
                                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                startActivity(callIntent);
                            }
                        });


        AlertDialog alert = builder.create();
        alert.show();
    }
}