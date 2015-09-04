package com.tapfury.ghostcall.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.tapfury.ghostcall.CallScreen;
import com.tapfury.ghostcall.R;
import com.tapfury.ghostcall.StartScreen;

/**
 * Created by Ynott on 9/2/15.
 */
public class GhostCallListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String notificationType = data.getString("message");
        Log.d("notificationType", notificationType);


        sendNotification(notificationType);

    }

    private void sendNotification(String notificationType) {
        if (notificationType.equals("Incoming call")) {
            Intent intent = new Intent(this, CallScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.gc_notif)
                    .setContentTitle("GhostCall")
                    .setContentText(notificationType)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(R.color.titleblue)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } else {
            Intent intent = new Intent(this, StartScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.gc_notif)
                    .setContentTitle("GhostCall")
                    .setContentText("IT WORKED")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(R.color.titleblue)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
