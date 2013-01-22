package com.example.vncreatures.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.TabsAdapter;
import com.example.vncreatures.model.CategoryModel;
import com.example.vncreatures.model.NewsModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.NewsCallback;

public class NewsTabsPagerActivity extends AbstractFragmentActivity {
	TabHost mTabHost;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.news_tabs_layout);

		// init Tabs
		initTabs();

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	protected void initTabs() {
		mTabHost = (TabHost) findViewById(R.id.news_tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.news_pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		HrmService service = new HrmService();
		service.setCallback(new NewsCallback() {

			@Override
			public void onGetCatSuccess(CategoryModel catModel) {
				for (int i = 0; i < catModel.count(); i++) {
				    setSupportProgressBarIndeterminateVisibility(false);

					TabSpec tabSpec = mTabHost.newTabSpec(catModel.get(i)
							.getCatName());
					tabSpec.setIndicator(catModel.get(i).getCatName(), null);
					Bundle bundle = new Bundle();
					bundle.putString(Common.CAT_EXTRA, catModel.get(i).getCatId());
					mTabsAdapter.addTab(tabSpec, NewsContentActivity.class,
							bundle);
				}
			}

			@Override
			public void onError() {

			}

			@Override
			public void onGetNewsSuccess(NewsModel newsModel) {
				// DO NOTHING
			}
		});
		service.requestGetCategory();
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	@Override
	protected View createView() {
		LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return li.inflate(R.layout.news_tabs_layout, null);
	}

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabNews_button;
    }
}
