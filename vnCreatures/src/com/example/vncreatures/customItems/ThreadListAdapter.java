package com.example.vncreatures.customItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.model.NewsItem;
import com.example.vncreatures.model.NewsModel;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.facebook.widget.ProfilePictureView;

public class ThreadListAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private ThreadModel mThreadModel;
    private Callback mCallback = null;

    public interface Callback {
        public void onClick(Thread thread);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public ThreadListAdapter(Context context, ThreadModel model) {
        super();
        this.mContext = context;
        this.mThreadModel = model;
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
            convertView = mLayoutInflater.inflate(R.layout.thread_item, null);
            holder.mProfilePicture = (ProfilePictureView) convertView.findViewById(R.id.profileAvatar_pic);
            holder.mTitle = (TextView) convertView.findViewById(R.id.topic_title_TextView);
            holder.mTime = (TextView) convertView.findViewById(R.id.time_textView);
            holder.mNumPost = (TextView) convertView.findViewById(R.id.num_of_post);
            holder.mUsername = (TextView) convertView.findViewById(R.id.username_textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AQuery aQuery = new AQuery(convertView);
        final Thread thread = mThreadModel.get(position);
        aQuery.id(holder.mTitle).text(thread.getThread_title());
        aQuery.id(holder.mUsername).text(thread.getName());
        aQuery.id(holder.mTime).text(thread.getThread_created_time());
        String postCount = String.format(mContext.getString(R.string.num_post), thread.getPost_count());
        aQuery.id(holder.mNumPost).text(postCount);
        holder.mProfilePicture.setProfileId(thread.getUser_avatar());
        
        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                mCallback.onClick(thread);
            }
        });
        
        return convertView;
    }

    static class ViewHolder {
        ProfilePictureView mProfilePicture;
        TextView mUsername;
        TextView mTime;
        TextView mTitle;
        TextView mNumPost;
    }
}
