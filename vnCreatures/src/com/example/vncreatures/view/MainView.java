package com.example.vncreatures.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.vncreatures.R;
import com.example.vncreatures.model.MainViewModel;

public class MainView extends RelativeLayout {
	
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
		mMainViewModel.mSearch_button = (Button) findViewById(R.id.search_button);
		mMainViewModel.mSearchBox_editText = (EditText) findViewById(R.id.searchBox_editText);
		mMainViewModel.mCreature_listview = (ListView) findViewById(R.id.creatures_lisview);
	}

}
