package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
private EditText email_Register;
private EditText password_Register, Name_Register,Phone_Register,town_Register,country_Register;
private Button btn_register;
private FirebaseAuth mAuth;
private Boolean valid = true;
private FirebaseFirestore fstore;
private ImageView google_img;
private GoogleSignInClient gsc;
private GoogleSignInOptions gso;

CheckBox checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        email_Register = findViewById(R.id.email_register);
        Name_Register = findViewById(R.id.Name_Register);
        Phone_Register = findViewById(R.id.Phone_Register);
        password_Register = findViewById(R.id.password_register);
        town_Register = findViewById(R.id.town_Register);
        country_Register = findViewById(R.id.country_Register);
        btn_register = findViewById(R.id.btn_register);
        ProInit();

        checker = findViewById(R.id.checker);
        checker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checker.isChecked()){
                    Button bg = (Button)btn_register;
                    bg.setEnabled(true);

                }else{
                    Button bg = (Button)btn_register;
                    bg.setEnabled(false);

                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override

            public void onClick(View view) {

                if (email_Register.getText().toString().isEmpty()|| password_Register.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Поля пустые",Toast.LENGTH_SHORT).show();
            }else{
                    mAuth.createUserWithEmailAndPassword(email_Register.getText().toString(), password_Register.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser users = mAuth.getCurrentUser();
                                    DocumentReference df = fstore.collection("Users").document(users.getUid());
                                    Map<String,Object> userinfo = new HashMap<>();
                                    userinfo.put("FullName",Name_Register.getText().toString());
                                    userinfo.put("Phone",Phone_Register.getText().toString());
                                    userinfo.put("Town",country_Register.getText().toString());
                                    userinfo.put("Country",town_Register.getText().toString());
                                    userinfo.put("isUser","1");
                                    df.set(userinfo);
                                    if (task.isSuccessful()){

                                        Intent intent = new Intent(RegisterActivity.this, GlavnayaUser.class);
                                        FirebaseUser cUser = mAuth.getCurrentUser();
                                        intent.putExtra("UserData","Пользователь:"+cUser.getEmail().toString());
                                        startActivity(intent);

                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Поля заполнены неверно, или есть ошибки в формате",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        Button b = (Button)btn_register;
        b.setEnabled(false);
        }

    public void Licenge(View view) {
        Intent intent = new Intent(RegisterActivity.this, Licenge.class);
        startActivity(intent);

    }
    private void ProInit(){
        google_img = findViewById(R.id.google_img);
    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    gsc= GoogleSignIn.getClient(this,gso);
    google_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SignIn();

        }
    });

    }
    private void   SignIn(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Relay();
            } catch (ApiException e) {
                Toast.makeText(RegisterActivity.this,"Ошибка",Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void Relay() {
        finish();
        Intent intent = new Intent(getApplicationContext(),GlavnayaUser.class);
        startActivity(intent);
    }
}
