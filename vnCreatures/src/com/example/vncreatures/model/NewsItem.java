package com.example.vncreatures.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
	private String mId;
	private String mTitle;
	private String mDescription;
	private String mContent;
	private String mImage;

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public String getImage() {
		return mImage;
	}

	public void setImage(String image) {
		mImage = image;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mDescription);
		dest.writeString(mTitle);
		dest.writeString(mContent);
		dest.writeString(mImage);
	}

	public NewsItem(Parcel in) {
		readFromParcel(in);
	}

	public NewsItem() {
		mId = "";
		mDescription = "";
		mTitle = "";
		mContent = "";
		mImage = "";
	}

	private void readFromParcel(Parcel in) {

		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		mId = in.readString();
		mDescription = in.readString();
		mTitle = in.readString();
		mContent = in.readString();
		mImage = in.readString();
	}

	public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
		public NewsItem createFromParcel(Parcel in) {
			return new NewsItem(in);
		}

		public NewsItem[] newArray(int size) {
			return new NewsItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
