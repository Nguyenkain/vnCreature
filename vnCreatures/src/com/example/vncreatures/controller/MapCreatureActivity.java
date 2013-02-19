/*
 * Copyright (C) 2012 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.vncreatures.controller;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.MapViewUtils;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.common.MapConfig;
import com.example.vncreatures.model.CreatureMapViewModel;
import com.example.vncreatures.model.Province;
import com.example.vncreatures.model.ProvinceModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.ProvinceCallback;
import com.example.vncreatures.view.CreatureMapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

public class MapCreatureActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener, OnClickListener {

	private static double MAX_LATITUDE = 23.37582;
	private static double MAX_LONGITUDE = 109.459099;
	private static double MIN_LATITUDE = 8.574163;
	private static double MIN_LONGITUDE = 102.140503;
	private static final String LOG_TAG = "MapCreatureActivity";
	
	private final ArrayList<Annotation> mAnnotation = new ArrayList<Annotation>();
	private PolarisMapView mMapView;
	private CreatureMapView mCreatureMapView = null;
	private CreatureMapViewModel mCreatureMapViewModel = new CreatureMapViewModel();
	private MapController mMapController;
	private String mCreatureId = null;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get preference
        pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

		// Action bar
		setTheme(Common.THEME);
		getSupportActionBar().setIcon(R.drawable.chikorita);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.map_search));

		// init view
        mCreatureMapView = new CreatureMapView(this, mCreatureMapViewModel);
        setContentView(mCreatureMapView);

		// Get creature id
		getFromExtras(savedInstanceState);
		// Initialize map view
		initMapSpanZoom();
		// Get province of creature on map
		getProvince();

		mCreatureMapViewModel.mapViewContainer.addView(mMapView, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mMapView.invalidate();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mMapView.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mMapView.onStop();
	}
	
	@Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onRegionChanged(PolarisMapView mapView) {
		if (MapConfig.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onRegionChanged");
		}
	}

	@Override
	public void onRegionChangeConfirmed(PolarisMapView mapView) {
		if (MapConfig.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onRegionChangeConfirmed");
		}
	}

	@Override
	public void onAnnotationSelected(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (MapConfig.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationSelected");
		}
		calloutView.setDisclosureEnabled(true);
		calloutView.setClickable(false);
		calloutView.setLeftAccessoryView(getLayoutInflater().inflate(
				R.layout.accessory, calloutView, false));
	}

	@Override
	public void onAnnotationDeselected(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (MapConfig.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationDeselected");
		}
	}

	@Override
	public void onAnnotationClicked(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (MapConfig.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationClicked");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	private void initMapSpanZoom() {

		mMapView = new PolarisMapView(this, MapConfig.GOOGLE_MAPS_API_KEY);
		mMapView.setUserTrackingButtonEnabled(true);
		mMapView.setOnRegionChangedListenerListener(this);
		mMapView.setOnAnnotationSelectionChangedListener(this);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setSatellite(false);

		mMapController = mMapView.getController();

		GeoPoint p = new GeoPoint((int) ((MAX_LATITUDE + MIN_LATITUDE) / 2 * 1E6),
				(int) ((MAX_LONGITUDE + MIN_LONGITUDE) / 2 * 1E6));
		mMapController.animateTo(p);
		mMapController.zoomToSpan((int) ((MAX_LATITUDE - MIN_LATITUDE) * 1E6),
				(int) ((MAX_LONGITUDE - MIN_LONGITUDE) * 1E6));
		mMapController.setZoom(6);
	}

	private void getFromExtras(Bundle savedInstanceState) {
		try {
		    Bundle extras = new Bundle();
            if(savedInstanceState != null) {
                extras = savedInstanceState;
            }
            else {
                extras = getIntent().getExtras();
            }
			if (extras != null) {
				mCreatureId = extras.getString(Common.CREATURE_EXTRA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putString(Common.CREATURE_EXTRA, mCreatureId);
	}

	private void getProvince() {
		HrmService service = new HrmService();
		service.setCallback(new ProvinceCallback() {

			@Override
			public void onSuccess(ProvinceModel provinceModel) {
				Province province = null;
				for (int i = 0; i < provinceModel.count(); i++) {
					province = provinceModel.get(i);
					mAnnotation.add(new Annotation(new GeoPoint(province
							.getLatitude(), province.getLongitude()), province
							.getProvince_name()));
				}
				// Set marker
				setMarkerOnMap();
			}

			@Override
			public void onError() {

			}
		});
		service.requestGetProvince(mCreatureId);
		setSupportProgressBarIndeterminateVisibility(true);
	}

	private void setMarkerOnMap() {
		// Prepare an alternate pin Drawable
		final Drawable altMarker = MapViewUtils
				.boundMarkerCenterBottom(getResources().getDrawable(
						R.drawable.map_pin_holed_violet));

		// Prepare the list of Annotation using the alternate Drawable for all
		for (Annotation annotation : mAnnotation) {
			annotation.setMarker(altMarker);
		}
		mMapView.setAnnotations(mAnnotation, R.drawable.map_pin_holed_blue);
		setSupportProgressBarIndeterminateVisibility(false);
	}
}