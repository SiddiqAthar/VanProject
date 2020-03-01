package com.example.privatevanmanagement.models;

public class AdminDetail_Model {
    String admin_id = "";
    String admin_name = "";
    String admin_cnic = "";
    String admin_contact = "";
    String admin_address = "";
//    String admin_email = "";

    public AdminDetail_Model() {
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_cnic() {
        return admin_cnic;
    }

    public void setAdmin_cnic(String admin_cnic) {
        this.admin_cnic = admin_cnic;
    }

    public String getAdmin_contact() {
        return admin_contact;
    }

    public void setAdmin_contact(String admin_contact) {
        this.admin_contact = admin_contact;
    }

    public String getAdmin_address() {
        return admin_address;
    }

    public void setAdmin_address(String admin_address) {
        this.admin_address = admin_address;
    }
}
