package com.example.practice;


public class LimitedTovarAdd {
    public String id, nazvanie, adress, fullprice, dateTime, status;

    public LimitedTovarAdd() {

    }

    public LimitedTovarAdd(String id, String nazvanie, String adress, String fullprice, String dateTime, String status) {
        this.id = id;
        this.nazvanie = nazvanie;
        this.fullprice = fullprice;
        this.adress = adress;
        this.dateTime = dateTime;
        this.status = status;

    }
}