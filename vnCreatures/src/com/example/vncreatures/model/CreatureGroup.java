package com.example.vncreatures.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CreatureGroup implements Parcelable {
    private String ID;
    private String Viet;
    private String Latin;

    public CreatureGroup(String mId, String mVName, String mLatin) {
        super();
        this.ID = mId;
        this.Viet = mVName;
        this.Latin = mLatin;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        this.ID = id;
    }

    public String getViet() {
        return Viet;
    }

    public void setViet(String viet) {
        this.Viet = viet;
    }

    public String getLatin() {
        return Latin;
    }

    public void setLatin(String latin) {
        this.Latin = latin;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Latin);
        dest.writeString(Viet);
    }

    public CreatureGroup(Parcel in) {
        readFromParcel(in);
    }

    public CreatureGroup() {
        ID = "";
        Latin = "";
        Viet = "";
    }

    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        ID = in.readString();
        Latin = in.readString();
        Viet = in.readString();
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