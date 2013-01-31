package com.example.vncreatures.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.CreaturesGroupsAdapter;
import com.example.vncreatures.customItems.CreaturesGroupsAdapter.Callback;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.GroupChooseModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.GroupCallback;
import com.example.vncreatures.view.GroupChooseView;

public class GroupChooseActivity extends AbstractActivity {

    private GroupChooseModel mModel = new GroupChooseModel();
    private GroupChooseView mView;
    private CreatureGroupListModel mCreatureGroupListModel = null;
    private CreaturesGroupsAdapter mAdapter;
    private Bundle mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Transition
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

        mView = new GroupChooseView(this, mModel);

        super.onCreate(savedInstanceState);

        // Get extras
        getFromExtras(savedInstanceState);

        // Init List
        filterList();

        setupUI(findViewById(R.id.layout_parent));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState = mExtras;
    }

    @Override
    protected View createView() {
        return mView;
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.filter_group);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    protected void getFromExtras(Bundle savedInstanceState) {
        try {
            if (savedInstanceState != null) {
                mExtras = savedInstanceState;
            } else {
                mExtras = getIntent().getExtras();
            }
            if (mExtras != null) {
                String familyId = mExtras.getString(Common.FAMILY_EXTRA);
                String orderId = mExtras.getString(Common.ORDER_EXTRA);
                String classId = mExtras.getString(Common.CLASS_EXTRA);
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
                setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void onError() {

            }
        });
        service.requestGetFamily(pref.getString(Common.KINGDOM, null), orderId,
                classId);
        setSupportProgressBarIndeterminateVisibility(true);
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
                setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void onError() {

            }
        });
        service.requestGetOrder(pref.getString(Common.KINGDOM, null), familyId,
                classId);
        setSupportProgressBarIndeterminateVisibility(true);
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
                setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void onError() {

            }
        });
        service.requestGetClass(pref.getString(Common.KINGDOM, null), orderId,
                familyId);
        setSupportProgressBarIndeterminateVisibility(true);
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
                    if (creatureGroup.getViet().toLowerCase()
                            .contains(s.toString().toLowerCase())
                            || creatureGroup.getLatin().toLowerCase()
                                    .contains(s.toString().toLowerCase())) {
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

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabHome_button;
    }

}
