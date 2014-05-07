package com.jesm3.newDualis.jinterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jesm3.newDualis.is.Utilities;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.VorlesungComparator;
import com.jesm3.newDualis.stupla.Wochenplan;
import com.jesm3.newDualis.stupla.Wochenplan.Days;

public class StundenplanGenerator {
	
	public static final String LAST_LOADED_WEEK = "letzteGeladeneWoche";
	
	private Utilities util = new Utilities();
	private Wochenplan std= new Wochenplan();
	
	public ArrayList<Wochenplan> generateStundenplan(ArrayList<Vorlesung> alleVorlesungen) {
		ArrayList<Wochenplan> stdl = new ArrayList<Wochenplan>();
		GregorianCalendar gc = new GregorianCalendar(Locale.GERMAN);
		gc.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//		alleVorlesungen = sortVorlesungen(alleVorlesungen);
		std = new Wochenplan();
		std.setAnfangsDatumDate(alleVorlesungen.get(0).getUhrzeitVon());
		Days lastDay = Days.MONTAG;
		for(int i = 0; i<alleVorlesungen.size(); i++) {
			gc.setTime(alleVorlesungen.get(i).getUhrzeitVon());
			Days day = dayOfWeekTranslate(GregorianCalendar.DAY_OF_WEEK);
			if(lastDay == Days.SAMSTAG && day == Days.MONTAG){
				std.setEndDatumDate(std.getDay(Days.SAMSTAG).get(0).getUhrzeitVon());
				std.setKalenderwoche(gc.get(GregorianCalendar.WEEK_OF_YEAR));
				stdl.add(std);
				std = new Wochenplan();
				std.setAnfangsDatumDate(alleVorlesungen.get(i).getUhrzeitVon());
			}
//			Log.d("gstd", alleVorlesungen.get(i).getUhrzeitVon().toGMTString());
			addVorlesung(day, alleVorlesungen.get(i));
			lastDay = day;
		}
		std.setEndDatumDate(std.getDay(Days.SAMSTAG).get(0).getUhrzeitVon());
		std.setKalenderwoche(gc.get(GregorianCalendar.WEEK_OF_YEAR));
		stdl.add(std);
		return stdl;
	}
	
	public Days dayOfWeekTranslate(int dow) {
		Days rt = Days.MONTAG;
		switch (dow) {
		case GregorianCalendar.MONDAY:  rt = Days.MONTAG;
        break;
		case GregorianCalendar.TUESDAY:  rt = Days.DIENSTAG;
        break;
		case GregorianCalendar.WEDNESDAY:  rt = Days.MITTWOCH;
        break;
		case GregorianCalendar.THURSDAY:  rt = Days.DONNERSTAG;
        break;
		case GregorianCalendar.FRIDAY:  rt = Days.FREITAG;
        break;
		case GregorianCalendar.SATURDAY:  rt = Days.SAMSTAG;
        break;
        default: break;
		}
		return rt;
	}
	
	public ArrayList<Vorlesung> sortVorlesungen(ArrayList<Vorlesung> alleVorlesungen) {
        VorlesungComparator comp = new VorlesungComparator();
        Collections.sort(alleVorlesungen, comp);
		return alleVorlesungen;
	}

	public Wochenplan getStd() {
		return std;
	}
	
	public StundenplanGenerator() {
		std =  new Wochenplan();
	}
	
	public void addVorlesung(Days day, Vorlesung v){
		std.addToDay(day, v);
//		switch (day) {
//		case 0:  std.addMontag(v);
//        break;
//		case 1:  std.addDienstag(v);
//        break;
//		case 2:  std.addMittwoch(v);
//        break;
//		case 3:  std.addDonnerstag(v);
//        break;
//		case 4:  std.addFreitag(v);
//        break;
//		case 5:  std.addSamstag(v);
//        break;
//        default: break;
//		}
	}
	
