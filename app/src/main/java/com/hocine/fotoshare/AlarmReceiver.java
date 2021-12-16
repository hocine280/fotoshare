package com.hocine.fotoshare;


import static android.provider.Settings.System.getString;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context.getApplicationContext());
        notificationHelper.notify(1, "FotoShare - Notifications", context.getString(R.string.notif_pub));
    }
}

