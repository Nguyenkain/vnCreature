package com.vncreatures.controller;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.vncreatures.R;

public class EditPreferences extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    protected void onResume()
    {
        super.onResume();
        addPreferencesFromResource(R.xml.splashscreen_preference);
    }
}
