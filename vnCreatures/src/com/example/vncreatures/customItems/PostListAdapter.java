package com.example.vncreatures.customItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alhneiti.QuickAction.QuickActionWidget;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;

public class PostListAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private ThreadModel mThreadModel;
    private Callback mCallback = null;
    private SharedPreferences pref = null;

    private Thread mThread;
    private DiscussionQuickAction quickAction;

    private EditText mContentEdit;
    private TextView mContent;

    public interface Callback {
        public void onEditThread(Thread thread, EditText contentEdit,
                TextView content);

        public void onDeleteThread(Thread thread);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public PostListAdapter(Context context, ThreadModel model) {
        super();
        this.mContext = context;
        this.mThreadModel = model;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
        // init QuickAction
        quickAction = new DiscussionQuickAction(mContext);
    }

    @Override
    public int getCount() {
        return mThreadModel.count();
    }

    @Override
    public Object getItem(int position) {
        return mThreadModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ThreadModel getModel() {
        return mThreadModel;
    }

    public void setModel(ThreadModel model) {
        this.mThreadModel = model;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.post_item, null);
            holder.mProfilePic = (ImageView) convertView
                    .findViewById(R.id.avatar_imageView);
            holder.progressBar = (ProgressBar) convertView
                    .findViewById(R.id.progressBar1);
            holder.mContent = (TextView) convertView
                    .findViewById(R.id.post_content_TextView);
            holder.mTime = (TextView) convertView
                    .findViewById(R.id.post_time_textView);
            holder.mUsername = (TextView) convertView
                    .findViewById(R.id.username_textView);
            holder.mContentEdit = (EditText) convertView
                    .findViewById(R.id.post_content_EditText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String userId = pref.getString(Common.USER_ID, null);
        String userName = pref.getString(Common.USER_NAME, null);
        String fbId = pref.getString(Common.FB_ID, null);
        if (userId != null && userName != null && fbId != null) {
            userId = userId.replace("\n", "").replace("\r", "").trim();
            userName = userName.replace("\n", "").replace("\r", "").trim();
            fbId = fbId.replace("\n", "").replace("\r", "").trim();
        }

        final AQuery aQuery = new AQuery(convertView);
        Thread thread = mThreadModel.get(position);
        aQuery.id(holder.mContent).text(thread.getPost_content());
        aQuery.id(holder.mContentEdit).text(thread.getPost_content());
        aQuery.id(holder.mUsername).text(thread.getName());
        aQuery.id(holder.mTime).text(thread.getPost_time_created());
        String url = "http://graph.facebook.com/" + thread.getUser_avatar()
                + "/picture?type=small";
        aQuery.id(holder.mProfilePic)
                .image(url, true, true, 0, R.drawable.no_thumb, null,
                        AQuery.FADE_IN_NETWORK).progress(holder.progressBar);

        // On click event
        final TextView textView = holder.mContent;
        final EditText editText = holder.mContentEdit;
        final Thread newThread = thread;
        final String newUserId = userId;

        convertView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                View view = v.findViewById(R.id.avatar_imageView);
                if (newThread.getUser_id().equalsIgnoreCase(newUserId)) {
                    quickAction.onShowBar(view);
                }

                mThread = newThread;
                mContent = textView;
                mContentEdit = editText;

                quickAction.setCallback(new DiscussionQuickAction.Callback() {

                    @Override
                    public void onQuickActionClicked(QuickActionWidget widget,
                            int position) {
                        switch (position) {
                        case 0:
                            /*
                             * EditText contentEdit = mAQuery.id(R.id
                             * .post_content_EditText) .getEditText(); TextView
                             * content = mAQuery .id(R.id.post_content_TextView)
                             * .getTextView();
                             */
                            /*mContent.setVisibility(View.GONE);
                            mContentEdit.setVisibility(View.VISIBLE);
                            mContentEdit.requestFocus();*/
                            if (mCallback != null) {
                                mCallback.onEditThread(mThread, mContentEdit,
                                        mContent);
                            }
                            break;
                        case 1:
                            if (mCallback != null) {
                                Thread newThread = new Thread();
                                newThread.setPost_id(mThread.getPost_id());
                                newThread.setUser_id(newUserId);
                                mCallback.onDeleteThread(newThread);
                            }
                            break;

                        default:
                            break;
                        }
                    }
                });
                return false;
            }

        });

        return convertView;
    }

    static class ViewHolder {
        ImageView mProfilePic;
        ProgressBar progressBar;
        TextView mUsername;
        TextView mTime;
        TextView mContent;
        EditText mContentEdit;
    }
}
