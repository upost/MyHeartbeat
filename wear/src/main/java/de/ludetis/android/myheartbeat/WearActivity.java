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
        // inflate layout depending on watch type (round or square)
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // as soon as layout is there...
                mTextView = (TextView) stub.findViewById(R.id.heartbeat);
                // bind to our service.
                bindService(new Intent(WearActivity.this, HeartbeatService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder binder) {
                        Log.d(LOG_TAG, "connected to service.");
                        // set our change listener to get change events
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onValueChanged(int newValue) {
        // will be called by the service whenever the heartbeat value changes.
        mTextView.setText(Integer.toString(newValue));
    }
}
