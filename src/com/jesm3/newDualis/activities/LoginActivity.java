package com.jesm3.newDualis.activities;

import java.util.ArrayList;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.activities.MainActivity.SectionsPagerAdapter;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.is.User;
import com.jesm3.newDualis.jinterface.DualisConnection;
import com.jesm3.newDualis.stupla.Stundenplan;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity{
	
	/**
	 * Variable f�r die ActionBar
	 */
	ActionBar actionBar;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		actionBar = getActionBar();
	
		final EditText passwortField = (EditText) findViewById(R.id.passwort);
		OnEditorActionListener listener2 = new OnEditorActionListener() {
			
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 boolean handled = false;
			     if (actionId == EditorInfo.IME_ACTION_DONE) {
			    	 login(v);
			         handled = true;
			        }
			     return handled;
			}
		};
		passwortField.setOnEditorActionListener(listener2);
		
		User theUser = ((CustomApplication)getApplication()).getUserManager().loadUserData();
		if (theUser != null) {
			startActivity(new Intent(this, MainActivity.class));
		}
		
		//Nur zu Testzwecken. Unterbindet eine Sicherung die es nicht erlaubt im Interface Thread Netzwerkaktivitäten zu verwenden.
				StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
		//-----------------------------------
	}

	/**
	 * Mehtode f�r den Login. Wird vom "Login"-Button aufgerufen. 
	 * @param v
	 */
	public void login(View v) {	    
    	EditText theUser = (EditText) findViewById(R.id.name);
		String theUsername = theUser.getText().toString().trim();
		if (!theUsername.contains("@")) {
			theUsername = theUsername + "@lehre.dhbw-" + ((Spinner)findViewById(R.id.spinner_standort)).getSelectedItem().toString() + ".de";
		}
    	EditText thePW = (EditText) findViewById(R.id.passwort);
    	CheckBox theSaveFlag = (CheckBox) findViewById(R.id.checkbox_save);
    	
    	if (((CustomApplication)getApplication()).getUserManager().login(theUsername, thePW.getText().toString(), theSaveFlag.isChecked())) {
    		startActivity(new Intent(this, MainActivity.class));
    		finish();
    	} else {
    		TextView label = (TextView) findViewById(R.id.error_label);
    		label.setVisibility(View.VISIBLE);
    	}
	}
}
