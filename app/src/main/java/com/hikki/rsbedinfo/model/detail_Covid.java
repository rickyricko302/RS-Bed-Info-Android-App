package com.hikki.rsbedinfo.model;

public class detail_Covid {
    private String time,title, bed_available, bed_empty, queue;

    public String getTitle() {
        return title;
    }

    public String getBed_available() {
        return bed_available;
    }

    public String getBed_empty() {
        return bed_empty;
    }

    public String getQueue() {
        return queue;
    }

    public String getTime() {
        return time;
    }

    public detail_Covid(String time, String title, String bed_available, String bed_empty, String queue) {
        this.title = title;
        this.bed_available = bed_available;
        this.bed_empty = bed_empty;
        this.queue = queue;
        this.time = time;
    }
}
