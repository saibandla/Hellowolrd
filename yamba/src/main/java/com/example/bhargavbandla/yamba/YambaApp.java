package com.example.bhargavbandla.yamba;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

/**
 * Created by BhargavBandla on 25/01/15.
 */
public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Twitter twitter;
    SharedPreferences prefs;

    public static final String ACTION_NEW_STATUS = "com.example.bhargavbandla.yamba.NEW_STATUS";
    public static final String ACTION_REFRESH_SERVICE = "com.example.bhargavbandla.yamba.RefreshServic";
    static final String TAG = "YambaApp";

    @Override
    public void onCreate() {
        super.onCreate();
        //Preferences Stuff
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);


        Log.d(TAG, "onCreateApplicationObject");
    }

    public Twitter getTwitter() {
        if (twitter == null) {
            String username = prefs.getString("username", "");
            String password = prefs.getString("password", "");
            String server = prefs.getString("server", "");

            //Twitter Stuff
            twitter = new Twitter(username, password);
            twitter.setAPIRootUrl(server);
        }
        return twitter;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        twitter = null;
        this.prefs = sharedPreferences;
        sendBroadcast(new Intent("com.yamba.Test"));
        Log.d(TAG, "onSharedPreferenceChanged for key: " + key);
    }

    long lastTimeStampseen = -1;
    long biggestTimestamp = -1;

    public int pullAndInsert() {
        int count = 0;

        try {
            List<Twitter.Status> status = getTwitter().getPublicTimeline();
            for (Twitter.Status status1 : status) {
                getContentResolver().insert(StatusProvider.CONTENT_URI, StatusProvider.statusToValues(status1));
                if (status1.createdAt.getTime() > this.lastTimeStampseen) {
                    count++;
                    Log.d(TAG, String.format("%s : %s", status1.user.name, status1.text));

                    biggestTimestamp = (status1.createdAt.getTime() > biggestTimestamp) ? status1.createdAt.getTime() : biggestTimestamp;
                }

            }
            this.lastTimeStampseen = biggestTimestamp;

        } catch (TwitterException e) {
            e.printStackTrace();
            Log.e(TAG, "Faiiled to pull Timeline");
        }
        if (count > 0) {
            sendBroadcast(new Intent(ACTION_NEW_STATUS).putExtra("count", count));
        }

        return count;
    }
}
