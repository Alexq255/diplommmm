package com.example.practice;

public class OrderClass {
    public String id,nazvanie,description,fullprice,imgTovar, warranty,idUser,dateTime,adress,itogPrice,status,countLn;




    public OrderClass() {

    }

    public OrderClass(String id, String nazvanie, String description, String fullPrice, String warranty, String category, String adress, String countLn, String dateTime, String idUser, String itogPrice, String status) {
        this.id = id;
        this.nazvanie = nazvanie;
        this.description = description;
        this.imgTovar = imgTovar;
        this.warranty = warranty;

        this.idUser = idUser;
        this.dateTime = dateTime;
        this.adress = adress;
        this.itogPrice = itogPrice;
        this.status = status;
        this.countLn = countLn;
    }
}
