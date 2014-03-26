package com.jesm3.newDualis.persist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;

import com.jesm3.newDualis.generatedDAO.AbstractNote;
import com.jesm3.newDualis.generatedDAO.AbstractNoteDao;
import com.jesm3.newDualis.generatedDAO.AbstractVorlesung;
import com.jesm3.newDualis.generatedDAO.AbstractVorlesungDao;
import com.jesm3.newDualis.generatedDAO.DaoMaster;
import com.jesm3.newDualis.generatedDAO.DaoMaster.DevOpenHelper;
import com.jesm3.newDualis.generatedDAO.DaoSession;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Vorlesung.Requests;

public class DatabaseManager {
	
	private SQLiteDatabase db;
	private DaoMaster masterDAO;
	private DaoSession sessionDAO;
	
	private AbstractVorlesungDao vorlesungDAO;
	private AbstractNoteDao noteDAO;
	
	private Context context;
	
	public DatabaseManager(CustomApplication anApplication) {
		context = anApplication.getApplicationContext();
		
		init();
	}
	
	private void init() {
		DevOpenHelper theHelper = new DaoMaster.DevOpenHelper(context, "dualis-db", null);
		db = theHelper.getWritableDatabase();
		masterDAO = new DaoMaster(db);
		sessionDAO = masterDAO.newSession();
		
		vorlesungDAO = sessionDAO.getAbstractVorlesungDao();
		noteDAO = sessionDAO.getAbstractNoteDao();
	}
	
	/**
	 * Speichert eine Vorlesung in die Datenbank.
	 * @param aVorlesung die gespeichert werden soll.
	 * @return Vorlesung mit DatenbankID.
	 */
	public Vorlesung insertVorlesung(Vorlesung aVorlesung) {
		vorlesungDAO.insert(aVorlesung);
		return aVorlesung;
	}
	/**
	 * Löscht eine Vorlesung.
	 * @param aVorlesung die gel�scht werden soll.
	 */
	public void deleteVorlesung(Vorlesung aVorlesung) {
		vorlesungDAO.delete(aVorlesung);
	}
	
	/**
	 * Gibt je nach Parameter eine Menge von Vorlesungen zurück.
	 * @param aRequest der Request Type.
	 * @return die Menge der Vorlesungen. Eine leere Liste, wenn der Request Type nicht gefunden wird.
	 */
	public List<Vorlesung> getVorlesungen(Requests aRequest) {
		// TODO Bastis Umwandelmethode verwenden.
		List<Vorlesung> vorlesungsList = createVorlesung(vorlesungDAO.loadAll());
		List<Vorlesung> removeList = new ArrayList<Vorlesung>();
		
		// Heute = System.currentTimeMillis() - die Millisekunden eines ganzen Tages (ansonsten werden heutige Vorlesungen als Vergangen betrachtet).
		long einTag = 86400000;
		long heute = System.currentTimeMillis() - einTag;

		switch (aRequest) {
		case REQUEST_ALL:
			return vorlesungsList;
			
		case REQUEST_NEXT:						
			for (Vorlesung eachVorlesung : vorlesungsList) {
				if (eachVorlesung.getUhrzeitVon().getTime() < heute) {
					removeList.add(eachVorlesung);
				}
			}
			vorlesungsList.removeAll(removeList);
			return vorlesungsList;
			
		case REQUEST_LAST:			
			for (Vorlesung eachVorlesung : vorlesungsList) {
				if (eachVorlesung.getUhrzeitVon().getTime() < heute) {
					removeList.add(eachVorlesung);
				}
			}
			vorlesungsList.removeAll(removeList);
			return vorlesungsList;
			
		default:
			return new ArrayList<Vorlesung>();
		}
	}
	
	// TODO: Michas Noten-Klasse importieren.
	/**
	 * Speichert eine Note in die Datenbank.
	 * @param aNote, zu speichernde Note.
	 * @return Note mit DatenbankID.
	 */
	public Note insertNote(Note aNote) {
		noteDAO.insert(aNote);
		return aNote;
	}
	
	/**
	 * Löscht eine Note aus der Datenbank.
	 * @param aNote, zu löschende Note.
	 */
	public void deleteNote(Note aNote) {
		noteDAO.delete(aNote);
	}
	
	/**
	 * Lädt alle Noten(Einträge) aus der Datenbank.
	 * @return Noten als List<Noten>.
	 */
	public List<Note> getNoten () {
		// TODO Bastis Umwandelmethode verwenden.
		//return createNote(noteDAO.loadAll());
	}
	
	public DaoMaster getMaster() {
		return masterDAO;
	}
}
