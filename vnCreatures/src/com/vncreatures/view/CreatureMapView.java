package com.vncreatures.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.vncreatures.R;
import com.vncreatures.model.CreatureMapViewModel;

public class CreatureMapView extends AbstractView {

	private CreatureMapViewModel mMapViewModel = null;

	protected CreatureMapView(Context context) {
		super(context);
	}

	public CreatureMapView(Context context, CreatureMapViewModel model) {
		super(context);
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.map_view, this);
		mMapViewModel = model;
		initUI();
	}

	private void initUI() {
		mMapViewModel.mapViewContainer = (FrameLayout) findViewById(R.id.map_view_container);
	}
}
