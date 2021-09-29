package com.hikki.rsbedinfo.model;

public class hospital {
    private String id,name,address,phone,queue,bed_availability,info;

    public hospital(String id, String name, String address, String phone, String queue, String bed_availability, String info) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.queue = queue;
        this.bed_availability = bed_availability;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getBed_availability() {
        return bed_availability;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getQueue() {
        return queue;
    }
}
