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
import android.os.Handler;
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
import java.util.PrimitiveIterator;

public class OrderActivity extends AppCompatActivity {
    private TextView twnazvanie1,idUserApiInfo,twfullprice4,twfullPrice12,IdUserApi,StatusText,textView24,textView22,twdescription1,twfullprice1,ShopCounter1,twCategory1,twWarranty1,twCountln,twAdress,datas,userid,OrderPrice;
    private ImageView imgTovar1;
    private CheckBox CheckBoxOrd;
    private int a,b,c;
    private String ORDERTOVAR = "Orders",dateText,timeText;
    private DatabaseReference mBase;
    private DatabaseReference Cbbase;
    private FirebaseAuth mAuth;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    private Button OrderZakaz,button12,AdmSavaChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
        getIntentMain();
        userid.setVisibility(View.GONE);
        AdmSavaChanges = findViewById(R.id.AdmSavaChanges);
        Button bg = (Button)OrderZakaz;
        bg.setEnabled(false);
        userid.setText("-NJ-fi--PU_evGT3OSuJ");
        AdmSavaChanges.setEnabled(false);
        twCountln.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (twCountln.getText().toString().isEmpty()){
                    Toast.makeText(OrderActivity.this,"Введите кол-во(Макс 999)",Toast.LENGTH_SHORT).show();

                    twCountln.setText("1");

                }else{
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textcheckerandcounter();
                        }
                    }, 300);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

twAdress.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      if (twAdress.getText().toString().isEmpty()){

      }else{
          Toast.makeText(OrderActivity.this,"Введите адрес",Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
});
    }


    public void textcheckerandcounter(){
        twCountln.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (twfullprice4.equals("")){

                }else{
                    Computations();
                }

            }


            @Override
            public void afterTextChanged(Editable editable) {



            }
        });
    }
    private void init(){
        textView22 = findViewById(R.id.textView22);
        idUserApiInfo = findViewById(R.id.idUserApiInfo);
        textView24 = findViewById(R.id.textView24);
        IdUserApi = findViewById(R.id.IdUserApi);
        twfullPrice12 = findViewById(R.id.twfullPrice12);
        StatusText = findViewById(R.id.StatusText);
        CheckBoxOrd = findViewById(R.id.checkBoxOrd);
        twnazvanie1 = findViewById(R.id.twNazavanie1);
        twdescription1 = findViewById(R.id.twdescription1);
        twfullprice1 = findViewById(R.id.twfullprice1);
        twfullprice4 = findViewById(R.id.twfullprice4);
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

        datas.setText(dateText+timeText);
        View bs = findViewById(R.id.AdmSavaChanges);
        bs.setVisibility(View.GONE);
        StatusText.setVisibility(View.GONE);
        IdUserApi.setVisibility(View.GONE);
        idUserApiInfo.setVisibility(View.GONE);


        CheckBoxOrd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (CheckBoxOrd.isChecked()){
                    Button bg = (Button)OrderZakaz;
                    bg.setEnabled(true);

                }else{
                    Button bg = (Button)OrderZakaz;
                    bg.setEnabled(false);

                }
            }
        });


    }
