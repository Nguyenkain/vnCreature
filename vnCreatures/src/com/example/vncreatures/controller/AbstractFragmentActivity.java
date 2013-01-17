package com.example.vncreatures.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;

public abstract class AbstractFragmentActivity extends SherlockFragmentActivity
        implements OnClickListener {

    SharedPreferences pref;
    int tabPosition;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        // Create indicator bar
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        // Action bar
        setTheme(Common.THEME);

        // get preference
        pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        // init view
        setContentView(R.layout.parent_container);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        initTabButton();
        container.addView(createView());
        this.tabPosition = indentifyTabPosition();
        pref.edit().putInt(Common.TAB_PREF, this.tabPosition).commit();
        resetTabState();

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.tabPosition = indentifyTabPosition();
        pref.edit().putInt(Common.TAB_PREF, this.tabPosition).commit();
        resetTabState();
    }

    protected abstract View createView();

    protected abstract int indentifyTabPosition();

    protected void initTabButton() {
        /*
         * Intent mainIntent = new Intent(context, NewsTabsPagerActivity.class);
         * startActivity(mainIntent);
         */

        resetTabState();

        View button = (View) findViewById(R.id.tabHome_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabNews_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabDiscussion_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabsMap_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabNews_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = pref.getInt(Common.TAB_PREF, R.id.tabHome_button);
        switch (v.getId()) {
        case R.id.tabHome_button:
            resetTabState();
            if (v.getId() != id) {
                Intent mainIntent = new Intent(this,
                        KingdomChooseActivity.class);
                startActivity(mainIntent);
            }
            break;
        case R.id.tabNews_button:
            resetTabState();
            if (v.getId() != id) {
                Intent mainIntent = new Intent(this,
                        NewsTabsPagerActivity.class);
                startActivity(mainIntent);
            }
            break;
        case R.id.tabDiscussion_button:
            resetTabState();
            break;
        case R.id.tabsMap_button:
            resetTabState();
            break;

        default:
            break;
        }
    }

    private void resetTabState() {
        int id = pref.getInt(Common.TAB_PREF, R.id.tabHome_button);
        LinearLayout tabControl = (LinearLayout) findViewById(R.id.tab_control);
        for (int i = 0; i < tabControl.getChildCount(); i++) {
            View v = tabControl.getChildAt(i);
            if (v.getId() == id)
                v.setSelected(true);
            else
                v.setSelected(false);
        }
    }
}
