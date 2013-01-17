package com.example.vncreatures.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
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
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class MainActivity extends AbstractActivity implements OnClickListener {

	private MainViewModel mMainViewModel = new MainViewModel();
	private MainView mMainView = null;
	private CreatureModel mCreatureModel = null;
	private CreaturesListAdapter mCreatureAdapter = null;

	private String mFamilyId = "";
	private String mOrderId = "";
	private String mKingdomId = "";
	private String mClassId = "";
	private String mName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		mMainView = new MainView(this, mMainViewModel);

		super.onCreate(savedInstanceState);

		// Get Preference
		mKingdomId = pref.getString(Common.KINGDOM, "");

		// Get All list
		getAllCreatures();

		// Search By Name
		// Button btn = mMainViewModel.search_button;
		// btn.setOnClickListener(this);

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

		// Setup UI
		setupUI(findViewById(R.id.layout_parent));
	}
	
	@Override
	protected View createView() {
		return mMainView;
	}

	@Override
	protected void onResume() {
		// Transition
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		// //Inflate customview
		// View refreshAction =
		// LayoutInflater.from(this).inflate(R.layout.refresh_action_provider,
		// null);
		// ImageButton refreshButton = (ImageButton)
		// refreshAction.findViewById(R.id.refresh_button);
		// refreshButton.setOnClickListener(this);
		//
		// //Show
		// getSupportActionBar().setCustomView(refreshAction, new
		// LayoutParams(Gravity.RIGHT));
		// getSupportActionBar().setDisplayShowCustomEnabled(true);

		// Inflate menu
		getSupportMenuInflater().inflate(R.menu.home_menu, menu);

		// Create the search view
		SearchView searchView = new SearchView(getSupportActionBar()
				.getThemedContext());
		searchView.setQueryHint(getString(R.string.search));

		menu.getItem(1).setActionView(searchView);

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				mName = query;
				Utils.hideView(mMainViewModel.advanceLayout);
				searchByName(false);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if(newText.equals("")) {
					mName = newText;
					searchByName(false);
				}
				return true;
			}
		});

		// init back button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_refresh:
			searchByName(false);
			break;
		case R.id.menu_item_advance:
			Utils.toogleLayout(mMainViewModel.advanceLayout);
			break;
		case R.id.preferences:
			startActivity(new Intent(this, EditPreferences.class));
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initList(CreatureModel creatureModel) {
		this.mCreatureModel = creatureModel;
		PullToRefreshListView creatureListView = mMainViewModel.creature_listview;
		mCreatureAdapter = new CreaturesListAdapter(this, creatureModel);
		creatureListView.setAdapter(mCreatureAdapter);
		mCreatureAdapter.notifyDataSetChanged();

		creatureListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				Creature creature = mCreatureModel.get(pos - 1);
				Intent intent = new Intent(Common.ACTION_EXTRA, null,
						MainActivity.this, CreatureActivity.class);
				intent.putExtra(Common.CREATURE_EXTRA, creature.getId());
				startActivityForResult(intent,
						Common.CREATURE_ACTIVITY_REQUEST_CODE);
			}
		});

		creatureListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				searchByName(true);
			}
		});
	}

	public void getAllCreatures() {
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				initList(creatureModel);
				mMainViewModel.creature_listview
						.setOnScrollListener(new EndlessScrollListener(
								mCreatureAdapter, mKingdomId));
				setSupportProgressBarIndeterminateVisibility(false);
			}

			@Override
			public void onError() {

			}
		});
		service.requestAllCreature("1", mKingdomId);
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		/*
		 * case R.id.search_button: searchByName(); break;
		 */
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

	private void searchByName(final boolean pull) {
		HrmService service = new HrmService();
		service.setCallback(new Callback() {

			@Override
			public void onGetAllCreaturesDone(CreatureModel creatureModel) {
				initList(creatureModel);

				mMainViewModel.creature_listview
						.setOnScrollListener(new EndlessScrollListener(
								mCreatureAdapter, mName, mKingdomId, mFamilyId,
								mOrderId, mClassId));
				// Set some status done
				setSupportProgressBarIndeterminateVisibility(false);
				if (pull)
					mMainViewModel.creature_listview.onRefreshComplete();
			}

			@Override
			public void onError() {

			}
		});
		service.requestCreaturesByName(mName, mKingdomId, "1", mFamilyId,
				mOrderId, mClassId);
		setSupportProgressBarIndeterminateVisibility(true);
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
					searchByName(false);
				} else if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_ORDER)) {
					CreatureGroup creatureGroup = data.getExtras()
							.getParcelable(Common.ORDER_EXTRA);
					mMainViewModel.orderTextView.setText(creatureGroup
							.getViet());
					mOrderId = creatureGroup.getId();
					autoFill(true, false);
					searchByName(false);
				} else if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_CLASS)) {
					CreatureGroup creatureGroup = data.getExtras()
							.getParcelable(Common.CLASS_EXTRA);
					mMainViewModel.classTextView.setText(creatureGroup
							.getViet());
					mClassId = creatureGroup.getId();
					searchByName(false);
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
		searchByName(false);
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
					setSupportProgressBarIndeterminateVisibility(false);
				}

				@Override
				public void onError() {

				}
			});
			service.requestGetClass(mKingdomId, mOrderId, mFamilyId);
			setSupportProgressBarIndeterminateVisibility(true);
		}

		if (orderFill) {
			HrmService service = new HrmService();
			service.setCallback(new GroupCallback() {

				@Override
				public void onSuccess(CreatureGroupListModel groupModel) {
					mMainViewModel.orderTextView.setText(groupModel.get(0)
							.getViet());
					mOrderId = groupModel.get(0).getId();
					setSupportProgressBarIndeterminateVisibility(false);
				}

				@Override
				public void onError() {

				}
			});
			service.requestGetOrder(mKingdomId, mFamilyId, mClassId);
			setSupportProgressBarIndeterminateVisibility(true);
		}
	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

	@Override
	protected int indentifyTabPosition() {
		return R.id.tabHome_button;
	}

}