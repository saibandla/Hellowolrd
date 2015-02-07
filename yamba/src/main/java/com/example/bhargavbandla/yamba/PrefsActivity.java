package com.example.bhargavbandla.yamba;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by BhargavBandla on 25/01/15.
 */
@SuppressWarnings("ALL")
public class PrefsActivity extends PreferenceActivity {
    static  final String TAG="PrefsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
    }
}
