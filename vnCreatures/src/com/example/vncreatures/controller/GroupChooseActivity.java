package com.example.vncreatures.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.CreaturesGroupsAdapter;
import com.example.vncreatures.customItems.CreaturesGroupsAdapter.Callback;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.GroupChooseModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.GroupCallback;
import com.example.vncreatures.view.GroupChooseView;

public class GroupChooseActivity extends AbstracActivity {

	private GroupChooseModel mModel = new GroupChooseModel();
	private GroupChooseView mView;
	private CreatureGroupListModel mCreatureGroupListModel = null;
	private CreaturesGroupsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = new GroupChooseView(this, mModel);
		setContentView(mView);

		getFromExtras();
		filterList();
	}

	protected void getFromExtras() {
		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				String familyId = extras.getString(Common.FAMILY_EXTRA);
				String orderId = extras.getString(Common.ORDER_EXTRA);
				String classId = extras.getString(Common.CLASS_EXTRA);
				String action = getIntent().getAction();
				if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_FAMILY))
					initFamilyList(familyId, classId, orderId);
				else if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_ORDER))
					initOrderList(familyId, classId, orderId);
				else if (action.equalsIgnoreCase(Common.ACTION_CHOOSE_CLASS))
					initClassList(familyId, classId, orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void initFamilyList(String familyId, String classId,
			String orderId) {
		HrmService service = new HrmService();
		service.setCallback(new GroupCallback() {

			@Override
			public void onSuccess(CreatureGroupListModel groupModel) {
				mAdapter = new CreaturesGroupsAdapter(GroupChooseActivity.this,
						groupModel);
				mModel.listView.setAdapter(mAdapter);
				mCreatureGroupListModel = groupModel;
				mAdapter.setCallback(new Callback() {

					@Override
					public void onClick(CreatureGroup creatureGroup) {
						Intent intent = new Intent();
						intent.setAction(Common.ACTION_CHOOSE_FAMILY);
						intent.putExtra(Common.FAMILY_EXTRA, creatureGroup);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				});
			}

			@Override
			public void onError() {

			}
		});
		service.requestGetFamily(Common.KINGDOM, orderId, classId);
	}

	protected void initOrderList(String familyId, String classId, String orderId) {
		HrmService service = new HrmService();
		service.setCallback(new GroupCallback() {

			@Override
			public void onSuccess(CreatureGroupListModel groupModel) {
				mAdapter = new CreaturesGroupsAdapter(GroupChooseActivity.this,
						groupModel);
				mModel.listView.setAdapter(mAdapter);
				mCreatureGroupListModel = groupModel;
				mAdapter.setCallback(new Callback() {

					@Override
					public void onClick(CreatureGroup creatureGroup) {
						Intent intent = new Intent();
						intent.setAction(Common.ACTION_CHOOSE_ORDER);
						intent.putExtra(Common.ORDER_EXTRA, creatureGroup);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				});
			}

			@Override
			public void onError() {

			}
		});
		service.requestGetOrder(Common.KINGDOM, familyId, classId);
	}

	protected void initClassList(String familyId, String classId, String orderId) {
		HrmService service = new HrmService();
		service.setCallback(new GroupCallback() {

			@Override
			public void onSuccess(CreatureGroupListModel groupModel) {
				mAdapter = new CreaturesGroupsAdapter(GroupChooseActivity.this,
						groupModel);
				mModel.listView.setAdapter(mAdapter);
				mCreatureGroupListModel = groupModel;
				mAdapter.setCallback(new Callback() {

					@Override
					public void onClick(CreatureGroup creatureGroup) {
						Intent intent = new Intent();
						intent.setAction(Common.ACTION_CHOOSE_CLASS);
						intent.putExtra(Common.CLASS_EXTRA, creatureGroup);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				});
			}

			@Override
			public void onError() {

			}
		});
		service.requestGetClass(Common.KINGDOM, orderId, familyId);
	}

	protected void filterList() {
		mModel.filterEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				CreatureGroupListModel temp = new CreatureGroupListModel();
				for (int i = 0; i < mCreatureGroupListModel.count(); i++) {
					CreatureGroup creatureGroup = mCreatureGroupListModel
							.get(i);
					if (creatureGroup.getViet().toLowerCase().contains(s.toString().toLowerCase())
							|| creatureGroup.getLatin().toLowerCase().contains(s.toString().toLowerCase())) {
						temp.add(creatureGroup);
					}
				}
				mAdapter.setCreatureModel(temp);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
