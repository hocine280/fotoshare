package com.hocine.fotoshare;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;


public class NotificationHelper extends ContextWrapper{
    private NotificationManager notifManager;

    private static final String CHANNEL_DEFAULT_ID = "DEFAULT_CHANNEL";
    private static final String CHANNEL_DEFAULT_NAME = "Notifications";


    public NotificationHelper( Context base ) {
        super( base );

        notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long [] swPattern = new long[] { 0, 500, 110, 500, 110, 450, 110, 200, 110,
                170, 40, 450, 110, 200, 110, 170, 40, 500 };

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannelDefault = new NotificationChannel(
                    CHANNEL_DEFAULT_ID, CHANNEL_DEFAULT_NAME, notifManager.IMPORTANCE_DEFAULT);
            notificationChannelDefault.enableVibration(true);
            notificationChannelDefault.setShowBadge(false);
            notificationChannelDefault.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notifManager.createNotificationChannel(notificationChannelDefault);
        }
    }


    public void notify(int id, String title, String message ) {
        Resources res = getApplicationContext().getResources();

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 456, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        title.toUpperCase();
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder( getApplicationContext(), CHANNEL_DEFAULT_ID )
                    .setContentIntent(contentIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo))
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#D3D3D3"))
                    .build();
        }

        notifManager.notify( id, notification );
    }
}
