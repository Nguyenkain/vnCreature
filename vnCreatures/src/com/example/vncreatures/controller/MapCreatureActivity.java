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
import com.example.vncreatures.model.Province;
import com.example.vncreatures.model.ProvinceModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.ProvinceCallback;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

public class MapCreatureActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener, OnClickListener {

	private static final String LOG_TAG = "MainActivity";
	private final ArrayList<Annotation> mAnnotation = new ArrayList<Annotation>();

	private PolarisMapView mMapView;
	private MapController mMapController;
	
	SharedPreferences pref;

	private String mCreatureId = null;

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
        setTitle(getString(R.string.map));

		// init view
		setContentView(R.layout.map_view);

		// Get creature id
		getFromExtras();
		// Initialize map view
		initMapSpanZoom();
		// Get province of creature on map
		getProvince();

		final FrameLayout mapViewContainer = (FrameLayout) findViewById(R.id.map_view_container);
		mapViewContainer.addView(mMapView, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mMapView.invalidate();
	}
	
//	protected void initTabButton() {
//        /*
//         * Intent mainIntent = new Intent(context, NewsTabsPagerActivity.class);
//         * startActivity(mainIntent);
//         */
//
//        resetTabState();
//
//        View button = (View) findViewById(R.id.tabHome_button);
//        button.setOnClickListener(this);
//        button = (View) findViewById(R.id.tabNews_button);
//        button.setOnClickListener(this);
//        button = (View) findViewById(R.id.tabDiscussion_button);
//        button.setOnClickListener(this);
//        button = (View) findViewById(R.id.tabsMap_button);
//        button.setOnClickListener(this);
//        button = (View) findViewById(R.id.tabNews_button);
//        button.setOnClickListener(this);
//    }

//    @Override
//    public void onClick(View v) {
//        int id = pref.getInt(Common.TAB_PREF, R.id.tabHome_button);
//        switch (v.getId()) {
//        case R.id.tabHome_button:
//            resetTabState();
//            if (v.getId() != id) {
//                Intent mainIntent = new Intent(this,
//                        KingdomChooseActivity.class);
//                startActivity(mainIntent);
//            }
//            break;
//        case R.id.tabNews_button:
//            resetTabState();
//            if (v.getId() != id) {
//                Intent mainIntent = new Intent(this,
//                        NewsTabsPagerActivity.class);
//                startActivity(mainIntent);
//            }
//            break;
//        case R.id.tabDiscussion_button:
//            resetTabState();
//            break;
//        case R.id.tabsMap_button:
//            resetTabState();
//            if (v.getId() != id) {
//                Intent mainIntent = new Intent(this, MapCreatureActivity.class);
//                startActivity(mainIntent);
//            }
//            break;
//
//        default:
//            break;
//        }
//    }
//    
//    private void resetTabState() {
//        int id = pref.getInt(Common.TAB_PREF, R.id.tabHome_button);
//        LinearLayout tabControl = (LinearLayout) findViewById(R.id.tab_control);
//        for (int i = 0; i < tabControl.getChildCount(); i++) {
//            View v = tabControl.getChildAt(i);
//            if (v.getId() == id)
//                v.setSelected(true);
//            else
//                v.setSelected(false);
//        }
//    }

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
//	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		int tabPosition = R.id.tabsMap_button;
//        pref.edit().putInt(Common.TAB_PREF, tabPosition).commit();
//        resetTabState();
//	}

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
		calloutView.setClickable(true);
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
		mMapView.setSatellite(true);

		mMapController = mMapView.getController();

		String coordinates[] = { "23.37582", "109.459099", "8.574163",
				"102.140503" };
		double maxlat = Double.parseDouble(coordinates[0]);
		double maxlng = Double.parseDouble(coordinates[1]);
		double minlat = Double.parseDouble(coordinates[2]);
		double minlng = Double.parseDouble(coordinates[3]);

		GeoPoint p = new GeoPoint((int) ((maxlat + minlat) / 2 * 1E6),
				(int) ((maxlng + minlng) / 2 * 1E6));
		mMapController.animateTo(p);
		mMapController.zoomToSpan((int) ((maxlat - minlat) * 1E6),
				(int) ((maxlng - minlng) * 1E6));
		mMapController.setZoom(6);
	}

	private void getFromExtras() {
		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mCreatureId = extras.getString(Common.CREATURE_EXTRA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	}
}