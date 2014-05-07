package com.jesm3.newDualis.is;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Base64;


public class UserManager {

	public static final String PREFS_USERNAME = "sharedPrefsUsername";
	public static final String PREFS_PASSWORD = "sharedPrefsPasswordii";
	
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
	
	private boolean login(User aUser, boolean aPersistantFlag) {
		if (getUser() == null && (app.getBackend().login(aUser))) {
			user = aUser;
			if (aPersistantFlag) {
				saveUserData();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * L�ｽscht die Benutzerdaten des Users und l�ｽscht auch die Daten aus den SharedPrefs.
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
			SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(context);
			Editor theEditor = thePrefs.edit();
			theEditor.putString(PREFS_USERNAME, getUser().getUsername());
			try {
				theEditor.putString(PREFS_PASSWORD, encrypt(getUser().getPassword()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return theEditor.commit();
		}
		return false;
	}
	
	/**
	 * Liest den Benutzernamen aus den SharedPrefs und erstellt daraus ein User objekt. Sind keine Benutzerdaten in 
	 * den SharedPrefs wird null zurückgegeben.
	 * @return ein User Objekt, wenn Benutzerdaten vorhanden sind.
	 */
	public boolean loadUserData() {
		SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String theUsername = thePrefs.getString(PREFS_USERNAME, "");
		String thePassword = thePrefs.getString(PREFS_PASSWORD, "");
		try {
			thePassword = decrypt(thePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(theUsername) || "".equals(thePassword)) {
			return false;
		}
		user = new User(theUsername, thePassword);
		return true;
	}
	
	private String context1 = new String("xyzabcdefghijklm");
	
	public String encrypt(String aWord) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(context1.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(aWord.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
	}
	
	public String decrypt(String aWord) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(context1.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(Base64.decode(aWord, Base64.DEFAULT));
        return new String(decrypted);
	}
}
