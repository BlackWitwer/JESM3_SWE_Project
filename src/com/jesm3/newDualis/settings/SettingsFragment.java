package com.jesm3.newDualis.settings;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.jesm3.newDualis.R;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	/**
	 * Kategorie Synchronisation
	 */
	public static final String KEY_PREF_SYNCHRONISATION_CAT = "pref_key_synchronisation_cat";
	/**
	 * Checkbox Synchronisation an/aus
	 */
	public static final String KEY_PREF_SYNC_ONOFF = "pref_key_sync_onoff";
	/**
	 * List Intervall Synchronisation
	 */
	public static final String KEY_PREF_INTERVALL_SYNC = "pref_key_intervall_sync";
	/**
	 * Checkbox Google-Kalendersynchronisation
	 */
	public static final String KEY_PREF_GCAL_SYNC_ONOFF = "pref_key_gcal_sync_onoff";
	/**
	 * Checkbox Mail-Synchronisation
	 */
	public static final String KEY_PREF_MAIL_SYNC_ONOFF = "pref_key_mail_sync_onoff";
	/**
	 * List Intervall Synchronisation
	 */
	public static final String KEY_PREF_INTERVALL_MAIL = "pref_key_intervall_mail";
	/**
	 * List Connection (3g/WLAN/3gWLAN)
	 */
	public static final String KEY_PREF_CONNECTION = "pref_key_connection";
	/**
	 * Kategorie Benachrichtigungen
	 */
	public static final String KEY_PREF_NOTIFICATIONS_CAT = "pref_key_notifications_cat";
	/**
	 * Checkbox Benachrichtigungen an/aus
	 */
	public static final String KEY_PREF_NOTIF_ONOFF = "pref_key_notif_onoff";
	/**
	 * List Intervall Benachrichtigung
	 */
	public static final String KEY_PREF_NOTIF_INTERVALL = "pref_key_notif_intervall";
	/**
	 * Checkbox Ton
	 */
	public static final String KEY_PREF_SOUND = "pref_key_sound";
	/**
	 * Checkbox Vibration
	 */
	public static final String KEY_PREF_VIBRATION = "pref_key_vibration";

	
	/**
	 * Umwandlungsmap
	 */
	private HashMap<String,String> convertMinutes;
	/**
	 * Umwandlungsmap
	 */
	private HashMap<String,String> convertMinutesSync;
	/**
	 * Umwandlungsmap
	 */
	private HashMap<String,String> convertConnection;
	
	/* (non-Javadoc)
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		initConversionMap();
		initConnectionMap();
		loadValues();
	}
	

	/**
	 * Initialisiert die Map für die Anzeige der Intervalle.
	 */
	private void initConnectionMap() {
		convertConnection = new HashMap<String, String>();
		
        convertConnection.put("0", "nur WLAN");
        convertConnection.put("1", "nur mobiles Internet");
        convertConnection.put("2", "Mobil/WLAN");
	}

	/**
	 * Initialisiert die Map für die Anzeige der Intervalle.
	 */
	private void initConversionMap() {
		convertMinutes = new HashMap<String, String>();
        convertMinutes.put("0","einmalig");
        convertMinutes.put("5","5 Min");
        convertMinutes.put("15","15 Min");
        convertMinutes.put("30","30 Min");
        convertMinutes.put("60","1 Stunden");
        convertMinutes.put("120","2 Stunden");
        convertMinutes.put("360","6 Stunden");
        convertMinutes.put("720","12 Stunden");
        convertMinutes.put("1440","1 Tag");
        convertMinutes.put("10080","7 Tage");
        convertMinutes.put("302400","1 Monat");
        convertMinutesSync = (HashMap<String, String>) convertMinutes.clone();
        convertMinutesSync.put("0", "manuell");
	}


	/**
	 * Setzt die  Summaries für die Einstellungen.
	 */
	private void loadValues() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Preference  sync_onoff = findPreference(KEY_PREF_SYNC_ONOFF);
		boolean sync_onoff_val = sharedPref.getBoolean(KEY_PREF_SYNC_ONOFF, false);
		Preference  sync_intervall = findPreference(KEY_PREF_INTERVALL_SYNC);
		String sync_intervall_val = sharedPref.getString(KEY_PREF_INTERVALL_SYNC, "");
		Preference  sync_gcal = findPreference(KEY_PREF_GCAL_SYNC_ONOFF);
		boolean sync_gcal_val = sharedPref.getBoolean(KEY_PREF_GCAL_SYNC_ONOFF, false);
		Preference  sync_mail = findPreference(KEY_PREF_MAIL_SYNC_ONOFF);
		boolean sync_mail_val = sharedPref.getBoolean(KEY_PREF_MAIL_SYNC_ONOFF, false);
		Preference  sync_mail_intervall = findPreference(KEY_PREF_INTERVALL_MAIL);
		String sync_mail_intervall_val = sharedPref.getString(KEY_PREF_INTERVALL_MAIL, "");
		Preference  sync_connection = findPreference(KEY_PREF_CONNECTION);
		String sync_connection_val = sharedPref.getString(KEY_PREF_CONNECTION, "");
		Preference  notif_onoff = findPreference(KEY_PREF_NOTIF_ONOFF);
		boolean notif_onoff_val = sharedPref.getBoolean(KEY_PREF_NOTIF_ONOFF, false);
		Preference  notif_intervall = findPreference(KEY_PREF_NOTIF_INTERVALL);
		String notif_intervall_val = sharedPref.getString(KEY_PREF_NOTIF_INTERVALL, "");
		Preference  notif_sound = findPreference(KEY_PREF_SOUND);
		boolean notif_sound_val = sharedPref.getBoolean(KEY_PREF_SOUND, false);
		Preference  notif_vibration = findPreference(KEY_PREF_VIBRATION);
		boolean notif_vibration_val = sharedPref.getBoolean(KEY_PREF_VIBRATION, false);
		
		if (sync_onoff_val) {
			sync_onoff.setSummary(R.string.sync_on);
		} else {
			sync_onoff.setSummary(R.string.sync_off);
		}
		
		sync_intervall.setSummary(convertMinutesSync.get(sync_intervall_val));
		
		if (sync_gcal_val) {
			sync_gcal.setSummary(R.string.gcal_sync_on);
		} else {
			sync_gcal.setSummary(R.string.gcal_sync_off);
		}
		
		if (sync_mail_val) {
			sync_mail.setSummary(R.string.mail_sync_on);
		} else {
			sync_mail.setSummary(R.string.mail_sync_off);
		}
		
		sync_mail_intervall.setSummary(convertMinutesSync.get(sync_mail_intervall_val));
		
		sync_connection.setSummary(convertConnection.get(sync_connection_val));
		
		if (notif_onoff_val) {
			notif_onoff.setSummary(R.string.notif_on);
		} else {
			notif_onoff.setSummary(R.string.notif_off);
		}

		notif_intervall.setSummary(convertMinutes.get(notif_intervall_val));
		
		if (notif_sound_val) {
			notif_sound.setSummary(R.string.sound_on);
		} else {
			notif_sound.setSummary(R.string.sound_off);
		}
		
		if (notif_vibration_val) {
			notif_vibration.setSummary(R.string.vibration_on);
		} else {
			notif_vibration.setSummary(R.string.vibration_off);
		}
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		loadValues();
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
}
