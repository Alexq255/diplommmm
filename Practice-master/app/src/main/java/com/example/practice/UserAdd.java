package com.example.practice;

public class UserAdd {
    public String id,name,image_id,dateStart,dateEnd,nazvanie,shortOp,fullOp;

    public UserAdd() {

    }





    public UserAdd(String id, String shortOp, String image_id, String fullOp, String nazvanie, String dateStart, String dateEnd) {
        this.id = id;
        this.nazvanie = nazvanie;
        this.shortOp = shortOp;
        this.fullOp = fullOp;
        this.image_id = image_id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }



}
