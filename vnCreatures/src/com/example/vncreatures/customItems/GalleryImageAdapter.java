package com.example.vncreatures.customItems;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.example.vncreatures.R;
import com.example.vncreatures.model.Creature;

public class GalleryImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	private Creature mCreature;
	
	public GalleryImageAdapter(Context context, Creature creature) {
		mCreature = creature;
		mContext = context;
		TypedArray typeArray = context
				.obtainStyledAttributes(R.styleable.GalleryTheme);
		mGalleryItemBackground = typeArray.getResourceId(
				R.styleable.GalleryTheme_android_galleryItemBackground, 0);
		typeArray.recycle();
	}

	public int getCount() {
		return mCreature.getCreatureImage().size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(mContext);
		iv.setImageBitmap(mCreature.getCreatureImage().get(position));
		iv.setLayoutParams(new Gallery.LayoutParams(270, 200));
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		iv.setBackgroundResource(mGalleryItemBackground);
		return iv;
	}
}