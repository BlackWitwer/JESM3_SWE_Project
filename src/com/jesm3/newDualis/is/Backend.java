package com.jesm3.newDualis.is;

import com.jesm3.newDualis.jinterface.DualisConnection;
import com.jesm3.newDualis.stupla.VorlesungsplanManager;

public class Backend {
// Synchronisationsmanager
// Filemanager
	
	private DualisConnection con;
	private VorlesungsplanManager vorlesungsplanManager;
	
	public Backend() {
		this.con = new DualisConnection(this);
		this.vorlesungsplanManager = new VorlesungsplanManager();
	}
	
	public boolean checkLogin(User aUser) {
		return con.simpleLoginCheck(aUser);
	}
	
	public boolean login(User aUser) {
		return con.login(aUser);
	}

	public DualisConnection getCon() {
		return con;
	}
	
	public VorlesungsplanManager getVorlesungsplanManager() {
		return vorlesungsplanManager;
	}
}