private void Computations(){
    a = Integer.parseInt(twfullprice4.getText().toString());
    b = Integer.parseInt(twCountln.getText().toString());
        if (a!=0) {
            if (b != 0) {
                c = a * b;
                OrderPrice.setText(c + "$");

            }
            }
        }









    private void getIntentMain(){
        Intent intent = getIntent();
        if (intent!=null){
            Picasso.get().load(intent.getStringExtra("Cart_imgTovar")).into(imgTovar1);
            twdescription1.setText(intent.getStringExtra("Cart_description"));
            twnazvanie1.setText(intent.getStringExtra("Cart_nazvanie"));
            twfullprice4.setText(intent.getStringExtra("Cart_fullprice"));
            twWarranty1.setText(intent.getStringExtra("Cart_warranty"));
            twfullPrice12.setText(twfullprice1.getText().toString());
            twWarranty1.setText(intent.getStringExtra("Cart_status"));
            twCategory1.setText(intent.getStringExtra("Cart_Category"));
            twAdress.setText(intent.getStringExtra("Cart_adress"));
            twfullprice1.setText(intent.getStringExtra("Cart_itogPrice"));
            OrderPrice.setText(intent.getStringExtra("Cart_itogPrice"));
            twCountln.setText(intent.getStringExtra("Cart_countLn"));
            IdUserApi.setText(intent.getStringExtra("Cart_id"));
            String Privelegy =(intent.getStringExtra("Admire"));
            if (Privelegy!=null){
                textView22.setText("Редактирование(Админ)");
                View b = findViewById(R.id.button12);
                b.setVisibility(View.GONE);
                twCountln.setVisibility(View.GONE);
                twAdress.setVisibility(View.GONE);
                CheckBoxOrd.setVisibility(View.GONE);
                idUserApiInfo.setVisibility(View.VISIBLE);
                textView24.setVisibility(View.GONE);
                OrderPrice.setVisibility(View.GONE);
                OrderZakaz.setVisibility(View.GONE);
                IdUserApi.setVisibility(View.VISIBLE);
                View bs = findViewById(R.id.AdmSavaChanges);
                bs.setVisibility(View.VISIBLE);
                StatusText.setVisibility(View.VISIBLE);
            }
            }else{

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
    private void saveData()
    {
        String id = mBase.push().getKey();
        String nazvanie = twnazvanie1.getText().toString();
        String description = twdescription1.getText().toString();
        String fullPrice = twfullprice4.getText().toString();
        String warranty = twWarranty1.getText().toString();
        String category = twCategory1.getText().toString();
        String adress = twAdress.getText().toString();
        String countLn = twCountln.getText().toString();
        String idUser= userid.getText().toString();
        String dateTime = datas.getText().toString();
        String ItogPrice = OrderPrice.getText().toString();

        String Status ="Новый заказ";



        OrderClass Cart = new OrderClass(id,nazvanie,description,fullPrice,warranty,category,adress,countLn,dateTime,idUser,ItogPrice,Status);
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(description)&&!TextUtils.isEmpty(fullPrice)){
            if (id != null)mBase.child(id).setValue(Cart);


        }else{
            Toast.makeText(OrderActivity.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }

    public void OrderZakaz(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setTitle("Оформить заказ?")
                .setCancelable(false)
                .setIcon(R.drawable.shopicon)
                .setMessage("После нажатия кнопки OK, вы не сможете изменить свой заказ, если у вас есть вопросы вы можете нам позвонить нажав Позвонить ")
                .setPositiveButton("Оформить заказ",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                if (twCountln.getText().toString().isEmpty()){
                                    Toast.makeText(OrderActivity.this,"Поле кол-во не может быть пустым",Toast.LENGTH_SHORT).show();
                                }else{
                                    if (twAdress.getText().toString().isEmpty()){
                                        Toast.makeText(OrderActivity.this,"Поле адрес не может быть пустым",Toast.LENGTH_SHORT).show();
                                    }else{
                                        uploadImage();
                                        Toast.makeText(OrderActivity.this,"Заказ на выбранные вами позиции успешно оформлен",Toast.LENGTH_SHORT).show();
                                    }
                                }




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


    public void AdminChangeStatus(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setTitle("Изменить статус")
                .setCancelable(false)
                .setIcon(R.drawable.shopicon)
                .setMessage("Выберите один из доступных статусов, он будет изменен в таблице БД, не забудьте нажать сохранить перед выходом!")
                .setPositiveButton("Отменен",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                StatusText.setText("Отменен");
                                Toast.makeText(OrderActivity.this,"Успех",Toast.LENGTH_SHORT).show();
                                AdmSavaChanges.setEnabled(true);

                            }

                        })
                .setNegativeButton("Доставляется",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                StatusText.setText("Доставляется");
                                Toast.makeText(OrderActivity.this,"Успех",Toast.LENGTH_SHORT).show();
                                AdmSavaChanges.setEnabled(true);
                            }
                        })
                .setNeutralButton("Доставлен",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                StatusText.setText("Доставлен");
                                Toast.makeText(OrderActivity.this,"Успех",Toast.LENGTH_SHORT).show();
                                AdmSavaChanges.setEnabled(true);
                            }
                        });

        AlertDialog alert =builder.create();
        alert.show();
    }

    public void SaveAdminData(View view) {
        saveDataForAdmin();
        Toast.makeText(OrderActivity.this,"Успех",Toast.LENGTH_SHORT).show();
    }
    private void saveDataForAdmin()
    {

        String id = IdUserApi.getText().toString();
        String nazvanie = twnazvanie1.getText().toString();
        String description = twdescription1.getText().toString();
        String fullPrice = twfullprice4.getText().toString();
        String warranty = "Гарантия 3 мес";
        String category = twCategory1.getText().toString();
        String adress = twAdress.getText().toString();
        String countLn = twCountln.getText().toString();
        String idUser= userid.getText().toString();
        String dateTime = datas.getText().toString();
        String ItogPrice = OrderPrice.getText().toString();

        String Status ="Новый заказ";
        if (StatusText!=null){
            Status=StatusText.getText().toString();
        }


        OrderClass Cart = new OrderClass(id,nazvanie,description,fullPrice,warranty,category,adress,countLn,dateTime,idUser,ItogPrice,Status);
        mBase.child(id).setValue(Cart);



    }

    public void backcart(View view) {
        Intent intent = new Intent(OrderActivity.this, ShopCartAct.class);
        startActivity(intent);
    }
}