package com.example.vncreatures.model.discussion;

import java.util.ArrayList;

public class ReportModel {
	private ArrayList<Report> mReportTypeList = new ArrayList<Report>();

	public ArrayList<Report> getReportTypeList() {
		return mReportTypeList;
	}

	public void setReportTypeList(ArrayList<Report> threadList) {
		this.mReportTypeList = threadList;
	}
	
	public void add(final Report item) {
		mReportTypeList.add(item);
	}
	
	public Report get(int index) {
		return mReportTypeList.get(index);
	}
	
	public void remove(final Report item) {
		mReportTypeList.remove(item);
	}
	
	public int count() {
		return mReportTypeList.size();
	}
	
	public void clear() {
		mReportTypeList.clear();
	}
	
	public void addAll(ReportModel model) {
		ArrayList<Report> newList = model.getReportTypeList();
		mReportTypeList.addAll(newList);
	}

}
