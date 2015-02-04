package com.example.bhargavbandla.yamba;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;


public class StatusActivity extends ActionBarActivity {
    Button buttonUpdate;
    EditText editTextStatus;
    static final String TAG = "StatusActivity";

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

    }


    public void onClick(View v) {
        final String statusText = editTextStatus.getText().toString();
        new PostToTwitter().execute(statusText);
        Log.d(TAG, "on Clicked Text:" + statusText);
    }



    class PostToTwitter extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                ((YambaApp)getApplication()).getTwitter().setStatus(params[0]);
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
