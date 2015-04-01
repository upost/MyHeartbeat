package de.ludetis.android.myheartbeat;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * Created by uwe on 01.04.15.
 */
public class HeartbeatService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private int currentValue=-9639;
    private static final String LOG_TAG = "MyHeart";
    private IBinder binder = new HeartbeatServiceBinder();
    private OnChangeListener onChangeListener;

    public interface OnChangeListener {
        void onValueChanged(int newValue);
    }
    public class HeartbeatServiceBinder extends Binder {
        public void setChangeListener(OnChangeListener listener) {
            onChangeListener = listener;
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        boolean res = mSensorManager.registerListener(this, mHeartRateSensor,  SensorManager.SENSOR_DELAY_UI);
        Log.d(LOG_TAG, " sensor registered: " + (res ? "yes" : "no"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Log.d(LOG_TAG," sensor unregistered");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_HEART_RATE && sensorEvent.values.length>0 ) {
            if(currentValue != Math.round(sensorEvent.values[0])) {
                currentValue=Math.round(sensorEvent.values[0]);
                Log.d(LOG_TAG,sensorEvent.sensor.getName() + ": " + currentValue);
                if(onChangeListener!=null)
                    onChangeListener.onValueChanged(currentValue);
            }
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}