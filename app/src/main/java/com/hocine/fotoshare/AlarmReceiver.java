package com.hocine.fotoshare;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Classe permettant de mettre en place un broadcasReceiver, qui déclenchera une notification afin d'avertir l'utilisateur
 * de venir faire un tour pour voir les dernières publications de ses amis
 *
 * @author Hocine
 * @version 1.0
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Création d'un broadcastReceiver
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context.getApplicationContext());
        notificationHelper.notify(1, "FotoShare - Notifications", context.getString(R.string.notif_pub));
    }
}

