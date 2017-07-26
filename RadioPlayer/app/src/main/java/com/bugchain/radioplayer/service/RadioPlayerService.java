package com.bugchain.radioplayer.service;
/*
 * Created by BugChain on 7/26/17.
 * chain.chaiyaphat@gmail.com
 */

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bugchain.radioplayer.Constants;

import java.io.IOException;

public class RadioPlayerService extends Service {

    private static final String TAG = RadioPlayerService.class.getSimpleName();

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "On Create");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "On Bind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "On start command");

        startRadio();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On Destroy");
        stopRadio();
    }

    private void startRadio() {
        final String URL = "http://fmone.plathong.net/7010/;stream.mp3";

        stopRadio();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBroadcast(Constants.ON_PREPARE);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setBroadcast(Constants.ON_PLAYING);
                mp.start();
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                setBroadcast(Constants.ON_BUFFERING);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                setBroadcast(Constants.ON_ERROR);
                return false;
            }
        });
        mediaPlayer.prepareAsync();

    }

    private void setBroadcast(String result){
        LocalBroadcastManager.getInstance(getApplicationContext())
               .sendBroadcast(new Intent(Constants.RADIO_KEY_FILTER).putExtra(Constants.RADIO_KEY_VALUE,result));
    }

    private void stopRadio() {

        if (mediaPlayer != null) {
            setBroadcast(Constants.ON_STOP);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


}
