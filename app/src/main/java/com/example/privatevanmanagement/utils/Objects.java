package com.example.privatevanmanagement.utils;

import com.example.privatevanmanagement.models.AdminDetail_Model;
import com.example.privatevanmanagement.models.DriverDetail_Model;
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.models.VanDetail_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Objects {

    static public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static public FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    static public String UserType = "";

    public static FirebaseAuth getInstance() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
            return mAuth;
        }
        return mAuth;
    }

    public static FirebaseDatabase getFirebaseInstance() {
        if (mFirebaseDatabase == null) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            return mFirebaseDatabase;
        }
        return mFirebaseDatabase;
    }

    static public Location location = new Location();

    static public class Location {
        public String Lat = "";
        public String Lng = "";
        public String Src = "";


        public Location() {
            Lat = "";
            Lng = "";
            Src = "";

        }

        public static void Location() {
            location = new Location();
        }


    }

    static public class UserID {
        static public UserID userID = new UserID();
        public static String Globaluser_ID = "";

        public UserID() {
            Globaluser_ID = "";
        }

        public static void UserID() {
            userID = new UserID();
        }
    }

    static public VanDetail_Model vanDetail_model;

    public static VanDetail_Model getVanDetailInstance() {
        if (vanDetail_model == null) {
            vanDetail_model = new VanDetail_Model();
        }
        return vanDetail_model;
    }

    static public StudentDetail_Model studentDetail_model;

    public static StudentDetail_Model getStudentDetailInstance() {
        if (studentDetail_model == null) {
            studentDetail_model = new StudentDetail_Model();
        }
        return studentDetail_model;
    }

    static public DriverDetail_Model driverDetail_model;

    public static DriverDetail_Model getDriverDetailInstance() {
        if (driverDetail_model == null) {
            driverDetail_model = new DriverDetail_Model();
        }
        return driverDetail_model;
    }

    static public AdminDetail_Model adminDetail_model;

    public static AdminDetail_Model getAdminDetailInstance() {
        if (adminDetail_model == null) {
            adminDetail_model = new AdminDetail_Model();
        }
        return adminDetail_model;
    }


}
