package com.example.vncreatures.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.PostTaskCallback;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

public class PostThreadFragment extends SherlockFragment implements
        ValidationListener {

    AQuery aQuery;
    SharedPreferences pref;
    Validator validator;

    @Required(order = 1, message = Common.TITLE_MESSAGE)
    @TextRule(order = 4, minLength = 8, message = Common.MINLENGTH_MESSAGE)
    private EditText mTitleEditText = null;

    @Required(order = 2, message = Common.CONTENT_MESSAGE)
    @TextRule(order = 3, minLength = 8, message = Common.MINLENGTH_MESSAGE)
    private EditText mContentEditText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_new_thread_layout, null);
        aQuery = new AQuery(view);
        setHasOptionsMenu(true);

        // init UI
        mTitleEditText = aQuery.id(R.id.title_editText).getEditText();
        mContentEditText = aQuery.id(R.id.content_editText).getEditText();

        // init validator
        validator = new Validator(this);
        validator.setValidationListener(this);

        // get preference
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState != null) {
            String userid = savedInstanceState.getString(Common.USER_ID, null);
            if (userid != null) {
                pref.edit().putString(Common.USER_ID, userid).commit();
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String userid = pref.getString(Common.USER_ID, null);
        if (userid != null) {
            outState.putString(Common.USER_ID, userid);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate menu
        getSherlockActivity().getSupportMenuInflater().inflate(
                R.menu.post_thread_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment frag = null;
        switch (item.getItemId()) {
        case R.id.menu_item_accept:
            validator.validateAsync();
            break;
        case R.id.menu_item_cancel:
            frag = new ThreadFragment();
            break;
        case android.R.id.home:
            frag = new ThreadFragment();
            break;
        default:
            break;
        }
        if (frag != null) {
            switchFragment(frag);
        }
        return super.onOptionsItemSelected(item);
    }

    private void postNewThread() {
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
                    Fragment frag = new ThreadFragment();
                    switchFragment(frag);
                }

                @Override
                public void onError() {

                }
            });
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

    @Override
    public void preValidation() {

    }

    @Override
    public void onSuccess() {
        postNewThread();
    }

    @Override
    public void onFailure(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidationCancelled() {

    }
}
