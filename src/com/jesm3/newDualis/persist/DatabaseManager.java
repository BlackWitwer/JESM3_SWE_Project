package com.jesm3.newDualis.persist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.stupla.AbstractVorlesung;
import com.jesm3.newDualis.stupla.AbstractVorlesungDao;
import com.jesm3.newDualis.stupla.AbstractVorlesungDao.Properties;
import com.jesm3.newDualis.stupla.DaoMaster;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.DaoMaster.DevOpenHelper;
import com.jesm3.newDualis.stupla.DaoSession;
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
	
	public Vorlesung insertVorlesung(Vorlesung aVorlesung) {
		vorlesungDAO.insert(aVorlesung);
		return aVorlesung;
	}
	
	public void deleteVorlesung(Vorlesung aVorlesung) {
		vorlesungDAO.delete(aVorlesung);
	}
	
	/**
	 * Gibt je nach Parameter eine Menge von Vorlesungen zurück.
	 * @param aRequest der Request Type.
	 * @return die Menge der Vorlesungen. Eine leere Liste, wenn der Request Type nicht gefunden wird.
	 */
	public List<Vorlesung> getVorlesungen(Requests aRequest) {
		switch (aRequest) {
		case REQUEST_ALL:
			return createVorlesung(vorlesungDAO.loadAll());
		default:
			return new ArrayList<Vorlesung>();
		}
	}
	
	private <T> List<T> castAll (Class<T> aClass, List aList) {
		List<T> theList = new ArrayList<T>();
		for (Object eachObject : aList) {
			theList.add(aClass.cast(eachObject));
		}
		return theList;
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
