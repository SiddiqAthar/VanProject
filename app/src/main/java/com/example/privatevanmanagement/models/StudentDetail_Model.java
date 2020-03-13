package com.example.privatevanmanagement.models;

public class StudentDetail_Model {
    String studend_id = "";

    String lat = "";
    String longi = "";

    String studend_name = "";
    String studend_email = "";
    String studend_cnic = "";
    String studend_contact = "";
    String studend_address = "";

    String group = "";
    String fee_status = "";
    String ammount = "";
    String status = ""; // is bloced or not
    String shift_time = "";
    String drop_time = "";
    String allocated_van = "";
    String driver_id = "";
    String driver_name = "";

    public StudentDetail_Model() {
    }

    public StudentDetail_Model(String studend_id,String lat,String longi, String studend_name, String studend_email, String studend_cnic, String studend_contact, String studend_address, String group, String fee_status, String ammount, String status, String shift_time, String drop_time, String allocated_van, String driver_id, String driver_name) {
        this.studend_id = studend_id;
        this.lat = lat;
        this.longi = longi;
        this.studend_name = studend_name;
        this.studend_email = studend_email;
        this.studend_cnic = studend_cnic;
        this.studend_contact = studend_contact;
        this.studend_address = studend_address;
        this.group = group;
        this.fee_status = fee_status;
        this.ammount = ammount;
        this.status = status;
        this.shift_time = shift_time;
        this.drop_time = drop_time;
        this.allocated_van = allocated_van;
        this.driver_id = driver_id;
        this.driver_name = driver_name;
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

    public String getStudend_email() {
        return studend_email;
    }

    public void setStudend_email(String studend_email) {
        this.studend_email = studend_email;
    }

    public String getStudend_cnic() {
        return studend_cnic;
    }

    public void setStudend_cnic(String studend_cnic) {
        this.studend_cnic = studend_cnic;
    }

    public String getStudend_contact() {
        return studend_contact;
    }

    public void setStudend_contact(String studend_contact) {
        this.studend_contact = studend_contact;
    }

    public String getStudend_address() {
        return studend_address;
    }

    public void setStudend_address(String studend_address) {
        this.studend_address = studend_address;
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
}
