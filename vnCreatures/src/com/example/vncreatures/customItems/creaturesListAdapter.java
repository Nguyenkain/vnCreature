package com.example.vncreatures.customItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vncreatures.R;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.common.Common.CREATURE;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.rest.HrmService;
import com.raptureinvenice.webimageview.image.WebImageView;

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
		WebImageView imageView = null;

		if (convertView == null) {
			mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.creature_list_item,
					null);
			holder.mVietName = (TextView) convertView
					.findViewById(R.id.vietName_textview);
			holder.mLatinName = (TextView) convertView
					.findViewById(R.id.latinName_textview);
			holder.mImageView = (WebImageView) convertView
					.findViewById(R.id.creatureList_imageView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Creature creatureItem = mCreatureModel.get(position);
		holder.mVietName.setText(creatureItem.getVName());
		holder.mLatinName.setText(creatureItem.getLatin());

		String name = CREATURE.getEnumNameForValue(creatureItem.getKingdom());
		String url = String.format(ServerConfig.IMAGE_PATH, name,
				creatureItem.getImageId());
		/*
		 * holder.mImageView.setImageWithURL( this.mContext,
		 * String.format(ServerConfig.IMAGE_PATH, name,
		 * creatureItem.getImageId()));
		 */

		holder.mImageView.setTag(url);
		BitmapManager.INSTANCE.loadBitmap(url, holder.mImageView, 120, 80);

		// Set Image
		/*
		 * HrmService service = new HrmService();
		 * service.downloadImages(creatureItem.getId(), creatureItem.getLoai(),
		 * imageView);
		 */

		return convertView;
	}

	static class ViewHolder {
		WebImageView mImageView;
		TextView mVietName;
		TextView mLatinName;
	}
}