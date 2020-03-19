package com.example.privatevanmanagement.models;

public class Complaint_Model {
    String name;
    String complaint_text;

    public Complaint_Model() {
    }

    public Complaint_Model(String name, String complaint_text) {
        this.name = name;
        this.complaint_text = complaint_text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComplaint_text() {
        return complaint_text;
    }

    public void setComplaint_text(String complaint_text) {
        this.complaint_text = complaint_text;
    }
}
