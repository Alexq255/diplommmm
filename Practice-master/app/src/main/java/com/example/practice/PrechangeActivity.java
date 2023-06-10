package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class PrechangeActivity extends AppCompatActivity {
    private ListView tovarList;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<TovarAddClass> listTemp;
    private DatabaseReference mBase;
    private String GROUPKEY ="Products";
    private TextView Countl;

    private Button SortBtn,SortClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prechange);
        init();



    }

    private void init(){
        tovarList = findViewById(R.id.tovarList);

        listdata = new ArrayList<>();

        listTemp = new ArrayList<TovarAddClass>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listdata);
        tovarList.setAdapter(adapter);
        mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
        getDataFromDB();
        setOnclickItem();



    }


    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (listdata.size()<0)listdata.clear();
                if (listTemp.size()<0)listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    TovarAddClass tovar = ds.getValue(TovarAddClass.class);
                    assert tovar != null;
                    listdata.add(tovar.nazvanie);
                    listTemp.add(tovar);

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
        tovarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TovarAddClass tovarAdd = listTemp.get(i);
                Integer ids = tovarList.getId();

                String idss = Integer.toString(ids);

                Intent is = new Intent(PrechangeActivity.this, ChangeActivity.class);
                is.putExtra("tovar_id",tovarAdd.id);
                is.putExtra("tovar_imgTovar",tovarAdd.imgTovar);
                is.putExtra("tovar_nazvanie",tovarAdd.nazvanie);
                is.putExtra("tovar_description",tovarAdd.description);
                is.putExtra("tovar_fullprice",tovarAdd.fullprice);
                is.putExtra("tovar_warranty",tovarAdd.warranty);
                is.putExtra("tovar_Category",tovarAdd.Category);
                is.putExtra("Id",ids.toString());
                startActivity(is);


            }
        });
    }


    public void BackToAdm(View view) {
        Intent is = new Intent(PrechangeActivity.this, Glavnaya.class);
        startActivity(is);
    }
}