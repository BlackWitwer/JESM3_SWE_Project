package com.jesm3.newDualis.activities;

import java.util.ArrayList;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.activities.MainActivity;
import com.jesm3.newDualis.activities.MainActivity.SectionsPagerAdapter;
import com.jesm3.newDualis.is.CustomApplication;
import com.jesm3.newDualis.is.User;
import com.jesm3.newDualis.jinterface.DualisConnection;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
		
		//Nur zu Testzwecken. Unterbindet eine Sicherung die es nicht erlaubt im Interface Thread Netzwerkaktivitäten zu verwenden.
		StrictMode.ThreadPolicy policy = new StrictMode.
				ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//-----------------------------------
		
		boolean theSuccessFlag = ((CustomApplication)getApplication()).getUserManager().loadUserData();
		if (theSuccessFlag) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}

	/**
	 * Mehtode f�r den Login. Wird vom "Login"-Button aufgerufen. 
	 * @param v
	 */
	public void login(View v) {	    
		final ProgressDialog theWaitDialog = ProgressDialog.show(LoginActivity.this, "Anmelden", "Sie werden angemeldet...", true);
		theWaitDialog.setCancelable(false);
		
    	EditText theUser = (EditText) findViewById(R.id.name);
		String theUsername = theUser.getText().toString().trim();
		if (!theUsername.contains("@")) {
			theUsername = theUsername + "@lehre.dhbw-" + ((Spinner)findViewById(R.id.spinner_standort)).getSelectedItem().toString() + ".de";
		}
    	EditText thePW = (EditText) findViewById(R.id.passwort);
    	CheckBox theSaveFlag = (CheckBox) findViewById(R.id.checkbox_save);
    	
    	final String theName = theUsername;
    	final String thePassword = thePW.getText().toString();
    	final boolean theFlag = theSaveFlag.isChecked();
    	final Activity theActivity = this;
    	//Im Fall einer Frage nicht zu MBA oder SEW kommen!
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (((CustomApplication)getApplication()).getUserManager().login(theName, thePassword, theFlag)) {
					theActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							theWaitDialog.dismiss();
						}
					});
					theActivity.startActivity(new Intent(theActivity, MainActivity.class));
					
					theActivity.finish();
				} else {
					theActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							theWaitDialog.dismiss();
							TextView label = (TextView) findViewById(R.id.error_label);
							label.setVisibility(View.VISIBLE);
						}
					});
				}
				
			}
		}).start();
	}
}
