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
import com.example.vncreatures.rest.HrmService;

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

	public CreatureModel getCreatureModel() {
		return mCreatureModel;
	}

	public void setCreatureModel(CreatureModel creatureModel) {
		this.mCreatureModel = creatureModel;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		ImageView imageView = null;
		
		mLayoutInflater = LayoutInflater.from(mContext);
		convertView = mLayoutInflater
				.inflate(R.layout.creature_list_item, null);
		holder.mVietName = (TextView) convertView
				.findViewById(R.id.vietName_textview);
		holder.mLatinName = (TextView) convertView
				.findViewById(R.id.latinName_textview);
		imageView = (ImageView) convertView
				.findViewById(R.id.creatureList_imageView);

		convertView.setTag(holder);

		Creature creatureItem = mCreatureModel.get(position);
		holder.mVietName.setText(creatureItem.getvName());
		holder.mLatinName.setText(creatureItem.getLatin());

		// Set Image
		HrmService service = new HrmService();
		service.downloadImages(creatureItem.getId(),
				creatureItem.getLoaiName(), imageView);

		return convertView;
	}

	static class ViewHolder {
		ImageView mImageView;
		TextView mVietName;
		TextView mLatinName;
	}
}
