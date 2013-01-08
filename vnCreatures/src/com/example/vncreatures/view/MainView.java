package com.example.vncreatures.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vncreatures.R;
import com.example.vncreatures.model.MainViewModel;
import com.markupartist.android.widget.PullToRefreshListView;

public class MainView extends AbstractView {
	
	private MainViewModel mMainViewModel = null;

	public MainView(Context context, MainViewModel model) {
		super(context);
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.main_view, this);
		mMainViewModel = model;
		initUI();
	}
	
	private void initUI() {
		/*mMainViewModel.search_button = (Button) findViewById(R.id.search_button);
		mMainViewModel.searchBox_editText = (EditText) findViewById(R.id.searchBox_editText);*/
		mMainViewModel.creature_listview = (PullToRefreshListView) findViewById(R.id.creatures_lisview);
		mMainViewModel.advanceLayout = (LinearLayout) findViewById(R.id.advanced_layout);
		mMainViewModel.familyLayout = (RelativeLayout) findViewById(R.id.ho_layout);
		mMainViewModel.classLayout = (RelativeLayout) findViewById(R.id.lop_layout);
		mMainViewModel.orderLayout = (RelativeLayout) findViewById(R.id.bo_layout);
		mMainViewModel.familyTextview = (TextView) findViewById(R.id.hoChoose_textview);
		mMainViewModel.orderTextView = (TextView) findViewById(R.id.boChoose_textview);
		mMainViewModel.classTextView = (TextView) findViewById(R.id.lopChoose_textview);
		mMainViewModel.familyClearButton = (ImageButton) findViewById(R.id.hoClear_button);
		mMainViewModel.orderClearButton = (ImageButton) findViewById(R.id.boClear_button);
		mMainViewModel.classClearButton = (ImageButton) findViewById(R.id.lopClear_button);
	}

}