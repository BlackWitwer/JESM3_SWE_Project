package com.jesm3.newDualis.stupla;

import java.util.HashMap;

public class VorlesungsplanManager {
	
	private HashMap<Integer, Wochenplan> wochenMap;
	
	public VorlesungsplanManager() {
		this.wochenMap = new HashMap<Integer, Wochenplan>();
	}
	
	public Wochenplan getWochenplan(int aKalenderwoche) {
/* Statische Werte für den Stundenplan...
		Wochenplan stupla = new Wochenplan();
		
		stupla.setKalenderwoche("41");
		
		stupla.addMontag(new Vorlesung(
				"09:00", 
				"12:30", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addMontag(new Vorlesung(
				"13:30", 
				"16:00", 
				"Rentschler", 
				"Software Engineering"));
		
		stupla.addDienstag(new Vorlesung(
				"09:00", 
				"12:30", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addDienstag(new Vorlesung(
				"13:30", 
				"16:00", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addDienstag(new Vorlesung(
				"16:30", 
				"17:30", 
				"Rentschler", 
				"Software Engineering"));
		
		stupla.addMittwoch(new Vorlesung(
				"09:00", 
				"12:30", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addMittwoch(new Vorlesung(
				"13:30", 
				"16:00", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addMittwoch(new Vorlesung(
				"16:30", 
				"17:30", 
				"Rentschler", 
				"Software Engineering"));
		
		stupla.addDonnerstag(new Vorlesung(
				"09:00", 
				"12:30", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addDonnerstag(new Vorlesung(
				"13:30", 
				"16:00", 
				"Rentschler", 
				"Software Engineering"));
		
		stupla.addFreitag(new Vorlesung(
				"09:00", 
				"12:30", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addFreitag(new Vorlesung(
				"13:30", 
				"16:00", 
				"Rentschler", 
				"Software Engineering"));
		stupla.addFreitag(new Vorlesung(
				"16:30", 
				"17:30", 
				"Rentschler", 
				"Software Engineering")); */
		return wochenMap.get(aKalenderwoche);
	}
	
	public HashMap<Integer, Wochenplan> getWochenMap() {
		return wochenMap;
	}

	public void addWochenplan(Wochenplan aWochenplan) {
		this.wochenMap.put(aWochenplan.getKalenderwoche(), aWochenplan);
	}
}
