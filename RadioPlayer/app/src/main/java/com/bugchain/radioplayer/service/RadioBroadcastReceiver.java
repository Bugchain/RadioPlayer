package com.bugchain.radioplayer.service;
/*
 * Created by BUG CHAIN on 27/07/2017.
 * ARIP Public Company Limited
 * Bangkok, Thailand
 */

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bugchain.radioplayer.Constants;
import com.bugchain.radioplayer.Utils;

public class RadioBroadcastReceiver extends BroadcastReceiver{

    private static final String TAG = RadioBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG,"action : " + action);

        if(action.equals(Constants.PLAYER_NOTIFICATION_INTENT_CLOSE)){
            // Close radio player notification
            NotificationManager manager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(Constants.RADIO_PLAYER_NOTIFICATION_ID);
            context.stopService(new Intent(context, RadioPlayerService.class));
        }else if(action.equals(Constants.PLAYER_NOTIFICATION_INTENT_PLAY_PAUSE)){
            boolean isPlaying = Utils.checkServiceRunning(context, RadioPlayerService.class);
            if(isPlaying){
                context.stopService(new Intent(context, RadioPlayerService.class));
                Utils.updateRadioPlayerNotification(context, false);
            }else {
                context.startService(new Intent(context, RadioPlayerService.class));
                Utils.updateRadioPlayerNotification(context, true);
            }
        }
    }



}
