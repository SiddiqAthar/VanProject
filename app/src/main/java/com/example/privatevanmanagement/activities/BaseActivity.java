package com.example.privatevanmanagement.activities;

import android.Manifest;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
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

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.example.privatevanmanagement.utils.Objects.Token;
import static com.example.privatevanmanagement.utils.Objects.group_list;
import static com.example.privatevanmanagement.utils.Objects.shift_list;

public class BaseActivity extends AppCompatActivity implements LocationListener {

    Dialog dialog;
    protected FrameLayout frameLayout;
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
//        getSupportActionBar().hide();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        getToken();
        group_list();
        shift_list();

    }

    public void changeFragment(Fragment fragment, Bundle bundle) {

        FrameLayout fl = (FrameLayout) findViewById(R.id.mlayout);
        fl.removeAllViews();
//        FrameLayout fl = (FrameLayout) findViewById(R.id.frameLayout);
//        fl.removeAllViews();
        if (bundle != null)
            fragment.setArguments(bundle);
        Field field = null;
        try {
            field = getSupportFragmentManager().getClass().getDeclaredField("mActive");
            field.setAccessible(true);
            ((SparseArray<Fragment>) field.get(getSupportFragmentManager())).clear();
            field = getSupportFragmentManager().getClass().getDeclaredField("mAdded");
            field.setAccessible(true);
            ((ArrayList<Object>) field.get(getSupportFragmentManager())).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
//        transaction1.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        transaction1.replace(R.id.mlayout, fragment);
        transaction1.commitAllowingStateLoss();
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
            if (islocationServiceEnabled()) {

                locationManager.requestLocationUpdates(NETWORK_PROVIDER, LOCATION_INTERVAL,
                        LOCATION_DISTANCE, (LocationListener) BaseActivity.this);
                locationManager.requestLocationUpdates(GPS_PROVIDER, LOCATION_INTERVAL,
                        LOCATION_DISTANCE, (LocationListener) BaseActivity.this);
                Criteria criteria = new Criteria();
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location lastknownlocationGps = locationManager.getLastKnownLocation(GPS_PROVIDER);
                Location lastknownlocationNetwork = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                Location lastknownlocationBest = locationManager.getLastKnownLocation(bestProvider);

                if (lastknownlocationGps != null) {
                    Objects.location.Lat = lastknownlocationGps.getLatitude() + "";
                    Objects.location.Lng = lastknownlocationGps.getLongitude() + "";
                    Objects.location.Src = "GPS";
                } else if (lastknownlocationNetwork != null) {
                    Objects.location.Lat = lastknownlocationNetwork.getLatitude() + "";
                    Objects.location.Lng = lastknownlocationNetwork.getLongitude() + "";
                    Objects.location.Src = "NETWORK";
                } else if (lastknownlocationBest != null) {
                    Objects.location.Lat = lastknownlocationBest.getLatitude() + "";
                    Objects.location.Lng = lastknownlocationBest.getLongitude() + "";
                    Objects.location.Src = lastknownlocationBest.getProvider() + "";

                } else {
                    Objects.location.Lat = "0.00";
                    Objects.location.Lng = "0.00";
                }
            } else {
                if (showDialog) {
                    msgLocationDisbled("Objects services disabled. Turn on location services to use application");
                }

            }


            //   Toast.makeText(BaseActivity.this, "GetLocation: " + Objects.location.Lat + " " + Objects.location.Lng + " src:" +   Objects.location.Src, Toast.LENGTH_SHORT).show();
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
        dialog.setContentView(R.layout.dialog);
        TextView text = (TextView) dialog.findViewById(R.id.successTextView);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setText(msg);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        dialogButton.setText("Enable Location");
        Button dialogcancel = (Button) dialog.findViewById(R.id.cancelButton);
        dialogcancel.setText("Cancel");
        dialogcancel.setVisibility(View.GONE);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });

        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLocationChanged(Location location) {
        Objects.location.Lat = location.getLatitude() + "";
        Objects.location.Lng = location.getLongitude() + "";
        Log.d("location", Objects.location.Lat + " " + Objects.location.Lng);
        //    Toast.makeText(BaseActivity.this, "Objects: " + Objects.location.Lat + " " + Objects.location.Lng + " ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void getToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Error", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        Token = task.getResult().getToken();

                        Toast.makeText(BaseActivity.this, Token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void group_list() {
        DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("Groups");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                group_list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    group_list.add(postSnapshot.getKey().toString());
                    // here you can access to name property like university.name
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


}
