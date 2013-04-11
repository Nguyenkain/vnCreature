package com.vncreatures.model.discussion;

import java.util.ArrayList;

public class ThreadModel {
	private ArrayList<Thread> mThreadList = new ArrayList<Thread>();

	public ArrayList<Thread> getThreadList() {
		return mThreadList;
	}

	public void setThreadList(ArrayList<Thread> threadList) {
		this.mThreadList = threadList;
	}
	
	public void add(final Thread item) {
		mThreadList.add(item);
	}
	
	public Thread get(int index) {
		return mThreadList.get(index);
	}
	
	public void remove(final Thread item) {
		mThreadList.remove(item);
	}
	
	public int count() {
		return mThreadList.size();
	}
	
	public void clear() {
		mThreadList.clear();
	}
	
	public void addAll(ThreadModel model) {
		ArrayList<Thread> newList = model.getThreadList();
		mThreadList.addAll(newList);
	}
	
	public int countAllNotification() {
	    int count = 0;
	    for (Thread thread : mThreadList) {
            if(thread.getViewed_status().equalsIgnoreCase("0"))
                count ++;
        }
	    return count;
	}

}
