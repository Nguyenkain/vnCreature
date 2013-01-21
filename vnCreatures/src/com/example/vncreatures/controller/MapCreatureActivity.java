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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.MapViewUtils;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Config;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;

public class MapCreatureActivity extends MapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	private static final String LOG_TAG = "MainActivity";

	// @formatter:off
	private static final Annotation[] sVietNam = {
			new Annotation(new GeoPoint(21033333, 105850000), "Ha Noi",
					"Ha noi Capital"),
			new Annotation(new GeoPoint(19806692, 105785182), "Thanh Hoa",
					"Thanh Hoa City"), };

	// private static final Annotation[][] sRegions = {sFrance, sEurope,
	// sUsaEastCoast, sUsaWestCoast};
	private static final Annotation[][] sRegions = { sVietNam };
	// @formatter:on

	private PolarisMapView mMapView;
	private MapController mMapController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		mMapView = new PolarisMapView(this, Config.GOOGLE_MAPS_API_KEY);
		mMapView.setUserTrackingButtonEnabled(true);
		mMapView.setOnRegionChangedListenerListener(this);
		mMapView.setOnAnnotationSelectionChangedListener(this);
		mMapView.setBuiltInZoomControls(true);

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

		// Prepare an alternate pin Drawable
		final Drawable altMarker = MapViewUtils
				.boundMarkerCenterBottom(getResources().getDrawable(
						R.drawable.map_pin_holed_violet));

		// Prepare the list of Annotation using the alternate Drawable for all
		// Annotation located in France
		final ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		for (Annotation[] region : sRegions) {
			for (Annotation annotation : region) {
				if (region == sVietNam) {
					annotation.setMarker(altMarker);
				}
				annotations.add(annotation);
			}
		}
		mMapView.setAnnotations(annotations, R.drawable.map_pin_holed_blue);

		final FrameLayout mapViewContainer = (FrameLayout) findViewById(R.id.map_view_container);
		mapViewContainer.addView(mMapView, new FrameLayout.LayoutParams(
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
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onRegionChanged(PolarisMapView mapView) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onRegionChanged");
		}
	}

	@Override
	public void onRegionChangeConfirmed(PolarisMapView mapView) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onRegionChangeConfirmed");
		}
	}

	@Override
	public void onAnnotationSelected(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationSelected");
		}
		calloutView.setDisclosureEnabled(true);
		calloutView.setClickable(true);
		if (!TextUtils.isEmpty(annotation.getSnippet())) {
			calloutView.setLeftAccessoryView(getLayoutInflater().inflate(
					R.layout.accessory, calloutView, false));
		} else {
			calloutView.setLeftAccessoryView(null);
		}
	}

	@Override
	public void onAnnotationDeselected(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationDeselected");
		}
	}

	@Override
	public void onAnnotationClicked(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationClicked");
		}
		Toast.makeText(this,
				getString(R.string.annotation_clicked, annotation.getTitle()),
				Toast.LENGTH_SHORT).show();
	}
}
