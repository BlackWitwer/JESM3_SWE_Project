package com.jesm3.newDualis.jinterface;

import java.util.ArrayList;
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
		if(v.getDozent().equals("DRFAIL")==false){
			setKalenderwoche(v);
		}
	}
	
	public Wochenplan mergeWeeks(Wochenplan w1, Wochenplan w2) {
		ArrayList<ArrayList<Vorlesung>> w1l = getDays(w1);
		ArrayList<ArrayList<Vorlesung>> w2l = getDays(w2);
		boolean merge = false;
		for(int i=0; i<6; i++){
			merge = w1l.get(i).get(0).getDozent().equals("DRFAIL");
			if(merge){
				w1l.set(i, w2l.get(i));
			}
		}
		if(merge){
			return listToWochenplan(w1l);
		}
		else{
			return null;
		}
	}
	
	public ArrayList<ArrayList<Vorlesung>> getDays(Wochenplan w){
		ArrayList<ArrayList<Vorlesung>> daylist= new ArrayList<ArrayList<Vorlesung>>();
		daylist.add(w.getMontag());
		daylist.add(w.getDienstag());
		daylist.add(w.getMittwoch());
		daylist.add(w.getDonnerstag());
		daylist.add(w.getFreitag());
		daylist.add(w.getSamstag());
		return daylist;
	}
	
	public Wochenplan listToWochenplan(ArrayList<ArrayList<Vorlesung>> l){
		Wochenplan out = new Wochenplan();
		out.setMontag(l.get(0));
		out.setDienstag(l.get(1));
		out.setMittwoch(l.get(2));
		out.setDonnerstag(l.get(3));
		out.setFreitag(l.get(4));
		out.setSamstag(l.get(5));
		return out;
	}
	
	private void setKalenderwoche(Vorlesung aVorlesung) {
		//TODO was passiert wenn eine Woche keine Vorlesung hat oder keine Vorlesung ein Datum?
		String[] theDate = aVorlesung.getDatum().split("\\.");
		//Der Java Kalender beginnt bei Tag/Monat/Jahr 0 heisst: 1.10.2013 --> 0.9.2012
		GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(theDate[2])-1, Integer.parseInt(theDate[1])-1, Integer.parseInt(theDate[0])-1);
		std.setKalenderwoche(gc.get(GregorianCalendar.WEEK_OF_YEAR));
	}
}