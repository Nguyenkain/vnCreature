package com.example.vncreatures.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureDescriptionViewModel;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.Callback;
import com.example.vncreatures.view.CreatureDescriptionView;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class CreatureActivity extends AbstracActivity implements
		OnClickListener {

	private CreatureDescriptionViewModel mCreatureDescriptionViewModel = new CreatureDescriptionViewModel();
	private CreatureDescriptionView mCreatureDescriptionView = null;

	private String mCreatureId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCreatureDescriptionView = new CreatureDescriptionView(this,
				mCreatureDescriptionViewModel);
		setContentView(mCreatureDescriptionView);

		// Transition
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

		initActionbar();

		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mCreatureId = extras.getString(Common.CREATURE_EXTRA);
			}
		} catch (Exception e) {
		}

		getCreatureById();
	}

	protected void initActionbar() {
		// Init actionbar
		mCreatureDescriptionViewModel.actionbar.setHomeAction(new IntentAction(
				this, MainActivity.createIntent(this),
				R.drawable.ic_title_home_default));
		mCreatureDescriptionViewModel.actionbar.setDisplayHomeAsUpEnabled(true);

		final Action shareAction = new IntentAction(this, createShareIntent(),
				R.drawable.social_share);
		mCreatureDescriptionViewModel.actionbar.addAction(shareAction);
	}

	protected Intent createShareIntent() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_social));
		return Intent.createChooser(intent, "Share");
	}

	protected void getCreatureById() {
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				Creature creature = creatureModel.get(0);
				mCreatureDescriptionView.setContent(creature);
				mCreatureDescriptionViewModel.actionbar
						.setProgressBarVisibility(View.GONE);
			}

			@Override
			public void onError() {

			}
		});
		service.requestCreaturesById(mCreatureId);
		mCreatureDescriptionViewModel.actionbar
				.setProgressBarVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {

	}

}
