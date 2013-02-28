package com.example.vncreatures.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.NotificationListAdapter;
import com.example.vncreatures.customItems.NotificationListAdapter.Callback;
import com.example.vncreatures.customItems.eventbus.BusProvider;
import com.example.vncreatures.customItems.eventbus.NotificationUpdateEvent;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.squareup.otto.Subscribe;

public class AccountControlFragment extends Fragment {

    private ProfilePictureView mProfilePictureView;
    private NotificationListAdapter adapter;
    private ListView mListView;
    AQuery aQuery;
    SharedPreferences pref;

    public AccountControlFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_control_layout, null);
        aQuery = new AQuery(view);
        mProfilePictureView = (ProfilePictureView) view
                .findViewById(R.id.profile_pic);

        // get preference
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initUI();
        return view;
    }

    private void initUI() {
        aQuery.id(R.id.logout_button).clicked(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Session session = Session.getActiveSession();
                if (!session.isClosed()) {
                    session.closeAndClearTokenInformation();
                    Intent mainIntent = new Intent(getActivity(),
                            LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        });

        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            Request.executeMeRequestAsync(session, new GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    mProfilePictureView.setProfileId(user.getId());
                    aQuery.id(R.id.profile_username_textview).text(
                            user.getName());
                }
            });
        }

        initList();
    }

    private void initList() {
        String userId = pref.getString(Common.USER_ID, null);
        mListView = aQuery.id(R.id.notification_listView).getListView();
        aQuery.id(R.id.notification_expandLayout).gone();
        aQuery.id(R.id.progressBar1).visible();
        ThreadModel model = new ThreadModel();
        adapter = new NotificationListAdapter(getActivity(), model);
        mListView.setAdapter(adapter);
        HrmService service = new HrmService();
        if (userId != null) {
            service.requestGetNotification(userId);
            service.setCallback(new ThreadTaskCallback() {

                @Override
                public void onSuccess(ThreadModel threadModel) {
                    adapter.setModel(threadModel);
                    aQuery.id(R.id.notification_overall).text(String.format(getString(R.string.notification_overall), threadModel.countAllNotification()));
                    adapter.notifyDataSetChanged();
                    aQuery.id(R.id.notification_expandLayout).visible();
                    aQuery.id(R.id.progressBar1).gone();
                }

                @Override
                public void onError() {

                }
            });
        }

        adapter.setCallback(new Callback() {

            @Override
            public void onClicked(Thread thread) {
                pref.edit().putString(Common.THREAD_ID, thread.getThread_id())
                        .commit();
                Fragment fragment = new ThreadDetailFragment();
                switchFragment(fragment);
            }
        });

    }

    // the meat of switching the above fragmen
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof DiscussionActivity) {
            DiscussionActivity dca = (DiscussionActivity) getActivity();
            dca.switchContent(fragment);
        }
    }
    
    @Subscribe
    public void onNotificationUpdate(NotificationUpdateEvent event) {
        initList();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
