package com.example.vncreatures.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.model.KingdomChooseViewModel;
import com.example.vncreatures.view.KingdomChooseView;

public class KingdomChooseActivity extends AbstractActivity implements
		OnClickListener {
	private KingdomChooseView mView = null;
	private KingdomChooseViewModel mModel = new KingdomChooseViewModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		mView = new KingdomChooseView(this, mModel);

		super.onCreate(savedInstanceState);

		initControl();
		
	}

	@Override
	protected View createView() {
		return mView;
	}

	private void initControl() {
		setTitle(R.string.kindom_choose);
		mModel.animalButton.setOnClickListener(this);
		mModel.plantButton.setOnClickListener(this);
		mModel.insectButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent mainIntent = new Intent(KingdomChooseActivity.this,
				MainActivity.class);
		switch (v.getId()) {
		case R.id.animal_button:
			pref.edit()
					.putString(Common.KINGDOM,
							Common.CREATURE.animal.toString()).commit();
			startActivity(mainIntent);
			break;
		case R.id.plant_button:
			pref.edit()
					.putString(Common.KINGDOM, Common.CREATURE.plant.toString())
					.commit();
			startActivity(mainIntent);
			break;
		case R.id.insect_button:
			pref.edit()
					.putString(Common.KINGDOM,
							Common.CREATURE.insect.toString()).commit();
			startActivity(mainIntent);
			break;

		default:

			break;
		}

	}

	@Override
	protected void onResume() {
		// Transition
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
		super.onResume();
	}

	@Override
	protected int indentifyTabPosition() {
		return R.id.tabHome_button;
	}
}
