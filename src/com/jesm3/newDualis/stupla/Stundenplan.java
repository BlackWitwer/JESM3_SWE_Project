/**
 * 
 */
package com.jesm3.newDualis.stupla;

import java.util.ArrayList;

public class Stundenplan {
	/**
	 * Alle Vorlesungen f�r Montag.
	 */
	ArrayList<Vorlesung> montag;
	/**
	 * Alle Vorlesungen f�r Dienstag.
	 */
	ArrayList<Vorlesung> dienstag;
	/**
	 * Alle Vorlesungen f�r Mittwoch.
	 */
	ArrayList<Vorlesung> mittwoch;
	/**
	 * Alle Vorlesungen f�r Donnerstag.
	 */
	ArrayList<Vorlesung> donnerstag;
	/**
	 * Alle Vorlesungen f�r Freitag.
	 */
	ArrayList<Vorlesung> freitag;
	/**
	 * Alle Vorlesungen f�r Samstag.
	 */
	ArrayList<Vorlesung> samstag;
	
	String kalenderwoche;
	
	
	public Stundenplan () {
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
	public String getKalenderwoche() {
		return kalenderwoche;
	}


	/**
	 * @param kalenderwoche the kalenderwoche to set
	 */
	public void setKalenderwoche(String kalenderwoche) {
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
	
	public String toString(){
		String sMontag="Montag:\n"+dayToString(montag)+"\n";
		String sDienstag="Dienstag:\n"+dayToString(dienstag)+"\n";
		String sMittwoch="Mittwoch:\n"+dayToString(mittwoch)+"\n";
		String sDonnerstag="Donnerstag:\n"+dayToString(donnerstag)+"\n";
		String sFreitag="Freitag:\n"+dayToString(freitag)+"\n";
		String sSamstag="Samstag:\n"+dayToString(samstag)+"\n";
		String stundenplan=sMontag+sDienstag+sMittwoch+sDonnerstag+sFreitag+sSamstag;
		return stundenplan;
	}

	public String dayToString(ArrayList<Vorlesung> al){
		String day="";
		for(int i=0;i<al.size();i++){
			day=day+al.get(i)+"\n";
		}
		return day;
	}
}
