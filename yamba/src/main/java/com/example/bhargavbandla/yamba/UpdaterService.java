package com.example.bhargavbandla.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by BhargavBandla on 24/01/15.
 */
public class UpdaterService extends Service {
    static final String TAG = "UpdaterService";
    Boolean flag = true;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        flag = true;
//       final int delay=((YambaApp)getApplication()).prefs.getInt("delay",DELAY);
        new Thread() {
            public void run() {
                try {
                    while (flag) {
                        ((YambaApp) getApplication()).pullAndInsert();


                        int delay = Integer.parseInt(((YambaApp) getApplication()).prefs.getString("delay", "30"));
                        Thread.sleep(delay * 1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        Log.d(TAG, "onDestroy");
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
