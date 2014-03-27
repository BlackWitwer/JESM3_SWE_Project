package com.jesm3.newDualis.persist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jesm3.newDualis.generatedDAO.AbstractMailContainerDao;
import com.jesm3.newDualis.generatedDAO.AbstractVorlesungDao;
import com.jesm3.newDualis.generatedDAO.DaoMaster;
import com.jesm3.newDualis.generatedDAO.DaoMaster.DevOpenHelper;
import com.jesm3.newDualis.generatedDAO.DaoSession;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.mail.MailContainer;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Vorlesung.Requests;
import com.jesm3.newDualis.noten.Note;

import de.greenrobot.dao.query.QueryBuilder;
import com.jesm3.newDualis.mail.*;
import com.jesm3.newDualis.generatedDAO.*;

public class DatabaseManager {
	
	private SQLiteDatabase db;
	private DaoMaster masterDAO;
	private DaoSession sessionDAO;
	
	private AbstractVorlesungDao vorlesungDAO;
	private AbstractMailContainerDao mailDAO;
	private AbstractNoteDao noteDAO;
	
	private Context context;
	
	public DatabaseManager(CustomApplication anApplication) {
		context = anApplication.getApplicationContext();
		
		init();
	}
	
	/**
	 * Initalisierung der Datenbankensessions für Vorlesungen und Noten.
	 */
	private void init() {
		DevOpenHelper theHelper = new DaoMaster.DevOpenHelper(context, "dualis-db", null);
		db = theHelper.getWritableDatabase();
		masterDAO = new DaoMaster(db);
		sessionDAO = masterDAO.newSession();
		
		vorlesungDAO = sessionDAO.getAbstractVorlesungDao();
		mailDAO = sessionDAO.getAbstractMailContainerDao();
		noteDAO = sessionDAO.getAbstractNoteDao();
	}
	
	/**
	 * Speichert eine Liste von Vorlesungen in die Datenbank.
	 * @param aVorlesungsList die gespeichert werden soll.
	 * @return List der Vorlesungen mit DatenbankID.
	 */
	public List<Vorlesung> insertVorlesungen(List<Vorlesung> aVorlesungsList) {
		vorlesungDAO.insertOrReplaceInTx(aVorlesungsList.toArray(new Vorlesung[0]));
		return aVorlesungsList;
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
	 * Löscht je nach Parameter eine Menge von Vorlesungen aus der Datenbank.
	 * Wird der Parameter nicht erkannt passiert nichts.
	 * @param aRequest der ReqeuestTyp.
	 */
	public void deleteVorlesungen(Requests aRequest) {
		List<Vorlesung> removeList = new ArrayList<Vorlesung>();
		
		switch (aRequest) {
		case REQUEST_ALL:
			vorlesungDAO.deleteAll();
			return;
			
		case REQUEST_NEXT:
			removeList = getVorlesungen(Requests.REQUEST_NEXT);
			vorlesungDAO.deleteInTx(removeList.toArray(new Vorlesung[0]));
			return;
			
		case REQUEST_LAST:
			removeList = getVorlesungen(Requests.REQUEST_LAST);
			vorlesungDAO.deleteInTx(removeList.toArray(new Vorlesung[0]));
			return;
			
		default:
			return;	
		}
	}
	
	/**
	 * Löscht die übergebene Vorlesung.
	 * @param aVorlesung zu löschende Vorlesung.
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
		List<Vorlesung> vorlesungsList = createConcretType(vorlesungDAO.loadAll(), Vorlesung.class);
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
	
	public MailContainer insertMailContainer(MailContainer aMail) {
		mailDAO.insertOrReplace(aMail);
		return aMail;
	}
	
	public void deleteMailContainer(MailContainer aMail) {
		mailDAO.delete(aMail);
	}
	
	/**
	 * Gibt alle gecachten MailContainer zur�ck.
	 * @return die Menge der MailContainer.
	 */
	public List<MailContainer> getMailContainer() {
		return createConcretType(mailDAO.loadAll(), MailContainer.class);
	}
	
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
		return createConcretType(noteDAO.loadAll(), Note.class);
	}
	
	/**
	 * Erstellt aus einer Liste von Abstracten Objekten eine Sub Type Klasse vom Typ T.
	 * @param aList die Liste der Abstracten Objekte.
	 * @param aClass die Sub Type Klasse.
	 * @return die Liste der konkretisierten Objekte.
	 */
	private <K, T extends K> List<T> createConcretType(List<K> aList, Class<T> aClass) {
		List<T> theResultList = new ArrayList<T>();
		
		for (K eachData : aList) {
			try {
				theResultList.add(aClass.getConstructor(eachData.getClass()).newInstance(eachData));
			} catch (Exception e) {
				// Sollte bei richtiger Verwendung der Methode nicht vorkommen...
				e.printStackTrace();
			}
		}
		
		return theResultList;
	}
	
	public DaoMaster getMaster() {
		return masterDAO;
	}
}
