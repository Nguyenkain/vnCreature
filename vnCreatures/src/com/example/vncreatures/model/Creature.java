package com.example.vncreatures.model;

public class Creature {
	private String mId = null;
	private String mVName = null;
	private String mLatin = null;
	private String mImageId = null;
	private String mLoai = null;
	private String mHo = null;
	private String mBo = null;
	private String mDescription = null;
	private String mAuthor = null;
	private String mLoaiName = null;

	public String getImageId() {
		return mImageId;
	}

	public String getLoaiName() {
		return mLoaiName;
	}

	public void setLoaiName(String loaiName) {
		this.mLoaiName = loaiName;
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

	public void setLoai(String loai) {
		this.mLoai = loai;
	}

	public String getLoai() {
		return mLoai;
	}

	public void setBo(String bo) {
		this.mBo = bo;
	}

	public String getBo() {
		return mBo;
	}

	public void setHo(String ho) {
		this.mHo = ho;
	}

	public String getHo() {
		return mHo;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setAuthor(String author) {
		this.mAuthor = author;
	}

	public String getAuthor() {
		return mAuthor;
	}

}
