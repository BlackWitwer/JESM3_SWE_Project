package com.jesm3.newDualis.is;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Wochenplan;
import com.jesm3.newDualis.stupla.Wochenplan.Days;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public class Utilities {
	public static final Locale LOCALE_GERMANY = Locale.GERMANY;

	/**
	 * @return true, wenn eine Internetverbindung besteht.
	 */
	public static boolean checkConnection(Context aContext) {
		ConnectivityManager connectivityManager = 
				(ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}
	
	public static boolean sameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
		                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}
	
	public static Wochenplan addDateToFreedays(Wochenplan wp){
		ArrayList<Vorlesung> stdl = new ArrayList<Vorlesung>();
		for(int i=0; i<Days.values().length; i++) {
//			switch (i) {
//			case 0:  stdl = wp.getMontag();
//	        break;
//			case 1:  stdl = wp.getDienstag();
//	        break;
//			case 2:  stdl = wp.getMittwoch();
//	        break;
//			case 3:  stdl = wp.getDonnerstag();
//	        break;
//			case 4:  stdl = wp.getFreitag();
//	        break;
//			case 5:  stdl = wp.getSamstag();
//	        break;
//	        default: break;
//			}
			stdl = wp.getDay(Days.values()[i]);
			for(int j = 0; j<stdl.size(); j++){
				if(stdl.get(j).getDozent().equals("FREEDAY")) {
					stdl.get(j).setUhrzeitVon(addDaysToDate(wp.getAnfangsDatum(), i));
					stdl.get(j).setUhrzeitBis(addDaysToDate(wp.getAnfangsDatum(), i));
				}
			}
		}
		return wp;
	}
	
	public static GregorianCalendar stringToGreg(String date) {
		String[] theDate = date.split("\\.");
		DateFormat df = new SimpleDateFormat("dd MM yyyy");
		Date dateD = null;
		try {
			dateD = df.parse(theDate[0]+" "+theDate[1]+" "+theDate[2]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Der Java Kalender beginnt bei Tag/Monat/Jahr 0 heisst: 1.10.2013 --> 0.9.2012
		GregorianCalendar gc = new GregorianCalendar(LOCALE_GERMANY);
		gc.setTime(dateD);
		return gc;
	}
	
	public static GregorianCalendar dateToGreg(Date d) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		return gc;
	}
	
	public static Date stringToDate(String date){
		return stringToGreg(date).getTime();
	}
	
	public static Date dateAndTimeToDate(String date, String time){
		Date d = null;
		String[] timeSplit = time.split(":");
		long addTime = Integer.parseInt(timeSplit[0])*1000*60*60;
		addTime = addTime + Integer.parseInt(timeSplit[1])*1000*60;
		long dayTime = stringToDate(date).getTime();
		return new Date(addTime+dayTime);
	}
	
	public static String dateToString(Date d){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", LOCALE_GERMANY);
		if (d != null) {
			return simpleDateFormat.format(d);
		}
		return "";
	}
	
	public static String dateToTime(Date aDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", LOCALE_GERMANY);
		if (aDate != null) {
			return simpleDateFormat.format(aDate);
		}
		return "";
	}
	
	public static int calcMonthsToGo(int weeks){
		Calendar c = Calendar.getInstance();
		Date d = new Date();
//		Log.d("parsetest", "Aktuelles Datum: "+d.toString());
		int actualmonth = d.getMonth();
		Date tarMonth = addDaysToDate(d,7*weeks);
		int monthToGo = tarMonth.getMonth();
//		Log.d("parsetest", "Zieldatum: "+tarMonth.toString());
		if(monthToGo<actualmonth){
			monthToGo = monthToGo + 12 - actualmonth;
		}
		else {
			monthToGo = monthToGo - actualmonth;
		}
		return monthToGo;
	}
	
	public static Date addDaysToDate(Date date, int noOfDays) {
	    Date newDate = new Date(date.getTime());

	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.setTime(newDate);
	    if(noOfDays>0){
	    	calendar.add(Calendar.DATE, noOfDays);
	    }
	    else {
	    	calendar.setTimeInMillis(calendar.getTimeInMillis()+noOfDays*24*60*60*1000);
	    }
	    newDate.setTime(calendar.getTime().getTime());

	    return newDate;
	}
	
	public static ArrayList<Vorlesung> vorlesungenToList(Wochenplan wl){
		ArrayList<Vorlesung> vll = new ArrayList<Vorlesung>();
		for (Days eachDay : Days.values()) {
			vll.addAll(wl.getDay(eachDay));
		}
		//TODO SEW war hier der Fehler wegen falscher Vorlesungen? 2x Mittwoch
//		vll.addAll(wl.getMittwoch());
//		vll.addAll(wl.getDienstag());
//		vll.addAll(wl.getMittwoch());
//		vll.addAll(wl.getDonnerstag());
//		vll.addAll(wl.getFreitag());
//		vll.addAll(wl.getSamstag());
		return vll;
	}
}
