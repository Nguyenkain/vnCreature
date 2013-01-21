package com.example.vncreatures.model;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Creature {
	private String mId = null;
	private String mVName = null;
	private String mLatin = null;
	private String mImageId = null;
	private String mKingdom = null;
	private String mDescription = null;
	private String mAuthor = null;
	private ArrayList<Bitmap> mCreatureImage = new ArrayList<Bitmap>();
	private ArrayList<String> mCreatureImages = new ArrayList<String>();
	
	public Creature() {
        // TODO Auto-generated constructor stub
    }
	
	

	public Creature(String mId, String mVName, String mLatin, String mImageId,
            String mKingdom, String mDescription, String mAuthor, ArrayList<String> mCreatureImages) {
        super();
        this.mId = mId;
        this.mVName = mVName;
        this.mLatin = mLatin;
        this.mImageId = mImageId;
        this.mKingdom = mKingdom;
        this.mDescription = mDescription;
        this.mAuthor = mAuthor;
        this.mCreatureImages = mCreatureImages;
    }



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

	public String getVName() {
		return mVName;
	}

	public void setVName(String vName) {
		this.mVName = vName;
	}

	public String getLatin() {
		return mLatin;
	}

	public void setLatin(String latin) {
		this.mLatin = latin;
	}

	public void setKingdom(String kingdom) {
		this.mKingdom = kingdom;
	}

	public String getKingdom() {
		return mKingdom;
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

	public void setCreatureImage(ArrayList<Bitmap> creatureImage) {
		this.mCreatureImage = creatureImage;
	}

	public ArrayList<Bitmap> getCreatureImage() {
		return mCreatureImage;
	}
	
	public void setCreatureImages(ArrayList<String> creatureImages) {
        this.mCreatureImages = creatureImages;
    }
	
	public ArrayList<String> getCreatureImages() {
        return mCreatureImages;
    }
}