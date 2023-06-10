package com.example.practice;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class GlavnayaUser extends AppCompatActivity {
    private EditText EditName, EditSurname, EditScam,EditStartDate,EditEndDate;
    private DatabaseReference mBase;
    private String USERKEY = "User";
    private ImageView loggout_btn,loggout_btn2;
    private FirebaseAuth mAuth;
    private TextView twAccount;
    private Button btnChoose, button,button7;

    private String name;
    private ImageView imageView,ProfilePic;
    private Uri uploadUri;
    private FirebaseFirestore fstore;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    private GoogleSignInOptions gso;
    private ImageView google_img;
    private GoogleSignInClient gsc;
    ListView acList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavnaya_user);
        EditStartDate = findViewById(R.id.EditStartDate);
        EditEndDate = findViewById(R.id.EditEndDate);
        EditName = findViewById(R.id.EditName);
        EditSurname = findViewById(R.id.EditSurname);
        EditScam = findViewById(R.id.EditScam);
        mBase = FirebaseDatabase.getInstance().getReference(USERKEY);
        loggout_btn = findViewById(R.id.loggout_btn);
        loggout_btn2 = findViewById(R.id.loggout_btn2);
        twAccount = findViewById(R.id.twAccount);
        btnChoose = findViewById(R.id.btnChoose);
        button = findViewById(R.id.button);
        button7 = findViewById(R.id.button7);
        imageView = findViewById(R.id.imgView);
        ProfilePic = findViewById(R.id.ProfilePic);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        Intent i = getIntent();
        fstore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        twAccount.setText(i.getStringExtra("UserData"));


        UserRestricted();
        ProInit();
        getname();



        acList = findViewById(R.id.acList);
        ArrayList<Actions> arrayList = new ArrayList<>();

        arrayList.add(new Actions(R.drawable.tinkoff,"Тинкофф","Пожалуй один из лучших банков для молодых и не очень граждан, кешбек, акции и многое другое"));
        arrayList.add(new Actions(R.drawable.alfa,"Альфабанк","Хорошие условия по кредиту, бонусы, топливные карты и куча других продуктов, они не оставят вас равнодушными"));
        arrayList.add(new Actions(R.drawable.binance2,"Binance","Самая крупная в мире криптобиржа, по совместительству платежная система и рынок криптовалют и акций"));
        arrayList.add(new Actions(R.drawable.sber,"Сбербанк","Банк не сильно щедрый в плане наград и бонусов, однако проверен временем, неплохие условия по кредитам"));
        arrayList.add(new Actions(R.drawable.rosselhoz,"Россельхоз банк","У банка слабая инфраструктура, но очень выгодные кредиты при больших обьемах"));
        arrayList.add(new Actions(R.drawable.raiff,"Райффайзен банк","Новый банк у нас, пока только собираем информацию"));



        AdapterList adapters = new AdapterList(this,R.layout.list_row,arrayList);
        acList.setAdapter(adapters);
        acList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });


    }

    private void UserRestricted(){
        Button b = (Button)btnChoose;
        b.setEnabled(false);
        Button bs =(Button)button;
        bs.setEnabled(false);
        Button bss =(Button)button7;
        bss.setEnabled(false);

    }


    private void saveUser() {
        String id = mBase.push().getKey();
        String name = EditName.getText().toString();
        String secname = EditSurname.getText().toString();
        String email = EditScam.getText().toString();
        String dateStart = EditStartDate.getText().toString();
        String  dateEnd= EditEndDate.getText().toString();
        UserAdd newUserAdd = new UserAdd(id, name, secname, email, uploadUri.toString(),dateStart,dateEnd);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(secname) && !TextUtils.isEmpty(email)) {
            if (id != null) mBase.child(id).setValue(newUserAdd);


        } else {
            Toast.makeText(GlavnayaUser.this, "Возможно некоторые поля пустые!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSave(View view) {

        uploadImage();
    }

    public void onClickRead(View view) {
        Intent Intent = new Intent(GlavnayaUser.this, ReadActivity.class);
        startActivity(Intent);


    }

    public void LogoutG(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });
    }
    public void Logout(View view) {

        Intent Intent = new Intent(GlavnayaUser.this, profileActivity.class);
        startActivity(Intent);


    }

    public void btnChoose(View view) {
        chooseImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) ;
        {
            if (resultCode == RESULT_OK) {
                Log.d("Mylog", "Image URI : " + data.getData());
                imageView.setImageURI(data.getData());

            }
        }
    }
    public void getname () {//chat name
        fstore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());


                                name =  (document.get("FullName").toString());


                            }
                        }
                    }

                });
    }

    private void chooseImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }

    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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
                saveUser();

            }


        });

    }
    public void onClickTovarka(View view) {
        Intent Intent = new Intent(GlavnayaUser.this, TovarkaActivity.class);
        startActivity(Intent);
    }

    public void AddTovar(View view) {
        Intent Intent = new Intent(GlavnayaUser.this, TovarAddAct.class);
        startActivity(Intent);
    }
    private void ProInit(){
        google_img = findViewById(R.id.google_img);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc= GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
            String Name = account.getDisplayName();
            String Pic = account.getPhotoUrl().toString();
            if (Pic != null){
                Picasso.get().load(Pic).into(ProfilePic);
            }else{
                ProfilePic.setVisibility(View.GONE);
            }
            twAccount.setText(Name);
            loggout_btn.setVisibility(View.GONE);
        }

}

    public void Loggout(View view) {
        LogoutG();
    }

    public void chatsonic(View view) {
        Intent intent = new Intent(GlavnayaUser.this,chatAact.class);
        intent.putExtra("Pname",name+":");
        startActivity(intent);
    }
}

