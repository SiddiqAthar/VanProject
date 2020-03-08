package com.example.privatevanmanagement.models;

public class Schedule_Student_Model {
    String studend_id = "";
    String studend_name = "";
    String shift_time = "";

    public Schedule_Student_Model(String studend_id, String studend_name, String shift_time) {
        this.studend_id = studend_id;
        this.studend_name = studend_name;
        this.shift_time = shift_time;
    }

    public String getStudend_id() {
        return studend_id;
    }

    public void setStudend_id(String studend_id) {
        this.studend_id = studend_id;
    }

    public String getStudend_name() {
        return studend_name;
    }

    public void setStudend_name(String studend_name) {
        this.studend_name = studend_name;
    }

    public String getShift_time() {
        return shift_time;
    }

    public void setShift_time(String shift_time) {
        this.shift_time = shift_time;
    }
}
