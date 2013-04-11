package com.vncreatures.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.vncreatures.R;
import com.vncreatures.common.Common;
import com.vncreatures.common.ServerConfig;
import com.vncreatures.common.Common.CREATURE;
import com.vncreatures.customItems.GalleryImageAdapter;
import com.vncreatures.model.Creature;
import com.vncreatures.model.CreatureDescriptionViewModel;
import com.vncreatures.model.CreatureModel;
import com.vncreatures.rest.HrmService;
import com.vncreatures.rest.HrmService.Callback;
import com.vncreatures.rest.HrmService.CheckUrlCallback;
import com.vncreatures.view.CreatureDescriptionView;

public class AboutActivity extends AbstractActivity implements
        OnClickListener {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        
    }

    @Override
    protected View createView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        return inflater.inflate(R.layout.about_layout, null);
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
        setTitle(getString(R.string.about_us));

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

    @Override
    protected int indentifyTabPosition() {
        return R.id.tab_about_button;
    }

}
