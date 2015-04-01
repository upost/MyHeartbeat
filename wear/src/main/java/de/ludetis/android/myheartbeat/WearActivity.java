package de.ludetis.android.myheartbeat;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class WearActivity extends Activity implements HeartbeatService.OnChangeListener {


    private static final String LOG_TAG = "MyHeart";

    private TextView mTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.heartbeat);
                bindService(new Intent(WearActivity.this, HeartbeatService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder binder) {
                        ((HeartbeatService.HeartbeatServiceBinder)binder).setChangeListener(WearActivity.this);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                }, Service.BIND_AUTO_CREATE);
            }
        });
    }


    @Override
    public void onValueChanged(int newValue) {
        mTextView.setText(Integer.toString(newValue));
    }
}
