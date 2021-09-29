package com.hikki.rsbedinfo.model;

import java.util.List;

public class non_Covid {
    private String id,name,address,phone, update;
    private int jumlah;
    public non_Covid(String id, String name, String address, String phone, int jumlah, String update) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.jumlah=jumlah;
        this.update=update;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getUpdate() {
        return update;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
