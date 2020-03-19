package com.example.privatevanmanagement.models;

public class Shift_Model {
    String shift_name;
    String shift_time;

    public Shift_Model() {
    }

    public Shift_Model(String shift_name, String shift_time) {
        this.shift_name = shift_name;
        this.shift_time = shift_time;
    }

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }

    public String getShift_time() {
        return shift_time;
    }

    public void setShift_time(String shift_time) {
        this.shift_time = shift_time;
    }
}
