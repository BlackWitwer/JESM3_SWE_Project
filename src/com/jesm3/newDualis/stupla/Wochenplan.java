package com.jesm3.newDualis.stupla;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


public class Wochenplan {
	private Date anfangsDatum=null;
	private Date endDatum=null;
	
	/**
	 * Alle Vorlesungen f�r Montag.
	 */
	private ArrayList<Vorlesung> montag;

	/**
	 * Alle Vorlesungen f�r Dienstag.
	 */
	private ArrayList<Vorlesung> dienstag;
	/**
	 * Alle Vorlesungen f�r Mittwoch.
	 */
	private ArrayList<Vorlesung> mittwoch;
	/**
	 * Alle Vorlesungen f�r Donnerstag.
	 */
	private ArrayList<Vorlesung> donnerstag;
	/**
	 * Alle Vorlesungen f�r Freitag.
	 */
	private ArrayList<Vorlesung> freitag;
	/**
	 * Alle Vorlesungen f�r Samstag.
	 */
	private ArrayList<Vorlesung> samstag;

	private int kalenderwoche=0;
	
	public enum Days {

		MONTAG(0),
		DIENSTAG(1),
		MITTWOCH(2),
		DONNERSTAG(3),
		FREITAG(4),
		SAMSTAG(5);
		
		/**
		 * Startet mit 0 am Montag und hört mit 5 am Samstag auf.
		 */
		private int numberInWeek;
		
		private Days(int aNumberInWeek) {
			numberInWeek = aNumberInWeek;
		}
		
		public int getNumberInWeek() {
			return numberInWeek;
		}
	}
	
	public Wochenplan() {
		montag = new ArrayList<Vorlesung>();
		dienstag = new ArrayList<Vorlesung>();
		mittwoch = new ArrayList<Vorlesung>();
		donnerstag = new ArrayList<Vorlesung>();
		freitag = new ArrayList<Vorlesung>();
		samstag = new ArrayList<Vorlesung>();
	}
	
	public Date stringToDate(String date){
		return stringToGreg(date).getTime();
	}
	
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
	
	public Date getAnfangsDatum() {
		return anfangsDatum;
	}

	public void setAnfangsDatum(String anfangsDatum) {
		this.anfangsDatum = stringToDate(anfangsDatum);
	}
	
	public void setAnfangsDatumDate(Date anfangsDatum) {
		this.anfangsDatum = anfangsDatum;
	}

	public Date getEndDatum() {
		return endDatum;
	}
	
	public void setEndDatum(String endDatum) {
		this.endDatum = stringToDate(endDatum);
	}
	
	public void setEndDatumDate(Date endDatum) {
		this.endDatum = endDatum;
	}

	public void setDay(Days aDay, ArrayList<Vorlesung> aVorlesungList) {
		switch (aDay) {
		case MONTAG:
			this.montag = aVorlesungList;
			break;
		case DIENSTAG:
			this.dienstag = aVorlesungList;
			break;
		case MITTWOCH:
			this.mittwoch = aVorlesungList;
			break;
		case DONNERSTAG:
			this.donnerstag = aVorlesungList;
			break;
		case FREITAG:
			this.freitag = aVorlesungList;
			break;
		case SAMSTAG:
			this.samstag = aVorlesungList;
			break;
		}
	}
	
	public ArrayList<Vorlesung> getDay(Days aDay) {
		switch (aDay) {
		case MONTAG:
			return this.montag;
		case DIENSTAG:
			return this.dienstag;
		case MITTWOCH:
			return this.mittwoch;
		case DONNERSTAG:
			return this.donnerstag;
		case FREITAG:
			return this.freitag;
		case SAMSTAG:
			return this.samstag;
		default:
			return null;
		}
	}
	
	public void addToDay(Days aDay, Vorlesung aVorlesung) {
		getDay(aDay).add(aVorlesung);
	}

	/**
	 * @return the kalenderwoche
	 */
	public int getKalenderwoche() {
		return kalenderwoche;
	}

	/**
	 * @param kalenderwoche
	 *            the kalenderwoche to set
	 */
	public void setKalenderwoche(int kalenderwoche) {
		this.kalenderwoche = kalenderwoche;
	}

	public String toString() {
		String sWochenHead ="++++" + anfangsDatum + " - " + endDatum +"++++\n";
		String sMontag = "Montag:\n" + dayToString(montag) + "\n";
		String sDienstag = "Dienstag:\n" + dayToString(dienstag) + "\n";
		String sMittwoch = "Mittwoch:\n" + dayToString(mittwoch) + "\n";
		String sDonnerstag = "Donnerstag:\n" + dayToString(donnerstag) + "\n";
		String sFreitag = "Freitag:\n" + dayToString(freitag) + "\n";
		String sSamstag = "Samstag:\n" + dayToString(samstag) + "\n";
		String stundenplan =sWochenHead + sMontag + sDienstag + sMittwoch + sDonnerstag
				+ sFreitag + sSamstag;
		return stundenplan;
	}

	public String dayToString(ArrayList<Vorlesung> al) {
		String day = "";
		for (int i = 0; i < al.size(); i++) {
			day = day + al.get(i) + "\n";
		}
		return day;
	}

}
