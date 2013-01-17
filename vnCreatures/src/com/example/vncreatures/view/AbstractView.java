package com.example.vncreatures.view;

import android.content.Context;
import android.widget.LinearLayout;

public class AbstractView extends LinearLayout {
	
	private Context mContext = null;

	protected AbstractView(Context context) {
		super(context);
		this.mContext = context;
	}
	
	private void initUI() {
		//Use to initUI
	}

}