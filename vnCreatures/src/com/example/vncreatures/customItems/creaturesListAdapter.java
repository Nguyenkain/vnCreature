package com.example.vncreatures.customItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vncreatures.R;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureModel;

public class CreaturesListAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private CreatureModel mCreatureModel;
	
	public CreaturesListAdapter(Context context, CreatureModel creatureModel) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.creature_list_item, null);
			holder = new ViewHolder();
			//holder.mImageView = (ImageView) convertView.findViewById(R.id.creatureList_imageView);
			holder.mVietName = (TextView) convertView.findViewById(R.id.vietName_textview);
			holder.mLatinName = (TextView) convertView.findViewById(R.id.latinName_textview);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Creature creatureItem = mCreatureModel.get(position);
		//holder.mImageId.setText(creatureItem.getImageId());
		holder.mVietName.setText(creatureItem.getvName());
		holder.mLatinName.setText(creatureItem.getLatin());
		return convertView;
	}
	
	static class ViewHolder {
		TextView mImageId;
		TextView mVietName;
		TextView mLatinName;
	}
}
