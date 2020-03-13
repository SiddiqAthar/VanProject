package com.example.privatevanmanagement.models;

public class DriverDetail_Model {
    String driver_id = "";
    String driver_lat = "";
    String driver_longi = "";

    String driver_name = "";
    String driver_email = "";
    String driver_cnic = "";
    String driver_contact = "";
    String driver_address = "";
    String driver_van_id = "";

    String salary_status = "";
    String salary_ammount = "";
    String status = "";
    String assigned_status = "";
    String van_number = "";
    String shift_start_time = "";
    String shift_drop_time = "";

    public DriverDetail_Model() {
    }

    public DriverDetail_Model(String driver_id, String driver_lat, String driver_longi, String driver_name, String driver_email, String driver_cnic, String driver_contact, String driver_address, String driver_van_id, String salary_status, String salary_ammount, String status, String assigned_status, String van_number, String shift_start_time, String shift_drop_time) {
        this.driver_id = driver_id;
        this.driver_lat = driver_lat;
        this.driver_longi = driver_longi;
        this.driver_name = driver_name;
        this.driver_email = driver_email;
        this.driver_cnic = driver_cnic;
        this.driver_contact = driver_contact;
        this.driver_address = driver_address;
        this.driver_van_id = driver_van_id;
        this.salary_status = salary_status;
        this.salary_ammount = salary_ammount;
        this.status = status;
        this.assigned_status = assigned_status;
        this.van_number = van_number;
        this.shift_start_time = shift_start_time;
        this.shift_drop_time = shift_drop_time;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_lat() {
        return driver_lat;
    }

    public void setDriver_lat(String driver_lat) {
        this.driver_lat = driver_lat;
    }

    public String getDriver_longi() {
        return driver_longi;
    }

    public void setDriver_longi(String driver_longi) {
        this.driver_longi = driver_longi;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_email() {
        return driver_email;
    }

    public void setDriver_email(String driver_email) {
        this.driver_email = driver_email;
    }

    public String getDriver_cnic() {
        return driver_cnic;
    }

    public void setDriver_cnic(String driver_cnic) {
        this.driver_cnic = driver_cnic;
    }

    public String getDriver_contact() {
        return driver_contact;
    }

    public void setDriver_contact(String driver_contact) {
        this.driver_contact = driver_contact;
    }

    public String getDriver_address() {
        return driver_address;
    }

    public void setDriver_address(String driver_address) {
        this.driver_address = driver_address;
    }

    public String getDriver_van_id() {
        return driver_van_id;
    }

    public void setDriver_van_id(String driver_van_id) {
        this.driver_van_id = driver_van_id;
    }

    public String getSalary_status() {
        return salary_status;
    }

    public void setSalary_status(String salary_status) {
        this.salary_status = salary_status;
    }

    public String getSalary_ammount() {
        return salary_ammount;
    }

    public void setSalary_ammount(String salary_ammount) {
        this.salary_ammount = salary_ammount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssigned_status() {
        return assigned_status;
    }

    public void setAssigned_status(String assigned_status) {
        this.assigned_status = assigned_status;
    }

    public String getVan_number() {
        return van_number;
    }

    public void setVan_number(String van_number) {
        this.van_number = van_number;
    }

    public String getShift_start_time() {
        return shift_start_time;
    }

    public void setShift_start_time(String shift_start_time) {
        this.shift_start_time = shift_start_time;
    }

    public String getShift_drop_time() {
        return shift_drop_time;
    }

    public void setShift_drop_time(String shift_drop_time) {
        this.shift_drop_time = shift_drop_time;
    }
}
