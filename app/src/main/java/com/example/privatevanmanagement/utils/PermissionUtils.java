package com.example.privatevanmanagement.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.M)
public class PermissionUtils {

    // TODO: handle never ask use case
    // https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en

    // region PERMISSION_CONSTANTS
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1199;
    // calendar
    public static final int PERMISSION_READ_CALENDAR = 1;
    public static final int PERMISSION_WRITE_CALENDAR = 2;
    // camera
    public static final int PERMISSION_CAMERA = 3;
    // contacts
    public static final int PERMISSION_GET_ACCOUNTS = 4;
    public static final int PERMISSION_READ_CONTACTS = 5;
    public static final int PERMISSION_WRITE_CONTACTS = 6;
    // location
    public static final int PERMISSION_LOCATION = 7;
    // microphone
    public static final int PERMISSION_RECORD_AUDIO = 8;
    // phone
    public static final int PERMISSION_READ_PHONE_STATE = 9;
    public static final int PERMISSION_CALL_PHONE = 10;
    public static final int PERMISSION_READ_CALL_LOG = 11;
    public static final int PERMISSION_WRITE_CALL_LOG = 12;
    public static final int PERMISSION_ADD_VOICE_EMAIL = 13;
    public static final int PERMISSION_USE_SIP = 14;
    public static final int PERMISSION_PROCESS_OUTGOING_CALLS = 15;
    // sensors
    public static final int PERMISSION_BODY_SENSORS = 16;
    // sms
    public static final int PERMISSION_SEND_SMS = 17;
    public static final int PERMISSION_RECEIVE_SMS = 18;
    public static final int PERMISSION_READ_SMS = 19;
    public static final int PERMISSION_RECEIVE_WAP_PUSH = 20;
    public static final int PERMISSION_RECEIVE_MMS = 21;
    // storage
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 22;
    public static final int PERMISSION_EXTERNAL_STORAGE = 23;
    // endregion


    public static boolean hasPermissionGranted(Context context, String[] permissions) {
        boolean hasGranted = false;
        for (String permission : permissions) {
            hasGranted = (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
            if (!hasGranted)
                return false;
        }
        return hasGranted;
    }

    public static boolean shouldShowPermissionRationale(Activity activity, String[] permissions) {
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissions) {
            shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (!shouldShowRequestPermissionRationale)
                return false;
        }
        return shouldShowRequestPermissionRationale;
    }

    public static void requestPermission(Object object, int permissionId, String[] permission) {
        if (object instanceof Activity)
            ActivityCompat.requestPermissions((Activity) object, permission, permissionId);
        else {
            Fragment fragment = ((Fragment) object);
            if (fragment.isAdded() && fragment.getActivity() != null)
                fragment.requestPermissions(permission, permissionId);
        }
    }

    public static boolean verifyPermission(int[] grantResults) {
        // at least one result must be checked.
        if (grantResults.length < 1)
            return false;
        // verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static boolean requestPermission(Context context, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else {
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).requestPermissions(new String[]{permission}, requestCode);
            }
        }
        return false;
    }

    /**
     * Check and ask for disabled permissions
     *
     * @param activity    Activity calling the method
     * @param permissions permissions array needed to be checked
     * @param requestCode request code associated with the request call
     * @return flag specifying permission are enabled or not
     */
    public static boolean checkAndRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        List<String> requiredPerm = new ArrayList<>();
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                requiredPerm.add(permission);

        if (requiredPerm.size() == 0)
            return true;
        String[] mPermission = new String[requiredPerm.size()];
        mPermission = requiredPerm.toArray(mPermission);
        if (mPermission != null)
            activity.requestPermissions(mPermission, requestCode);
        return false;
    }

    public static boolean hasLocationPermissionGranted(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestLocationPermissions(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, getLocationPermissions(), requestCode);
    }

    public static String[] getLocationPermissions() {
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    public static boolean checkPlayServices(Context context) {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode))
                api.getErrorDialog(((Activity) context), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else {
                Toast.makeText(context, "This device is not supported.", Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
            return false;
        }
        return true;
    }
}
