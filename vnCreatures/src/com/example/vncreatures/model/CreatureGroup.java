package com.example.vncreatures.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CreatureGroup implements Parcelable {
	private String mId;
	private String mViet;
	private String mLatin;

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getViet() {
		return mViet;
	}

	public void setViet(String viet) {
		this.mViet = viet;
	}

	public String getLatin() {
		return mLatin;
	}

	public void setLatin(String latin) {
		this.mLatin = latin;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mLatin);
		dest.writeString(mViet);
	}

	public CreatureGroup(Parcel in) {
		readFromParcel(in);
	}

	public CreatureGroup() {
		mId = "";
		mLatin = "";
		mViet = "";
	}

	private void readFromParcel(Parcel in) {

		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		mId = in.readString();
		mLatin = in.readString();
		mViet = in.readString();
	}

	public static final Creator<CreatureGroup> CREATOR = new Creator<CreatureGroup>() {
		public CreatureGroup createFromParcel(Parcel in) {
			return new CreatureGroup(in);
		}

		public CreatureGroup[] newArray(int size) {
			return new CreatureGroup[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
