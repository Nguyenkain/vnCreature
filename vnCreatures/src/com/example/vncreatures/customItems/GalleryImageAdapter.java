package com.example.vncreatures.customItems;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;
import com.androidquery.util.AQUtility;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Utils;
import com.example.vncreatures.model.Creature;

public class GalleryImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    private Creature mCreature;
    private LayoutInflater mLayoutInflater = null;
    private ArrayList<String> mListImages = null;

    public ArrayList<String> getListImages() {
        return mListImages;
    }

    public void setListImages(ArrayList<String> listImages) {
        this.mListImages = listImages;
    }

    public GalleryImageAdapter(Context context, Creature creature) {
        mCreature = creature;
        mContext = context;
        TypedArray typeArray = context
                .obtainStyledAttributes(R.styleable.GalleryTheme);
        mGalleryItemBackground = typeArray.getResourceId(
                R.styleable.GalleryTheme_android_galleryItemBackground, 0);
        typeArray.recycle();
    }

    public GalleryImageAdapter(Context context, ArrayList<String> listImages) {
        mListImages = listImages;
        mContext = context;
        TypedArray typeArray = context
                .obtainStyledAttributes(R.styleable.GalleryTheme);
        mGalleryItemBackground = typeArray.getResourceId(
                R.styleable.GalleryTheme_android_galleryItemBackground, 0);
        typeArray.recycle();
    }

    public int getCount() {
        if (mCreature != null)
            return mCreature.getCreatureImages().size();
        else
            return mListImages.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Inflate layout
        mLayoutInflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.gallery_item, null);
        }
        final View v = convertView;

        // Test Url
        /*
         * for (String urlTest : mListImages) { if (!Utils.testUrl(urlTest))
         * mListImages.remove(urlTest); }
         */

        // Identify url
        String url = null;
        if (mCreature != null) {
            url = mCreature.getCreatureImages().get(position);
        } else {
            url = mListImages.get(position);
        }

        // Set Image using aquery
        AQuery aQuery = new AQuery(convertView);
        ImageView iv = (ImageView) convertView
                .findViewById(R.id.gallery_item_imageView);
        ProgressBar progressBar = (ProgressBar) convertView
                .findViewById(R.id.progressBar1);

        aQuery.id(iv)
                .progress(progressBar)
                .image(url, true, true, 0, AQuery.GONE, null,
                        AQuery.FADE_IN_NETWORK);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setBackgroundResource(mGalleryItemBackground);

        return convertView;
    }
}
