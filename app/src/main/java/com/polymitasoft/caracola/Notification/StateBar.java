package com.polymitasoft.caracola.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;

/**
 * Created by asio on 2/24/2017.
 */
public class StateBar {

    public void notificar(Context context_emisor, Class<CaracolaApplication> context_receptor, String titulo, String text, String info, String ticker, String nota) {

        // Patrón de vibración: 1 segundo vibra, 0.5 segundos para, 1 segundo vibra
        long[] pattern = new long[]{0, 2000, 0};
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context_emisor).setSmallIcon(R.drawable.ic_email_black_24dp).setContentTitle(titulo).setContentText(text).setContentInfo(info).setTicker(ticker);

        Intent notIntent = new Intent(context_emisor, context_receptor.getClass());
        PendingIntent contIntent = PendingIntent.getActivity(context_emisor, 0, notIntent, 0);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(defaultSound);
        mBuilder.setVibrate(pattern);
        mBuilder.setLights(Color.RED, 1, 0);

        mBuilder.setContentIntent(contIntent);

        Intent intent = new Intent(context_emisor, context_receptor.getClass());
        intent.setAction(Intent.ACTION_VIEW);

        //        PendingIntent piDismiss = PendingIntent.getActivity(context_emisor, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(nota)).addAction(R.drawable.ic_done_black_24dp, "Confirmar", null).setAutoCancel(true);
        mBuilder.setFullScreenIntent(contIntent, true);
        mBuilder.setAutoCancel(false);

        NotificationManager mNotificationManager = (NotificationManager) context_emisor.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
