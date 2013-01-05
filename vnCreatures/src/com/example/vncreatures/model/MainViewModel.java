package com.example.vncreatures.model;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainViewModel extends AbstractModel {
	public EditText searchBox_editText = null;
	public Button search_button = null;
	public ListView creature_listview = null;
	public Button advanceButton = null;
	public LinearLayout advanceLayout = null;
	public RelativeLayout classLayout = null;
	public RelativeLayout familyLayout = null;
	public RelativeLayout orderLayout = null;
	public TextView familyTextview = null;
	public TextView orderTextView = null;
	public TextView classTextView = null;
	public ImageButton familyClearButton = null;
	public ImageButton orderClearButton = null;
	public ImageButton classClearButton = null;
}