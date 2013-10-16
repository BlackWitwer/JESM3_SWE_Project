package com.jesm3.newDualis.activities;


import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.jinterface.DualisConnection;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private long timestamp;
	private long tempTimestamp;
	private DualisConnection dcon;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		((TextView) findViewById(R.id.textViewLeft)).setMovementMethod(new ScrollingMovementMethod());
		((TextView) findViewById(R.id.textViewRight)).setMovementMethod(new ScrollingMovementMethod());
		((TextView) findViewById(R.id.textView)).setMovementMethod(new ScrollingMovementMethod());
		//Nur zu Testzwecken. Unterbindet eine Sicherung die es nicht erlaubt im Interface Thread NetzwerkaktivitÃ¤ten zu verwenden.
		StrictMode.ThreadPolicy policy = new StrictMode.
				ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//-----------------------------------
		
		timestamp = System.currentTimeMillis();
		tempTimestamp = System.currentTimeMillis();

		try {
			dcon = new DualisConnection(this);
			dcon.login();  // Dualis Login
			dcon.loadStundenplan();  // Stundenplan laden
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// In rechte Textview posten
	public void appendToRightTV(String aText){
		((TextView) findViewById(R.id.textViewRight)).setText(aText);
	}
	
	// In linke Textview posten
	public void appendToLeftTV(String aText){
		((TextView) findViewById(R.id.textViewLeft)).setText(aText);
	}

	// In Konsole posten
	public void appendToConsole(String aText) {
		((TextView) findViewById(R.id.textView)).append(aText+"\n");
	}

	// Zeitstempel machen, gib Gesamtzeit und Differenz zum letzten Zeitstempel zurück
	public void appendTimestamp(String aText) {
		long time = System.currentTimeMillis();
		((TextView) findViewById(R.id.textView)).append(aText+ " ----> Seit Start: " + (time-timestamp) + "ms / Differenz: " + (time-tempTimestamp) + "ms\n");
		tempTimestamp = time;
	}
}

