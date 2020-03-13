package com.example.privatevanmanagement.models;

public class VanDetail_Model {

    String vanRegisteration = "";
    String vanModel = "";
    String vanMake = "";
    String vanColor = "";
    String vanType = "";
    String vanCapacity = "";

    String Assign_Status = "";
    String Assign_DriverId = "";
    String Assign_DriverName = "";

    public VanDetail_Model() {
    }

    public VanDetail_Model(String vanRegisteration, String vanModel, String vanMake, String vanColor, String vanType, String vanCapacity, String assign_Status, String assign_DriverId, String assign_DriverName) {
        this.vanRegisteration = vanRegisteration;
        this.vanModel = vanModel;
        this.vanMake = vanMake;
        this.vanColor = vanColor;
        this.vanType = vanType;
        this.vanCapacity = vanCapacity;
        Assign_Status = assign_Status;
        Assign_DriverId = assign_DriverId;
        Assign_DriverName = assign_DriverName;
    }

    public String getVanRegisteration() {
        return vanRegisteration;
    }

    public void setVanRegisteration(String vanRegisteration) {
        this.vanRegisteration = vanRegisteration;
    }

    public String getVanModel() {
        return vanModel;
    }

    public void setVanModel(String vanModel) {
        this.vanModel = vanModel;
    }

    public String getVanMake() {
        return vanMake;
    }

    public void setVanMake(String vanMake) {
        this.vanMake = vanMake;
    }

    public String getVanColor() {
        return vanColor;
    }

    public void setVanColor(String vanColor) {
        this.vanColor = vanColor;
    }

    public String getVanType() {
        return vanType;
    }

    public void setVanType(String vanType) {
        this.vanType = vanType;
    }

    public String getVanCapacity() {
        return vanCapacity;
    }

    public void setVanCapacity(String vanCapacity) {
        this.vanCapacity = vanCapacity;
    }

    public String getAssign_Status() {
        return Assign_Status;
    }

    public void setAssign_Status(String assign_Status) {
        Assign_Status = assign_Status;
    }

    public String getAssign_DriverId() {
        return Assign_DriverId;
    }

    public void setAssign_DriverId(String assign_DriverId) {
        Assign_DriverId = assign_DriverId;
    }

    public String getAssign_DriverName() {
        return Assign_DriverName;
    }

    public void setAssign_DriverName(String assign_DriverName) {
        Assign_DriverName = assign_DriverName;
    }
}
