package com.example.vncreatures.customItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vncreatures.R;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.NewsItem;
import com.example.vncreatures.model.NewsModel;
import com.raptureinvenice.webimageview.image.WebImageView;

public class NewsListAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private NewsModel mNewsModel;
	private Callback mCallback = null;

	public interface Callback {
		public void onClick(CreatureGroup creatureGroup);
	}

	public void setCallback(Callback callback) {
		mCallback = callback;
	}

	public NewsListAdapter(Context context,
			NewsModel newsModel) {
		super();
		this.mContext = context;
		this.mNewsModel = newsModel;
	}

	@Override
	public int getCount() {
		return mNewsModel.count();
	}

	@Override
	public Object getItem(int position) {
		return mNewsModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public NewsModel getNewsModel() {
		return mNewsModel;
	}

	public void setNewsModel(NewsModel newsModel) {
		this.mNewsModel = newsModel;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		ImageView imageView = null;

		if (convertView == null) {
			mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.news_item,
					null);
			holder.mTitle = (TextView) convertView
					.findViewById(R.id.newsTitle_textview);
			holder.mDescription = (TextView) convertView
					.findViewById(R.id.newsDes_textview);
			holder.mImageView = (ImageView) convertView
					.findViewById(R.id.news_imageView);
			holder.mImageContainer = (LinearLayout) convertView
                    .findViewById(R.id.image_container);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final NewsItem newsItem = mNewsModel.get(position);
		holder.mTitle.setText(newsItem.getTitle());
		holder.mDescription.setText(newsItem.getDescription());
		String url = String.format(ServerConfig.NEWS_IMAGE_PATH, newsItem.getImage());
		holder.mImageView.setTag(url);
		BitmapManager.INSTANCE.loadBitmap(url, holder.mImageContainer, 120, 100);
		/*convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCallback.onClick(creatureItem);
			}
		});*/
		

		return convertView;
	}

	static class ViewHolder {
		ImageView mImageView;
		TextView mTitle;
		TextView mDescription;
		LinearLayout mImageContainer;
	}
}