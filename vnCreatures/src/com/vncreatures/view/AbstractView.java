package com.vncreatures.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AbstractView extends RelativeLayout {
	
	private Context mContext = null;

	protected AbstractView(Context context) {
		super(context);
		this.mContext = context;
	}
	
	private void initUI() {
		//Use to initUI
	}

}