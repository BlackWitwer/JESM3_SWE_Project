package com.jesm3.newDualis.is;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Wochenplan;

import android.util.Log;

public class Utilities {
	public GregorianCalendar stringToGreg(String date) {
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
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dateD);
		return gc;
	}
	
	public GregorianCalendar dateToGreg(Date d) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		return gc;
	}
	
	public Date stringToDate(String date){
		return stringToGreg(date).getTime();
	}
	
	public Date dateAndTimeToDate(String date, String time){
		Date d = null;
		String[] timeSplit = time.split(":");
		long addTime = Integer.parseInt(timeSplit[0])*1000*60*60;
		addTime = addTime + Integer.parseInt(timeSplit[1])*1000*60;
		long dayTime = stringToDate(date).getTime();
		return new Date(addTime+dayTime);
	}
	
	public String dateToString(Date d){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return simpleDateFormat.format(d);
	}
	
	public int calcMonthsToGo(int weeks){
		Calendar c = Calendar.getInstance();
		Date d = new Date();
		Log.d("parsetest", "Aktuelles Datum: "+d.toString());
		int actualmonth = d.getMonth();
		Date tarMonth = addDaysToDate(d,7*weeks);
		int monthToGo = tarMonth.getMonth();
		Log.d("parsetest", "Zieldatum: "+tarMonth.toString());
		if(monthToGo<actualmonth){
			monthToGo = monthToGo + 12 - actualmonth;
		}
		else {
			monthToGo = monthToGo - actualmonth;
		}
		return monthToGo;
	}
	
	public Date addDaysToDate(Date date, int noOfDays) {
	    Date newDate = new Date(date.getTime());

	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.setTime(newDate);
	    calendar.add(Calendar.DATE, noOfDays);
	    newDate.setTime(calendar.getTime().getTime());

	    return newDate;
	}
	
	public ArrayList<Vorlesung> vorlesungenToList(Wochenplan wl){
		ArrayList<Vorlesung> vll = new ArrayList<Vorlesung>();
		vll.addAll(wl.getMittwoch());
		vll.addAll(wl.getDienstag());
		vll.addAll(wl.getMittwoch());
		vll.addAll(wl.getDonnerstag());
		vll.addAll(wl.getFreitag());
		vll.addAll(wl.getSamstag());
		return vll;
	}
}
