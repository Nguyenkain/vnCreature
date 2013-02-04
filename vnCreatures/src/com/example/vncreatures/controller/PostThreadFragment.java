package com.example.vncreatures.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class PostThreadFragment extends SherlockFragment {

    AQuery aQuery;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_new_thread_layout, null);
        aQuery = new AQuery(view);
        setHasOptionsMenu(true);

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
            postNewThread();
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
        String title = aQuery.id(R.id.title_editText).getText().toString();
        String content = aQuery.id(R.id.content_editText).getText().toString();
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
}
