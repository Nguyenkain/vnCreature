package com.example.vncreatures.controller;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.BitmapManager;
import com.example.vncreatures.view.ImageViewTouchViewPager;

public class ImageViewFlipperActivity extends AbstracActivity {
	private int mPosition = -1;
	private int mFlag = 0;
	private ArrayList<Bitmap> mCreatureImage = new ArrayList<Bitmap>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creature_image_flipper);

		ImageViewTouchViewPager viewPager = (ImageViewTouchViewPager) findViewById(R.id.view_pager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);

		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mPosition = extras
						.getInt(Common.CREATURE_URL_IMAGES_POSITION_EXTRA);
			}
		} catch (Exception e) {
		}
		mFlag = BitmapManager.INSTANCE.getCreatureArrayBitmap().size();
		if (mPosition != -1) {
			for (int i = mPosition; mFlag > 0; i++) {
				// This will create true position image view and add them to
				// ViewFlipper
				--mFlag;
				if (i + 1 > BitmapManager.INSTANCE.getCreatureArrayBitmap()
						.size()) {
					i = 0;
				}
				mCreatureImage.add(BitmapManager.INSTANCE
						.getCreatureArrayBitmap().get(i));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setTitle(getString(R.string.view_image));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class ImagePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mCreatureImage.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageViewTouch) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = ImageViewFlipperActivity.this;
			ImageViewTouch imageView = new ImageViewTouch(context, null);
			imageView.setImageBitmap(mCreatureImage.get(position));
			imageView.setPadding(10, 0, 10, 0);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

	}
}
