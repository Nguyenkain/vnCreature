package com.example.vncreatures.customItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionProvider;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.eventbus.BusProvider;
import com.example.vncreatures.customItems.eventbus.ShowSlideMenuEvent;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;

public class NotificationActionProvider extends ActionProvider {

    private final Context mContext;
    private SharedPreferences mPref;
    private String mUserId;
    private String mThreadId;
    private int mNotiCount;
    private TextView mNoticountTextView;
    private View mNotificationCountLayout;

    public NotificationActionProvider(Context context) {
        super(context);
        this.mContext = context;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mUserId = mPref.getString(Common.USER_ID, null);
        mThreadId = mPref.getString(Common.THREAD_ID, null);
    }

    @Override
    public View onCreateActionView() {
        // Inflate the action view to be shown on the action bar.
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(
                R.layout.notification_action_provider, null);
        ImageButton button = (ImageButton) view
                .findViewById(R.id.notification_button);
        mNoticountTextView = (TextView) view
                .findViewById(R.id.notification_count);
        mNotificationCountLayout = view
                .findViewById(R.id.notificationCount_layout);
        mNotificationCountLayout.setVisibility(View.GONE);
        // Attach a click listener for launching the system settings.

        updateNotification();

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new ShowSlideMenuEvent());
            }
        });
        return view;
    }

    @Override
    public boolean onPerformDefaultAction() {
        return super.onPerformDefaultAction();
    }

    private void updateNotification() {
        HrmService service = new HrmService();
        if (mUserId != null) {
            service.requestGetNotification(mUserId);
            service.setCallback(new ThreadTaskCallback() {

                @Override
                public void onSuccess(ThreadModel threadModel) {
                    mNotiCount = threadModel.countAllNotification();
                    if (mNotiCount != 0) {
                        mNotificationCountLayout.setVisibility(View.VISIBLE);
                        mNoticountTextView.setText(String.valueOf(mNotiCount));
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
