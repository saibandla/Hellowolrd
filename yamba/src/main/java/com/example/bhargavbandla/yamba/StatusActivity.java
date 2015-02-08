package com.example.bhargavbandla.yamba;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import winterwell.jtwitter.TwitterException;


public class StatusActivity extends Activity implements LocationListener {
    Button buttonUpdate;
    EditText editTextStatus;
    static final String TAG = "StatusActivity";
    LocationManager locationManager;
    Location location;

    @Override
    protected void onStop() {
        super.onStop();
//        Debug.stopMethodTracing();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Debug.startMethodTracing("yambaa");
        setContentView(R.layout.activity_status);
        Log.d(TAG, "onClicked with Bundle: " + savedInstanceState);
        editTextStatus = (EditText) findViewById(R.id.editText_status);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Log.d("Location Service","on Location hanged"+location.toString());

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 1000, this);
    }

    public void onClick(View v) {
        final String statusText = editTextStatus.getText().toString();
        new PostToTwitter().execute(statusText);
        Log.d(TAG, "on Clicked Text:" + statusText);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.d("Location Service", "on Location hanged" + location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    class PostToTwitter extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                ((YambaApp) getApplication()).getTwitter().setStatus(params[0]);
                return "Successfully Posted " + params[0];
            } catch (TwitterException e) {
                e.printStackTrace();
                return "Failed to Post " + params[0];
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(StatusActivity.this, s, Toast.LENGTH_LONG).show();
        }
    }
}
