package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class ReadActivity extends AppCompatActivity {
private ListView listView;
private ArrayAdapter<String> adapter;
private List<String> listdata;
private List<UserAdd> listTemp;
private SearchView Finder;
private DatabaseReference mBase;
private String USERKEY = "User";
private TextView textView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        init();
        getDataFromDB();
        setOnclickItem();

        Finder.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);
                return false;
            }
        });
    }
    private void init(){
        listView = findViewById(R.id.listView);
        Finder = findViewById(R.id.Finder);
        listdata = new ArrayList<>();
        listTemp = new ArrayList<UserAdd>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listdata);
        listView.setAdapter(adapter);
        mBase = FirebaseDatabase.getInstance().getReference(USERKEY);
        textView9 = findViewById(R.id.textView9);

    }
    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listdata.size()<0)listdata.clear();
                if (listTemp.size()<0)listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    UserAdd user = ds.getValue(UserAdd.class);
                    assert user != null;
                    listdata.add(user.nazvanie);
                    listTemp.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mBase.addValueEventListener(vListener);
    }
    private void setOnclickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            UserAdd userAdd = listTemp.get(i);
                Intent intent = new Intent(ReadActivity.this, ShowDataActivity.class);
                intent.putExtra("user_nazvanie",userAdd.nazvanie);
                intent.putExtra("user_shortOp",userAdd.shortOp);
                intent.putExtra("user_fullOp",userAdd.fullOp);
                intent.putExtra("user_image_id",userAdd.image_id);
                intent.putExtra("user_dateStart",userAdd.dateStart);
                intent.putExtra("user_dateEnd",userAdd.dateEnd);
                startActivity(intent);
            }
        });
    }
}