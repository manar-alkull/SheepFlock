package com.example.sheepflock.system;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.NotificationCompat;

import com.example.sheepflock.R;


public class SystemServices {
   // String channelId="1122";
  /*  public void notify(Context context, Notification notification) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context,channelId)
                        .setSmallIcon(R.drawable.app_on)
                        .setContentTitle(notification.Header)
                        .setContentText(notification.Text);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }*/

    //private NotificationCompat.Builder notification_builder;
    public static void notifyWithSound(Context context, String notificationText, String notificationHeader, Intent open_activity_intent) {
        NotificationCompat.Builder builder=notify(context,notificationText,notificationHeader,open_activity_intent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
    }
    public static NotificationCompat.Builder notify(Context context, String notificationText, String notificationHeader, Intent open_activity_intent) {
        PendingIntent pending_intent = PendingIntent
                .getActivity(context, 0, open_activity_intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return notify_pending(context,notificationText,notificationHeader,pending_intent);
    }

    public static NotificationCompat.Builder notify_pending(Context context, String notificationText, String notificationHeader, PendingIntent pending_intent) {
        //Intent open_activity_intent = new Intent(context, NotificationActivity.class);

        NotificationManager notification_manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder  notification_builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "sheepsAPI";
            CharSequence name = "sheepsAPI";
            String description = "sheepsAPI Chanel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notification_manager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(context, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(context);
        }
        notification_builder.setSmallIcon(R.drawable.sheepnotifiy)
                .setContentTitle(notificationHeader)
                .setContentText(notificationText)
                .setAutoCancel(true)
                .setContentIntent(pending_intent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification_builder.build());
        return notification_builder;
    }

    public static void  doAfterPeriod(Runnable runnable, int periodSec){
        new Handler().postDelayed(runnable,periodSec*1000);
    }
}
