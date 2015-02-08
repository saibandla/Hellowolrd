package com.example.bhargavbandla.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by BhargavBandla on 25/01/15.
 */
public class RefreshService extends IntentService {
    static final String TAG = "RefreshService";

    public RefreshService() {
        super(TAG);
    }

    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ((YambaApp) getApplication()).pullAndInsert();
        Log.d(TAG, "onHandleIntent");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
