package com.example.vncreatures.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.example.vncreatures.rest.HrmService.PostTaskCallback;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.facebook.widget.ProfilePictureView;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;

public class ThreadDetailFragment extends SherlockFragment implements
        ValidationListener {
    AQuery aQuery;
    Validator validator;
    private String mThreadId = null;
    private ListView mListView = null;
    SharedPreferences pref;

    @Required(order = 1, message = Common.CONTENT_MESSAGE)
    private EditText mPostContent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thread_detail_layout, null);

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

        // init ListView
        mListView = (ListView) view
                .findViewById(R.id.post_listView);
        View threadDetail = inflater.inflate(
                R.layout.thread_detail_footer_header, null);
        mListView.addHeaderView(threadDetail);

        // init UI
        aQuery = new AQuery(threadDetail);
        mPostContent = (EditText) view.findViewById(R.id.post_editText);

        // init validator
        validator = new Validator(this);
        validator.setValidationListener(this);

        // Event
        Button button = (Button) view.findViewById(R.id.post_button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                validator.validateAsync();
            }
        });

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
        if (mThreadId != null) {
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
                        String url = "http://graph.facebook.com/"
                                + thread.getUser_avatar()
                                + "/picture?type=small";
                        aQuery.id(R.id.avatar_imageView).image(url, true, true,
                                0, R.drawable.no_thumb, null,
                                AQuery.FADE_IN_NETWORK);
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
        HrmService service = new HrmService();
        ThreadModel model = new ThreadModel();
        final PostListAdapter adapter = new PostListAdapter(getActivity(),
                model);
        mListView.setAdapter(adapter);
        service.setCallback(new ThreadTaskCallback() {

            @Override
            public void onSuccess(ThreadModel threadModel) {
                if (threadModel != null) {
                    adapter.setModel(threadModel);
                    adapter.notifyDataSetChanged();
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

    private void postNewComment() {
        String content = mPostContent.getText().toString();
        String userid = pref.getString(Common.USER_ID, null);
        Thread thread = new Thread();
        thread.setPost_content(content);
        thread.setUser_id(userid);
        thread.setThread_id(mThreadId);
        if (userid != null) {
            HrmService service = new HrmService();
            service.requestAddPost(thread);
            service.setCallback(new PostTaskCallback() {

                @Override
                public void onSuccess(String result) {
                    mPostContent.setText("");
                    getSherlockActivity()
                            .setSupportProgressBarIndeterminateVisibility(true);
                    initCommentData();
                }

                @Override
                public void onError() {

                }
            });
        }
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
            initData();
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

    @Override
    public void preValidation() {
    }

    @Override
    public void onSuccess() {
        postNewComment();
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
