package com.example.vncreatures.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.vncreatures.R;

public class SplashScreen extends Activity {
	// Set the display time, in milliseconds (or extract it out as a configurable parameter)
    private final int SPLASH_DISPLAY_LENGTH = 1500;
 
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
    }
 
    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // Obtain the sharedPreference, default to true if not available
        boolean isSplashEnabled = sp.getBoolean("isSplashEnabled", true);
 
        if (isSplashEnabled)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    //Finish the splash activity so it can't be returned to.
                	SplashScreen.this.finish();
                    // Create an Intent that will start the main activity.
                    Intent mainIntent = new Intent(SplashScreen.this, KingdomChooseActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        else
        {
            // if the splash is not enabled, then finish the activity immediately and go to main.
            finish();
            Intent mainIntent = new Intent(SplashScreen.this, KingdomChooseActivity.class);
            SplashScreen.this.startActivity(mainIntent);
        }
    }
}