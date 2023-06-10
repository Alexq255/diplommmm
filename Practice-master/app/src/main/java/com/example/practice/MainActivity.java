package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PublicKey;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText email_login;
    private EditText password_login;
    private Button loginbtn;
    private TextView textView3;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fstore = FirebaseFirestore.getInstance();
        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);

        loginbtn = findViewById(R.id.loginbtn);
        textView3 = findViewById(R.id.textView3);
        mAuth = FirebaseAuth.getInstance();
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(Intent);
                FirebaseUser cUser = mAuth.getCurrentUser();

            }

        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_login.getText().toString().isEmpty()|| password_login.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Поля не могут быть пусты",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email_login.getText().toString(),password_login.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        checkUserAcessLevel();

                                    }else{
                                        Toast.makeText(MainActivity.this, "Неверные данные",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });






    }

    private void checkUserAcessLevel(){
        DocumentReference df = fstore.collection("Users").document(mAuth.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess"+ documentSnapshot.getData());
                if (documentSnapshot.getString("isAdmin") !=null){
                    //startActivity(new Intent(getApplicationContext(),Glavnaya.class));
                    //inish();
                    Intent intent = new Intent(MainActivity.this, Glavnaya.class);
                    FirebaseUser cUser = mAuth.getCurrentUser();
                    intent.putExtra("UserData","Админ:"+cUser.getEmail().toString());
                    intent.putExtra("Adm","1");
                    startActivity(intent);

                }
                if (documentSnapshot.getString("isUser")!= null){
                    //startActivity(new Intent(getApplicationContext(),GlavnayaUser.class) );
                    Intent intent = new Intent(MainActivity.this, GlavnayaUser.class);
                    FirebaseUser cUser = mAuth.getCurrentUser();
                    intent.putExtra("UserData","Пользователь:"+cUser.getEmail().toString());
                    startActivity(intent);

                }
                if (documentSnapshot.getString("isBanned")!= null){
                    //startActivity(new Intent(getApplicationContext(),GlavnayaUser.class) );
                    Intent intent = new Intent(MainActivity.this, BanActivity.class);
                    FirebaseUser cUser = mAuth.getCurrentUser();
                    intent.putExtra("UserData","Пользователь:"+cUser.getEmail().toString());
                    startActivity(intent);

                }
            }
        });
    }

    public void LoginByNumber(View view) {

        Intent intent = new Intent(MainActivity.this, SendOtpActivity.class);
        startActivity(intent);
    }
    public void Reg(View view) {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }
}