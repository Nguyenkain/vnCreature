package com.example.vncreatures.controller;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.example.vncreatures.R;
import com.example.vncreatures.customItems.CreaturesListAdapter;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.MainViewModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.Callback;
import com.example.vncreatures.view.MainView;

public class MainActivity extends AbstracActivity {

	private MainViewModel mMainViewModel = new MainViewModel();
	private MainView mMainView = null;
	private CreatureModel mCreatureModel = null;
	private CreaturesListAdapter mCreatureAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = new MainView(this, mMainViewModel);
		setContentView(mMainView);
		
		//Get All list
		getAllCreatures();
	}

	public void initList(CreatureModel creatureModel) {

		ListView creatureListView = mMainViewModel.mCreature_listview;
		mCreatureAdapter = new CreaturesListAdapter(this, creatureModel);
		creatureListView.setAdapter(mCreatureAdapter);
	}

	public void getAllCreatures() {
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				initList(creatureModel);
				showActivityIndicator(false);
			}

			@Override
			public void onError() {

			}
		});
		service.requestAllCreature("1", "5");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_view, menu);
		return true;
	}
}
