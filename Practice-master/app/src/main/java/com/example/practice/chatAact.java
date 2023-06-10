package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class chatAact extends AppCompatActivity {
    private DatabaseReference mBase;
    private EditText message_input;
    private String GROUPKEY ="Messages";

    private ListView messageList;
    private String name;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private TextView nameup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_aact);
        message_input = findViewById(R.id.message_input);
        messageList = findViewById(R.id.messageList);

        listdata = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listdata);
        messageList.setAdapter(adapter);
        mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
        getDataFromDB();
        Intent i = getIntent();
        name = i.getStringExtra("Pname");
        nameup = findViewById(R.id.nameup);
        nameup.setText("Здравствуйте,"+i.getStringExtra("Pname"));
        checks();
    }
    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listdata.size()>0)listdata.clear();

                for (DataSnapshot ds:snapshot.getChildren()) {
                    MessagesClass msg = ds.getValue(MessagesClass.class);
                    assert msg !=null;
                    listdata.add(msg.message);
                }
                adapter.notifyDataSetChanged();





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mBase.addValueEventListener(vListener);
    }

public void checks(){
        message_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int maxLength = 60;

                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                message_input.setFilters(FilterArray);
                if (message_input.getText().toString().length()>=60){
                    Toast.makeText(chatAact.this, "Макс длина сообщения 60 символов!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
}


    private void saveData() {


        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
            String id = mBase.push().getKey();
            String message =name+ message_input.getText().toString()+"       "+date;
            MessagesClass msgadd = new MessagesClass(id,message);
            if (!TextUtils.isEmpty(message)) {
                if (message != null) mBase.child(id).setValue(msgadd);


            } else {
                Toast.makeText(chatAact.this, "Нельзя отправить пустое сообщение", Toast.LENGTH_SHORT).show();
            }
        }





    public void Send(View view) {
        if (message_input.getText().toString().isEmpty()){
            Toast.makeText(chatAact.this, "Нельзя отправить пустое сообщение", Toast.LENGTH_SHORT).show();
        }else{
            saveData();
            message_input.setText(null);
        }

    }

}
