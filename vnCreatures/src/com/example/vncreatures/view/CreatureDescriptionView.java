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
		mCreatureDescriptionViewModel.mVietName = (TextView) findViewById(R.id.vietNameDetail_textview);
		mCreatureDescriptionViewModel.mLatinName = (TextView) findViewById(R.id.latinNameDetail_textview);
		mCreatureDescriptionViewModel.mCreatureImageView = (ImageView) findViewById(R.id.creatureList_imageView);
		mCreatureDescriptionViewModel.mCreatureDesWebview = (WebView) findViewById(R.id.creatureDes_webview);
		mCreatureDescriptionViewModel.mGalleryImage = (Gallery) findViewById(R.id.creatureImage_gallery);
	}

	public void setContent(Creature creature) {
		mCreatureDescriptionViewModel.mVietName.setText(creature.getVName());
		mCreatureDescriptionViewModel.mLatinName.setText(creature.getLatin());
		HrmService service = new HrmService();
		service.downloadImages(mContext, creature.getId(), creature.getLoai(),
				mCreatureDescriptionViewModel.getGalleryImage());
		
		
		//mCreatureDescriptionViewModel.getCreatureDesWebview().setBackgroundColor(0x00000000);

		mCreatureDescriptionViewModel.getCreatureDesWebview().getSettings()
				.setSupportZoom(false);
		mCreatureDescriptionViewModel.mCreatureDesWebview.getSettings()
				.setDefaultTextEncodingName("utf-8");
		mCreatureDescriptionViewModel.mCreatureDesWebview.loadDataWithBaseURL(
				null, creature.getDescription(), "text/html", "utf-8", null);

	}

}
