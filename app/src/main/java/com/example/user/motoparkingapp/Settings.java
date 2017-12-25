package com.example.user.motoparkingapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by User on 24/12/2017.
 */

public class Settings extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
