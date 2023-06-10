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
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FullTovarAct extends AppCompatActivity {
    private TextView twnazvanie,twdescription,twfullprice,ShopCounter,twCategory,twWarranty,counter,idZapiski;
    private ImageView imgTovar,imgTovar4,imgTovar5;
    private String SHOPCART = "Cart";
    private DatabaseReference mBase;
    private DatabaseReference Cbbase;
    private FirebaseAuth mAuth;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    private ListView CountView;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<Korzina> listTemp;
    private String uuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_tovar);
        init();
        getIntentMain();
        getDataFromDB();





    }


    private void init(){
        ShopCounter = findViewById(R.id.ShopCounter);
        twnazvanie = findViewById(R.id.twStartDate);
        twdescription = findViewById(R.id.shortop);
        idZapiski = findViewById(R.id.idZapiski);
        idZapiski.setVisibility(View.GONE);

        twfullprice = findViewById(R.id.twfullprice);
        twCategory = findViewById(R.id.twCategory);
        twWarranty = findViewById(R.id.twWarranty);
        counter = findViewById(R.id.Counter);
        imgTovar = findViewById(R.id.imgTovar);
        imgTovar4 = findViewById(R.id.imgTovar4);
        imgTovar5 = findViewById(R.id.imgTovar5);
        mBase = FirebaseDatabase.getInstance().getReference(SHOPCART);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        Cbbase = FirebaseDatabase.getInstance().getReference();



    }
    private void getIntentMain(){
        Intent is = getIntent();
        if (is!=null){
            Picasso.get().load(is.getStringExtra("tovar_imgTovar")).into(imgTovar);
            Picasso.get().load(is.getStringExtra("tovar_imgTovar2")).into(imgTovar4);
            Picasso.get().load(is.getStringExtra("tovar_imgTovar3")).into(imgTovar5);
            twnazvanie.setText(is.getStringExtra("tovar_nazvanie"));
            twdescription.setText(is.getStringExtra("tovar_description"));
            twfullprice.setText(is.getStringExtra("tovar_fullprice"));
            twWarranty.setText(is.getStringExtra("tovar_warranty"));
            twCategory.setText(is.getStringExtra("tovar_Category"));
            idZapiski.setText(is.getStringExtra("iD"));


        }
    }

    public void toList(View view) {
        Intent intent = new Intent(FullTovarAct.this, TovarkaActivity.class);
        startActivity(intent);
    }

    public void OrderTo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FullTovarAct.this);
        builder.setTitle("Добавление в корзину")
                .setCancelable(false)
                .setIcon(R.drawable.cart)
                .setMessage("Добавить товар в корзину?")
                .setPositiveButton("OK",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                uploadImage();
                                Toast.makeText(FullTovarAct.this,"Выбранный товар успешно добавлен",Toast.LENGTH_SHORT).show();
                                ShopCounter.setText("Перейти к корзине");
                            }

                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {

                            }
                        });

        AlertDialog alert =builder.create();
        alert.show();
    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                counter.setText(""+snapshot.getChildrenCount());


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mBase.addValueEventListener(vListener);
    }



    private void saveData()
    {
        String id = mBase.push().getKey();
        String nazvanie = twnazvanie.getText().toString();
        String description = twdescription.getText().toString();
        String fullPrice = twfullprice.getText().toString();
        String warranty = twWarranty.getText().toString();
        String category = twCategory.getText().toString();
        Korzina Cart = new Korzina(id,nazvanie,description,fullPrice,uploadUri.toString(),warranty,category);
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(description)&&!TextUtils.isEmpty(fullPrice)){
            if (id != null)mBase.child(id).setValue(Cart);


        }else{
            Toast.makeText(FullTovarAct.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveDate(View view) {

        uploadImage();
    }


    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imgTovar.getDrawable()).getBitmap();
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

    public void ShopCart(View view) {
        Intent intent = new Intent(getApplicationContext(),ShopCartAct.class);
        startActivity(intent);

    }
}