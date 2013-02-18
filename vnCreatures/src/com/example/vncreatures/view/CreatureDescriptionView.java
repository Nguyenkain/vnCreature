package com.example.vncreatures.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vncreatures.R;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.common.Common.CREATURE;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureDescriptionViewModel;
import com.example.vncreatures.rest.HrmService;

public class CreatureDescriptionView extends AbstractView {

	private Context mContext = null;
	private CreatureDescriptionViewModel mCreatureDescriptionViewModel = null;

	public CreatureDescriptionView(Context context,
			CreatureDescriptionViewModel model) {
		super(context);
		mContext = context;
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.creature_description, this);
		mCreatureDescriptionViewModel = model;
		iniUI();
	}

	private void iniUI() {
		mCreatureDescriptionViewModel.vietName = (TextView) findViewById(R.id.vietNameDetail_textview);
		mCreatureDescriptionViewModel.latinName = (TextView) findViewById(R.id.latinNameDetail_textview);
		mCreatureDescriptionViewModel.creatureImageView = (ImageView) findViewById(R.id.creatureList_imageView);
		mCreatureDescriptionViewModel.creatureDesWebview = (WebView) findViewById(R.id.creatureDes_webview);
		mCreatureDescriptionViewModel.galleryImage = (Gallery) findViewById(R.id.creatureImage_gallery);
	}

	public void setContent(Creature creature) {
		mCreatureDescriptionViewModel.vietName.setText(creature.getVName());
		mCreatureDescriptionViewModel.latinName.setText(creature.getLatin());

		// mCreatureDescriptionViewModel.getCreatureDesWebview().setBackgroundColor(0x00000000);
		mCreatureDescriptionViewModel.creatureDesWebview.getSettings()
				.setSupportZoom(false);
		mCreatureDescriptionViewModel.creatureDesWebview.getSettings()
				.setDefaultTextEncodingName("utf-8");
		mCreatureDescriptionViewModel.creatureDesWebview.getSettings()
				.setDefaultFontSize(18);
		mCreatureDescriptionViewModel.creatureDesWebview.loadDataWithBaseURL(
				null, creature.getDescription(), "text/html", "utf-8", null);

	}

}
