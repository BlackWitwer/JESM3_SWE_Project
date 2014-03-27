package com.jesm3.newDualis.synchronization;

/**
 * @author mji
 */
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.jesm3.newDualis.is.Backend;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.persist.DatabaseManager;
import com.jesm3.newDualis.settings.SettingsFragment;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Vorlesung.Requests;

public class SyncService extends Service implements
		OnSharedPreferenceChangeListener {

	private ConnectivityManager connectivityManager;

	SharedPreferences sharedPrefs;
	private Timer timer = new Timer();
	// The SyncIntervall in minutes
	private int syncIntervallMin;
	private boolean syncActive;
	private Backend backend;

	private DatabaseManager dbManager;
	private CustomApplication customApplication;

	private String logname = "SyncService";

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public SyncService getService() {
			return SyncService.this;
		}
	}

	/**
	 * Called when the service is first created.
	 * */
	public void onCreate(Bundle savedInstanceState) {

		customApplication = (CustomApplication) getApplication();
		dbManager = customApplication.getBackend().getDbManager();

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		syncActive = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_SYNC_ONOFF, false);
		syncIntervallMin = Integer.parseInt(sharedPrefs.getString(
				SettingsFragment.KEY_PREF_INTERVALL_SYNC, "720"));

		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Toast.makeText(this, "Service gestartet", Toast.LENGTH_LONG).show();

	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(logname, "bound");
		customApplication = (CustomApplication) getApplication();
		dbManager = customApplication.getBackend().getDbManager();

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		syncActive = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_SYNC_ONOFF, false);
		syncIntervallMin = Integer.parseInt(sharedPrefs.getString(
				SettingsFragment.KEY_PREF_INTERVALL_SYNC, "720"));
		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		sharedPrefs.registerOnSharedPreferenceChangeListener(this);

		return mBinder;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		return Service.START_NOT_STICKY;
	}

	/**
	 * @return What kind of connection the device is using, 0=mobile 1=wifi
	 */
	private int checkConnection() {
		int connection;
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork != null) {
			connection = activeNetwork.getType();
		} else {
			connection = -1;
		}

		return connection;

	}

	/**
	 * starting the automated synchronization
	 * 
	 * @return the result (0 -> OK; -1 -> connection invalid; -2 -> unmatching
	 *         settings)
	 */
	public int startAutoSync() {
		int result = 0;
		int connection = checkConnection();
		String prefs = sharedPrefs.getString(
				SettingsFragment.KEY_PREF_CONNECTION, "0");
		boolean valid = ConnectivityManager.isNetworkTypeValid(connection);
		Log.d(logname, "Connection in autoSync " + connection);
		Log.d(logname, "Preferences " + prefs);
		if (valid && syncActive) {
			if (connection == ConnectivityManager.TYPE_MOBILE
					&& (prefs.equals("1") || prefs.equals("2"))) {
				result = sync();

			} else if (connection == ConnectivityManager.TYPE_WIFI
					&& (prefs.equals("0") || prefs.equals("2"))) {
				result = sync();

			} else {
				Log.d(logname, "unmatching settings");
				result = -2;
			}
		} else {
			// Connection invalid
			Log.d(logname, "connection invalid");
			result = -1;
		}
		return result;
	}

	private void refreshSyncTimer() {

		timer.cancel();
		if (syncActive) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					startAutoSync();
				}
			}, syncIntervallMin * 60 * 1000, syncIntervallMin * 60 * 1000);
			Log.d(logname, "Timer aktualisiert");
		}
	}

	/**
	 * The manualSync Called for manual synchronization
	 * 
	 * @return the result (0 -> OK, 1 -> invalid network)
	 */
	public int manualSync() {
		int connection = checkConnection();
		boolean valid = ConnectivityManager.isNetworkTypeValid(connection);
		Log.d(logname, "Connection: " + connection);
		int result = 0;
		if (valid) {
			result = sync();
			refreshSyncTimer();
		} else {
			result = 1;
		}

		return result;
	}

	/**
	 * Doing the actual synchronization
	 * 
	 * @return the result (0 -> OK)
	 */
	private int sync() {
		Log.d(logname, "starting Sync");
		int result = 0;
		// TODO the actual Sync
		// get the next lectures
		List<Vorlesung> vorlesungen = dbManager
				.getVorlesungen(Requests.REQUEST_NEXT);

		return result;
	}

	/**
	 * To get the next Lectures
	 * 
	 * @return the next lectures
	 */
	public List<Vorlesung> getNextLectures() {
		return null;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(SettingsFragment.KEY_PREF_INTERVALL_SYNC)
				&& syncIntervallMin != Integer.parseInt(sharedPreferences
						.getString(SettingsFragment.KEY_PREF_INTERVALL_SYNC,
								"720"))) {

			syncIntervallMin = Integer.parseInt(sharedPreferences.getString(
					SettingsFragment.KEY_PREF_INTERVALL_SYNC, "720"));

			refreshSyncTimer();

			Log.d(logname, "syncintervall changed to " + syncIntervallMin
					+ " minutes");
		} else if (key.equals(SettingsFragment.KEY_PREF_SYNC_ONOFF)
				&& syncActive != sharedPreferences.getBoolean(key, false)) {
			syncActive = sharedPreferences.getBoolean(key, false);
			refreshSyncTimer();
		}
	}
}
