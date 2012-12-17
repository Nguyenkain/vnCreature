package com.example.vncreatures.model;

import java.util.ArrayList;

public class CreatureModel {
	private ArrayList<Creature> mCreatureList = new ArrayList<Creature>();

	public ArrayList<Creature> getCreatureList() {
		return mCreatureList;
	}

	public void setCreatureList(ArrayList<Creature> creatureList) {
		this.mCreatureList = creatureList;
	}
	
	public void add(final Creature creature) {
		mCreatureList.add(creature);
	}
	
	public Creature get(int index) {
		return mCreatureList.get(index);
	}
	
	public void remove(final Creature creature) {
		mCreatureList.remove(creature);
	}
	
	public int count() {
		return mCreatureList.size();
	}
	
	public void clear() {
		mCreatureList.clear();
	}

}
