package com.jesm3.newDualis.activities;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;
import com.jesm3.newDualis.*;
import com.jesm3.newDualis.is.*;
import com.jesm3.newDualis.jinterface.DualisConnection;
import com.jesm3.newDualis.mail.*;
import java.util.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.jesm3.newDualis.mail.ExpandableListAdapter;
import com.jesm3.newDualis.stupla.Stundenplan;

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
		
		//Nur zu Testzwecken. Unterbindet eine Sicherung die es nicht erlaubt im Interface Thread Netzwerkaktivitäten zu verwenden.
		StrictMode.ThreadPolicy policy = new StrictMode.
			ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//-----------------------------------
		
//		initMailView();
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
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	View rootView = inflater.inflate(R.layout.error_main, container, false);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
            	rootView = inflater.inflate(R.layout.stundenplan_main, container, false);
            	break;
            case 2:
            	rootView = inflater.inflate(R.layout.semesterplan_main, container, false);
            	break;
            case 3:
            	rootView = inflater.inflate(R.layout.noten_main, container, false);
            	break;
            case 4:
            	rootView = inflater.inflate(R.layout.mail_main, container, false);
   ExpandableListAdapter listAdapter;
   ExpandableListView expListView;
   expListView = (ExpandableListView) rootView.findViewById(R.id.mailExpandView);
   MailManager manager = new MailManager(((CustomApplication)getActivity().getApplication()).getUserManager().getUser());
   listAdapter = new ExpandableListAdapter(getActivity(), manager.getMessagesFromTo(1, 30));

   expListView.setAdapter(listAdapter);
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
        
    }
    
    /**
     * Initialisiert die ExpandableListView der Mailansicht.
     */
    public void initMailView() {
    	ExpandableListAdapter listAdapter;
        ExpandableListView expListView;
    	
        expListView = (ExpandableListView) findViewById(R.id.mailExpandView);
        MailManager manager = new MailManager(((CustomApplication)getApplication()).getUserManager().getUser());
        listAdapter = new ExpandableListAdapter(this, manager.getMessagesFromTo(1, 30));
 
        expListView.setAdapter(listAdapter);
    }
    
    /**
     * Startet die Login-Activity, beendet die Main-Activity und versteckt die ActionBar.
     */
    public void logout() {
    	((CustomApplication)getApplication()).getUserManager().logout();
    	startActivity(new Intent(this, LoginActivity.class));
    	finish();
    	actionBar.hide();
	}

    public void showSettings() {
    	startActivity(new Intent(this, SettingsActivity.class));
	}
}