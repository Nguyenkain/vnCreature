package com.vncreatures.model;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Creature {
    private String ID = null;
    private String Viet = null;
    private String Latin = null;
    private String Img = null;
    private String Loai = null;
    private String Description = null;
    private String AuthorName = null;
    private ArrayList<String> mCreatureImages = new ArrayList<String>();

    public Creature() {
        // TODO Auto-generated constructor stub
    }

    public Creature(String mId, String mVName, String mLatin, String mImageId,
            String mKingdom, String mDescription, String mAuthor,
            ArrayList<String> mCreatureImages) {
        super();
        this.ID = mId;
        this.Viet = mVName;
        this.Latin = mLatin;
        this.Img = mImageId;
        this.Loai = mKingdom;
        this.Description = mDescription;
        this.AuthorName = mAuthor;
        this.mCreatureImages = mCreatureImages;
    }

    public String getImageId() {
        return Img;
    }

    public void setImageId(String imageId) {
        this.Img = imageId;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        this.ID = id;
    }

    public String getVName() {
        return Viet;
    }

    public void setVName(String vName) {
        this.Viet = vName;
    }

    public String getLatin() {
        return Latin;
    }

    public void setLatin(String latin) {
        this.Latin = latin;
    }

    public void setKingdom(String kingdom) {
        this.Loai = kingdom;
    }

    public String getKingdom() {
        return Loai;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setAuthor(String author) {
        this.AuthorName = author;
    }

    public String getAuthor() {
        return AuthorName;
    }

    public void setCreatureImages(ArrayList<String> creatureImages) {
        this.mCreatureImages = creatureImages;
    }

    public ArrayList<String> getCreatureImages() {
        return mCreatureImages;
    }
}