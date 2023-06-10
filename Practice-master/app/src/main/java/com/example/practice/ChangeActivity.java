package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.security.identity.IdentityCredentialStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;

public class ChangeActivity extends AppCompatActivity {
    private EditText twnazvanie2,twdescription2,twfullprice2,ShopCounter2,twCategory2,twWarranty2,counter2;
    private ImageView imgTovar2;
    private String SHOPCART = "Products",ids;
    private DatabaseReference mBase;
    private DatabaseReference Cbbase;
    private FirebaseAuth mAuth;
    private Button button18,SaveAll;
    private Uri uploadUri,uploadUri2,uploadUri3;
    private StorageReference mStorageRef;
    private ListView CountView;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<Korzina> listTemp;
    private TextView idZap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        init();
        getIntentMain();
        getDataFromDB();
        button18.setEnabled(false);





    }


    private void init(){

        twnazvanie2 = findViewById(R.id.twNazavanie2);
        twdescription2 = findViewById(R.id.twdescription2);
        button18 = findViewById(R.id.button18);
        twfullprice2 = findViewById(R.id.twfullprice2);
        twCategory2 = findViewById(R.id.twCategory2);
        twWarranty2 = findViewById(R.id.twWarranty2);
        SaveAll =findViewById(R.id.SaveAll);
        idZap = findViewById(R.id.idZap);

        imgTovar2 = findViewById(R.id.imgTovar2);
        mBase = FirebaseDatabase.getInstance().getReference(SHOPCART);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        Cbbase = FirebaseDatabase.getInstance().getReference();
        SaveAll.setEnabled(false);




    }
    private void getIntentMain(){
        Intent is = getIntent();
        if (is!=null){
            Picasso.get().load(is.getStringExtra("tovar_imgTovar")).into(imgTovar2);
            twnazvanie2.setText(is.getStringExtra("tovar_nazvanie"));
            twdescription2.setText(is.getStringExtra("tovar_description"));
            twfullprice2.setText(is.getStringExtra("tovar_fullprice"));
            twWarranty2.setText(is.getStringExtra("tovar_warranty"));
            twCategory2.setText(is.getStringExtra("tovar_Category"));
            idZap.setText(is.getStringExtra("tovar_id"));


        }

    }

    public void toList(View view) {
        Intent intent = new Intent(ChangeActivity.this, TovarkaActivity.class);
        startActivity(intent);
    }

    public void OrderTo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeActivity.this);
        builder.setTitle("Добавление в корзину")
                .setCancelable(false)
                .setIcon(R.drawable.cart)
                .setMessage("Добавить товар в корзину?")
                .setPositiveButton("OK",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                uploadImage();
                                Toast.makeText(ChangeActivity.this,"Выбранный товар успешно добавлен",Toast.LENGTH_SHORT).show();

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



            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mBase.addValueEventListener(vListener);
    }





    private void saveData()
    {
        String id = idZap.getText().toString();
        String nazvanie = twnazvanie2.getText().toString();
        String description = twdescription2.getText().toString();
        String fullPrice = twfullprice2.getText().toString();
        String warranty = twWarranty2.getText().toString();
        String category = twCategory2.getText().toString();
        Toast.makeText(ChangeActivity.this,"Данные успешно загружены!",Toast.LENGTH_SHORT).show();


        TovarAddClass Cart = new TovarAddClass(id,nazvanie,description,fullPrice,uploadUri.toString(),warranty,category,uploadUri2.toString(),uploadUri3.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(description)&&!TextUtils.isEmpty(fullPrice)){
            if (id != null)mBase.child(id).setValue(Cart);



        }else{
            Toast.makeText(ChangeActivity.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveDate(View view) {

        saveData();
    }


    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imgTovar2.getDrawable()).getBitmap();
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
                if(uploadUri == null){
                    uploadUri = task.getResult();
                    button18.setText("Сохранить картинку 2");
                    Toast.makeText(ChangeActivity.this,"Добавлена первая картинка!!",Toast.LENGTH_SHORT).show();
                }
                else if(uploadUri2 == null) {
                    uploadUri2 = task.getResult();
                    button18.setText("Cохранить картинку 3");
                    Toast.makeText(ChangeActivity.this,"Добавлена вторая картинка!!",Toast.LENGTH_SHORT).show();
                }
                else if(uploadUri3 == null){
                    uploadUri3 = task.getResult();
                    button18.setEnabled(false);
                    SaveAll.setEnabled(true);

                    Toast.makeText(ChangeActivity.this,"Добавлена третья картинка!!",Toast.LENGTH_SHORT).show();
                }

            }


        });

    }



    public void ShopCart(View view) {
        Intent intent = new Intent(getApplicationContext(),ShopCartAct.class);
        startActivity(intent);
    }

    public void SaveAll(View view) {
        if (twnazvanie2.getText().toString().isEmpty()){
            Toast.makeText(ChangeActivity.this,"Поле название пустое",Toast.LENGTH_SHORT).show();
        }else{
            if (twdescription2.getText().toString().isEmpty()){
                Toast.makeText(ChangeActivity.this,"Поле описание пустое!",Toast.LENGTH_SHORT).show();
            }else
                if (twCategory2.getText().toString().isEmpty()){
                    Toast.makeText(ChangeActivity.this,"Поле Категория пустое!",Toast.LENGTH_SHORT).show();
                }else{
                    if (twWarranty2.getText().toString().isEmpty()){

                    }else{
                        if(twfullprice2.getText().toString().isEmpty()){
                            Toast.makeText(ChangeActivity.this,"Поле цена пустое",Toast.LENGTH_SHORT).show();
                        }else{
                            saveData();
                            Toast.makeText(ChangeActivity.this,"Данные загружаются",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        }



    }

    public void ImgChoose(View view) {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1&&data !=null && data.getData () != null);
        {
            if (resultCode == RESULT_OK)
            {
                button18.setEnabled(true);
                Log.d("Mylog","Image URI : "+ data.getData());
                imgTovar2.setImageURI(data.getData());

            }
        }
    }

    public void ImgAdd1(View view) {
        uploadImage();
    }
}