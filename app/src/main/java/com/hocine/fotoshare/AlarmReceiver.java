package com.hocine.fotoshare;


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
        Log.d("kaka", "Heho je suis la");
        NotificationHelper notificationHelper = new NotificationHelper(context.getApplicationContext());
        notificationHelper.notify(1, "FotoShare - Notifications", "Venez voir les derniers posts de tes amis" );
    }
}

