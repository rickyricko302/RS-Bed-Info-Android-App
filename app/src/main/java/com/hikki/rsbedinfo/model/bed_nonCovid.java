package com.hikki.rsbedinfo.model;

public class bed_nonCovid {
    private final String available;
    private final String bed_class;
    private final String room_name;
    private final String info;

    public bed_nonCovid(String available, String bed_class, String room_name, String info) {
        this.available = available;
        this.bed_class = bed_class;
        this.room_name = room_name;
        this.info = info;
    }

    public String getAvailable() {
        return available;
    }

    public String getBed_class() {
        return bed_class;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getInfo() {
        return info;
    }
}
