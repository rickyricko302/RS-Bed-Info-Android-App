package com.hikki.rsbedinfo.model;

public class provinces {
    private String id,nama;
    public provinces(String id, String nama){
        //ini provinsi
        this.id = id;
        this.nama = nama;
    }

    public String getId(){
        return this.id;
    }
    public String getNama(){
        return this.nama;
    }
}
