package de.ludetis.android.myheartbeat;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView textView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // message from API client! message from wear! The contents is the heartbeat.
            if(textView!=null)
                textView.setText(Integer.toString(msg.what));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.heartbeat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register our handler with the DataLayerService. This ensures we get messages whenever the service receives something.
        DataLayerListenerService.setHandler(handler);
    }

    @Override
    protected void onPause() {
        // unregister our handler so the service does not need to send its messages anywhere.
        DataLayerListenerService.setHandler(null);
        super.onPause();
    }
}


