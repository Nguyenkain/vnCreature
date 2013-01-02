package com.example.vncreatures.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.common.Utils;
import com.example.vncreatures.customItems.CreaturesListAdapter;
import com.example.vncreatures.customItems.EndlessScrollListener;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.MainViewModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.Callback;
import com.example.vncreatures.rest.HrmService.GroupCallback;
import com.example.vncreatures.view.MainView;

public class MainActivity extends AbstracActivity implements OnClickListener {

	private MainViewModel mMainViewModel = new MainViewModel();
	private MainView mMainView = null;
	private CreatureModel mCreatureModel = null;
	private CreaturesListAdapter mCreatureAdapter = null;

	private String mFamilyId = "";
	private String mOrderId = "";
	private String mKingdomId = "";
	private String mClassId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = new MainView(this, mMainViewModel);
		setContentView(mMainView);

		// Get All list
		getAllCreatures();

		// Search By Name
		Button btn = mMainViewModel.search_button;
		btn.setOnClickListener(this);

		// Advance search
		btn = mMainViewModel.advanceButton;
		btn.setOnClickListener(this);

		// Select Family
		RelativeLayout layout = mMainViewModel.familyLayout;
		layout.setOnClickListener(this);

		// Select Order
		layout = mMainViewModel.orderLayout;
		layout.setOnClickListener(this);

		// Select Class
		layout = mMainViewModel.classLayout;
		layout.setOnClickListener(this);

		// Clear Event
		ImageButton iBtn = mMainViewModel.familyClearButton;
		iBtn.setOnClickListener(this);

		iBtn = mMainViewModel.orderClearButton;
		iBtn.setOnClickListener(this);

		iBtn = mMainViewModel.classClearButton;
		iBtn.setOnClickListener(this);
	}

	public void initList(CreatureModel creatureModel) {
		this.mCreatureModel = creatureModel;
		ListView creatureListView = mMainViewModel.creature_listview;
		mCreatureAdapter = new CreaturesListAdapter(this, creatureModel);
		creatureListView.setAdapter(mCreatureAdapter);
		mCreatureAdapter.notifyDataSetChanged();

		creatureListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				Creature creature = mCreatureModel.get(pos);
				Intent intent = new Intent(Common.ACTION_EXTRA, null,
						MainActivity.this, CreatureActivity.class);
				intent.putExtra(Common.CREATURE_EXTRA, creature.getId());
				startActivityForResult(intent,
						Common.CREATURE_ACTIVITY_REQUEST_CODE);
			}
		});
	}

	public void getAllCreatures() {
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				initList(creatureModel);
				showActivityIndicator(false);
				mMainViewModel.creature_listview
						.setOnScrollListener(new EndlessScrollListener(
								mCreatureAdapter));
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
		case R.id.advance_button:
			Utils.toogleLayout(mMainViewModel.advanceLayout);
			break;
		case R.id.ho_layout:
			getFamily();
			break;
		case R.id.bo_layout:
			getOrder();
			break;
		case R.id.lop_layout:
			getClassType();
			break;
		case R.id.hoClear_button:
			clearEvent(true, false, false);
			break;
		case R.id.boClear_button:
			clearEvent(true, true, false);
			break;
		case R.id.lopClear_button:
			clearEvent(true, true, true);
			break;
		default:
			break;
		}
	}

	private void searchByName() {
		final String name = mMainViewModel.searchBox_editText.getText()
				.toString();
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				showActivityIndicator(false);
				initList(creatureModel);

				mMainViewModel.creature_listview
						.setOnScrollListener(new EndlessScrollListener(
								mCreatureAdapter, name, mFamilyId, mOrderId,
								mClassId));
			}

			@Override
			public void onError() {

			}
		});
		service.requestCreaturesByName(name, "1", mFamilyId, mOrderId, mClassId);
	}

	private void getFamily() {
		Intent intent = new Intent(Common.ACTION_CHOOSE_FAMILY, null,
				MainActivity.this, GroupChooseActivity.class);
		intent.putExtra(Common.ORDER_EXTRA, mOrderId);
		intent.putExtra(Common.CLASS_EXTRA, mClassId);
		startActivityForResult(intent, Common.CREATURE_ACTIVITY_REQUEST_CODE);
	}

	private void getOrder() {
		Intent intent = new Intent(Common.ACTION_CHOOSE_ORDER, null,
				MainActivity.this, GroupChooseActivity.class);
		intent.putExtra(Common.FAMILY_EXTRA, mFamilyId);
		intent.putExtra(Common.CLASS_EXTRA, mClassId);
		startActivityForResult(intent, Common.CREATURE_ACTIVITY_REQUEST_CODE);
	}

	private void getClassType() {
		Intent intent = new Intent(Common.ACTION_CHOOSE_CLASS, null,
				MainActivity.this, GroupChooseActivity.class);
		intent.putExtra(Common.ORDER_EXTRA, mOrderId);
		intent.putExtra(Common.FAMILY_EXTRA, mFamilyId);
		startActivityForResult(intent, Common.CREATURE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Check which request we're responding to
		if (requestCode == Common.CREATURE_ACTIVITY_REQUEST_CODE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				String action = data.getAction();
				if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_FAMILY)) {
					CreatureGroup creatureGroup = data.getExtras()
							.getParcelable(Common.FAMILY_EXTRA);
					mMainViewModel.familyTextview.setText(creatureGroup
							.getViet());
					mFamilyId = creatureGroup.getId();
					autoFill(true, true);
					searchByName();
				} else if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_ORDER)) {
					CreatureGroup creatureGroup = data.getExtras()
							.getParcelable(Common.ORDER_EXTRA);
					mMainViewModel.orderTextView.setText(creatureGroup
							.getViet());
					mOrderId = creatureGroup.getId();
					autoFill(true, false);
					searchByName();
				} else if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_CLASS)) {
					CreatureGroup creatureGroup = data.getExtras()
							.getParcelable(Common.CLASS_EXTRA);
					mMainViewModel.classTextView.setText(creatureGroup
							.getViet());
					mClassId = creatureGroup.getId();
					searchByName();
				}
			}
		}
	}

	protected void clearEvent(boolean family, boolean order, boolean classType) {
		String defaultText = getString(R.string.touch_to_select);
		if (family) {
			mFamilyId = "";
			mMainViewModel.familyTextview.setText(defaultText);
		}
		if (order) {
			mOrderId = "";
			mMainViewModel.orderTextView.setText(defaultText);
		}
		if (classType) {
			mClassId = "";
			mMainViewModel.classTextView.setText(defaultText);
		}
	}

	protected void autoFill(boolean classFill, boolean orderFill) {
		if (classFill) {
			HrmService service = new HrmService();
			service.setCallback(new GroupCallback() {

				@Override
				public void onSuccess(CreatureGroupListModel groupModel) {
					mMainViewModel.classTextView.setText(groupModel.get(0)
							.getViet());
					mClassId = groupModel.get(0).getId();
				}

				@Override
				public void onError() {

				}
			});
			service.requestGetClass(Common.KINGDOM, mOrderId, mFamilyId);
		}
		
		if (orderFill) {
			HrmService service = new HrmService();
			service.setCallback(new GroupCallback() {

				@Override
				public void onSuccess(CreatureGroupListModel groupModel) {
					mMainViewModel.orderTextView.setText(groupModel.get(0)
							.getViet());
					mOrderId = groupModel.get(0).getId();
				}

				@Override
				public void onError() {

				}
			});
			service.requestGetOrder(Common.KINGDOM, mFamilyId, mClassId);
		}
	}
}
