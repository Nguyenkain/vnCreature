package com.example.vncreatures.model;

import java.util.ArrayList;

public class CategoryModel {
	private ArrayList<Category> mCatList = new ArrayList<Category>();

	public ArrayList<Category> getCatList() {
		return mCatList;
	}

	public void setCatList(ArrayList<Category> creatureList) {
		this.mCatList = creatureList;
	}
	
	public void add(final Category cat) {
		mCatList.add(cat);
	}
	
	public Category get(int index) {
		return mCatList.get(index);
	}
	
	public void remove(final Category cat) {
		mCatList.remove(cat);
	}
	
	public int count() {
		return mCatList.size();
	}
	
	public void clear() {
		mCatList.clear();
	}
	
	public void addAll(CategoryModel model) {
		ArrayList<Category> cats = model.getCatList();
		mCatList.addAll(cats);
	}

}
