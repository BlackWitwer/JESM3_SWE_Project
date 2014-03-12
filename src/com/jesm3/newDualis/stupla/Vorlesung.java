package com.jesm3.newDualis.stupla;

import com.jesm3.newDualis.generatedDAO.AbstractVorlesung;

public class Vorlesung extends AbstractVorlesung {
	
	public enum Requests {
		/**
		 * Alle Einträge der Datenbank werden geladen. 
		 */
		REQUEST_ALL,
		
		/**
		 * Alle zukünftigen Einträge inklusive des heutigen Tages.
		 */
		REQUEST_NEXT,
		
		/**
		 * Alle vergangenen Einträge exklusive des heutigen Tages.
		 */
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
	
//	TODO Wird diese Methode Ã¼berhaupt gebraucht?? Wenn nicht lÃ¶schen.
//	public String toString(){
//		return Name+": "+UhrzeitVon+" - "+UhrzeitBis+" von "+Dozent+" am "+Datum+" in "+Raum;
//	}
}
