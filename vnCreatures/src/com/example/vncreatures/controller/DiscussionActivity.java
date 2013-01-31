package com.example.vncreatures.controller;

import java.util.TooManyListenersException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.model.LoginViewModel;
import com.example.vncreatures.view.LoginView;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.slidingmenu.lib.SlidingMenu;

public class DiscussionActivity extends AbstractFragmentActivity implements
        OnClickListener {

    private LoginViewModel mModel = new LoginViewModel();
    private LoginView mView;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Transition
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

        mView = new LoginView(this, mModel);

        super.onCreate(savedInstanceState);
        
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            // show home as up so we can toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
        
     // set the Behind View Fragment
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.menu_frame, new AccountControlFragment())
        .commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

        // init session
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback,
                        savedInstanceState);
            }
            else {
                Intent mainIntent = new Intent(this,
                        LoginActivity.class);
                startActivity(mainIntent);
            }
        }
        updateView();
    }

    @Override
    protected View createView() {
        LayoutInflater li = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return li.inflate(R.layout.profile_layout, null);
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.login);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            toggle();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        final AQuery aQuery = new AQuery(this);
        if (session.isOpened()) {
            Request.executeMeRequestAsync(session, new GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        aQuery.id(R.id.username_textView).text(user.getUsername());
                    }
                }
            });
        }
    }

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabDiscussion_button;
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            updateView();
        }
    }

}
