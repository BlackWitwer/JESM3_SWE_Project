package com.jesm3.newDualis.activities;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.mail.Message;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.mail.ExpandableListAdapter;
import com.jesm3.newDualis.mail.MailListener;
import com.jesm3.newDualis.mail.MailManager;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.VorlesungsplanManager;
import com.jesm3.newDualis.stupla.Wochenplan;
import com.jesm3.newDualis.synchronization.SyncService;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	/**
	 * Die ActionBar
	 */
	ActionBar actionBar;

	private boolean doubleClicked;
	private SyncService mBoundSyncService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// start the SyncService
		startService(new Intent(this, SyncService.class));
		bindService(new Intent(this, SyncService.class),
				new ServiceConnection() {

					@Override
					public void onServiceDisconnected(ComponentName name) {
						mBoundSyncService = null;
					}

					@Override
					public void onServiceConnected(ComponentName name,
							IBinder service) {
						mBoundSyncService = ((SyncService.LocalBinder) service)
								.getService();
						Toast.makeText(mBoundSyncService, "Service Verbunden",
								Toast.LENGTH_SHORT).show();
					}
				}, 0);

		actionBar = getActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_main);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		actionBar.show();

		// Nur zu Testzwecken. Unterbindet eine Sicherung die es nicht erlaubt
		// im Interface Thread Netzwerkaktivitäten zu verwenden.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// -----------------------------------

		// initMailView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (doubleClicked) {
			super.onBackPressed();
			return;
		}
		this.doubleClicked = true;
		Toast.makeText(this, "Nochmal klicken um die App zu schlie�en...",
				Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleClicked = false;
			}
		}, 2000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			showSettings();
			return true;
		case R.id.action_logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent
	 * )
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	public void addNotification(View v) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification myNotification = new Notification(R.drawable.icon,
				"Notification!", System.currentTimeMillis());
		Context context = getApplicationContext();
		String notificationTitle = "Exercise of Notification!";
		String notificationText = "http://android-er.blogspot.com/";
		Intent myIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				MainActivity.this, 0, myIntent, Intent.FILL_IN_ACTION);
		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		myNotification.setLatestEventInfo(context, notificationTitle,
				notificationText, pendingIntent);
		notificationManager.notify(1, myNotification);

		// Intent intent = new Intent(this, MainActivity.class);
		// PendingIntent pi = PendingIntent.getActivity(this, 0,intent, 0);
		//
		// NotificationCompat.Builder mBuilder =
		// new NotificationCompat.Builder(this)
		// .setSmallIcon(R.drawable.icon)
		// .setContentTitle("Neue Mail")
		// .setContentText("Sie haben 1 neue Mail erhalten!");
		//
		// Notification note = mBuilder.build();
		// note.setLatestEventInfo(this, "New Email", "Unread Conversation",
		// pi);
		// note.flags |= Notification.FLAG_AUTO_CANCEL;
		//
		// NotificationManager mNotificationManager =
		// (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotificationManager.notify(1, mBuilder.build());
	}

	/**
	 * Die Funktion, welche vom Aktualisieren Button aufgerufen wird.
	 */
	public void updateStupla(View v) {
		// TODO MJI hier soll der Stundenplan aktualisiert werden.
		mBoundSyncService.manualSync();

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 5 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		private Wochenplan stupla;
		private ArrayList<Note> noten;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		private void setLecturesOnGUI(View aContainer) {
			LinearLayout lay_montag = (LinearLayout) aContainer
					.findViewById(R.id.lay_montag);
			LinearLayout lay_dienstag = (LinearLayout) aContainer
					.findViewById(R.id.lay_dienstag);
			LinearLayout lay_mittwoch = (LinearLayout) aContainer
					.findViewById(R.id.lay_mittwoch);
			LinearLayout lay_donnerstag = (LinearLayout) aContainer
					.findViewById(R.id.lay_donnerstag);
			LinearLayout lay_freitag = (LinearLayout) aContainer
					.findViewById(R.id.lay_freitag);
			LinearLayout lay_samstag = (LinearLayout) aContainer
					.findViewById(R.id.lay_samstag);

			lay_montag.removeAllViews();
			lay_dienstag.removeAllViews();
			lay_mittwoch.removeAllViews();
			lay_donnerstag.removeAllViews();
			lay_freitag.removeAllViews();
			lay_samstag.removeAllViews();

			generateDay(stupla.getMontag(), lay_montag);
			generateDay(stupla.getDienstag(), lay_dienstag);
			generateDay(stupla.getMittwoch(), lay_mittwoch);
			generateDay(stupla.getDonnerstag(), lay_donnerstag);
			generateDay(stupla.getFreitag(), lay_freitag);
			generateDay(stupla.getSamstag(), lay_samstag);

		}

		private void initializeLectures(final View aContainer) {
			final GregorianCalendar cal = new GregorianCalendar();
			final VorlesungsplanManager theVorlesungsplanManager = ((CustomApplication) getActivity()
					.getApplication()).getBackend().getVorlesungsplanManager();
			stupla = theVorlesungsplanManager.getWochenplan(cal
					.get(GregorianCalendar.WEEK_OF_YEAR));
			List<Integer> theWochen = new ArrayList<Integer>(
					theVorlesungsplanManager.getWochenMap().keySet());
			Spinner kwSpinner = (Spinner) aContainer
					.findViewById(R.id.spinner_kalenderwoche);
			ArrayAdapter<Integer> theKwAdapter = new ArrayAdapter<Integer>(
					aContainer.getContext(),
					android.R.layout.simple_spinner_item, theWochen);
			kwSpinner.setAdapter(theKwAdapter);
			// XXX MBA Nur bis die Wochenmap gefixt ist...
			kwSpinner.setSelection(1);
			kwSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					stupla = theVorlesungsplanManager
							.getWochenplan(((Integer) arg0.getSelectedItem())
									.intValue());
					setLecturesOnGUI(aContainer);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO MBA erstmal nichts...

				}
			});
		}

		private void generateDay(ArrayList<Vorlesung> aDayList,
				LinearLayout aLayout) {
			for (Vorlesung eachLecture : aDayList) {
				LinearLayout subLayout = new LinearLayout(getActivity());
				TextView zeit = new TextView(getActivity());
				TextView dozent = new TextView(getActivity());
				TextView name = new TextView(getActivity());

				subLayout.setOrientation(LinearLayout.VERTICAL);
				if (aLayout.getChildCount() % 2 == 0) {
					subLayout.setBackgroundColor(Color.WHITE);
				} else {
					subLayout.setBackgroundColor(Color.LTGRAY);
				}
				subLayout.setWeightSum(1);

				zeit.setText(eachLecture.getUhrzeitVon() + " - "
						+ eachLecture.getUhrzeitBis() + " Uhr");
				subLayout.addView(zeit);

				name.setText(eachLecture.getName());
				subLayout.addView(name);

				dozent.setText(eachLecture.getDozent());
				subLayout.addView(dozent);

				aLayout.addView(subLayout);
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.error_main, container,
					false);
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
			case 1:
				rootView = inflater.inflate(R.layout.stundenplan_main,
						container, false);
				initializeLectures(rootView);
				setLecturesOnGUI(rootView);
				rootView.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				break;
			case 2:
				rootView = inflater.inflate(R.layout.semesterplan_main,
						container, false);
				break;
			case 3:
				rootView = inflater.inflate(R.layout.noten_main, container,
						false);
				initializeMarks();
				setMarksOnGui(rootView);
				rootView.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				break;
			case 4:
				rootView = inflater.inflate(R.layout.mail_main, container,
						false);
				initMailView(rootView);
				break;
			case 5:
				rootView = inflater.inflate(R.layout.dozent_main, container,
						false);
				break;
			default:
				rootView = inflater.inflate(R.layout.error_main, container,
						false);
				break;
			}

			return rootView;
		}

		private void setMarksOnGui(View aContainer) {
			TableLayout lay_table = (TableLayout) aContainer
					.findViewById(R.id.noten_table);
			addRow("Fach", "Note", "Credits", lay_table);

			for (Note eachMark : noten) {
				addRow(eachMark.getName(), eachMark.getNote(),
						eachMark.getCredits(), lay_table);
			}
		}

		public void addRow(String aVal1, String aVal2, String aVal3,
				TableLayout aLayout) {
			TableRow row = new TableRow(getActivity());
			TextView fach = new TextView(getActivity());
			TextView note = new TextView(getActivity());
			TextView credits = new TextView(getActivity());
			fach.setText(aVal1);
			note.setText(aVal2);
			credits.setText(aVal3);

			// fach.setBackgroundColor(Color.WHITE);
			// note.setBackgroundColor(Color.WHITE);
			// credits.setBackgroundColor(Color.WHITE);

			Drawable theBorderDrawable = getResources().getDrawable(
					R.drawable.cell_shape);
			fach.setBackgroundDrawable(theBorderDrawable);
			note.setBackgroundDrawable(theBorderDrawable);
			credits.setBackgroundDrawable(theBorderDrawable);

			row.addView(fach);
			row.addView(note);
			row.addView(credits);

			TableRow.LayoutParams llp = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
			// llp.setMargins(2, 2, 2, 2); // llp.setMargins(left, top, right,
			// bottom);
			llp.weight = 5;
			fach.setLayoutParams(llp);
			llp.weight = 1;
			note.setLayoutParams(llp);
			llp.weight = 1;
			credits.setLayoutParams(llp);
			fach.setEms(20);
			note.setEms(10);
			credits.setEms(10);
			aLayout.setBackgroundDrawable(theBorderDrawable);
			aLayout.addView(row);
		}

		private void initializeMarks() {
			noten = new ArrayList<Note>();
			noten.add(new Note("Angewantde Mathematik", "2.1", "6"));
			noten.add(new Note("Software Enineering", "1.0", "6"));
			noten.add(new Note("Datenbanken I", "2.8", "8"));
			noten.add(new Note("Rechnerarchitekturen", "3.8", "5"));
			noten.add(new Note("Formale Sprachen und Automaten", "5.0", "8"));
			noten.add(new Note("Netztechnik", "3.5", "6"));

		}

		/**
		 * Initialisiert die ExpandableListView der Mailansicht.
		 */
		public void initMailView(View aView) {
			// TODO Mailmanager auslagern könnte performance verbessern.
			MailManager manager = new MailManager(
					((CustomApplication) getActivity().getApplication())
							.getUserManager().getUser());
			final ExpandableListAdapter listAdapter = new ExpandableListAdapter(
					getActivity(), new ArrayList<Message>());
			final ExpandableListView expListView;
			expListView = (ExpandableListView) aView
					.findViewById(R.id.mailExpandView);
			expListView.setAdapter(listAdapter);
			manager.getMessagesFromTo(1, 40, new MailListener() {
				@Override
				public void mailReceived(List<Message> someMails) {
					listAdapter.addAllMessages(someMails);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							listAdapter.notifyDataSetChanged();
						}
					});
				}
			});
			expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

				private int lastExpand = -1;

				@Override
				public void onGroupExpand(int groupPosition) {
					if (lastExpand > -1 && lastExpand != groupPosition) {
						expListView.collapseGroup(lastExpand);
					}
					lastExpand = groupPosition;
				}
			});
		}
	}

	/**
	 * Startet die Login-Activity, beendet die Main-Activity und versteckt die
	 * ActionBar.
	 */
	public void logout() {
		((CustomApplication) getApplication()).getUserManager().logout();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
		actionBar.hide();
	}

	public void showSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
	}
}