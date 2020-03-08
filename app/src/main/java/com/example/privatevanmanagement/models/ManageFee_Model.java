package com.example.privatevanmanagement.models;

public class ManageFee_Model {
    String id;
    String name;
    String amount;
    String status;

    public ManageFee_Model(String id, String name, String amount, String status) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
