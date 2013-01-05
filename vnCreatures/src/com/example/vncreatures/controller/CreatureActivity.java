package com.example.vncreatures.controller;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.vncreatures.common.Common;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureDescriptionViewModel;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.Callback;
import com.example.vncreatures.view.CreatureDescriptionView;

public class CreatureActivity extends AbstracActivity implements
		OnClickListener {

	private CreatureDescriptionViewModel mCreatureDescriptionViewModel = new CreatureDescriptionViewModel();
	private CreatureDescriptionView mCreatureDescriptionView = null;
	
	private String mCreatureId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCreatureDescriptionView = new CreatureDescriptionView(this, mCreatureDescriptionViewModel);
		setContentView(mCreatureDescriptionView);
		
		try {
			Bundle extras = getIntent().getExtras();
			if (extras !=  null) {
				mCreatureId =  extras.getString(Common.CREATURE_EXTRA);
			}
		} catch (Exception e) {
		}
		
		getCreatureById();
	}
	
	protected void getCreatureById() {
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				Creature creature = creatureModel.get(0);
				mCreatureDescriptionView.setContent(creature);
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
