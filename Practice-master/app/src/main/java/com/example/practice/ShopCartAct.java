package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShopCartAct extends AppCompatActivity {
    private ListView listkorzina;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<Korzina> listTemp;
    private List<OrderClass> listTemp2;
    private SearchView Finder;
    private DatabaseReference mBase;
    private String USERKEY = "";
    private String selectedItem;
    private int admin;
    private TextView AdminRoleTitle,AdminChose,Korzinka;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        init();

        deleteitem();

        Intent Intentor = getIntent();
        if (Intentor!=null){
            String Privelegy=(Intentor.getStringExtra("Adm"));
            if (Privelegy !=null){
                AdminChose.setVisibility(View.VISIBLE);
                AdminRoleTitle.setVisibility(View.VISIBLE);
                Korzinka.setVisibility(View.GONE);
                USERKEY = "Orders";
                mBase = FirebaseDatabase.getInstance().getReference(USERKEY);
                getDataFromDB2();
                admin =10;



            }else{
                USERKEY = "Cart";
                mBase = FirebaseDatabase.getInstance().getReference(USERKEY);
                getDataFromDB();
            }



        }

    }
    private void init(){
        listkorzina = findViewById(R.id.listkorzina);
        Korzinka = findViewById(R.id.KorzinkaTitle);
        listdata = new ArrayList<>();
        listTemp = new ArrayList<Korzina>();
        listTemp2 = new ArrayList<OrderClass>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listdata);
        listkorzina.setAdapter(adapter);

        AdminRoleTitle = findViewById(R.id.AdminRoleTitle);
        AdminChose = findViewById(R.id.AdminChose);
        AdminChose.setVisibility(View.GONE);
        AdminRoleTitle.setVisibility(View.GONE);
        Korzinka.setVisibility(View.VISIBLE);



    }



private void Delete(String id){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(id);
        dbRef.removeValue();
}
    private void getDataFromDB2(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listdata.size()<0)listdata.clear();
                if (listTemp2.size()<0)listTemp2.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    OrderClass Cart = ds.getValue(OrderClass.class);
                    assert Cart != null;
                    listdata.add(Cart.nazvanie);
                    listTemp2.add(Cart);
                }
                adapter.notifyDataSetChanged();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mBase.addValueEventListener(vListener);
    }
    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listdata.size()<0)listdata.clear();
                if (listTemp.size()<0)listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Korzina Cart = ds.getValue(Korzina.class);
                    assert Cart != null;
                    listdata.add(Cart.nazvanie);
                    listTemp.add(Cart);
                }
                adapter.notifyDataSetChanged();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mBase.addValueEventListener(vListener);
    }


public void deleteitem() {

    listkorzina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {




            AlertDialog.Builder builder = new AlertDialog.Builder(ShopCartAct.this);
            builder.setTitle("Перейти к заказу или удалить?");
            builder.setCancelable(false);
            builder.setIcon(R.drawable.cart);
            builder.setMessage("Вы можете перейти на страницу с заказом товара, или удалить выбранную позицию из корзины нажав Удалить");
            builder.setPositiveButton("Перейти к заказу",

                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            if (admin ==10){
                                Toast.makeText(ShopCartAct.this, "Функция только для клиентов!", Toast.LENGTH_SHORT).show();
                            }else{
                                Korzina OrderAdd = listTemp.get(position);
                                Intent intent = new Intent(ShopCartAct.this, OrderActivity.class);
                                intent.putExtra("Cart_imgTovar", OrderAdd.imgTovar);
                                intent.putExtra("Cart_nazvanie", OrderAdd.nazvanie);
                                intent.putExtra("Cart_description", OrderAdd.description);
                                intent.putExtra("Cart_fullprice", OrderAdd.fullprice);
                                intent.putExtra("Cart_warranty", OrderAdd.warranty);
                                intent.putExtra("Cart_Category", OrderAdd.Category);
                                startActivity(intent);
                            }

                        }

                    });
            builder.setNegativeButton("Удалить запись",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            Toast.makeText(ShopCartAct.this, "Выбранный товар успешно добавлен", Toast.LENGTH_SHORT).show();
                            mBase.removeValue();
                            adapter.remove(adapter.getItem(position));

                            listkorzina.setAdapter( adapter );
                            adapter.notifyDataSetChanged();
                            //remove value()



                        }
                    }).setNeutralButton("Редактировать(Только Админ)",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent Intentor = getIntent();
                            if (Intentor!=null) {
                                String Privelegy = (Intentor.getStringExtra("Adm"));
                                if (Privelegy != null) {
                                    AdminChose.setVisibility(View.VISIBLE);
                                    AdminRoleTitle.setVisibility(View.VISIBLE);


                                }


                                if (Privelegy != null) {





                                    OrderClass OrderAdds = listTemp2.get(position);
                                    Intent intent = new Intent(ShopCartAct.this, OrderActivity.class);
                                    intent.putExtra("Cart_imgTovar", OrderAdds.imgTovar);
                                    intent.putExtra("Cart_adress", OrderAdds.adress);
                                    intent.putExtra("Cart_itogPrice", OrderAdds.itogPrice);
                                    intent.putExtra("Cart_countLn", OrderAdds.countLn);
                                    intent.putExtra("Cart_nazvanie", OrderAdds.nazvanie);
                                    intent.putExtra("Cart_description", OrderAdds.description);
                                    intent.putExtra("Cart_fullprice", OrderAdds.fullprice);
                                    intent.putExtra("Cart_warranty", OrderAdds.warranty);
                                    intent.putExtra("Cart_status", OrderAdds.status);
                                    intent.putExtra("Cart_id", OrderAdds.id);
                                    intent.putExtra("Admire",Privelegy);

                                    startActivity(intent);

                                } else {
                                    Toast.makeText(ShopCartAct.this, "У вас не прав, войдите как Админ", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    });

                    AlertDialog alert = builder.create();
            alert.show();

        }






    });


}

}

