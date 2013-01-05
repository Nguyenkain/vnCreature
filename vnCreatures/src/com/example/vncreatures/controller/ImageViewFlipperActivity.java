package com.example.vncreatures.controller;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.BitmapManager;

public class ImageViewFlipperActivity extends Activity implements
		OnGestureListener {
	private ViewFlipper mViewFlipper;
	private int mPosition = -1;
	private int mFlag = 0;
	private GestureDetector mGestureDetector;
	private ArrayList<Bitmap> mCreatureImage = new ArrayList<Bitmap>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creature_image_flipper);

		mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		
		mCreatureImage = BitmapManager.INSTANCE.getCreatureArrayBitmap();
		
		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
//				mUrl = extras
//						.getStringArrayList(Common.CREATURE_URL_IMAGES_EXTRA);
				mPosition = extras
						.getInt(Common.CREATURE_URL_IMAGES_POSITION_EXTRA);
			}
		} catch (Exception e) {
		}
		mFlag = mCreatureImage.size();
		if (mPosition != -1) {
			for (int i = mPosition; mFlag > 0; i++) {
				// This will create dynamic image view and add them to
				// ViewFlipper
				--mFlag;
				if (i + 1 > mCreatureImage.size()) {
					i = 0;
				}
				setFlipperImage(i);
			}
		}
		mGestureDetector = new GestureDetector(this);
	}

	private void setFlipperImage(int position) {
		ImageViewTouch image = new ImageViewTouch(getApplicationContext(), null);
//		BitmapCreatureImage.INSTANCE.loadOriginBitmap(res, image);
		image.setImageBitmap(mCreatureImage.get(position));
		mViewFlipper.addView(image);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		super.dispatchTouchEvent(e);
		return this.mGestureDetector.onTouchEvent(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.i("Fling", "Fling Happened!");
		if (e1.getX() - e2.getX() > 235) {
			this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
					this, R.anim.push_left_out));
			if (this.mViewFlipper.getChildCount() == 1) {
				return true;
			}
			this.mViewFlipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -235) {
			this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
					this, R.anim.push_right_out));
			if (this.mViewFlipper.getChildCount() == 1) {
				return true;
			}
			this.mViewFlipper.showPrevious();
			return true;
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
