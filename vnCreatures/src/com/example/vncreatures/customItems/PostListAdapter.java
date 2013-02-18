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

public class PostListAdapter extends BaseAdapter {
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

    public PostListAdapter(Context context, ThreadModel model) {
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
            convertView = mLayoutInflater.inflate(R.layout.post_item, null);
            holder.mProfilePic = (ImageView) convertView.findViewById(R.id.avatar_imageView);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);
            holder.mContent = (TextView) convertView
                    .findViewById(R.id.post_content_TextView);
            holder.mTime = (TextView) convertView
                    .findViewById(R.id.post_time_textView);
            holder.mUsername = (TextView) convertView
                    .findViewById(R.id.username_textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AQuery aQuery = new AQuery(convertView);
        final Thread thread = mThreadModel.get(position);
        aQuery.id(holder.mContent).text(thread.getPost_content());
        aQuery.id(holder.mUsername).text(thread.getName());
        aQuery.id(holder.mTime).text(thread.getPost_time_created());
        String url = "http://graph.facebook.com/" + thread.getUser_avatar()
                + "/picture?type=small";
        aQuery.id(holder.mProfilePic).image(url, true, true, 0, R.drawable.no_thumb, null,
                AQuery.FADE_IN_NETWORK).progress(holder.progressBar);
        return convertView;
    }

    static class ViewHolder {
        ImageView mProfilePic;
        ProgressBar progressBar;
        TextView mUsername;
        TextView mTime;
        TextView mContent;
    }
}
