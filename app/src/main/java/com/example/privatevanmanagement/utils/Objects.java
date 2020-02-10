package com.example.privatevanmanagement.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Objects {

    static public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseAuth getInstance() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
            return mAuth;
        }
        return mAuth;
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


}
