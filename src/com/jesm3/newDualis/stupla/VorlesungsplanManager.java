package com.jesm3.newDualis.stupla;

public class VorlesungsplanManager {
	public Wochenplan getWochenplan(int aKalenderwoche) {
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
				"Software Engineering"));
		return stupla;
	}
}
