package com.jesm3.newDualis.jinterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

import com.jesm3.newDualis.is.Utilities;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Wochenplan;

public class StundenplanGenerator {
	private Utilities util = new Utilities();
	private Wochenplan std= new Wochenplan();
	
	public ArrayList<Wochenplan> generateStundenplan(ArrayList<Vorlesung> alleVorlesungen) {
		ArrayList<Wochenplan> stdl = new ArrayList<Wochenplan>();
		GregorianCalendar gc = new GregorianCalendar();
		std = new Wochenplan();
		return stdl;
	}

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
	}
	
	public Wochenplan mergeWeeks(Wochenplan w1, Wochenplan w2) {
		ArrayList<ArrayList<Vorlesung>> w1l = getDays(w1);
		ArrayList<ArrayList<Vorlesung>> w2l = getDays(w2);
		boolean merge = false;
		for(int i=0; i<6; i++){
			merge = w1l.get(i).get(0).getDozent().equals("DRFAIL");  //Unterscheidung ferien/monatsende???
			if(merge){
				w1l.set(i, w2l.get(i));
			}
		}
		if(merge){
			Wochenplan resultwl = listToWochenplan(w1l);
			resultwl.setAnfangsDatumDate(w1.getAnfangsDatum());
			resultwl.setEndDatumDate(w1.getEndDatum());
			return resultwl;
		}
		else{
			return null;
		}
	}
	
	public Date addDaysToDate(Date date, int noOfDays) {
	    Date newDate = new Date(date.getTime());

	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.setTime(newDate);
	    calendar.add(Calendar.DATE, noOfDays);
	    newDate.setTime(calendar.getTime().getTime());
	    return newDate;
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
	
	public void setKalenderwoche(String somedate) {
		//TODO was passiert wenn eine Woche keine Vorlesung hat oder keine Vorlesung ein Datum?
		GregorianCalendar gc = util.stringToGreg(somedate);
		std.setKalenderwoche(gc.get(GregorianCalendar.WEEK_OF_YEAR));
	}
}