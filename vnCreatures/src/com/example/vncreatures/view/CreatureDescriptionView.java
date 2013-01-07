package com.example.vncreatures.view;

import com.example.vncreatures.R;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureDescriptionViewModel;
import com.example.vncreatures.rest.HrmService;
import com.markupartist.android.widget.ActionBar;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
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
		mCreatureDescriptionViewModel
				.setVietName((TextView) findViewById(R.id.vietNameDetail_textview));
		mCreatureDescriptionViewModel
				.setLatinName((TextView) findViewById(R.id.latinNameDetail_textview));

		mCreatureDescriptionViewModel
				.setCreatureImageView((ImageView) findViewById(R.id.creatureList_imageView));
		mCreatureDescriptionViewModel
				.setCreatureDesWebview((WebView) findViewById(R.id.creatureDes_webview));
		mCreatureDescriptionViewModel.actionbar = (ActionBar) findViewById(R.id.actionbar);
	}

	public void setContent(Creature creature) {
		mCreatureDescriptionViewModel.getVietName()
				.setText(creature.getvName());
		mCreatureDescriptionViewModel.getLatinName().setText(
				creature.getLatin());

		HrmService service = new HrmService();
		service.downloadImages(creature.getId(), creature.getLoai(),
				mCreatureDescriptionViewModel.getCreatureImageView());

		mCreatureDescriptionViewModel.getCreatureDesWebview().getSettings()
				.setSupportZoom(false);
		mCreatureDescriptionViewModel.getCreatureDesWebview().getSettings()
				.setDefaultTextEncodingName("utf-8");
		mCreatureDescriptionViewModel.getCreatureDesWebview()
				.loadDataWithBaseURL(null, creature.getDescription(),
						"text/html", "utf-8", null);
		// mCreatureDescriptionViewModel.getCreatureDesWebview().loadData(
		// creature.getDescription(), "text/html", "utf-8");
	}
}
