package com.example.vncreatures.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.NewsListAdapter;
import com.example.vncreatures.model.CategoryModel;
import com.example.vncreatures.model.NewsModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.NewsCallback;
import com.example.vncreatures.view.NewsListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class NewsContentActivity extends SherlockFragment {

	private String mCatId = null;
	private NewsListAdapter mAdapter = null;
	private NewsListView mView = null;
	private NewsModel mNewsModel = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.news);
		mView = new NewsListView(getActivity());
		mCatId = this.getArguments().getString(Common.CAT_EXTRA, "");
		initNewsList();
		
		mView.mNewsListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				initNewsList();
			}
		});
	}
	
	private void dataListInit() {
		mAdapter = new NewsListAdapter(getActivity(), mNewsModel);
		mView.mNewsListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void initNewsList() {
		HrmService service = new HrmService();
		service.setCallback(new NewsCallback() {

			@Override
			public void onGetNewsSuccess(NewsModel newsModel) {
				mNewsModel = newsModel;
				dataListInit();
				mView.mNewsListView.onRefreshComplete();
			}

			@Override
			public void onGetCatSuccess(CategoryModel catModel) {

			}

			@Override
			public void onError() {

			}
		});
		service.requestGetNews(mCatId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mView;
	}
}
