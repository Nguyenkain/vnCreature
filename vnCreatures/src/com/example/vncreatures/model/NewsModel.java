package com.example.vncreatures.model;

import java.util.ArrayList;

public class NewsModel {
	private ArrayList<NewsItem> mNewsList = new ArrayList<NewsItem>();

	public ArrayList<NewsItem> getNewsList() {
		return mNewsList;
	}

	public void setNewsList(ArrayList<NewsItem> creatureList) {
		this.mNewsList = creatureList;
	}
	
	public void add(final NewsItem newsItem) {
		mNewsList.add(newsItem);
	}
	
	public NewsItem get(int index) {
		return mNewsList.get(index);
	}
	
	public void remove(final NewsItem newsItem) {
		mNewsList.remove(newsItem);
	}
	
	public int count() {
		return mNewsList.size();
	}
	
	public void clear() {
		mNewsList.clear();
	}
	
	public void addAll(NewsModel model) {
		ArrayList<NewsItem> newList = model.getNewsList();
		mNewsList.addAll(newList);
	}

}
