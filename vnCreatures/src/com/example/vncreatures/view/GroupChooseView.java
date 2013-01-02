package com.example.vncreatures.view;

import com.example.vncreatures.R;
import com.example.vncreatures.model.GroupChooseModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ListView;

public class GroupChooseView extends AbstractView {

	private GroupChooseModel mModel;

	public GroupChooseView(Context context) {
		super(context);
	}

	public GroupChooseView(Context context, GroupChooseModel model) {
		super(context);
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.group_filter_screen, this);
		this.mModel = model;
		initUI();
	}

	protected void initUI() {
		mModel.listView = (ListView) findViewById(R.id.groupChoose_listview);
		mModel.filterEditText = (EditText) findViewById(R.id.filter_editText);
	}

}
