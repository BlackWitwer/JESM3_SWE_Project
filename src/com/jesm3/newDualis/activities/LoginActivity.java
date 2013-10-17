package com.jesm3.newDualis.activities;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.activities.MainActivity.SectionsPagerAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity{
	
	/**
	 * Variable für die ActionBar
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
	}

	/**
	 * Mehtode für den Login. Wird vom "Login"-Button aufgerufen. 
	 * @param v 
	 */
	public void login(View v) {
    	EditText theUser = (EditText) findViewById(R.id.name);
    	EditText thePW = (EditText) findViewById(R.id.passwort);
//    	if ("Manu".equals(theUser.getText().toString()) && "asdf".equals(thePW.getText().toString())) {
    		startActivity(new Intent(this, MainActivity.class));
    		finish();
//    	} else {
//    		TextView label = (TextView) findViewById(R.id.error_label);
//    		label.setVisibility(View.VISIBLE);
//    	}
	}
}
