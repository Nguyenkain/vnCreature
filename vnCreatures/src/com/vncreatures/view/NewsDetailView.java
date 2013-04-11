package com.vncreatures.view;

import com.androidquery.AQuery;
import com.vncreatures.R;
import com.vncreatures.model.NewsViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.Gallery;

public class NewsDetailView extends AbstractView {

    private AQuery mAQueryView = null;
    private NewsViewModel mNewsViewModel = null;

    public NewsDetailView(Context context, NewsViewModel model) {
        super(context);
        LayoutInflater li = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.news_detail, this);
        mAQueryView = new AQuery(context);
        this.mNewsViewModel = model;

        initUI();
    }

    private void initUI() {
        mNewsViewModel.newsWebView = (WebView) findViewById(R.id.newsDetail_webview);
        mNewsViewModel.newsGallery = (Gallery) findViewById(R.id.newsImage_gallery);
    }

    public AQuery getaQueryView() {
        return mAQueryView;
    }

}
