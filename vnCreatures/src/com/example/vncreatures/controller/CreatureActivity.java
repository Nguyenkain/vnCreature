package com.example.vncreatures.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.example.vncreatures.R;
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
		mCreatureDescriptionView = new CreatureDescriptionView(this,
				mCreatureDescriptionViewModel);
		setContentView(mCreatureDescriptionView);

		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mCreatureId = extras.getString(Common.CREATURE_EXTRA);
			}
		} catch (Exception e) {
		}

		getCreatureById();
		
	}
	
	@Override
	protected void onResume() {
		// Transition
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getString(R.string.detail));
		
		// Inflate your menu.
        getSupportMenuInflater().inflate(R.menu.share_action, menu);

        // Set file with share history to the provider and set the share intent.
        MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        actionProvider.setShareIntent(createShareIntent());

        //XXX: For now, ShareActionProviders must be displayed on the action bar
        // Set file with share history to the provider and set the share intent.
        //MenuItem overflowItem = menu.findItem(R.id.menu_item_share_action_provider_overflow);
        //ShareActionProvider overflowProvider =
        //    (ShareActionProvider) overflowItem.getActionProvider();
        //overflowProvider.setShareHistoryFileName(
        //    ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        //overflowProvider.setShareIntent(createShareIntent());
        
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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