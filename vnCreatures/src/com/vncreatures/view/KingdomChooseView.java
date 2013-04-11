package com.vncreatures.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.vncreatures.R;
import com.vncreatures.model.KingdomChooseViewModel;

public class KingdomChooseView extends AbstractView {

	private KingdomChooseViewModel mModel = null;

	public KingdomChooseView(Context context) {
		super(context);
	}

	public KingdomChooseView(Context context, KingdomChooseViewModel model) {
		super(context);
		this.mModel = model;
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.kingdom_choose, this);
		initUI();
	}
	
	private void initUI() {
		mModel.animalButton = (ImageButton) findViewById(R.id.animal_button);
		mModel.insectButton = (ImageButton) findViewById(R.id.insect_button);
		mModel.plantButton = (ImageButton) findViewById(R.id.plant_button);
		mModel.newsButton = (ImageButton) findViewById(R.id.news_button);
		mModel.discussButton = (ImageButton) findViewById(R.id.discuss_button);
		mModel.parkButton = (ImageButton) findViewById(R.id.park_button);
		mModel.aboutButton = (ImageButton) findViewById(R.id.about_button);
	}

}
