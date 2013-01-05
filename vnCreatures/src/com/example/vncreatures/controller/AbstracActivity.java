package com.example.vncreatures.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.vncreatures.R;

public class AbstracActivity extends Activity {
	private ProgressDialog mIndicatorBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create indicator bar
		mIndicatorBar = new ProgressDialog(getApplicationContext());
		mIndicatorBar.setMessage(String.valueOf(R.string.loading));
		mIndicatorBar.setCancelable(false);
	}

	public void showActivityIndicator(final boolean shown) {
		if (shown == true)
			mIndicatorBar.show();
		else
			mIndicatorBar.hide();
	}
}