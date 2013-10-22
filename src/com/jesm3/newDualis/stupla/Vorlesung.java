package com.jesm3.newDualis.stupla;

public class Vorlesung {
	private String UhrzeitVon;
	private String UhrzeitBis;
	private String Dozent;
	private String Name;
	/**
	 * Kostruktor
	 * 
	 * @param uhrzeitVon Beginn der Vorlesung.
	 * @param uhrzeitBis ende der Vorlesung.
	 * @param dozent Name des Dozenten.
	 * @param name Name der Vorlesung.
	 */
	public Vorlesung(String uhrzeitVon, String uhrzeitBis, String dozent,
			String name) {
		super();
		UhrzeitVon = uhrzeitVon;
		UhrzeitBis = uhrzeitBis;
		Dozent = dozent;
		Name = name;
	}
	
	/**
	 * @return the uhrzeitVon
	 */
	public String getUhrzeitVon() {
		return UhrzeitVon;
	}
	/**
	 * @param uhrzeitVon the uhrzeitVon to set
	 */
	public void setUhrzeitVon(String uhrzeitVon) {
		UhrzeitVon = uhrzeitVon;
	}
	/**
	 * @return the uhrzeitBis
	 */
	public String getUhrzeitBis() {
		return UhrzeitBis;
	}
	/**
	 * @param uhrzeitBis the uhrzeitBis to set
	 */
	public void setUhrzeitBis(String uhrzeitBis) {
		UhrzeitBis = uhrzeitBis;
	}
	/**
	 * @return the dozent
	 */
	public String getDozent() {
		return Dozent;
	}
	/**
	 * @param dozent the dozent to set
	 */
	public void setDozent(String dozent) {
		Dozent = dozent;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	
	public String toString(){
		return Name+": "+UhrzeitVon+" - "+UhrzeitBis+" von "+Dozent;
	}

}
