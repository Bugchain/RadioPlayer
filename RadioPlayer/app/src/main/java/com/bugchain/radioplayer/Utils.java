package com.bugchain.radioplayer;
/*
 * Created by BUG CHAIN on 27/07/2017.
 * ARIP Public Company Limited
 * Bangkok, Thailand
 */

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class Utils {

    public static boolean checkServiceRunning(Context context, Class className){
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceInfo.service.getClassName().equals(className.getName())){
                return true;
            }
        }
        return false;
    }


    public static void updateRadioPlayerNotification(Context context, boolean playStatus){

        Intent intentPlayPause = new Intent(Constants.PLAYER_NOTIFICATION_INTENT_PLAY_PAUSE);
        Intent intentOpen = new Intent(context, MainActivity.class);
        intentOpen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intentClose = new Intent(Constants.PLAYER_NOTIFICATION_INTENT_CLOSE);


        PendingIntent playPausePending = PendingIntent.getBroadcast(context, 100, intentPlayPause, 0);
        PendingIntent openPending = PendingIntent.getActivity(context, 101, intentOpen, 0);
        PendingIntent closePending = PendingIntent.getBroadcast(context, 102, intentClose, 0);

        RemoteViews notificationContentView = new RemoteViews(context.getPackageName(), R.layout.player_notification);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher);


        notificationContentView.setImageViewResource(R.id.btnPlayPause, playStatus ? R.drawable.btn_playback_pause : R.drawable.btn_playback_play);
        notificationContentView.setOnClickPendingIntent(R.id.btnPlayPause, playPausePending);
        notificationContentView.setOnClickPendingIntent(R.id.btnClose, closePending);

        Notification notification = notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomBigContentView(notificationContentView)
                .setContentIntent(openPending)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setUsesChronometer(true)
                .setOngoing(true)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT;

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.RADIO_PLAYER_NOTIFICATION_ID, notification);

    }
}
