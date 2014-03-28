package com.jesm3.newDualis.stupla;

import java.util.Date;

import com.jesm3.newDualis.generatedDAO.AbstractVorlesung;

public class Vorlesung extends AbstractVorlesung {
	
	public enum Requests {
		/**
		 * Alle Eintr�ge der Datenbank werden geladen. 
		 */
		REQUEST_ALL,
		
		/**
		 * Alle zuk�nftigen Eintr�ge inklusive des heutigen Tages.
		 */
		REQUEST_NEXT,
		
		/**
		 * Alle vergangenen Eintr�ge exklusive des heutigen Tages.
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
	public Vorlesung(Date uhrzeitVon, Date uhrzeitBis, String dozent,
			String name, String raum) {
		super(null, name, dozent, uhrzeitVon, uhrzeitBis, raum);
	}
	/**
	 * Kostruktor
	 * 
	 * @param uhrzeitVon Beginn der Vorlesung.
	 * @param uhrzeitBis ende der Vorlesung.
	 * @param dozent Name des Dozenten.
	 * @param name Name der Vorlesung.
	 */
	public Vorlesung(Date uhrzeitVon, Date uhrzeitBis, String dozent,
			String name) {
		this(uhrzeitVon,uhrzeitBis,dozent,name,"");
	}
	
	public Vorlesung() {
		this(null,null,"","");
	}
	
	public Vorlesung(AbstractVorlesung aVorlesung) {
		super(aVorlesung.getId(), aVorlesung.getName(), aVorlesung.getDozent(), aVorlesung.getUhrzeitVon(), aVorlesung.getUhrzeitBis(), aVorlesung.getRaum());
	}
}
