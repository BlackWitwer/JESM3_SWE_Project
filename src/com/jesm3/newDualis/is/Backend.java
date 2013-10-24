package com.jesm3.newDualis.is;

import com.jesm3.newDualis.jinterface.DualisConnection;

public class Backend {
// Synchronisationsmanager
// Filemanager
	
	private DualisConnection con;
	
	public Backend() {
		this.con = new DualisConnection();
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
}
