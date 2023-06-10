package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Glavnaya extends AppCompatActivity {

    private EditText EditName,EditSurname,EditScam,EditStartDate,EditEndDate;
    private DatabaseReference mBase;
    private String USERKEY = "User";
    private ImageView loggout_btn;
    private FirebaseAuth mAuth;
    private TextView twAccount;
    private Button btnChoose, button;

    private ImageView imageView;
    private Uri uploadUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavnaya);
        EditName = findViewById(R.id.EditName);
        EditStartDate = findViewById(R.id.EditStartDate);
        EditEndDate = findViewById(R.id.EditEndDate);
        EditSurname = findViewById(R.id.EditSurname);
        EditScam = findViewById(R.id.EditScam);
        mBase = FirebaseDatabase.getInstance().getReference(USERKEY);
        loggout_btn = findViewById(R.id.loggout_btn);
        twAccount = findViewById(R.id.twAccount);
        btnChoose = findViewById(R.id.btnChoose);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imgView);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        Intent i = getIntent();
        twAccount.setText(i.getStringExtra("UserData"));

        button.setEnabled(false);


    }
    private void saveUser()
    {
        String id = mBase.push().getKey();
        String nazvanie = EditName.getText().toString();
        String shortOp = EditSurname.getText().toString();
        String fullOp = EditScam.getText().toString();
        String dateStart = EditStartDate.getText().toString();
        String  dateEnd= EditEndDate.getText().toString();
        UserAdd newUserAdd = new UserAdd(id,nazvanie,uploadUri.toString(),shortOp,fullOp,dateStart,dateEnd);
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(shortOp)&&!TextUtils.isEmpty(fullOp)){
            if (id != null)mBase.child(id).setValue(newUserAdd);


        }else{
            Toast.makeText(Glavnaya.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickSave(View view) {
        if (EditName.getText().toString().isEmpty()){
            Toast.makeText(Glavnaya.this,"Поле название акции пустое!",Toast.LENGTH_SHORT).show();
        }else{
            if (EditSurname.getText().toString().isEmpty()){
                Toast.makeText(Glavnaya.this,"Поле текст предложения пустое!",Toast.LENGTH_SHORT).show();
            }else{
                if (EditScam.getText().toString().isEmpty()){
                    Toast.makeText(Glavnaya.this,"Поле почта пустое!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if (EditStartDate.getText().toString().isEmpty()){
                        Toast.makeText(Glavnaya.this,"Поле дата  начала пустое",Toast.LENGTH_SHORT).show();
                    }else{
                       if (EditEndDate.getText().toString().isEmpty()){
                           Toast.makeText(Glavnaya.this,"Поле дата окончания пустое!",Toast.LENGTH_SHORT).show();
                       }else{
                          uploadImage();
                           Toast.makeText(Glavnaya.this,"Вы добавили новое предложение",Toast.LENGTH_SHORT).show();
                       }
                    }
                }
            }
        }


    }

    public void onClickRead(View view) {
        Intent Intent = new Intent(Glavnaya.this, ReadActivity.class);
        startActivity(Intent);


    }

    public void Logout(View view) {

        Intent intent = new Intent(Glavnaya.this, profileActivity.class);
        intent.putExtra("ifAdmin","Роль:Админ");
        intent.putExtra("Roleid","Роль:"+1);
        startActivity(intent);

    }

    public void btnChoose(View view) {
        chooseImage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1&&data !=null && data.getData () != null);
        {
            if (resultCode == RESULT_OK)
            {
                Log.d("Mylog","Image URI : "+ data.getData());
                imageView.setImageURI(data.getData());
                button.setEnabled(true);

            }
        }
    }

    private void chooseImage()
    {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }
    private void uploadImage()
    {
        Bitmap bitmap =((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] byteArray= baos.toByteArray();
        StorageReference mRef = mStorageRef.child(System.currentTimeMillis()+"MyImg");
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
                saveUser();


            }


        });










    }



    public void onClickTovarka(View view) {
        Intent Intent = new Intent(Glavnaya.this, TovarkaActivity.class);
        startActivity(Intent);
    }

    public void AddTovar(View view) {
        Intent Intent = new Intent(Glavnaya.this, TovarAddAct.class);
        startActivity(Intent);
    }


    public void Prechanges(View view) {
        Intent Intents = new Intent(Glavnaya.this, PrechangeActivity.class);
        startActivity(Intents);
    }

    public void ClientOrderChangeActivity(View view) {
        Intent Intentor = new Intent(Glavnaya.this, ShopCartAct.class);
        Intentor.putExtra("Adm","1");
        startActivity(Intentor);

    }
}