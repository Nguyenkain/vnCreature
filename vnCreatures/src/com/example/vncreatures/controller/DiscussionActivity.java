package com.example.vncreatures.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.example.vncreatures.R;
import com.example.vncreatures.customItems.NotificationActionProvider;
import com.example.vncreatures.customItems.eventbus.BusProvider;
import com.example.vncreatures.customItems.eventbus.NotificationUpdateEvent;
import com.example.vncreatures.customItems.eventbus.ShowSlideMenuEvent;
import com.example.vncreatures.customItems.wakefulservice.AppListener;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.squareup.otto.Subscribe;

public class DiscussionActivity extends AbstractFragmentActivity implements
        OnClickListener {

    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private UiLifecycleHelper uiHelper;
    private Fragment mContent;
    private MenuItem notificationItem;
    private static Interpolator interp = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t + 1.0f;
        }       
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Service
        WakefulIntentService.scheduleAlarms(new AppListener(), this, false);

        // Transition
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

        super.onCreate(savedInstanceState);
        
        CanvasTransformer transformer = new CanvasTransformer() {
            
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
            }
        };

        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu()
                    .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            // show home as up so we can toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        // set the Above View Fragment
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new ThreadFragment(getApplicationContext());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();

        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new AccountControlFragment())
                .commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
        sm.setBehindCanvasTransformer(transformer);

        // init session
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback,
                        savedInstanceState);
            } else {
                Intent mainIntent = new Intent(this, LoginActivity.class);
                startActivity(mainIntent);
            }
        }

    }

    @Override
    protected View createView() {
        LayoutInflater li = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return li.inflate(R.layout.discussion_layout, null);
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
        uiHelper.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
        Log.d("Pause", "On Destroy Called");
        //BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
        Log.d("Pause", "On Pause Called");
        BusProvider.getInstance().unregister(this);
    }
    
    @Override
    protected void onStop() {
        Log.d("Pause", "On Stop Called");
        //finish();
        super.onStop();
    }
    
    @Override
    protected void onRestart() {
        Log.d("Pause", "On Restart Called");
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle(R.string.discuss);
        // Inflate menu
        getSupportMenuInflater().inflate(R.menu.dicussion_menu, menu);
        notificationItem = menu.findItem(R.id.menu_item_notification);
        NotificationActionProvider notificationActionProvider = new NotificationActionProvider(
                this);
        notificationItem.setActionProvider(notificationActionProvider);
        return super.onCreateOptionsMenu(menu);
    }

    @Subscribe
    public void onNotificationUpdate(NotificationUpdateEvent event) {
        NotificationActionProvider notificationActionProvider = new NotificationActionProvider(
                this);
        notificationItem.setActionProvider(notificationActionProvider);
    }
    
    @Subscribe
    public void onShowSlideMenu(ShowSlideMenuEvent event) {
        getSlidingMenu().showMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_notification:
            getSlidingMenu().showMenu();
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

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabDiscussion_button;
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchContent(final Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).addToBackStack("tag")
                .commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                            .getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
        return ret;
    }

}
