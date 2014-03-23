package com.jesm3.newDualis.synchronization;

/**
 * @author mji
 */
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.jesm3.newDualis.settings.SettingsFragment;

public class SyncService extends Service {

	private ConnectivityManager connectivityManager;
	SharedPreferences sharedPrefs;
	private Timer timer = new Timer();
	private int syncIntervall;

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

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		syncIntervall = sharedPrefs.getInt(
				SettingsFragment.KEY_PREF_INTERVALL_SYNC, 1 * 60 * 1000);

		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Toast.makeText(this, "Service gestartet", Toast.LENGTH_LONG).show();

	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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
				SettingsFragment.KEY_PREF_CONNECTION, "1");
		boolean valid = ConnectivityManager.isNetworkTypeValid(connection);
		Log.d(logname, "Connection in autoSync " + connection);
		Log.d(logname, "Preferences " + prefs);
		if (valid) {
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
		// TODO is this the right place to start the timer?
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				startAutoSync();
			}
		}, syncIntervall);
		return result;
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
	public int sync() {
		Log.d(logname, "starting Sync");
		int result = 0;
		// TODO the actual Sync

		// XXX insert sexual content here
		// Log.wtf(logname, "(.)(.)");
		// Log.wtf(logname, " )  ( ");
		// Log.wtf(logname, "(  y )");

		return result;
	}
}
