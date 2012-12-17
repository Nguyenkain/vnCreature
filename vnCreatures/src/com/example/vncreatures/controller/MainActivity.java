package com.example.vncreatures.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.vncreatures.R;
import com.example.vncreatures.customItems.CreaturesListAdapter;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.MainViewModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.Callback;
import com.example.vncreatures.view.MainView;

public class MainActivity extends AbstracActivity implements OnClickListener {

	private MainViewModel mMainViewModel = new MainViewModel();
	private MainView mMainView = null;
	private CreatureModel mCreatureModel = null;
	private CreaturesListAdapter mCreatureAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = new MainView(this, mMainViewModel);
		setContentView(mMainView);

		// Get All list
		getAllCreatures();
		
		//Search By Name
		Button btn = mMainViewModel.mSearch_button;
		btn.setOnClickListener(this);
	}

	public void initList(CreatureModel creatureModel) {

		ListView creatureListView = mMainViewModel.mCreature_listview;
		mCreatureAdapter = new CreaturesListAdapter(this, creatureModel);
		creatureListView.setAdapter(mCreatureAdapter);
		mCreatureAdapter.notifyDataSetChanged();
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
		service.requestAllCreature("1");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_view, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_button:
			searchByName();
			break;

		default:
			break;
		}
	}

	private void searchByName() {
		String name = mMainViewModel.mSearchBox_editText.getText().toString();
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				showActivityIndicator(false);
				initList(creatureModel);
			}

			@Override
			public void onError() {

			}
		});
		service.requestCreaturesByName(name, "1");
	}
}
