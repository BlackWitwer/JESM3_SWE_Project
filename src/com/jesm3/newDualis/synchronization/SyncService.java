package com.jesm3.newDualis.synchronization;

/**
 * @author mji
 */
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

	/** Called when the service is first created. */
	public void onCreate(Bundle savedInstanceState) {

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Toast.makeText(this, "Service gestartet", Toast.LENGTH_LONG).show();
		startSync();

	}

	public void onBind(Bundle savedInstanceState) {

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		startSync();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		return Service.START_NOT_STICKY;
	}

	/**
	 * @return If the Devices is connected to a Network
	 */
	private int checkConnection() {
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		int connection = activeNetwork.getType();

		return connection;
	}

	/**
	 * starting the synchronization
	 * 
	 * @param view
	 * @return the result (0 -> OK; -1 -> connection invalid; -2 -> unmatching
	 *         settings)
	 */
	public int startSync() {
		int result = 0;
		int connection = checkConnection();
		String prefs = sharedPrefs.getString(
				SettingsFragment.KEY_PREF_CONNECTION, "1");
		boolean valid = ConnectivityManager.isNetworkTypeValid(connection);
		Log.d(logname, "" + connection);
		Log.d(logname, "Preferences" + prefs);
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
		return result;
	}

	/**
	 * The Sync
	 * 
	 * @return the result (0 -> OK)
	 */
	public int sync() {
		Log.d(logname, "starting Sync");
		int result = 0;

		return result;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
