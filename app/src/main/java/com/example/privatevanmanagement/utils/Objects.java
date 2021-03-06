package com.example.privatevanmanagement.utils;

import com.example.privatevanmanagement.ChatModule.Model.ChatList;
import com.example.privatevanmanagement.models.AdminDetail_Model;
import com.example.privatevanmanagement.models.DriverDetail_Model;
import com.example.privatevanmanagement.models.Shift_Model;
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.models.VanDetail_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Objects {

    static public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static public FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    static public String Token = "";
    static public String UserType = "";

    static public List<StudentDetail_Model> student_modelList = new ArrayList<>();
    static public List<StudentDetail_Model> sortedList = new ArrayList<>();
    static public List<VanDetail_Model> vanList = new ArrayList<>();
    static public List<VanDetail_Model> freevanList = new ArrayList<>();
    static public List<DriverDetail_Model> driver_modelList = new ArrayList<>();
    static public List<DriverDetail_Model> freeDriverList = new ArrayList<>();
    static public List<DriverDetail_Model> trackDriverbyVan = new ArrayList<>();
    static public List<ChatList> scheduled_list = new ArrayList<>();
    static public List<String> group_list = new ArrayList<>();
    static public List<Shift_Model> shift_list = new ArrayList<>();
    static public String driverLatLongTable = "driver_lat_long";
    static public String studentLatLongTable = "student_lat_long";
    public static android.location.Location driverLastLocation = null;
    public static android.location.Location studentLastLocation = null;
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
