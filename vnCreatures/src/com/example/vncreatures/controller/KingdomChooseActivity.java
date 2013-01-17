package com.example.vncreatures.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.model.KingdomChooseViewModel;
import com.example.vncreatures.view.KingdomChooseView;

public class KingdomChooseActivity extends AbstracActivity implements
		OnClickListener {
	private KingdomChooseView mView = null;
	private KingdomChooseViewModel mModel = new KingdomChooseViewModel();
	SharedPreferences kingdomPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = new KingdomChooseView(this, mModel);
		setContentView(mView);

		initControl();
	}

	private void initControl() {
		setTitle(R.string.kindom_choose);
		kingdomPref = PreferenceManager.getDefaultSharedPreferences(this);
		mModel.animalButton.setOnClickListener(this);
		mModel.plantButton.setOnClickListener(this);
		mModel.insectButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.animal_button:
			kingdomPref.edit().putString(Common.KINGDOM,
					Common.CREATURE.animal.toString()).commit();
			break;
		case R.id.plant_button:
			kingdomPref.edit().putString(Common.KINGDOM,
					Common.CREATURE.plant.toString()).commit();
			break;
		case R.id.insect_button:
			kingdomPref.edit().putString(Common.KINGDOM,
					Common.CREATURE.insect.toString()).commit();
			break;

		default:
			break;
		}
		Intent mainIntent = new Intent(KingdomChooseActivity.this,
				MainActivity.class);
		startActivity(mainIntent);
	}

	@Override
	protected void onResume() {
		// Transition
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		super.onResume();
	}
}
