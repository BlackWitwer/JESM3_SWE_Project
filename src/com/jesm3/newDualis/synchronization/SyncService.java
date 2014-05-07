package com.jesm3.newDualis.synchronization;

/**
 * @author mji
 */
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.activities.LoginActivity;
import com.jesm3.newDualis.activities.MainActivity.GUICallbackIF;
import com.jesm3.newDualis.is.Backend;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.jinterface.StundenplanGenerator;
import com.jesm3.newDualis.noten.Note;
import com.jesm3.newDualis.persist.DatabaseManager;
import com.jesm3.newDualis.settings.SettingsFragment;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Vorlesung.Requests;
import com.jesm3.newDualis.stupla.Wochenplan;

public class SyncService extends Service implements
		OnSharedPreferenceChangeListener {

	private ConnectivityManager connectivityManager;

	SharedPreferences sharedPrefs;
	private Timer syncTimer = new Timer();
	private Timer mailTimer = new Timer();
	// The SyncIntervall in minutes
	private int syncIntervallMin;
	private int mailSyncIntervallMin;
	private boolean syncActive;
	private String prefs;
	private boolean mailSyncActive;
	private boolean doNotification;
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

		Log.d(logname, "Service Created");
		customApplication = (CustomApplication) getApplication();
		backend = customApplication.getBackend();
		dbManager = backend.getDbManager();

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		syncActive = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_SYNC_ONOFF, false);
		mailSyncActive = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_MAIL_SYNC_ONOFF, false);
		doNotification = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_NOTIF_ONOFF, false);
		prefs = sharedPrefs
				.getString(SettingsFragment.KEY_PREF_CONNECTION, "0");
		syncIntervallMin = Integer.parseInt(sharedPrefs.getString(
				SettingsFragment.KEY_PREF_INTERVALL_SYNC, "720"));
		mailSyncIntervallMin = Integer.parseInt(sharedPrefs.getString(
				SettingsFragment.KEY_PREF_INTERVALL_MAIL, "720"));
		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Toast.makeText(this, "Service gestartet", Toast.LENGTH_LONG).show();

	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		
		Log.d(logname, "bound");
		customApplication = (CustomApplication) getApplication();
		backend = customApplication.getBackend();
		dbManager = backend.getDbManager();

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		syncActive = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_SYNC_ONOFF, false);
		mailSyncActive = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_MAIL_SYNC_ONOFF, false);
		doNotification = sharedPrefs.getBoolean(
				SettingsFragment.KEY_PREF_NOTIF_ONOFF, false);
		prefs = sharedPrefs
				.getString(SettingsFragment.KEY_PREF_CONNECTION, "0");
		syncIntervallMin = Integer.parseInt(sharedPrefs.getString(
				SettingsFragment.KEY_PREF_INTERVALL_SYNC, "720"));
		mailSyncIntervallMin = Integer.parseInt(sharedPrefs.getString(
				SettingsFragment.KEY_PREF_INTERVALL_MAIL, "720"));
		connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		sharedPrefs.registerOnSharedPreferenceChangeListener(this);

		return mBinder;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(logname, "Service started");
		return Service.START_NOT_STICKY;
	}
	
    @Override
    public void onDestroy() {
    }

	public void getLecturesforGui(final GUICallbackIF aCallbackIF) {
		List<Wochenplan> parsedWeeks = null;
		List<Vorlesung> oldVorlesungen = dbManager
				.getVorlesungen(
				Requests.REQUEST_ALL);
		if (oldVorlesungen.size() == 0) {
			new Thread(new Runnable() {
				public void run() {
					int result = sync();
					if (aCallbackIF != null && result == 0)
						refreshLectures(aCallbackIF);
					}
			}).start();
		} else {
			parsedWeeks = new StundenplanGenerator()
					.generateWochenplaene(oldVorlesungen, customApplication);
			for (Wochenplan eachWoche : parsedWeeks) {
				this.backend.getVorlesungsplanManager()
						.addWochenplan(eachWoche);
				// new
				// StundenplanGenerator().generateWochenplaene(eachWoche.getDay(Days.MONTAG));
			}
		}
	}

	/**
	 * For refreshing the marks in the NotenManager
	 * @return result (0 -> OK)
	 */
	public void getMarksForGui(final GUICallbackIF aCallbackIF) {
		List<Note> noten = dbManager.getNoten();
		if (noten.size() == 0) {
			Log.d(logname, "noten sind " + noten.size());
			new Thread(new Runnable() {
				public void run() {
					int result = markSync();
					if (aCallbackIF != null && result == 0)
						refreshMarks(aCallbackIF);
					}
			}).start();
		} else {
			this.backend.getNotenManager().setNoten((ArrayList<Note>) noten);
			Log.d(logname, "Noten Ã¼bergeben: " + noten.size());
		}
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
	 *         settings; -3 MarkSync or LectureSync failed)
	 */
	public int startAutoSync() {
		int result = 0;
		int resultLectures = 0;
		int resultMarks = 0;
		int connection = checkConnection();

		boolean valid = ConnectivityManager.isNetworkTypeValid(connection);
		Log.d(logname, "Connection in autoSync " + connection);
		Log.d(logname, "Preferences " + prefs);
		if (syncActive && valid) {
			if (connection == ConnectivityManager.TYPE_MOBILE
					&& (prefs.equals("1") || prefs.equals("2"))) {
				sync();
				markSync();

			} else if (connection == ConnectivityManager.TYPE_WIFI
					&& (prefs.equals("0") || prefs.equals("2"))) {
				sync();
				markSync();
				mailSync();

			} else {
				Log.d(logname, "unmatching settings");
				result = -2;
			}
		} else {
			// Connection invalid
			Log.d(logname, "connection invalid");
			result = -1;
		}
		if (resultLectures == 0 && resultMarks == 0) {
			return result;
		} else {
			result = -3;
			return result;
		}
	}

	private void refreshSyncTimer() {

		syncTimer.cancel();

		if (syncActive && syncIntervallMin != 0) {
			syncTimer = new Timer();
			syncTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					startAutoSync();
				}
			}, syncIntervallMin * 60 * 1000, syncIntervallMin * 60 * 1000);
			// }, 40 * 1000, 40 * 1000);
			Log.d(logname, "SyncTimer aktualisiert");
		}
	}

	/**
	 * The refreshtimer Method for mail
	 */
	@SuppressWarnings("unused")
	private void refreshMailSyncTimer() {
		mailTimer.cancel();
		
		if (mailSyncActive && mailSyncIntervallMin != 0) {
			mailTimer = new Timer();
			mailTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					//TODO Mailsync
					
				}
			}, mailSyncIntervallMin * 60 * 1000,
					mailSyncIntervallMin * 60 * 1000);
		}
	}

	/**
	 * The manualSync Called for manual synchronization of lectures
	 * 
	 * @return the result (0 -> OK, 1 -> invalid network)
	 */

	public int manualSync(final GUICallbackIF aCallbackIF) {
		int connection = checkConnection();
		boolean valid = ConnectivityManager.isNetworkTypeValid(connection);
		Log.d(logname, "Connection: " + connection);
		int result = 0;
		if (valid) {
			new Thread(new Runnable() {
				public void run() {
					sync();
					refreshSyncTimer();
					if (aCallbackIF != null)
						refreshLectures(aCallbackIF);
					}
			}).start();
		} else {
			result = 1;
		}

		return result;
	}
	
	private void refreshLectures(GUICallbackIF aCallbackIF) {
		aCallbackIF.refreshLectures();		
	}

	/**
	 * Doing the actual synchronization of lectures
	 * 
	 * @return the result (0 -> OK)
	 */
	private int sync() {
		int result = 0;
		Log.d(logname, "starting Sync of lectures");
		// get the next lectures for safety
		List<Vorlesung> vorlesungen = dbManager
				.getVorlesungen(Requests.REQUEST_NEXT);
		Log.d(logname, "Vorlesungselement: " + vorlesungen.size());

		// get new lectures from dualis
		// TODO settings: int weeks
		List<Vorlesung> newLecturesList = null;
		try {
			newLecturesList = backend.getConnnection()
.loadStundenplan(5);
		} catch (Exception e) {
			result = 1;
			// TODO find solution for this Toast
			// Toast.makeText(this,
			// "Stundenplan Synchronisierung fehlgeschlagen",
			// Toast.LENGTH_LONG).show();
			Log.e(logname, "Parsing failed");
			return result;
		}
		Log.d(logname, "newLecturesList: " + newLecturesList.size());
		dbManager.deleteVorlesungen(Requests.REQUEST_NEXT);
		
		GregorianCalendar theCalendar = new GregorianCalendar(Locale.GERMANY);
		theCalendar.setTimeInMillis(System.currentTimeMillis());
		//-2, da der Montag die Zahl 2 hat und im Fall des Montags nichts abgezogen werden soll.
		theCalendar.add(GregorianCalendar.DAY_OF_WEEK, -(theCalendar.get(GregorianCalendar.DAY_OF_WEEK)-2));		
		theCalendar.add(GregorianCalendar.HOUR_OF_DAY, -(theCalendar.get(GregorianCalendar.HOUR_OF_DAY)));
		
		for (Vorlesung eachVorlesung : dbManager.getVorlesungen(theCalendar.getTime(), null)) {
			dbManager.deleteVorlesung(eachVorlesung);
		}
		
		dbManager.insertVorlesungen(newLecturesList);

		List<Wochenplan> parsedWeeks = new StundenplanGenerator()
				.generateWochenplaene(
						backend.getDbManager().getVorlesungen(
								Requests.REQUEST_ALL),
 customApplication);

		for (Wochenplan eachWoche : parsedWeeks) {
			this.backend.getVorlesungsplanManager().addWochenplan(eachWoche);
			// new
			// StundenplanGenerator().generateWochenplaene(eachWoche.getDay(Days.MONTAG));


		}
		return result;
	}

	/**
	 * The manualSync Called for manual synchronization of marks
	 * 
	 * @return the result (0 -> OK, 1 -> invalid network)
	 */

	public int manualMarkSync(final GUICallbackIF aCallbackIF) {
		int connection = checkConnection();
		boolean valid = ConnectivityManager.isNetworkTypeValid(connection);
		Log.d(logname, "Connection: " + connection);
		int result = 0;
		if (valid) {
			new Thread(new Runnable() {
				public void run() {
					markSync();
					if (aCallbackIF != null)
						refreshMarks(aCallbackIF);
				}
			}).start();
		} else {
			result = 1;
		}

		
		return result;
	}
	private void refreshMarks(GUICallbackIF aCallbackIF) {
		aCallbackIF.refreshMarks();
	}

	/**
	 * Doing the actual MarkSync
	 * 
	 * @return the result (0 -> OK)
	 */
	public int markSync() {
		Log.d(logname, "starting Sync of Marks");
		int result = 0;
		// get the next lectures for safety
		List<Note> oldMarks = dbManager.getNoten();
		Log.d(logname, "Notenelemente: " + oldMarks.size());

		// get new marks from dualis
		ArrayList<Note> newMarksList = null;
		try {
			newMarksList = backend.getConnnection().loadNoten();
		} catch (Exception e) {
			// TODO find solution for this Toast
			// Toast.makeText(this, "Noten Synchronisierung fehlgeschlagen",
			// Toast.LENGTH_LONG).show();
			result = 1;
			return result;
		}
		Log.d(logname, "newLecturesList: " + newMarksList.size());
		for (Note oldMark : oldMarks) {
			dbManager.deleteNote(oldMark);
		}
		for (Note newMark : newMarksList) {
			dbManager.insertNote(newMark);
		}
		Log.d(logname, "Elemente in DB: " + dbManager.getNoten().size());
		// make notification if new mark is available
		if (oldMarks.size() != 0) {
			
			for (int i = 0; i < oldMarks.size(); i++) {
				Note oldMark = oldMarks.get(i);
				Note newMark = newMarksList.get(i);

				if (oldMark.getTitel().equals(newMark.getTitel())
						&& !oldMark.getCredits().equals(newMark.getCredits())) {
					// trigger Notification
					if (doNotification) {
						createMarkNotification();
					}
				}
			}
		}

		this.backend.getNotenManager().setNoten(newMarksList);
	

		return result;

	}

	/**
	 * starts the Sync of mails
	 */
	public void mailSync() {
		// TODO mailSync
		backend.getMailManager().sync();
		// if new mail
		if (doNotification) {
			createMailNotification();
		}
	}

	/**
	 * Makes the new mark arrived Notification
	 */
	private void createMarkNotification() {
		
		int mId = 1;
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);

		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
.setSmallIcon(R.drawable.icon)
				.setContentTitle(getString(R.string.app_name))
				.setContentText("Neue Noten verfügbar")
.setLargeIcon(bm);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, LoginActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(LoginActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mId, mBuilder.build());
		
	}

	/**
	 * Makes the new mail arrived Notification
	 */
	private void createMailNotification() {

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
			Log.d(logname, "syncintervall changed to " + syncIntervallMin
					+ " minutes");


			refreshSyncTimer();
		} else if (key.equals(SettingsFragment.KEY_PREF_SYNC_ONOFF)
				&& syncActive != sharedPreferences.getBoolean(key, false)) {
			syncActive = sharedPreferences.getBoolean(key, false);
			refreshSyncTimer();
		} else if (key.equals(SettingsFragment.KEY_PREF_CONNECTION)
				&& !prefs.equals(sharedPreferences.getString(key, "0"))) {
			prefs = sharedPreferences.getString(key, "0");
		} else if (key.equals(SettingsFragment.KEY_PREF_NOTIF_ONOFF)
				&& doNotification != sharedPreferences.getBoolean(key, false)) {
			doNotification = sharedPreferences.getBoolean(key, false);
		}
	}
}
