package com.jesm3.newDualis.activities;

import java.util.ArrayList;
import java.util.Locale;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.stupla.Stundenplan;
import com.jesm3.newDualis.stupla.Vorlesung;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		actionBar.show();
    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (doubleClicked) {
			super.onBackPressed();
			return;
		}
		this.doubleClicked = true;
		Toast.makeText(this, "Nochmal klicken um die App zu schlie�en...", Toast.LENGTH_SHORT).show();
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
            // Show 3 total pages.
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
    	
    	private Stundenplan stupla;
    	private ArrayList<Note> noten;
    	
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }
        
        private void setLecturesOnGUI(View aContainer) {
        	TextView kw = (TextView) aContainer.findViewById(R.id.stupla_kalenderwoche);
    		LinearLayout lay_montag = (LinearLayout) aContainer.findViewById(R.id.lay_montag);
    		LinearLayout lay_dienstag = (LinearLayout) aContainer.findViewById(R.id.lay_dienstag);
    		LinearLayout lay_mittwoch = (LinearLayout) aContainer.findViewById(R.id.lay_mittwoch);
    		LinearLayout lay_donnerstag = (LinearLayout) aContainer.findViewById(R.id.lay_donnerstag);
    		LinearLayout lay_freitag = (LinearLayout) aContainer.findViewById(R.id.lay_freitag);
    		LinearLayout lay_samstag = (LinearLayout) aContainer.findViewById(R.id.lay_samstag);
    		TableLayout tab_all = (TableLayout) aContainer.findViewById(R.id.stupla_table);
    		
    		generateDay(stupla.getMontag(), lay_montag);
    		generateDay(stupla.getDienstag(), lay_dienstag);
    		generateDay(stupla.getMittwoch(), lay_mittwoch);
    		generateDay(stupla.getDonnerstag(), lay_donnerstag);
    		generateDay(stupla.getFreitag(), lay_freitag);
    		generateDay(stupla.getSamstag(), lay_samstag);
    		kw.setText("Kalenderwoche " + stupla.getKalenderwoche());
    		
    	}
        
        private void initializeLectures() {
    		stupla = new Stundenplan();
    		stupla.setKalenderwoche("41");
    		
    		stupla.addMontag(new Vorlesung(
    				"09:00", 
    				"12:30", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addMontag(new Vorlesung(
    				"13:30", 
    				"16:00", 
    				"Rentschler", 
    				"Software Engineering"));
    		
    		stupla.addDienstag(new Vorlesung(
    				"09:00", 
    				"12:30", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addDienstag(new Vorlesung(
    				"13:30", 
    				"16:00", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addDienstag(new Vorlesung(
    				"16:30", 
    				"17:30", 
    				"Rentschler", 
    				"Software Engineering"));
    		
    		stupla.addMittwoch(new Vorlesung(
    				"09:00", 
    				"12:30", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addMittwoch(new Vorlesung(
    				"13:30", 
    				"16:00", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addMittwoch(new Vorlesung(
    				"16:30", 
    				"17:30", 
    				"Rentschler", 
    				"Software Engineering"));
    		
    		stupla.addDonnerstag(new Vorlesung(
    				"09:00", 
    				"12:30", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addDonnerstag(new Vorlesung(
    				"13:30", 
    				"16:00", 
    				"Rentschler", 
    				"Software Engineering"));
    		
    		stupla.addFreitag(new Vorlesung(
    				"09:00", 
    				"12:30", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addFreitag(new Vorlesung(
    				"13:30", 
    				"16:00", 
    				"Rentschler", 
    				"Software Engineering"));
    		stupla.addFreitag(new Vorlesung(
    				"16:30", 
    				"17:30", 
    				"Rentschler", 
    				"Software Engineering"));
    	}
    	
    	private void generateDay(ArrayList<Vorlesung> aDayList, LinearLayout aLayout) {
    		for(Vorlesung eachLecture : aDayList ) {
    			LinearLayout subLayout = new LinearLayout(getActivity());
    			TextView zeit = new TextView(getActivity());
    			TextView dozent= new TextView(getActivity());
    			TextView name= new TextView(getActivity());
    			
    			subLayout.setOrientation(LinearLayout.VERTICAL);
    			if (aLayout.getChildCount() % 2 == 0) {
    				subLayout.setBackgroundColor(Color.WHITE);
    			} else {
    				subLayout.setBackgroundColor(Color.LTGRAY);
    			}
    			subLayout.setWeightSum(1);
    			
    			zeit.setText(eachLecture.getUhrzeitVon() + " - " + eachLecture.getUhrzeitBis() + " Uhr");
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
        	View rootView = inflater.inflate(R.layout.error_main, container, false);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
            	rootView = inflater.inflate(R.layout.stundenplan_main, container, false);
            	initializeLectures();
            	setLecturesOnGUI(rootView);
            	rootView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            	break;
            case 2:
            	rootView = inflater.inflate(R.layout.semesterplan_main, container, false);
            	break;
            case 3:
            	rootView = inflater.inflate(R.layout.noten_main, container, false);
            	initializeMarks();
            	setMarksOnGui(rootView);
            	rootView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            	break;
            case 4:
            	rootView = inflater.inflate(R.layout.mail_main, container, false);
            	break;
            case 5:
            	rootView = inflater.inflate(R.layout.dozent_main, container, false);
            	break;
            default:
            	rootView = inflater.inflate(R.layout.error_main, container, false);
            	break;
            }
            
            return rootView;
        }

		private void setMarksOnGui(View aContainer) {
			TableLayout lay_table = (TableLayout) aContainer.findViewById(R.id.noten_table);
			
			for(Note eachMark : noten) {
				TableRow row = new TableRow(getActivity());
				TextView fach = new TextView(getActivity());
				TextView note = new TextView(getActivity());
				TextView credits = new TextView(getActivity());
				fach.setText(eachMark.getName());
				note.setText(eachMark.getNote());
				credits.setText(eachMark.getCredits());
				
				fach.setBackgroundColor(Color.WHITE);
				note.setBackgroundColor(Color.WHITE);
				credits.setBackgroundColor(Color.WHITE);
				
				row.addView(fach);
				row.addView(note);
				row.addView(credits);
				lay_table.addView(row);
			}
		}

		private void initializeMarks() {
			noten = new ArrayList<Note>();
//			noten.add(new Note("Mathe", "2", "6"));
			
		}
        
    }
    
    /**
     * Startet die Login-Activity, beendet die Main-Activity und versteckt die ActionBar.
     */
    public void logout() {
    	startActivity(new Intent(this, LoginActivity.class));
    	finish();
    	actionBar.hide();
	}

    public void showSettings() {
    	startActivity(new Intent(this, SettingsActivity.class));
	}
}
