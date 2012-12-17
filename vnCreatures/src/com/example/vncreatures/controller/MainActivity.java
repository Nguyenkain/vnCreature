package com.example.vncreatures.controller;

import com.example.vncreatures.R;
import com.example.vncreatures.customItems.creaturesListAdapter;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.MainViewModel;
import com.example.vncreatures.view.MainView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

	private MainViewModel mMainViewModel = new MainViewModel();
	private MainView mMainView = null;
	private CreatureModel mCreatureModel = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = new MainView(getApplicationContext(), mMainViewModel);
		setContentView(mMainView);
		initList();
	}

	public void initList() {
		mCreatureModel = new CreatureModel();
		for (int i = 0; i < 10; i++) {
			Creature creature = new Creature();
			creature.setId(String.valueOf(i));
			creature.setvName("Viet Nam name " + i);
			creature.setvName("Latin name " + i);
			mCreatureModel.add(creature);
		}
		
		ListView creatureListView = mMainViewModel.mCreature_listview;
		creaturesListAdapter creaturesListAdapter = new creaturesListAdapter(this, mCreatureModel);
		creatureListView.setAdapter(creaturesListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_view, menu);
		return true;
	}
}
