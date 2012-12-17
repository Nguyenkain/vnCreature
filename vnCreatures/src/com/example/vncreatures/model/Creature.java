package com.example.vncreatures.model;

public class Creature {
	private String mId = null;
	private String mVName = null;
	private String mLatin = null;
	private String mImageId = null;

	public String getImageId() {
		return mImageId;
	}

	public void setImageId(String imageId) {
		this.mImageId = imageId;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getvName() {
		return mVName;
	}

	public void setvName(String vName) {
		this.mVName = vName;
	}

	public String getLatin() {
		return mLatin;
	}

	public void setLatin(String latin) {
		this.mLatin = latin;
	}

}
