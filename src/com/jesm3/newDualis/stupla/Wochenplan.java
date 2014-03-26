package com.jesm3.newDualis.stupla;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


public class Wochenplan {
	/**
	 * Alle Vorlesungen f�r Montag.
	 */
	private Date anfangsDatum=null;
	private Date endDatum=null;
	
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

	private ArrayList<Vorlesung> montag;
	public void setMontag(ArrayList<Vorlesung> montag) {
		this.montag = montag;
	}

	public void setDienstag(ArrayList<Vorlesung> dienstag) {
		this.dienstag = dienstag;
	}

	public void setMittwoch(ArrayList<Vorlesung> mittwoch) {
		this.mittwoch = mittwoch;
	}

	public void setDonnerstag(ArrayList<Vorlesung> donnerstag) {
		this.donnerstag = donnerstag;
	}

	public void setFreitag(ArrayList<Vorlesung> freitag) {
		this.freitag = freitag;
	}

	public void setSamstag(ArrayList<Vorlesung> samstag) {
		this.samstag = samstag;
	}

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

	public Wochenplan() {
		montag = new ArrayList<Vorlesung>();
		dienstag = new ArrayList<Vorlesung>();
		mittwoch = new ArrayList<Vorlesung>();
		donnerstag = new ArrayList<Vorlesung>();
		freitag = new ArrayList<Vorlesung>();
		samstag = new ArrayList<Vorlesung>();
	}

	/**
	 * F�gt dem Tag eine Vorlesung hinzu.
	 */
	public void addMontag(Vorlesung aVorlesung) {
		montag.add(aVorlesung);
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

	/**
	 * F�gt dem Tag eine Vorlesung hinzu.
	 */
	public void addDienstag(Vorlesung aVorlesung) {
		dienstag.add(aVorlesung);
	}

	/**
	 * F�gt dem Tag eine Vorlesung hinzu.
	 */
	public void addMittwoch(Vorlesung aVorlesung) {
		mittwoch.add(aVorlesung);
	}

	/**
	 * F�gt dem Tag eine Vorlesung hinzu.
	 */
	public void addDonnerstag(Vorlesung aVorlesung) {
		donnerstag.add(aVorlesung);
	}

	/**
	 * F�gt dem Tag eine Vorlesung hinzu.
	 */
	public void addFreitag(Vorlesung aVorlesung) {
		freitag.add(aVorlesung);
	}

	/**
	 * F�gt dem Tag eine Vorlesung hinzu.
	 */
	public void addSamstag(Vorlesung aVorlesung) {
		samstag.add(aVorlesung);
	}

	/**
	 * @return the montag
	 */
	public ArrayList<Vorlesung> getMontag() {
		return montag;
	}

	/**
	 * @return the dienstag
	 */
	public ArrayList<Vorlesung> getDienstag() {
		return dienstag;
	}

	/**
	 * @return the mittwoch
	 */
	public ArrayList<Vorlesung> getMittwoch() {
		return mittwoch;
	}

	/**
	 * @return the donnerstag
	 */
	public ArrayList<Vorlesung> getDonnerstag() {
		return donnerstag;
	}

	/**
	 * @return the freitag
	 */
	public ArrayList<Vorlesung> getFreitag() {
		return freitag;
	}

	/**
	 * @return the samstag
	 */
	public ArrayList<Vorlesung> getSamstag() {
		return samstag;
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
