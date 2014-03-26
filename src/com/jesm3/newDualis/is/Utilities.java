package com.jesm3.newDualis.is;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utilities {
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
		GregorianCalendar gc = new GregorianCalendar();
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
		long dayTime = stringToDate(date).getTime();
		return new Date(addTime+dayTime);
	}
	
	public static String dateToString(Date d){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		if (d != null) {
			return simpleDateFormat.format(d);
		}
		return "";
	}
	
	public static String dateToTime(Date aDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
		if (aDate != null) {
			return simpleDateFormat.format(aDate);
		}
		return "";
	}
}
