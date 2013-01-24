package com.example.vncreatures.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
    private String news_id;
    private String title;
    private String short_description;
    private String news_content;
    private String image;

    public NewsItem(String mId, String mTitle, String mDescription,
            String mContent, String mImage) {
        super();
        this.news_id = mId;
        this.title = mTitle;
        this.short_description = mDescription;
        this.news_content = mContent;
        this.image = mImage;
    }

    public String getId() {
        return news_id;
    }

    public void setId(String id) {
        news_id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return short_description;
    }

    public void setDescription(String description) {
        short_description = description;
    }

    public String getContent() {
        return news_content;
    }

    public void setContent(String content) {
        news_content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(news_id);
        dest.writeString(short_description);
        dest.writeString(title);
        dest.writeString(news_content);
        dest.writeString(image);
    }

    public NewsItem(Parcel in) {
        readFromParcel(in);
    }

    public NewsItem() {
        news_id = "";
        short_description = "";
        title = "";
        news_content = "";
        image = "";
    }

    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        news_id = in.readString();
        short_description = in.readString();
        title = in.readString();
        news_content = in.readString();
        image = in.readString();
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
