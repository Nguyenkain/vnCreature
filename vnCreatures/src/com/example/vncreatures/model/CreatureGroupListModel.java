package com.example.vncreatures.model;

import java.util.ArrayList;

public class CreatureGroupListModel {
	private ArrayList<CreatureGroup> mCreatureGroupList = new ArrayList<CreatureGroup>();

	public ArrayList<CreatureGroup> getCreatureList() {
		return mCreatureGroupList;
	}

	public void setCreatureList(ArrayList<CreatureGroup> creatureList) {
		this.mCreatureGroupList = creatureList;
	}
	
	public void add(final CreatureGroup creature) {
		mCreatureGroupList.add(creature);
	}
	
	public CreatureGroup get(int index) {
		return mCreatureGroupList.get(index);
	}
	
	public void remove(final Creature creature) {
		mCreatureGroupList.remove(creature);
	}
	
	public int count() {
		return mCreatureGroupList.size();
	}
	
	public void clear() {
		mCreatureGroupList.clear();
	}
	
	public void addAll(CreatureGroupListModel model) {
		ArrayList<CreatureGroup> creatures = model.getCreatureList();
		mCreatureGroupList.addAll(creatures);
	}

}
