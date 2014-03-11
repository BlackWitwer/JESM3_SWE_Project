package com.jesm3.newDualis.jinterface;

import java.util.Date;
import java.util.GregorianCalendar;

import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Wochenplan;

public class StundenplanGenerator {
	
	private Wochenplan std= new Wochenplan();
	
	public Wochenplan getStd() {
		return std;
	}
	
	public StundenplanGenerator() {
		std =  new Wochenplan();
	}
	
	public void addVorlesung(int day, Vorlesung v){
		switch (day) {
		case 0:  std.addMontag(v);
        break;
		case 1:  std.addDienstag(v);
        break;
		case 2:  std.addMittwoch(v);
        break;
		case 3:  std.addDonnerstag(v);
        break;
		case 4:  std.addFreitag(v);
        break;
		case 5:  std.addSamstag(v);
        break;
        default: break;
		}
		setKalenderwoche(v);
	}
	
	private void setKalenderwoche(Vorlesung aVorlesung) {
		//TODO was passiert wenn eine Woche keine Vorlesung hat oder keine Vorlesung ein Datum?
		String[] theDate = aVorlesung.getDatum().split("\\.");
		//Der Java Kalender beginnt bei Tag/Monat/Jahr 0 heisst: 1.10.2013 --> 0.9.2012
		GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(theDate[2])-1, Integer.parseInt(theDate[1])-1, Integer.parseInt(theDate[0])-1);
		std.setKalenderwoche(gc.get(GregorianCalendar.WEEK_OF_YEAR));
	}
}