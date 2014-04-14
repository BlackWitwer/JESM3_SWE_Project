package com.jesm3.newDualis.stupla;

import java.util.HashMap;

import com.jesm3.newDualis.stupla.Vorlesung.Requests;

public class VorlesungsplanManager {
	
	private HashMap<Integer, Wochenplan> wochenMap;
	
	public VorlesungsplanManager() {
		this.wochenMap = new HashMap<Integer, Wochenplan>();
	}
	
	public Wochenplan getWochenplan(int aKalenderwoche) {
		return wochenMap.get(aKalenderwoche);
	}
	
	public HashMap<Integer, Wochenplan> getWochenMap() {
		return wochenMap;
	}

	public void addWochenplan(Wochenplan aWochenplan) {
		this.wochenMap.put(aWochenplan.getKalenderwoche(), aWochenplan);
	}
}
