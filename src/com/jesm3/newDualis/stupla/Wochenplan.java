/**
 * 
 */
package com.jesm3.newDualis.stupla;

import java.util.ArrayList;

public class Wochenplan {
	/**
	 * Alle Vorlesungen f�r Montag.
	 */
	private String anfangsDatum;
	private String endDatum;
	public String getAnfangsDatum() {
		return anfangsDatum;
	}

	public void setAnfangsDatum(String anfangsDatum) {
		this.anfangsDatum = anfangsDatum;
	}

	public String getEndDatum() {
		return endDatum;
	}

	public void setEndDatum(String endDatum) {
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

	private int kalenderwoche;

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
