package com.jesm3.newDualis.stupla;

public class Vorlesung extends AbstractVorlesung {
	
	public enum Requests {
		REQUEST_ALL,
		REQUEST_NEXT,
		REQUEST_LAST
	}
	
	/**
	 * Kostruktor
	 * 
	 * @param uhrzeitVon Beginn der Vorlesung.
	 * @param uhrzeitBis ende der Vorlesung.
	 * @param dozent Name des Dozenten.
	 * @param name Name der Vorlesung.
	 */
	public Vorlesung(String uhrzeitVon, String uhrzeitBis, String dozent,
			String name, String datum, String raum) {
		super(null, name, dozent, datum, uhrzeitVon, uhrzeitBis, raum);
	}
	
	public Vorlesung(AbstractVorlesung aVorlesung) {
		super(aVorlesung.getId(), aVorlesung.getName(), aVorlesung.getDozent(), aVorlesung.getDatum(), aVorlesung.getUhrzeitVon(), aVorlesung.getUhrzeitBis(), aVorlesung.getRaum());
	}
	
//	TODO Wird diese Methode überhaupt gebraucht?? Wenn nicht löschen.
//	public String toString(){
//		return Name+": "+UhrzeitVon+" - "+UhrzeitBis+" von "+Dozent+" am "+Datum+" in "+Raum;
//	}
}
