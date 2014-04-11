package com.jesm3.newDualis.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import com.jesm3.newDualis.is.Utilities;
import com.jesm3.newDualis.mail.ExpandableListAdapter;
import com.jesm3.newDualis.mail.MailContainer;
import com.jesm3.newDualis.mail.MailExpandableListView;
import com.jesm3.newDualis.mail.MailListener;
import com.jesm3.newDualis.mail.MailManager;
import com.jesm3.newDualis.noten.Note;
import com.jesm3.newDualis.noten.NotenManager;
import com.jesm3.newDualis.stupla.SemesterplanExportDialog;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.VorlesungsplanManager;
import com.jesm3.newDualis.stupla.Wochenplan;
import com.jesm3.newDualis.stupla.Wochenplan.Days;
import com.jesm3.newDualis.stupla.WochenplanArrayAdapter;

public class MainActivity extends FragmentActivity implements SemesterplanExportDialog.NoticeDialogListener{

	public interface GUICallbackIF {
		public void refreshMarks();
		
		public void refreshLectures();
	}
	
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
	private static String logname = "mainActivity";
	// for nude purpose only
	private long timestamp = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (doubleClicked) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			return;
		}
		this.doubleClicked = true;
		Toast.makeText(this, R.string.app_close,
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
		case R.id.action_export:
			SemesterplanExportDialog theDialog = new SemesterplanExportDialog();
			theDialog.show(getFragmentManager(), "ExportDialog");
			return true;
		case R.id.action_logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	public void addNotification(View v) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    Notification myNotification = new Notification(R.drawable.icon, "Notification!", System.currentTimeMillis());
	    Context context = getApplicationContext();
	    String notificationTitle = "Exercise of Notification!";
	    String notificationText = "http://android-er.blogspot.com/";
	    Intent myIntent = new Intent(this, MainActivity.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,   myIntent, Intent.FILL_IN_ACTION);
	    myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
	    myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
	    notificationManager.notify(1, myNotification);
	}
	
	/**
	 * Die Funktion, welche vom Aktualisieren Button des Wochenplans aufgerufen
	 * wird.
	 */
	public void updateStupla(final View v) {
		final Activity theActivity = this;

		GUICallbackIF guiCallback = new GUICallbackIF() {
			
			@Override
			public void refreshMarks() {
				//Nichts...
			}
			
			@Override
			public void refreshLectures() {
				theActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((SectionFragment) mSectionsPagerAdapter.getItem(0))
								.initializeLectures(v.getRootView());
						((SectionFragment) mSectionsPagerAdapter.getItem(0))
								.setLecturesOnGui(v.getRootView());
					}
				});
			}
		};
		
		mSectionsPagerAdapter.getItem(0);
		int result = ((CustomApplication) getApplication()).getSyncService()
				.manualSync(guiCallback);
		if (result != 0) {
			Toast.makeText(
					((CustomApplication) getApplication()).getSyncService(),
					"Verbindung fehlgeschlagen", Toast.LENGTH_SHORT).show();
		} else {

		}
		getFragmentManager().findFragmentById(R.layout.stundenplan_main);
		// XXX
		if (System.currentTimeMillis() - timestamp < 100) {
			Toast.makeText(((CustomApplication) getApplication()).getSyncService(), "( . )( . )", Toast.LENGTH_SHORT)
					.show();
		}
		timestamp = System.currentTimeMillis();
	}
	
	/**
	 * Die Funktion, welche vom Aktualisieren Button der Notenseite aufgerufen wird.
	 */
	public void updateNoten(final View v) {
		final Activity theActivity = this;
		GUICallbackIF guiCallback = new GUICallbackIF() {
			
			@Override
			public void refreshMarks() {
				theActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						((SectionFragment) mSectionsPagerAdapter.getItem(0)).initializeMarks();
						((SectionFragment) mSectionsPagerAdapter.getItem(0)).setMarksOnGui(v.getRootView());						
					}
				});
				
			}
			
			@Override
			public void refreshLectures() {
				//Nichts...
			}
		};
		int result = ((CustomApplication) getApplication()).getSyncService()
				.manualMarkSync(guiCallback);
		// XXX
		if (System.currentTimeMillis() - timestamp < 100) {
			this.startActivity(new Intent(this, SpecialActivity.class));
		}
		timestamp = System.currentTimeMillis();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Map<Integer, Fragment> fragmentMap = new HashMap<Integer, Fragment>();
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (fragmentMap.containsKey(position)) {
				return fragmentMap.get(position);
			}
			Fragment fragment = new SectionFragment();
			Bundle args = new Bundle();
			args.putInt(SectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			fragmentMap.put(position, fragment);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section3).toUpperCase(l);
			case 2:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A fragment representing a section of the app.
	 */
	public static class SectionFragment extends Fragment {

		private Wochenplan stupla;
		private ArrayList<Note> noten;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";


		public SectionFragment() {
		}

		protected void setLecturesOnGui(View aContainer) {
    		LinearLayout lay_montag = (LinearLayout) aContainer.findViewById(R.id.lay_montag);
    		LinearLayout lay_dienstag = (LinearLayout) aContainer.findViewById(R.id.lay_dienstag);
    		LinearLayout lay_mittwoch = (LinearLayout) aContainer.findViewById(R.id.lay_mittwoch);
    		LinearLayout lay_donnerstag = (LinearLayout) aContainer.findViewById(R.id.lay_donnerstag);
    		LinearLayout lay_freitag = (LinearLayout) aContainer.findViewById(R.id.lay_freitag);
    		LinearLayout lay_samstag = (LinearLayout) aContainer.findViewById(R.id.lay_samstag);
    		
    		lay_montag.removeAllViews();
    		lay_dienstag.removeAllViews();
    		lay_mittwoch.removeAllViews();
    		lay_donnerstag.removeAllViews();
    		lay_freitag.removeAllViews();
    		lay_samstag.removeAllViews();
    		
    		TextView text_montag = new TextView(aContainer.getContext());
    		TextView text_dienstag = new TextView(aContainer.getContext());
    		TextView text_mittwoch = new TextView(aContainer.getContext());
    		TextView text_donnerstag = new TextView(aContainer.getContext());
    		TextView text_freitag = new TextView(aContainer.getContext());
    		TextView text_samstag = new TextView(aContainer.getContext());
    		
    		text_montag.setText("Montag");
    		text_dienstag.setText("Dienstag");
    		text_mittwoch.setText("Mittwoch");
    		text_donnerstag.setText("Donnerstag");
    		text_freitag.setText("Freitag");
    		text_samstag.setText("Samstag");
    		
    		text_montag.setTextAppearance(lay_montag.getContext(), android.R.style.TextAppearance_Medium);
    		text_dienstag.setTextAppearance(lay_dienstag.getContext(), android.R.style.TextAppearance_Medium);
    		text_mittwoch.setTextAppearance(lay_mittwoch.getContext(), android.R.style.TextAppearance_Medium);
    		text_donnerstag.setTextAppearance(lay_donnerstag.getContext(), android.R.style.TextAppearance_Medium);
    		text_freitag.setTextAppearance(lay_freitag.getContext(), android.R.style.TextAppearance_Medium);
    		text_samstag.setTextAppearance(lay_samstag.getContext(), android.R.style.TextAppearance_Medium);
    		
    		lay_montag.addView(text_montag);
    		lay_dienstag.addView(text_dienstag);
    		lay_mittwoch.addView(text_mittwoch);
    		lay_donnerstag.addView(text_donnerstag);
    		lay_freitag.addView(text_freitag);
    		lay_samstag.addView(text_samstag);
    		
    		if (stupla != null) {
    			generateDay(stupla.getDay(Days.MONTAG), lay_montag);
    			generateDay(stupla.getDay(Days.DIENSTAG), lay_dienstag);
    			generateDay(stupla.getDay(Days.MITTWOCH), lay_mittwoch);
    			generateDay(stupla.getDay(Days.DONNERSTAG), lay_donnerstag);
    			generateDay(stupla.getDay(Days.FREITAG), lay_freitag);
    			generateDay(stupla.getDay(Days.SAMSTAG), lay_samstag);
    			
    		}
    		
    	}
        
        private void initializeLectures(final View aContainer) {
        	final GregorianCalendar cal = new GregorianCalendar();
    		final VorlesungsplanManager theVorlesungsplanManager = ((CustomApplication)getActivity().getApplication()).getBackend().getVorlesungsplanManager();
			stupla = theVorlesungsplanManager.getWochenplan(cal.get(GregorianCalendar.WEEK_OF_YEAR));
			if (stupla == null) {
				Log.d(logname, "stupla is null");
				((CustomApplication) getActivity()
						.getApplication()).getSyncService().getLecturesforGui();
				while(stupla == null) {
					stupla = theVorlesungsplanManager.getWochenplan(cal
						.get(GregorianCalendar.WEEK_OF_YEAR));
				}
				stupla = new Wochenplan();
			}
    		HashMap<Integer, Wochenplan> theWochenMap = theVorlesungsplanManager.getWochenMap();
    		
    		Set<Entry<Integer, Wochenplan>> theEntrySet = theWochenMap.entrySet();
    		List<Wochenplan> theWochenplaene = new ArrayList<Wochenplan>();
    		for (Entry<Integer, Wochenplan> eachEntry : theEntrySet) {
				theWochenplaene.add(eachEntry.getValue());
			}
    		Collections.sort(theWochenplaene, new Comparator<Wochenplan>() {

				@Override
				public int compare(Wochenplan lhs, Wochenplan rhs) {
					if (lhs.getKalenderwoche() < rhs.getKalenderwoche()) {
						return -1;
					} else {
						return 1;
					}
				}
			});
    		
    		Spinner kwSpinner = (Spinner) aContainer.findViewById(R.id.spinner_kalenderwoche);
    		WochenplanArrayAdapter theKwAdapter = new WochenplanArrayAdapter(aContainer.getContext(), android.R.layout.simple_spinner_item, theWochenplaene);
    		
    		kwSpinner.setAdapter(theKwAdapter);
    		kwSpinner.setOnItemSelectedListener( new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					stupla = (Wochenplan) arg0.getSelectedItem();
					setLecturesOnGui(aContainer);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// erstmal nichts...
					
				}
			});
    	}
    	
    	private void generateDay(ArrayList<Vorlesung> aDayList, LinearLayout aLayout) {
    		for(Vorlesung eachLecture : aDayList ) {
    			LinearLayout subLayout = new LinearLayout(getActivity());
    			subLayout.setOrientation(LinearLayout.VERTICAL);
    			if (aLayout.getChildCount() % 2 == 0) {
    				subLayout.setBackgroundColor(Color.WHITE);
    			} else {
    				subLayout.setBackgroundColor(Color.LTGRAY);
    			}
    			if (!"FREEDAY".equals(eachLecture.getDozent())) {
    				TextView zeit = new TextView(getActivity());
//    				FIXME Dozent noch nicht implementiert
//    				TextView dozent= new TextView(getActivity());
    				TextView name= new TextView(getActivity());
    				
    				
    				zeit.setText(Utilities.dateToTime(eachLecture.getUhrzeitVon()) + " - " + Utilities.dateToTime(eachLecture.getUhrzeitBis()) + " Uhr");
    				subLayout.addView(zeit);
    				
    				name.setText(eachLecture.getName());
    				subLayout.addView(name);
    				
//    				FIXME Dozent noch nicht implementiert
//    				dozent.setText(eachLecture.getDozent());
//    				subLayout.addView(dozent);
    				
    				aLayout.addView(subLayout);
    			} else {
    				TextView name= new TextView(getActivity());
    				name.setText("Frei/Praxis/Klausur");
    				
    				subLayout.addView(name);
    				
    				aLayout.addView(subLayout);
    			}
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
            	setLecturesOnGui(rootView);
            	rootView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				break;
			case 2:
				rootView = inflater.inflate(R.layout.noten_main, container,
						false);
				initializeMarks();
				setMarksOnGui(rootView);
				rootView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				break;
			case 3:
				rootView = inflater.inflate(R.layout.mail_main, container,
						false);
				initMailView(rootView);
				break;
			default:
				rootView = inflater.inflate(R.layout.error_main, container,
						false);
				break;
			}

			return rootView;
		}

		protected void setMarksOnGui(View aContainer) {
			TableLayout lay_table = (TableLayout) aContainer.findViewById(R.id.noten_table);
			lay_table.removeAllViews();
			addHeaderRow(lay_table);
			
			for(Note eachMark : noten) {
				addRow(eachMark.getTitel(), eachMark.getNote(), eachMark.getCredits(), lay_table);
			}
			

		}
		
		public void addHeaderRow(TableLayout aLayout) {
			TableRow row = new TableRow(getActivity());
			TextView fach = new TextView(getActivity());
			TextView note = new TextView(getActivity());
			TextView credits = new TextView(getActivity());
			
			fach.setText("Fach");
			note.setText("Note");
			credits.setText("Credits");
			
			fach.setTextAppearance(aLayout.getContext(), R.style.Bold);
			note.setTextAppearance(aLayout.getContext(), R.style.Bold);
			credits.setTextAppearance(aLayout.getContext(), R.style.Bold);

			
			fach.setBackgroundResource(R.drawable.border_header);
			note.setBackgroundResource(R.drawable.border_header);
			credits.setBackgroundResource(R.drawable.border_header);
			
			
		    TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		    llp.weight = 5;
		    fach.setLayoutParams(llp);
		    llp.weight = 1;
		    note.setLayoutParams(llp);
		    llp.weight = 2;
		    credits.setLayoutParams(llp);
			
		    fach.setEms(20);
			note.setEms(10);
			credits.setEms(10);
			
			row.addView(fach);
			row.addView(note);
			row.addView(credits);
			
			aLayout.addView(row);
		}
		
		public void addRow(String aVal1, String aVal2, String aVal3, TableLayout aLayout) {
			// Neue Layouts und Views erstellen...
			TableRow row = new TableRow(getActivity());
			TextView fach = new TextView(getActivity());
			TextView note = new TextView(getActivity());
			TextView credits = new TextView(getActivity());
			
			
			// Borders setzen
			fach.setBackgroundResource(R.drawable.border);
			note.setBackgroundResource(R.drawable.border);
			credits.setBackgroundResource(R.drawable.border);
			
			// TextViews in das Layout einfÃ¼gen...
			row.addView(fach);
			row.addView(note);
			row.addView(credits);
			
		    // Layoutparamter setzen...
			TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		    llp.setMargins(0, 5, 0, 0);
			llp.weight = 5;
		    fach.setLayoutParams(llp);
		    llp.weight = 1;
		    note.setLayoutParams(llp);
		    llp.weight = 2;
		    credits.setLayoutParams(llp);
			
		    fach.setEms(20);
			note.setEms(10);
			credits.setEms(10);
			
			//Werte setzen...
			fach.setText(aVal1);
			note.setText(aVal2);
			credits.setText(aVal3);
			
			// Die fertige TableRow in das parentLayout einfÃ¼gen...
			aLayout.addView(row);
		}
		
		private void initializeMarks() {
			//TODO Hier muss noch die anzeige gemacht werden
			noten = new ArrayList<Note>();
			final NotenManager theNotenManager = ((CustomApplication)getActivity().getApplication()).getBackend().getNotenManager();
			noten.addAll(theNotenManager.getNoten()); 
			if (noten.size() == 0) {
				((CustomApplication) getActivity()
						.getApplication()).getSyncService().getMarksForGui();
				while (noten.size() == 0){
					noten.addAll(theNotenManager.getNoten());
				}
			}

			//noten.addAll()
			//VorlesungsplanManager.get
			/*
			noten = new ArrayList<Note>();
			noten.add(new Note("Angewantde Mathematik", "2.1", "6"));
			noten.add(new Note("Software Enineering", "1.0", "6"));
			noten.add(new Note("Datenbanken I", "2.8", "8"));
			noten.add(new Note("Rechnerarchitekturen", "3.8", "5"));
			noten.add(new Note("Formale Sprachen und Automaten", "5.0", "8"));
			noten.add(new Note("Netztechnik", "3.5", "6"));
			*/
		}

		/**
		 * Initialisiert die ExpandableListVi2ew der Mailansicht.
		 */
		private void initMailView(final View aView) {
			final MailManager manager = ((CustomApplication) getActivity().getApplication()).getBackend().getMailManager();
			final ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(),
					new ArrayList<MailContainer>());
			final MailExpandableListView expListView;
			expListView = (MailExpandableListView) aView
					.findViewById(R.id.mailExpandView);
			expListView.setAdapter(listAdapter);
			expListView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
			listAdapter.setMessages(manager.getCachedMails());
			if (manager.getCachedMails().isEmpty()) {
				aView.findViewById(R.id.mailProgressBar).setVisibility(View.VISIBLE);
			} else {
				aView.findViewById(R.id.mailProgressBar).setVisibility(View.GONE);
			}
			manager.getLatestMessages(10, new MailListener() {
				@Override
				public void mailReceived() {
					listAdapter.setMessages(((CustomApplication) getActivity().getApplication()).getBackend().getMailManager().getCachedMails());
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							aView.findViewById(R.id.mailProgressBar).setVisibility(View.GONE);
						}
					});
				}
			});
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (true) {
						if (expListView.getLastVisiblePosition() >= listAdapter.getGroupCount()-1) {
							System.out.println("LADEN");
							manager.getNextOlderMessage(2);
							listAdapter.setMessages(manager.getCachedMails());
						}
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
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
		((CustomApplication) getApplication()).logout();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
		actionBar.hide();
	}

	public void showSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
	}

	@Override
	public void onDialogPositiveClick(SemesterplanExportDialog dialog) {
		// TODO MBA/Zeitdieb Semesterplan Export anstoÃŸen hier
		Log.d("TestMBA", "Pos" + dialog.getmSelectedItems());
	}

	@Override
	public void onDialogNegativeClick(SemesterplanExportDialog dialog) {
		// Nichts tun
		
	}
}