	public Wochenplan mergeWeeks(Wochenplan w1, Wochenplan w2) {
		ArrayList<ArrayList<Vorlesung>> w1l = getDays(w1);
		ArrayList<ArrayList<Vorlesung>> w2l = getDays(w2);
		boolean merge = false;
		for(int i=0; i<6; i++){
			merge = w1l.get(i).size() == 0;
//			merge = w1l.get(i).get(0).getDozent().equals("DRFAIL");  //Unterscheidung ferien/monatsende???
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
		for (Days eachDay : Days.values()) {
			daylist.add(w.getDay(eachDay));
		}
//		daylist.add(w.getMontag());
//		daylist.add(w.getDienstag());
//		daylist.add(w.getMittwoch());
//		daylist.add(w.getDonnerstag());
//		daylist.add(w.getFreitag());
//		daylist.add(w.getSamstag());
		return daylist;
	}
	
	public Wochenplan listToWochenplan(ArrayList<ArrayList<Vorlesung>> l){
		Wochenplan out = new Wochenplan();
		int i = 0;
		for (Days eachDay : Days.values()) {
			out.setDay(eachDay, l.get(i++));
		}
//		out.setMontag(l.get(0));
//		out.setDienstag(l.get(1));
//		out.setMittwoch(l.get(2));
//		out.setDonnerstag(l.get(3));
//		out.setFreitag(l.get(4));
//		out.setSamstag(l.get(5));
		return out;
	}
	
	public void setKalenderwoche(String somedate) {
		//TODO was passiert wenn eine Woche keine Vorlesung hat oder keine Vorlesung ein Datum?
		GregorianCalendar gc = util.stringToGreg(somedate);
		std.setKalenderwoche(gc.get(GregorianCalendar.WEEK_OF_YEAR));
	}
	
	public List<Wochenplan> generateWochenplaene(List<Vorlesung> aVorlesungList, Context aContext) {
		//Sortiere die Liste nach UhrzeitVon.
		Collections.sort(aVorlesungList, new Comparator<Vorlesung>() {
			@Override
			public int compare(Vorlesung lhs, Vorlesung rhs) {
				return lhs.getUhrzeitBis().compareTo(rhs.getUhrzeitVon());
			}
		});
		
		//Sortieren Vorlesungen in WochenplanMap.
		Map<Integer, Wochenplan> theWochenplanMap = new HashMap<Integer, Wochenplan>();
		GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
		int theWeek;
		int theDay;
		for (Vorlesung eachVorlesung : aVorlesungList) {
			calendar.setTime(eachVorlesung.getUhrzeitVon());
			theWeek = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
			theDay = calendar.get(GregorianCalendar.DAY_OF_WEEK);
			if (!theWochenplanMap.containsKey(theWeek)) {
				Wochenplan theWochenplan = new Wochenplan();
				theWochenplanMap.put(theWeek, theWochenplan);
				//-2, da der Montag die Zahl 2 hat und im Fall des Montags nichts abgezogen werden soll.
				calendar.add(GregorianCalendar.DAY_OF_WEEK, -(calendar.get(GregorianCalendar.DAY_OF_WEEK)-2));
				theWochenplan.setAnfangsDatumDate(calendar.getTime());
				theWochenplan.setKalenderwoche(calendar.get(GregorianCalendar.WEEK_OF_YEAR));
				calendar.add(GregorianCalendar.DAY_OF_WEEK, 6);
				theWochenplan.setEndDatumDate(calendar.getTime());
			}
			
			Wochenplan theWochenplan = theWochenplanMap.get(theWeek);
			//Der Tag minus 2 entspricht der Stelle im Array, da Montag im Gregorian Kalender die Nummer 2 hat und im Array die Nummer 0.
			theWochenplan.addToDay(Days.values()[theDay-2], eachVorlesung);
		}
		
		//Finish Wochenpläne. Fügt leere Wochen ein.
		SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(aContext);
		int lastWeek = thePrefs.getInt(LAST_LOADED_WEEK, 0);
		calendar.setTime(new Date(System.currentTimeMillis()));
		int firstWeek = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
		
		for (int i = firstWeek; i <= lastWeek; i++) {
			if (!theWochenplanMap.containsKey(i)) {
				Wochenplan theWochenplan = new Wochenplan();
				theWochenplanMap.put(i, theWochenplan);
				//-2, da der Montag die Zahl 2 hat und im Fall des Montags nichts abgezogen werden soll.
				calendar.add(GregorianCalendar.DAY_OF_WEEK, -(calendar.get(GregorianCalendar.DAY_OF_WEEK)-2));
				theWochenplan.setAnfangsDatumDate(calendar.getTime());
				theWochenplan.setKalenderwoche(calendar.get(GregorianCalendar.WEEK_OF_YEAR));
				//Hier findet ein Wechsel der Kalenderwoche statt.
				calendar.add(GregorianCalendar.DAY_OF_WEEK, 6);
				theWochenplan.setEndDatumDate(calendar.getTime());
			} else {
				calendar.add(GregorianCalendar.WEEK_OF_YEAR, 1);
			}
		}
		
		//Füllt leere Tage mit einer Freien Tag Vorlesung auf.
		List<Wochenplan> theWochenplaene = new ArrayList<Wochenplan>();
		theWochenplaene.addAll(theWochenplanMap.values());
		
		GregorianCalendar theCalendar = new GregorianCalendar(Locale.GERMANY);
		for (Wochenplan eachWochenplan : theWochenplaene) {
			for (Days eachDay : Days.values()) {
				if (eachWochenplan.getDay(eachDay).isEmpty()) {
					Vorlesung theVorlesung = new Vorlesung();
					theVorlesung.setDozent("FREEDAY");
					
					theCalendar.setTime(eachWochenplan.getAnfangsDatum());
					theCalendar.add(GregorianCalendar.DAY_OF_WEEK, eachDay.getNumberInWeek());
					
					theVorlesung.setUhrzeitVon(theCalendar.getTime());
					eachWochenplan.addToDay(eachDay, theVorlesung);
				}
			}
		}
		return theWochenplaene;
	}
}