package com.jesm3.newDualis.persist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jesm3.newDualis.generatedDAO.AbstractVorlesung;
import com.jesm3.newDualis.generatedDAO.AbstractVorlesungDao;
import com.jesm3.newDualis.generatedDAO.DaoMaster;
import com.jesm3.newDualis.generatedDAO.DaoMaster.DevOpenHelper;
import com.jesm3.newDualis.generatedDAO.DaoSession;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Vorlesung.Requests;

import de.greenrobot.dao.query.QueryBuilder;

public class DatabaseManager {
	
	private SQLiteDatabase db;
	private DaoMaster masterDAO;
	private DaoSession sessionDAO;
	
	private AbstractVorlesungDao vorlesungDAO;
	
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
	 * @param aVorlesung die gelöscht werden soll.
	 */
	public void deleteVorlesung(Vorlesung aVorlesung) {
		vorlesungDAO.delete(aVorlesung);
	}
	
	/**
	 * Gibt je nach Parameter eine Menge von Vorlesungen zurÃ¼ck.
	 * @param aRequest der Request Type.
	 * @return die Menge der Vorlesungen. Eine leere Liste, wenn der Request Type nicht gefunden wird.
	 */
	public List<Vorlesung> getVorlesungen(Requests aRequest) {
		List<Vorlesung> vorlesungsList = createVorlesung(vorlesungDAO.loadAll());
		List<Vorlesung> removeList = new ArrayList<Vorlesung>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yyyy" );
		Date datumVorlesung = new Date();
		// Heute = System.currentTimeMillis() - die Millisekunden eines ganzen Tages (ansonsten werden heutige Vorlesungen als Vergangen betrachtet).
		long einTag = 86400000;
		long heute = System.currentTimeMillis() - einTag;

		switch (aRequest) {
		case REQUEST_ALL:
			return createVorlesung(vorlesungDAO.loadAll());
			
		case REQUEST_NEXT:						
			for (Vorlesung eachVorlesung : vorlesungsList) {
				try {
					datumVorlesung = dateFormat.parse(eachVorlesung.getDatum());
					
					if (datumVorlesung.getTime() < heute) {
						removeList.add(eachVorlesung);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			vorlesungsList.removeAll(removeList);
			return vorlesungsList;
			
		case REQUEST_LAST:			
			for (Vorlesung eachVorlesung : vorlesungsList) {
				try {
					datumVorlesung = dateFormat.parse(eachVorlesung.getDatum());
					
					if (datumVorlesung.getTime() >= heute) {
						removeList.add(eachVorlesung);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			vorlesungsList.removeAll(removeList);
			return vorlesungsList;
			
		default:
			return new ArrayList<Vorlesung>();
		}
	}
	
	private List<Vorlesung> createVorlesung(List<AbstractVorlesung> aList) {
		List<Vorlesung> theResultList = new ArrayList<Vorlesung>();
		
		for (AbstractVorlesung eachData : aList) {
			theResultList.add(new Vorlesung(eachData));
		}
		
		return theResultList;
	}
	
	public DaoMaster getMaster() {
		return masterDAO;
	}
}
