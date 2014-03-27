package com.jesm3.newDualis.noten;

import java.util.ArrayList;

import com.jesm3.newDualis.jinterface.DualisParser;

public class NotenManager {
	
	ArrayList<Note> noten;
	
	public NotenManager() {
		noten = new ArrayList<Note>();
	}

	public ArrayList<Note> getNoten() {
		return noten;
	}

	public void setNoten(ArrayList<Note> noten) {
		this.noten = noten;
	}
	
}