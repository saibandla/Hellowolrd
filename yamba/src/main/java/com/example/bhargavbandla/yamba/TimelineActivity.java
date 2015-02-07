package com.example.bhargavbandla.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by BhargavBandla on 25/01/15.
 */
@SuppressWarnings("deprecation")
public class TimelineActivity extends ActionBarActivity {
    static ListView list;
    Cursor cursor;

    TimelineReciever reciever;
    static final String[] FROM = {StatusData.C_USER, StatusData.C_TEXT, StatusData.C_CREATED_AT};
    static final int[] TO = {R.id.textUserName, R.id.textUserText, R.id.textTime};
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        YambaApp app = (YambaApp) getApplication();
        list = (ListView) findViewById(R.id.list);
        cursor = app.statusData.query();
        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
        adapter.setViewBinder(VIEW_BINDER);

//        ((TextView)list.getEmptyView()).setText("Sorry");
        list.setAdapter(adapter);
    }

    static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() != R.id.textTime)
                return false;

            long time = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
            TextView textTime = (TextView) view;
            textTime.setText(relativeTime);
            return true;

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciever);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reciever == null)
            reciever = new TimelineReciever();
        registerReceiver(reciever, new IntentFilter(YambaApp.ACTION_NEW_STATUS));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intentService = new Intent(getBaseContext(), UpdaterService.class);
        Intent intentRefresh = new Intent(this, RefreshService.class);
        Intent intentPreferences = new Intent(getBaseContext(), PrefsActivity.class);
        switch (id) {
            case R.id.action_start_service:
                startService(intentService);
                return true;
            case R.id.action_stop_service:
                stopService(intentService);
                return true;
            case R.id.action_refresh_service:
                startService(intentRefresh);
                return true;
            case R.id.action_prefs_activity:
                startActivity(intentPreferences);
                return true;
            case R.id.action_status_update:
                startActivity(new Intent(getBaseContext(), StatusActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class TimelineReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            cursor = ((YambaApp) getApplication()).statusData.query();
            adapter.changeCursor(cursor);

            Log.d("TimelineReciever", "TimeLineReciever on Recieve changeCursor with Count:" + intent.getIntExtra("count", 0));
        }
    }
}
