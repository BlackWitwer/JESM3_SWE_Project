package com.jesm3.newDualis.is;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

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
		long dayTime = stringToDate(date).getTime();
		return new Date(addTime+dayTime);
	}
	
	public String dateToString(Date d){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return simpleDateFormat.format(d);
	}
}
