package com.example.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.activities.LoginActivity;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.privatevanmanagement.utils.Objects.UserID.Globaluser_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        updateUserToken(s);
    }

    private void updateUserToken(String s) {
        DatabaseReference databaseReference = Objects.getFirebaseInstance().getReference().child("Token").child(Globaluser_ID);
        databaseReference.setValue(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            showNofication(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        } catch (Exception e) {
            Log.e("ex", java.util.Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
        Log.d("Msg sent", s);

    }

    private void showNofication(String title, String body) {
        final int NOTIFICATION_ID = 101;
        final String NOTIFICATION_CHANNEL_ID = "channel_id";
        final String CHANNEL_NAME = "JazzCash";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        builder.setContentTitle(title);
        builder.setContentTitle("New Announcment");
        builder.setSmallIcon(R.mipmap.myicon);
        builder.setContentText(body);
        builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(body));
        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.message, "View", pendingIntent);


//        Intent intent = notes.getNoteType() == 1 ? new Intent(mContext, CreateNotesActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) :
//                new Intent(mContext, CreateCheckListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        intent.putExtra("isNotNew", true);
//        intent.putExtra("route", "adapter");
//        intent.putExtra("fromNotify", true);
//        intent.putExtra("userNotes", gson.toJson(notes));
//        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);

    }


}
