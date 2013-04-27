package com.vncreatures.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.vncreatures.R;
import com.vncreatures.common.Common;
import com.vncreatures.customItems.EndlessScrollListener;
import com.vncreatures.customItems.MyExceptionHandler;
import com.vncreatures.customItems.NewsListAdapter;
import com.vncreatures.customItems.NewsListAdapter.Callback;
import com.vncreatures.model.CategoryModel;
import com.vncreatures.model.NewsItem;
import com.vncreatures.model.NewsModel;
import com.vncreatures.rest.HrmService;
import com.vncreatures.rest.HrmService.NewsCallback;
import com.vncreatures.view.NewsListView;

public class NewsContentActivity extends SherlockFragment {

    private String mCatId = null;
    private NewsListAdapter mAdapter = null;
    private NewsListView mView = null;
    private NewsModel mNewsModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        getSherlockActivity().getSupportActionBar().setTitle(R.string.news);

        // GET FROM EXTRAS
        Bundle extras = new Bundle();
        if (savedInstanceState != null) {
            extras = savedInstanceState;
        } else {
            extras = this.getArguments();
        }
        mCatId = extras.getString(Common.CAT_EXTRA);
        mView = new NewsListView(getActivity());
        initNewsList();

        mView.mNewsListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                initNewsList();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Common.CAT_EXTRA, mCatId);
    }

    private void dataListInit() {
        mAdapter = new NewsListAdapter(getActivity(), mNewsModel);
        mView.mNewsListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setCallback(new Callback() {

            @Override
            public void onClick(NewsItem newsItem) {
                Intent intent = new Intent(Common.ACTION_EXTRA, null,
                        getSherlockActivity(), NewsDetailActivity.class);
                intent.putExtra(Common.NEWS_EXTRA, newsItem.getId());
                startActivityForResult(intent,
                        Common.CREATURE_ACTIVITY_REQUEST_CODE);
            }
        });

        mView.mNewsListView
                .setOnScrollListener(new EndlessScrollListener.EndlessScrollNewsListener(
                        mAdapter, mCatId));
    }

    private void initNewsList() {
        HrmService service = new HrmService();
        service.setCallback(new NewsCallback() {

            @Override
            public void onGetNewsSuccess(NewsModel newsModel) {
                getSherlockActivity()
                        .setSupportProgressBarIndeterminateVisibility(false);
                if (newsModel != null && newsModel.count() > 0) {

                    mNewsModel = newsModel;
                    dataListInit();
                    mView.mNewsListView.onRefreshComplete();
                }
            }

            @Override
            public void onGetCatSuccess(CategoryModel catModel) {

            }

            @Override
            public void onError() {
                Log.d("Error", "Not found");
            }
        });
        service.requestGetNews(mCatId, "1");
        getSherlockActivity()
                .setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return mView;
    }
}
