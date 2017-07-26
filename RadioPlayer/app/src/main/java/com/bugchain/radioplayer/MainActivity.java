package com.bugchain.radioplayer;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bugchain.radioplayer.service.RadioPlayerService;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button btnPlayPause;
    private TextView textStatus;
    private boolean isRadioPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStatus = (TextView)findViewById(R.id.textPlayStatus);
        btnPlayPause = (Button)findViewById(R.id.btnPlayPause);
        btnPlayPause.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getExtras() != null) {
                    String status = intent.getStringExtra(Constants.RADIO_KEY_VALUE);
                    Log.w("Main", status);
                    updateTextStatus(status);
                }
            }
        }, new IntentFilter(Constants.RADIO_KEY_FILTER));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlayPause:
                    if(isRadioPlaying){
                        stopRadioPlayerService();
                        isRadioPlaying = false;
                    }else {
                        isRadioPlaying = true;
                        startRadioPlayerService();
                    }
                    updateUI(isRadioPlaying);
                break;
            default:
                break;
        }
    }

    private void startRadioPlayerService(){
        Intent intentService = new Intent(getApplicationContext(), RadioPlayerService.class);
        startService(intentService);
    }
    private void stopRadioPlayerService(){
        Intent intentService = new Intent(getApplicationContext(), RadioPlayerService.class);
        stopService(intentService);
    }

    private boolean isRadioServiceRunning(){
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceInfo.service.getClassName().equals(RadioPlayerService.class.getName())){
                return true;
            }
        }
        return false;
    }

    private void updateUI(boolean isRadioPlaying){
        if(isRadioPlaying){
            btnPlayPause.setText("Stop");
            textStatus.setText("Play status : playing");
        }else {
            textStatus.setText("Play status : Stop");
            btnPlayPause.setText("Play");
        }
    }

    public void updateTextStatus(String status){
        if(status.equals(Constants.ON_BUFFERING)){
            textStatus.setText("Play status : buffering");
        }else if(status.equals(Constants.ON_PLAYING)){
            textStatus.setText("Play status : playing");
        }
        else if(status.equals(Constants.ON_PREPARE)){
            textStatus.setText("Play status : buffering");
        }else {
            textStatus.setText("Play status : stop");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isRadioPlaying = isRadioServiceRunning();
        updateUI(isRadioPlaying);
    }

}
