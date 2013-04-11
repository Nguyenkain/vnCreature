package com.vncreatures.customItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vncreatures.R;
import com.vncreatures.model.discussion.Thread;
import com.vncreatures.model.discussion.ThreadModel;

public class NotificationListAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private ThreadModel mThreadModel;
    private Callback mCallback = null;
    private SharedPreferences pref = null;

    private Thread mThread;

    public interface Callback {
        public void onClicked(Thread thread);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public NotificationListAdapter(Context context, ThreadModel model) {
        super();
        this.mContext = context;
        this.mThreadModel = model;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
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
            convertView = mLayoutInflater.inflate(R.layout.notification_item,
                    null);
            holder.mNotificationThreadTextView = (TextView) convertView
                    .findViewById(R.id.notification_thread);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Thread thread = mThreadModel.get(position);
        /*
         * holder.mNotificationThreadTextView.setText(String.format(
         * mContext.getString(R.string.notification_message),
         * Html.fromHtml("<b><font color='#0000FF'>" + thread.getThread_title()
         * + "</font></b>")));
         */
        holder.mNotificationThreadTextView.setText(thread.getThread_title());

        if (thread.getViewed_status().equalsIgnoreCase("0")) {
            convertView.setBackgroundColor(mContext.getResources().getColor(
                    R.color.noti_back_color));
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onClicked(thread);
                }

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView mNotificationThreadTextView;
    }
}
