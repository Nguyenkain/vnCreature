package com.vncreatures.customItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.vncreatures.R;
import com.raptureinvenice.webimageview.image.WebImageView;
import com.vncreatures.common.ServerConfig;
import com.vncreatures.model.CreatureGroup;
import com.vncreatures.model.CreatureGroupListModel;

public class CreaturesGroupsAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private CreatureGroupListModel mCreatureModel;
    private Callback mCallback = null;

    public interface Callback {
        public void onClick(CreatureGroup creatureGroup);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public CreaturesGroupsAdapter(Context context,
            CreatureGroupListModel creatureModel) {
        super();
        this.mContext = context;
        this.mCreatureModel = creatureModel;
    }

    @Override
    public int getCount() {
        return mCreatureModel.count();
    }

    @Override
    public Object getItem(int position) {
        return mCreatureModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public CreatureGroupListModel getCreatureModel() {
        return mCreatureModel;
    }

    public void setCreatureModel(CreatureGroupListModel creatureModel) {
        this.mCreatureModel = creatureModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        WebImageView imageView = null;

        if (convertView == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.group_list_item,
                    null);
            holder.mVietName = (TextView) convertView
                    .findViewById(R.id.vietName_textview);
            holder.mLatinName = (TextView) convertView
                    .findViewById(R.id.latinName_textview);
            holder.mImageView = (WebImageView) convertView
                    .findViewById(R.id.creatureList_imageView);
            holder.mImageContainer = (RelativeLayout) convertView
                    .findViewById(R.id.image_container);
            holder.mProgressBar = (ProgressBar) convertView
                    .findViewById(R.id.progressBar1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CreatureGroup creatureItem = mCreatureModel.get(position);
        String url = String.format(ServerConfig.ICON_PATH,
                creatureItem.getIcon());
        AQuery aQuery = new AQuery(mContext);
        aQuery.id(holder.mImageView).progress(holder.mProgressBar).image(url);
        holder.mVietName.setText(creatureItem.getViet());
        holder.mLatinName.setText(creatureItem.getLatin());
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallback.onClick(creatureItem);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        WebImageView mImageView;
        TextView mVietName;
        TextView mLatinName;
        RelativeLayout mImageContainer;
        ProgressBar mProgressBar;
    }
}
