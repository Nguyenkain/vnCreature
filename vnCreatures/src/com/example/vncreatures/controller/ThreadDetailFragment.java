package com.example.vncreatures.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.PostListAdapter;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.facebook.widget.ProfilePictureView;

public class ThreadDetailFragment extends SherlockFragment {
    AQuery aQuery;
    private String mThreadId = null;
    private ProfilePictureView mProfilePic = null;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thread_detail_layout, null);
        aQuery = new AQuery(view);
        setHasOptionsMenu(true);
        // get preference
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState != null) {
            String userid = savedInstanceState.getString(Common.USER_ID, null);
            if (userid != null) {
                pref.edit().putString(Common.USER_ID, userid).commit();
                pref.edit().putString(Common.THREAD_ID, mThreadId).commit();
            }
        }
        mThreadId = pref.getString(Common.THREAD_ID, null);
        mProfilePic = (ProfilePictureView) view
                .findViewById(R.id.avatarProfile_pic);
        initData();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String userid = pref.getString(Common.USER_ID, null);
        if (userid != null) {
            outState.putString(Common.USER_ID, userid);
        }
        if(mThreadId != null) {
            outState.putString(Common.THREAD_ID, mThreadId);
        }
    }

    private void initData() {
        HrmService service = new HrmService();
        service.setCallback(new ThreadTaskCallback() {

            @Override
            public void onSuccess(ThreadModel threadModel) {
                if (threadModel != null) {
                    Thread thread = threadModel.get(0);
                    if (thread != null) {
                        aQuery.id(R.id.username_textView)
                                .text(thread.getName());
                        aQuery.id(R.id.topic_title_TextView).text(
                                thread.getThread_title());
                        aQuery.id(R.id.time_textView).text(
                                thread.getThread_created_time());
                        aQuery.id(R.id.content_textView).text(
                                thread.getThread_content());
                        mProfilePic.setProfileId(thread.getUser_avatar());
                    }
                }
            }

            @Override
            public void onError() {

            }
        });
        if (mThreadId != null) {
            service.requestGetThreadById(mThreadId);
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
                    true);
            initCommentData();
        }
    }

    private void initCommentData() {
        final ListView listView = aQuery.id(R.id.post_listView).getListView();
        HrmService service = new HrmService();
        service.setCallback(new ThreadTaskCallback() {

            @Override
            public void onSuccess(ThreadModel threadModel) {
                if (threadModel != null) {
                    PostListAdapter adapter = new PostListAdapter(
                            getActivity(), threadModel);
                    listView.setAdapter(adapter);
                    getSherlockActivity()
                            .setSupportProgressBarIndeterminateVisibility(false);
                }
            }

            @Override
            public void onError() {

            }
        });
        service.requestGetPostByThreadId(mThreadId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate menu
        getSherlockActivity().getSupportMenuInflater().inflate(
                R.menu.detail_menu, menu);
        menu.removeItem(R.id.menu_item_map);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment frag = null;
        switch (item.getItemId()) {
        case R.id.menu_item_refresh:
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
