package com.example.vncreatures.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.ThreadListAdapter;
import com.example.vncreatures.customItems.ThreadListAdapter.Callback;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class ThreadFragment extends SherlockFragment {

    private View mView;
    private ThreadListAdapter mAdapter;
    PullToRefreshListView mListView;
    SharedPreferences pref;

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
            frag = new PostThreadFragment();
            break;
        default:
            break;
        }
        if (frag != null) {
            switchFragment(frag);
        }
        return super.onOptionsItemSelected(item);
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
                            pref.edit().putString(Common.THREAD_ID,
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
}
