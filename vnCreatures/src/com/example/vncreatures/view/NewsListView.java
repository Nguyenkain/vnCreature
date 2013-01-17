package com.example.vncreatures.view;

import com.example.vncreatures.R;
import com.markupartist.android.widget.PullToRefreshListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class NewsListView extends RelativeLayout {
	
	public PullToRefreshListView mNewsListView = null;
	
	public NewsListView(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.news_layout, this);
		mNewsListView = (PullToRefreshListView) findViewById(R.id.news_lisview);
	}

}
