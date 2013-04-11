package com.vncreatures.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import com.vncreatures.R;
import com.vncreatures.common.Common;
import com.vncreatures.model.KingdomChooseViewModel;
import com.vncreatures.view.KingdomChooseView;

public class KingdomChooseActivity extends AbstractActivity implements
        OnClickListener {
    private KingdomChooseView mView = null;
    private KingdomChooseViewModel mModel = new KingdomChooseViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mView = new KingdomChooseView(this, mModel);

        super.onCreate(savedInstanceState);
        
        showTabbar(false);

        initControl();

    }

    @Override
    protected View createView() {
        return mView;
    }

    private void initControl() {
        setTitle(R.string.kindom_choose);
        mModel.animalButton.setOnClickListener(this);
        mModel.plantButton.setOnClickListener(this);
        mModel.insectButton.setOnClickListener(this);
        mModel.discussButton.setOnClickListener(this);
        mModel.newsButton.setOnClickListener(this);
        mModel.aboutButton.setOnClickListener(this);
        mModel.parkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent mainIntent = new Intent(KingdomChooseActivity.this,
                MainActivity.class);
        switch (v.getId()) {
        case R.id.animal_button:
            pref.edit()
                    .putString(Common.KINGDOM,
                            Common.CREATURE.animal.toString()).commit();
            startActivity(mainIntent);
            break;
        case R.id.plant_button:
            pref.edit()
                    .putString(Common.KINGDOM, Common.CREATURE.plant.toString())
                    .commit();
            startActivity(mainIntent);
            break;
        case R.id.insect_button:
            pref.edit()
                    .putString(Common.KINGDOM,
                            Common.CREATURE.insect.toString()).commit();
            startActivity(mainIntent);
            break;
        case R.id.about_button:
            mainIntent = new Intent(this, AboutActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            break;
        case R.id.park_button:
            mainIntent = new Intent(this, MapNationalParkActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            break;
        case R.id.news_button:
            mainIntent = new Intent(this,
                    NewsTabsPagerActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            break;
        case R.id.discuss_button:
            mainIntent = new Intent(this, LoginActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            break;

        default:

            break;
        }

    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        super.onResume();
        mModel.animalButton.setEnabled(true);
        mModel.plantButton.setEnabled(true);
        mModel.insectButton.setEnabled(true);
    }

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabHome_button;
    }

}
