package com.vncreatures.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.vncreatures.R;
import com.vncreatures.customItems.Base64;
import com.vncreatures.model.LoginViewModel;
import com.vncreatures.model.discussion.FacebookUser;
import com.vncreatures.rest.HrmService;
import com.vncreatures.rest.HrmService.PostTaskCallback;
import com.vncreatures.view.LoginView;

public class LoginActivity extends AbstractActivity implements OnClickListener {

    private LoginViewModel mModel = new LoginViewModel();
    private LoginView mView;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private UiLifecycleHelper uiHelper;
    private boolean mHandleLogin = false;
    private View mLoginView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Transition
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

        mView = new LoginView(this, mModel);
        super.onCreate(savedInstanceState);
        mLoginView = findViewById(R.id.login_layout);
        mProgressView = findViewById(R.id.progress_layout);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        // setupUI(findViewById(R.id.layout_parent));

        // init session
        /*
         * Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
         * 
         * Session session = Session.getActiveSession(); if (session == null) {
         * if (savedInstanceState != null) { session =
         * Session.restoreSession(this, null, statusCallback,
         * savedInstanceState);
         * 
         * } if (session == null) { session = new Session(this); } else { Intent
         * mainIntent = new Intent(LoginActivity.this,
         * DiscussionActivity.class);
         * mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         * startActivity(mainIntent); } Session.setActiveSession(session); if
         * (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
         * session.openForRead(new Session.OpenRequest(this) .setPermissions(
         * Arrays.asList("email", "user_hometown",
         * "user_birthday")).setCallback( statusCallback)); } } else { if
         * (!session.isClosed()) { Intent mainIntent = new
         * Intent(LoginActivity.this, DiscussionActivity.class);
         * mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         * startActivity(mainIntent); } }
         */

        // Event
        mModel.loginButton.setOnClickListener(this);
        mModel.anonymousLoginButton.setOnClickListener(this);
        mView.setVisibility(View.VISIBLE);
        mLoginView.setVisibility(View.VISIBLE);
    }

    @Override
    protected View createView() {
        return mView;
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
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
            finish();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.facebookLogin_button:
            loginToFacebook();
            break;
        case R.id.normalLogin_button:
            Intent mainIntent = new Intent(LoginActivity.this,
                    DiscussionActivity.class);
            startActivity(mainIntent);
            break;
        default:
            break;
        }
    }

    private void loginToFacebook() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setPermissions(
                    Arrays.asList("email", "user_hometown", "user_birthday"))
                    .setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
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
            mLoginView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
            String userid = pref.getString(
                    com.vncreatures.common.Common.USER_ID, null);
            if (state == SessionState.OPENING) {
                mHandleLogin = true;
            }
            if (session != null && session.isOpened()) {
                // if the session is already open, try to show the discussion
                // activity
                if (mHandleLogin) {
                    addUser(session);
                    mHandleLogin = false;
                } else {
                    if (userid == null) {
                        addUser(session);
                    } else {
                        addUser(session);
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), state.toString(),
                        Toast.LENGTH_LONG).show();
                mLoginView.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.GONE);
            }
        }
    }

    private void updateUser(Session session) {

        Request.executeMeRequestAsync(session, new GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    Gson gson = new Gson();
                    String json2 = user.getInnerJSONObject().toString();
                    final FacebookUser fb = gson.fromJson(json2,
                            FacebookUser.class);
                    pref.edit()
                            .putString(com.vncreatures.common.Common.USER_NAME,
                                    fb.getName()).commit();
                    pref.edit()
                            .putString(com.vncreatures.common.Common.FB_ID,
                                    fb.getId()).commit();
                    Intent mainIntent = new Intent(LoginActivity.this,
                            DiscussionActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });
    }

    private void addUser(Session session) {
        mView.setVisibility(View.GONE);
        setSupportProgressBarIndeterminateVisibility(true);

        try {
            Request.executeMeRequestAsync(session, new GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        Gson gson = new Gson();
                        String json2 = user.getInnerJSONObject().toString();
                        final FacebookUser fb = gson.fromJson(json2,
                                FacebookUser.class);
                        HrmService service = new HrmService();
                        service.setCallback(new PostTaskCallback() {

                            @Override
                            public void onSuccess(String result) {
                                result = result.replace("\n", "");
                                if (result.equalsIgnoreCase("banned")) {
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setIcon(
                                                    android.R.drawable.ic_dialog_alert)
                                            .setTitle(R.string.login)
                                            .setMessage(R.string.ban_message)
                                            .setPositiveButton(
                                                    R.string.confirm,
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            pref.edit()
                                                                    .remove(com.vncreatures.common.Common.USER_ID)
                                                                    .commit();
                                                            Intent mainIntent = new Intent(
                                                                    LoginActivity.this,
                                                                    DiscussionActivity.class);
                                                            startActivity(mainIntent);
                                                        }
                                                    }).show();
                                } else {
                                    pref.edit()
                                            .putString(
                                                    com.vncreatures.common.Common.USER_ID,
                                                    result).commit();
                                    pref.edit()
                                            .putString(
                                                    com.vncreatures.common.Common.USER_NAME,
                                                    fb.getName()).commit();
                                    pref.edit()
                                            .putString(
                                                    com.vncreatures.common.Common.FB_ID,
                                                    fb.getId()).commit();
                                    setSupportProgressBarIndeterminateVisibility(false);
                                    Intent mainIntent = new Intent(
                                            LoginActivity.this,
                                            DiscussionActivity.class);
                                    mainIntent
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
                        service.requestAddUser(fb);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.exception_message),
                    Toast.LENGTH_SHORT).show();
            restartActivity();
        }

    }

    void restartActivity() {
        finish();
        Intent mIntent = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(mIntent);
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
    }

}
