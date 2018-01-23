package it.cnr.iit.contextlabeler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

class NotificationController {

    private static final String NOTIFICATION_CHANNEL = "ContextLabelerNotifications";

    static void showNotification(Context context, String activityName){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setVibrate(new long[]{0L})
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_stat_price_tag)
                .setSound(null)
                .setContentTitle(activityName)
                .setContentText("Reading data...")
                .setContentIntent(contentIntent);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) notificationManager.notify(1, b.build());
    }

    static void removeNotification(Context context){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) notificationManager.cancelAll();
    }
}
