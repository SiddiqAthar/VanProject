package com.example.privatevanmanagement.models;

public class StudentDetail_Model {
    String student_id = "";

    String lat = "";
    String longi = "";

    String student_name = "";
    String student_email = "";
    String student_cnic = "";
    String student_contact = "";
    String student_address = "";

    String group = "";
    String fee_status = "";
    String ammount = "";
    String status = ""; // is bloced or not
    String shift_time = "";
    String drop_time = "";
    String allocated_van = "";
    String driver_id = "";
    String driver_name = "";
    String arrival = "";

    public StudentDetail_Model() {
    }

    public StudentDetail_Model(String student_id,String lat,String longi, String student_name, String student_email, String student_cnic, String student_contact, String student_address, String group, String fee_status, String ammount, String status, String shift_time, String drop_time, String allocated_van, String driver_id, String driver_name, String arrival) {
        this.student_id = student_id;
        this.lat = lat;
        this.longi = longi;
        this.student_name = student_name;
        this.student_email = student_email;
        this.student_cnic = student_cnic;
        this.student_contact = student_contact;
        this.student_address = student_address;
        this.group = group;
        this.fee_status = fee_status;
        this.ammount = ammount;
        this.status = status;
        this.shift_time = shift_time;
        this.drop_time = drop_time;
        this.allocated_van = allocated_van;
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.arrival = arrival;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getStudent_cnic() {
        return student_cnic;
    }

    public void setStudent_cnic(String student_cnic) {
        this.student_cnic = student_cnic;
    }

    public String getStudent_contact() {
        return student_contact;
    }

    public void setStudent_contact(String student_contact) {
        this.student_contact = student_contact;
    }

    public String getStudent_address() {
        return student_address;
    }

    public void setStudent_address(String student_address) {
        this.student_address = student_address;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFee_status() {
        return fee_status;
    }

    public void setFee_status(String fee_status) {
        this.fee_status = fee_status;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShift_time() {
        return shift_time;
    }

    public void setShift_time(String shift_time) {
        this.shift_time = shift_time;
    }

    public String getDrop_time() {
        return drop_time;
    }

    public void setDrop_time(String drop_time) {
        this.drop_time = drop_time;
    }

    public String getAllocated_van() {
        return allocated_van;
    }

    public void setAllocated_van(String allocated_van) {
        this.allocated_van = allocated_van;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
