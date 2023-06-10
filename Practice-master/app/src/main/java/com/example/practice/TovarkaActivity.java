package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TovarkaActivity extends AppCompatActivity {

    private ListView tovarList;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<TovarAddClass> listTemp;
    private DatabaseReference mBase;
    private String GROUPKEY ="Asic";
    private TextView Countl;
    private SearchView Finder;
    private Button SortBtn,SortClear;
    private TextView textView14;
    private EditText minprice,maxprice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovarka);
        init();
        CatChoser();
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
        tovarList = findViewById(R.id.tovarList);
        textView14 = findViewById(R.id.textView14);
        SortClear = findViewById(R.id.SortClear);
        SortClear.setVisibility(View.GONE);
        SortBtn = findViewById(R.id.SortBtn);
        Finder = findViewById(R.id.Finder);

        listdata = new ArrayList<>();
        Countl = findViewById(R.id.Countl);
        listTemp = new ArrayList<TovarAddClass>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listdata);
        tovarList.setAdapter(adapter);
        mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);

        setOnclickItem();



    }
    private void CatChoser(){

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
                    Countl.setText("Общее кол-во предложений:"+tovarList.getAdapter().getCount());
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
                Intent is = new Intent(TovarkaActivity.this, FullTovarAct.class);
                is.putExtra("tovar_imgTovar",tovarAdd.imgTovar);
                is.putExtra("tovar_imgTovar2",tovarAdd.imgTovar1);
                is.putExtra("tovar_imgTovar3",tovarAdd.imgTovar2);
                is.putExtra("tovar_nazvanie",tovarAdd.nazvanie);
                is.putExtra("tovar_description",tovarAdd.description);
                is.putExtra("tovar_fullprice",tovarAdd.fullprice);
                is.putExtra("tovar_warranty",tovarAdd.warranty);
                is.putExtra("tovar_Category",tovarAdd.Category);
                is.putExtra("iD",tovarAdd.id);
                startActivity(is);

            }
        });
    }


    public void SortByCat(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выбор категории");
        builder.setMessage("Выберите одну из категорий, для этого нажмите на нужную кнопку, список будет отсортировани под нужную вам категорию");

        // add the buttons
        builder.setPositiveButton("Асики", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.clear();
                listTemp.clear();
                GROUPKEY= "Asic";
                tovarList.setAdapter(adapter);
                mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
                getDataFromDB();
                textView14.setText("Асики");
                SortBtn.setVisibility(View.GONE);
                SortClear.setVisibility(View.VISIBLE);
            }
        });
        builder.setNeutralButton("Банковские продукты", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.clear();
                listTemp.clear();
                GROUPKEY= "Products";
                tovarList.setAdapter(adapter);
                textView14.setText("Банковские продукты");
                mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
                getDataFromDB();
                SortBtn.setVisibility(View.GONE);
                SortClear.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("Услуги", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.clear();
                listTemp.clear();
                GROUPKEY= "Services";
                tovarList.setAdapter(adapter);
                textView14.setText("Услуги");
                mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
                getDataFromDB();
                SortBtn.setVisibility(View.GONE);
                SortClear.setVisibility(View.VISIBLE);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void SortClear(View view) {


        adapter.notifyDataSetChanged();
        SortBtn.setVisibility(View.VISIBLE);
        adapter.clear();
        listTemp.clear();
        tovarList.setAdapter(null);
        SortClear.setVisibility(View.GONE);
    }
public void priceFilter(){
    ValueEventListener vListenerr = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listdata.size()>0)listdata.clear();
            if (listTemp.size()>0)listTemp.clear();

            for (DataSnapshot ds : snapshot.getChildren()) {

                if (maxprice.getText().toString().isEmpty() || minprice.getText().toString().isEmpty()) {
                    TovarAddClass tovar = ds.getValue(TovarAddClass.class);
                    assert tovar != null;
                    listdata.add(tovar.nazvanie);
                    listTemp.add(tovar);
                } else {
                    TovarAddClass tovar = ds.getValue(TovarAddClass.class);
                    assert tovar != null;
                    int price = Integer.parseInt(tovar.fullprice);
                    int maxpricer = Integer.parseInt(maxprice.getText().toString());
                    int minpricer = Integer.parseInt(minprice.getText().toString());
                    if (price >= minpricer && price <= maxpricer) {
                        listdata.add(tovar.nazvanie);
                        listTemp.add(tovar);
                    }

                    assert tovar != null;
                    listdata.add(tovar.nazvanie);
                    listTemp.add(tovar);

                }
                adapter.notifyDataSetChanged();
            }



        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    }
    public void Accept(View view) {


    };}

