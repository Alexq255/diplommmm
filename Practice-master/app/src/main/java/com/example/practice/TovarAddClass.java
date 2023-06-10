package com.example.practice;

import android.text.TextUtils;
import android.widget.Toast;

public class TovarAddClass {
    public String id,nazvanie,description,fullprice,imgTovar, warranty,Category,imgTovar1,imgTovar2;

    public TovarAddClass() {

    }

    public TovarAddClass(String id, String nazvanie, String description, String fullprice,String imgTovar,String warranty,String Category,String imgTovar1,String imgTovar2) {
        this.id = id;
        this.nazvanie = nazvanie;
        this.description = description;
        this.fullprice = fullprice;
        this.imgTovar = imgTovar;
        this.imgTovar1 = imgTovar1;
        this.imgTovar2= imgTovar2;
        this.warranty = warranty;
        this.Category = Category;
    }


    public TovarAddClass(String id, String nazvanie, String adress, String fullPrice, String dateTime, String status) {
    }
}
