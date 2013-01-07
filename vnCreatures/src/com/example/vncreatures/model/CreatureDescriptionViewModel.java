package com.example.vncreatures.model;

import com.markupartist.android.widget.ActionBar;

import android.webkit.WebView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatureDescriptionViewModel extends AbstractModel {
	private ImageView mCreatureImageView = null;
	private TextView mVietName = null;
	private TextView mLatinName = null;
	private WebView mCreatureDesWebview = null;
	public ActionBar actionbar = null;
	private Gallery mGalleryImage = null;

	public WebView getCreatureDesWebview() {
		return mCreatureDesWebview;
	}

	public void setCreatureDesWebview(WebView creatureDesWebview) {
		this.mCreatureDesWebview = creatureDesWebview;
	}

	public TextView getVietName() {
		return mVietName;
	}

	public void setVietName(TextView vietName) {
		this.mVietName = vietName;
	}

	public TextView getLatinName() {
		return mLatinName;
	}

	public void setLatinName(TextView latinName) {
		this.mLatinName = latinName;
	}

	public ImageView getCreatureImageView() {
		return mCreatureImageView;
	}

	public void setCreatureImageView(ImageView creatureImageView) {
		this.mCreatureImageView = creatureImageView;
	}

	public void setGalleryImage(Gallery galleryImage) {
		this.mGalleryImage = galleryImage;
	}

	public Gallery getGalleryImage() {
		return mGalleryImage;
	}

}
