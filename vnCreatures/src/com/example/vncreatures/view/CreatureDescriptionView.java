package com.example.vncreatures.view;

import com.example.vncreatures.R;
import com.example.vncreatures.model.CreatureDescriptionViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatureDescriptionView extends AbstractView {

	private CreatureDescriptionViewModel mCreatureDescriptionViewModel = null;

	public CreatureDescriptionView(Context context,
			CreatureDescriptionViewModel model) {
		super(context);
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.creature_description, this);
		mCreatureDescriptionViewModel = model;
		iniUI();
	}

	private void iniUI() {
		mCreatureDescriptionViewModel.mVietName = (TextView) findViewById(R.id.vietName_textview);
		mCreatureDescriptionViewModel.mCreatureImageView = (ImageView) findViewById(R.id.creatureList_imageView);
		mCreatureDescriptionViewModel.mLatinName = (TextView) findViewById(R.id.latinName_textview);
		mCreatureDescriptionViewModel.mCreatureDesWebview = (WebView) findViewById(R.id.creatureDes_webview);
	}
}
