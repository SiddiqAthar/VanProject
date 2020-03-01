package com.example.privatevanmanagement.models;

public class VanDetail_Model {

    String vanID = "";
    String vanModel = "";
    String vanName = "";
    String vanNumber = "";
    VanDetail_Model detail_model;

    public VanDetail_Model() {

    }

    public VanDetail_Model(String vanID,String vanModel, String vanName, String vanNumber) {
        this.vanID = vanID;
        this.vanModel = vanModel;
        this.vanName = vanName;
        this.vanNumber = vanNumber;
    }

    public String getVanID() {
        return vanID;
    }

    public void setVanID(String vanID) {
        this.vanID = vanID;
    }

    public String getVanModel() {
        return vanModel;
    }

    public void setVanModel(String vanModel) {
        this.vanModel = vanModel;
    }

    public String getVanName() {
        return vanName;
    }

    public void setVanName(String vanName) {
        this.vanName = vanName;
    }

    public String getVanNumber() {
        return vanNumber;
    }

    public void setVanNumber(String vanNumber) {
        this.vanNumber = vanNumber;
    }
}
