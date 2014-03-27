package com.jesm3.newDualis.is;

import java.util.List;

import com.jesm3.newDualis.jinterface.DualisConnection;
import com.jesm3.newDualis.noten.NotenManager;
import com.jesm3.newDualis.persist.DatabaseManager;
import com.jesm3.newDualis.stupla.Vorlesung.Requests;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.VorlesungsplanManager;
import com.jesm3.newDualis.mail.*;

public class Backend {
// Synchronisationsmanager
	
	private DatabaseManager dbManager;
	private DualisConnection connection;
	private VorlesungsplanManager vorlesungsplanManager;
	private MailManager mailManager;
	private CustomApplication customApplication;
	private NotenManager notenManager;
	
	public Backend(CustomApplication aCustomApplication) {
		this.customApplication = aCustomApplication;
		
		this.connection = new DualisConnection(this);
		this.vorlesungsplanManager = new VorlesungsplanManager();
		this.dbManager = new DatabaseManager(customApplication);
		this.notenManager = new NotenManager();
	}
	
	public boolean checkLogin(User aUser) {
		return connection.simpleLoginCheck(aUser);
	}
	
	public boolean login(User aUser) {
		return connection.login(aUser);
	}

	public DualisConnection getConnnection() {
		return connection;
	}
	
	public VorlesungsplanManager getVorlesungsplanManager() {
		return vorlesungsplanManager;
	}
	
	public DatabaseManager getDbManager() {
		return dbManager;
	}
	
	public CustomApplication getCustomApplication() {
		return customApplication;
	}
	
	public NotenManager getNotenManager() {
		return notenManager;
	}
	
	public MailManager getMailManager() {
		if (mailManager == null) {
			mailManager = new MailManager(this, getCustomApplication().getUserManager().getUser());
		}
		return mailManager;
	}
}
