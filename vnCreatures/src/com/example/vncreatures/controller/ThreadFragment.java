package com.example.vncreatures.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.ThreadListAdapter;
import com.example.vncreatures.customItems.ThreadListAdapter.Callback;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.PostTaskCallback;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

public class ThreadFragment extends SherlockFragment implements OnClickListener {

    private View mView;
    private ThreadListAdapter mAdapter;
    private Dialog mComposeWindow;
    PullToRefreshListView mListView;
    SharedPreferences pref;
    
    //Post Thread Item
    Validator validator;    
    @Required(order = 1, message = Common.TITLE_MESSAGE)
    @TextRule(order = 4, minLength = 8, message = Common.MINLENGTH_MESSAGE)
    private EditText mTitleEditText = null;

    @Required(order = 2, message = Common.CONTENT_MESSAGE)
    @TextRule(order = 3, minLength = 8, message = Common.MINLENGTH_MESSAGE)
    private EditText mContentEditText = null;

    public ThreadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.thread_layout, null);

        // get preference
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initLayout();
        initList();
        setHasOptionsMenu(true);
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate menu
        getSherlockActivity().getSupportMenuInflater().inflate(
                R.menu.dicussion_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment frag = null;
        switch (item.getItemId()) {
        case R.id.menu_item_refresh:
            initList();
            break;
        case R.id.menu_item_post:
            // frag = new PostThreadFragment();
            initPopupWindow();
            break;
        default:
            break;
        }
        if (frag != null) {
            switchFragment(frag);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    private void initPopupWindow() {
       
        try {
            // We need to get the instance of the LayoutInflater, use the
            // context of this activity
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.compose_layout, null);
            mComposeWindow = new Dialog(getActivity(),
                    R.style.Theme_D1NoTitleDim);

            //init UI
            AQuery aQView = new AQuery(layout);
            mTitleEditText = aQView.id(R.id.title_editText).getEditText();
            mContentEditText = aQView.id(R.id.post_content_EditText).getEditText();
            Button sendButton = aQView.id(R.id.send_button).getButton();
            
            String userName = pref.getString(Common.USER_NAME, null);
            String fbId = pref.getString(Common.FB_ID, null);
            if (userName != null && fbId != null) {
                userName = userName.replace("\n", "").replace("\r", "").trim();
                fbId = fbId.replace("\n", "").replace("\r", "").trim();
            }
            String url = "http://graph.facebook.com/" + fbId
                    + "/picture?type=small";
            aQView.id(R.id.avatar_imageView).image(url).image(url, true, true, 0, R.drawable.no_thumb, null,
                    AQuery.FADE_IN_NETWORK);
            aQView.id(R.id.username_textView).text(userName);
            
            // init validator
            validator = new Validator(this);
            ValidateListener listener = new ValidateListener((View) sendButton);
            validator.setValidationListener(listener);
            
            // init Event
            sendButton.setOnClickListener(this);
            aQView.id(R.id.cancel_button).clicked(this);

            /* blur background */
            WindowManager.LayoutParams lp = mComposeWindow.getWindow()
                    .getAttributes();
            lp.dimAmount = 0.0f;
            lp.gravity = Gravity.CENTER;
            mComposeWindow.getWindow().setAttributes(lp);
            mComposeWindow.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            // set view
            mComposeWindow.setContentView(layout);
            mComposeWindow.setCanceledOnTouchOutside(true);
            mComposeWindow.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mComposeWindow.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof DiscussionActivity) {
            DiscussionActivity dca = (DiscussionActivity) getActivity();
            dca.switchContent(fragment);
        }
    }

    private void initLayout() {
        mListView = (PullToRefreshListView) mView
                .findViewById(R.id.thread_lisview);
        mListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                initList();
            }
        });
    }

    private void initList() {
        HrmService service = new HrmService();
        service.setCallback(new ThreadTaskCallback() {

            @Override
            public void onSuccess(ThreadModel threadModel) {
                if (threadModel != null && threadModel.count() > 0) {
                    mAdapter = new ThreadListAdapter(getActivity(), threadModel);
                    mListView.setAdapter(mAdapter);
                    mListView.onRefreshComplete();
                    getSherlockActivity()
                            .setSupportProgressBarIndeterminateVisibility(false);
                    mAdapter.setCallback(new Callback() {

                        @Override
                        public void onClick(Thread thread) {
                            pref.edit()
                                    .putString(Common.THREAD_ID,
                                            thread.getThread_id()).commit();
                            Fragment fragment = new ThreadDetailFragment();
                            switchFragment(fragment);
                        }
                    });
                }
            }

            @Override
            public void onError() {

            }
        });
        getSherlockActivity()
                .setSupportProgressBarIndeterminateVisibility(true);
        service.requestGetAllThread();
    }
    
    private final class ValidateListener implements ValidationListener {

        private View mView;

        public ValidateListener(View v) {
            this.mView = v;
        }

        @Override
        public void preValidation() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess() {
            postNewThread(mView);
        }

        @Override
        public void onFailure(View failedView, Rule<?> failedRule) {
            String message = failedRule.getFailureMessage();
            mView.setEnabled(true);

            if (failedView instanceof EditText) {
                failedView.requestFocus();
                ((EditText) failedView).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onValidationCancelled() {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.send_button:
            v.setEnabled(false);
            validator.validateAsync();
            break;
        case R.id.cancel_button:
            mComposeWindow.dismiss();
            mComposeWindow = null;
            break;
        default:
            break;
        }
    }
    
    private void postNewThread(final View v) {
        String title = mTitleEditText.getText().toString();
        String content = mContentEditText.getText().toString();
        String userid = pref.getString(Common.USER_ID, null);
        Thread thread = new Thread();
        thread.setThread_title(title);
        thread.setThread_content(content);
        thread.setUser_id(userid);
        if (userid != null) {
            HrmService service = new HrmService();
            service.requestAddThread(thread);
            service.setCallback(new PostTaskCallback() {

                @Override
                public void onSuccess(String result) {
                    v.setEnabled(true);
                    mComposeWindow.dismiss();
                    mComposeWindow = null;
                    initList();
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
