package com.jesm3.newDualis.is;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class UserManager {

	private static final String PREFS_USERNAME = "sharedPrefsUsername";
	private static final String PREFS_PASSWORD = "sharedPrefsPasswordii";
	
	private User user;
	private Context context;
	
	public UserManager(Context aContext) {
		context = aContext;
	}
	
	public User getUser() {
		return user;
	}
	
	/**
	 * Versucht sich mit den übergebenen Werten einzuloggen. Ist der login erfolgreich werden die Userdaten gespeichert. Ansonsten wird
	 * false zurückgegeben.
	 * @param aUsername der Benutzername
	 * @param aPassword das Passwort.
	 * @return true, wenn der login erfolgreich war sonst false.
	 */
	public boolean login(String aUsername, String aPassword, boolean aPersistantFlag) {
		if (getUser() == null) {
			user = new User(aUsername, aPassword);
			if (aPersistantFlag) {
				saveUserData();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Löscht die Benutzerdaten des Users und löscht auch die Daten aus den SharedPrefs.
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
			// TODO Passwort verschlüsselung.
			SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(context);
			Editor theEditor = thePrefs.edit();
			theEditor.putString(PREFS_USERNAME, getUser().getUsername());
			theEditor.putString(PREFS_PASSWORD, getUser().getPassword());
			return theEditor.commit();
		}
		return false;
	}
	
	/**
	 * Liest den Benutzernamen aus den SharedPrefs und erstellt daraus ein User objekt. Prüft nicht auf login. Sind keine Benutzerdaten in 
	 * den SharedPrefs wird null zurückgegeben.
	 * @return ein User Objekt, wenn Benutzerdaten vorhanden sind.
	 */
	public User loadUserData() {
		SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String theUsername = thePrefs.getString(PREFS_USERNAME, "");
		String thePassword = thePrefs.getString(PREFS_PASSWORD, "");
		if ("".equals(theUsername) || "".equals(thePassword)) {
			return null;
		}
		return new User(theUsername, thePassword);
	}
}
