package com.example.privatevanmanagement.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.example.privatevanmanagement.models.Shift_Model;
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.privatevanmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.example.privatevanmanagement.utils.Objects.Token;
import static com.example.privatevanmanagement.utils.Objects.group_list;
import static com.example.privatevanmanagement.utils.Objects.shift_list;

public class BaseActivity extends AppCompatActivity {
    LocationManager locationManager;
    static public final int REQUEST_LOCATION = 1;
    public static final long DISCONNECT_TIMEOUT = 600000; // 5 min = 5 * 60 * 1000 ms
    private static final int LOCATION_INTERVAL = 100;
    private static final float LOCATION_DISTANCE = 1f;


    DialogInterface.OnKeyListener keyListner = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                sendBroadcast(closeDialog);
                return true;
            } else return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocation(true);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        group_list();
        shift_list();
    }


    public boolean checkInternetConnection() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void getLocation(boolean showDialog) {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(BaseActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(BaseActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (showDialog) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                }
                return;
            }
            if (!islocationServiceEnabled()) {
                if (showDialog) {
                    msgLocationDisbled("Turn on location services to use application");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean islocationServiceEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (gps_enabled || network_enabled) {
            return true;
        } else {
            return false;
        }

    }


    public void msgLocationDisbled(String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(keyListner);
        dialog.setContentView(R.layout.location_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.successTextView);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setText(msg);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        dialogButton.setText("Enable Location");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }




    public void group_list() {
        DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("Groups");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                group_list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    group_list.add(postSnapshot.getKey().toString());
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BaseActivity.this, "Some Error in fetching Group Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shift_list() {
        shift_list.clear();
        DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("Shifts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Shift_Model tempModel = new Shift_Model(postSnapshot.getKey(), postSnapshot.getValue().toString());
                    shift_list.add(tempModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BaseActivity.this, "Some Error in fetching Group Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        hideKeyboard(this);

        String backStateName = fragment.getClass().getName();
        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.mlayout, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void replaceFragmentUserActivity(Fragment fragment, Bundle bundle) {
        hideKeyboard(this);
        String backStateName = fragment.getClass().getName();
        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);


        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frameLayout, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && activity.getWindow().getDecorView().getRootView().getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
            inputManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
         }
    }
}
