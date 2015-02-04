package com.example.bhargavbandla.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by BhargavBandla on 25/01/15.
 */
public class BootReceiver extends BroadcastReceiver {
    static PendingIntent lastop;
    @Override
    public void onReceive(Context context, Intent intent) {

        long intervel = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(context).getString("delay", "15"));
        PendingIntent operation = PendingIntent.getService(context, -1, new Intent(YambaApp.ACTION_REFRESH_SERVICE), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(lastop);
        if(intervel>0) {
            alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), intervel, operation);
            Log.d("BootReceiver","on Recieve: delay:"+intervel);
        }
        lastop=operation;
//        context.startService(new Intent(context,UpdaterService.class));
        Log.d("BootReceiver", "onReceive");
    }
}
