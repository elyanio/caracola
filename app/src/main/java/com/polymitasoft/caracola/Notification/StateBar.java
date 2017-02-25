package com.polymitasoft.caracola.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.polymitasoft.caracola.CaracolaApplication;

/**
 * Created by asio on 2/24/2017.
 */

public class StateBar {

    public void notificar(Context context_emisor, Class<CaracolaApplication> context_receptor, String titulo, String text, String info, String ticker, String nota) {


        // Patrón de vibración: 1 segundo vibra, 0.5 segundos para, 1 segundo vibra
        long[] pattern = new long[]{0, 1000, 0};
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context_emisor).setSmallIcon(android.R.drawable.sym_action_email).setContentTitle(titulo).setContentText(text).setContentInfo(info).setTicker(ticker);

        Intent notIntent = new Intent(context_emisor, context_receptor.getClass());
        PendingIntent contIntent = PendingIntent.getActivity(context_emisor, 0, notIntent, 0);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(defaultSound);
        mBuilder.setVibrate(pattern);
        mBuilder.setLights(Color.RED, 1, 0);

        mBuilder.setContentIntent(contIntent);

        Intent intent = new Intent(context_emisor, context_receptor.getClass());
        intent.setAction(Intent.ACTION_VIEW);

        PendingIntent piDismiss = PendingIntent.getActivity(context_emisor, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(nota)).addAction(android.R.drawable.sym_action_chat, "Responder", piDismiss).addAction(android.R.drawable.sym_action_call, "Ignorar", null).setAutoCancel(true);
        mBuilder.setFullScreenIntent(contIntent, true);

        NotificationManager mNotificationManager = (NotificationManager) context_emisor.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
