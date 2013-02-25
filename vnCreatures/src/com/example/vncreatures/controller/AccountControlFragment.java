package com.example.vncreatures.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class AccountControlFragment extends Fragment {

    private ProfilePictureView mProfilePictureView;
    private ListView mListView;
    AQuery aQuery;

    public AccountControlFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_control_layout, null);
        aQuery = new AQuery(view);
        mProfilePictureView = (ProfilePictureView) view
                .findViewById(R.id.profile_pic);
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
    }
}
