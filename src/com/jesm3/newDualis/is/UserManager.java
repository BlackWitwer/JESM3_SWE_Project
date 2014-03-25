package com.jesm3.newDualis.is;

import android.content.*;
import android.content.SharedPreferences.*;
import android.preference.*;
import java.security.*;
import javax.crypto.*;

public class UserManager {

	private static final String PREFS_USERNAME = "sharedPrefsUsername";
	private static final String PREFS_PASSWORD = "sharedPrefsPasswordii";
	
	private User user;
	private Context context;
	private CustomApplication app;
	
	public UserManager(CustomApplication anApp) {
		this.context = anApp.getApplicationContext();
		this.app = anApp;
	}
	
	public User getUser() {
		return user;
	}
	
	/**
	 * Versucht sich mit den �bergebenen Werten einzuloggen. Ist der login erfolgreich werden die Userdaten gespeichert. Ansonsten wird
	 * false zur�ckgegeben.
	 * @param aUsername der Benutzername
	 * @param aPassword das Passwort.
	 * @return true, wenn der login erfolgreich war sonst false.
	 */
	public boolean login(String aUsername, String aPassword, boolean aPersistantFlag) {
		return login(new User(aUsername, aPassword), aPersistantFlag);
	}
	
	public boolean login(User aUser, boolean aPersistantFlag) {
		return login(aUser, aPersistantFlag, true);
	}
	
	private boolean login(User aUser, boolean aPersistantFlag, boolean anOnlineFlag) {
		//TODO auf checkLogin() umstellen sobald Sync funktioniert.
		// if (getUser() == null && (!anOnlineFlag || app.getBackend().login(aUser))) {
		if (getUser() == null && (app.getBackend().login(aUser))) {
			//TODO entfernen sobald Sync funktioniert.
			app.getBackend().getConnnection().loadStundenplan(5);

			user = aUser;
			if (aPersistantFlag) {
				saveUserData();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * L�scht die Benutzerdaten des Users und l�scht auch die Daten aus den SharedPrefs.
	 */
	public void logout() {
		user = null;
		SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor theEditor = thePrefs.edit();
		theEditor.remove(PREFS_USERNAME);
		theEditor.remove(PREFS_PASSWORD);
		theEditor.commit();
	}
	
	/**
	 * Speichert die Daten des momentan eingeloggten Users in den SharedPrefs.
	 * @return true, wenn die Aktion erfolgreich war.
	 */
	private boolean saveUserData() {
		if (user != null) {
			// TODO Passwort verschl�sselung.
			try {
				Cipher theCipher = Cipher.getInstance("AES");
//				theCipher
				SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(context);
				Editor theEditor = thePrefs.edit();
				theEditor.putString(PREFS_USERNAME, getUser().getUsername());
				theEditor.putString(PREFS_PASSWORD, getUser().getPassword());
				return theEditor.commit();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}
	
	/**
	 * Liest den Benutzernamen aus den SharedPrefs und erstellt daraus ein User objekt. Dieser User wird direkt eingeloggt. Sind keine Benutzerdaten in 
	 * den SharedPrefs wird null zur�ckgegeben.
	 * @return ein User Objekt, wenn Benutzerdaten vorhanden sind.
	 */
	public boolean loadUserData() {
		SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String theUsername = thePrefs.getString(PREFS_USERNAME, "");
		String thePassword = thePrefs.getString(PREFS_PASSWORD, "");
		if ("".equals(theUsername) || "".equals(thePassword)) {
			return false;
		}
		User theUser = new User(theUsername, thePassword);
		return login(theUser, false, false);
	}
}
