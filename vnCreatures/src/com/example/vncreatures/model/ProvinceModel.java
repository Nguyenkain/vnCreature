package com.example.vncreatures.model;

import java.util.ArrayList;

public class ProvinceModel {
	private ArrayList<Province> mProvinceList = new ArrayList<Province>();

	public ArrayList<Province> getProvinceList() {
		return mProvinceList;
	}

	public void setProvinceList(ArrayList<Province> provinceList) {
		this.mProvinceList = provinceList;
	}
	
	public void add(final Province province) {
		mProvinceList.add(province);
	}
	
	public Province get(int index) {
		return mProvinceList.get(index);
	}
	
	public void remove(int index) {
		mProvinceList.remove(index);
	}
	
	public int count() {
		return mProvinceList.size();
	}
	
	public void clear() {
		mProvinceList.clear();
	}
	
	public void addAll(ProvinceModel model) {
		ArrayList<Province> provinces = model.getProvinceList();
		mProvinceList.addAll(provinces);
	}

}
