package com.example.vncreatures.controller;

import java.io.UTFDataFormatException;

import com.example.vncreatures.common.Common;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.rest.HrmService.Callback;

import com.example.vncreatures.R;
import com.example.vncreatures.rest.HrmService;

import android.R.string;
import android.app.Service;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatureActivity extends AbstracActivity implements
		OnClickListener {
	
	private ImageView mCreatureImageView = null;
	private TextView mVietName = null;
	private TextView mLatinName = null;
	private WebView mCreatureDesWebview = null;

	private String mCreatureId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creature_description);
		
		try {
			Bundle extras = getIntent().getExtras();
			if (extras !=  null) {
				mCreatureId =  extras.getString(Common.CREATURE_EXTRA);
			}
		} catch (Exception e) {
		}
		
		initUI();
	}
	
	protected boolean initUI() {
		mCreatureImageView = (ImageView) findViewById(R.id.creatureList_imageView);
		mVietName = (TextView) findViewById(R.id.vietName_textview);
		mLatinName = (TextView) findViewById(R.id.latinName_textview);
		mCreatureDesWebview = (WebView) findViewById(R.id.creatureDes_webview);
		
		getCreatureById();
		return true;
	}
	
	protected void getCreatureById() {
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				Creature creature = creatureModel.get(0);
				mVietName.setText(creature.getvName());
				mLatinName.setText(creature.getLatin());
				mCreatureDesWebview.loadData(creature.getDescription(), "text/html", "");
			}

			@Override
			public void onError() {
				
			}			
		});
		service.requestCreaturesById(mCreatureId);
	}

	@Override
	public void onClick(View v) {

	}

}
