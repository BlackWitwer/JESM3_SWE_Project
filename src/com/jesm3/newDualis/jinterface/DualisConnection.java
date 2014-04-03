package com.jesm3.newDualis.jinterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.activities.MainActivity;
import com.jesm3.newDualis.is.Backend;
import com.jesm3.newDualis.is.User;
import com.jesm3.newDualis.noten.Note;
import com.jesm3.newDualis.noten.Semester;
import com.jesm3.newDualis.is.Utilities;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Vorlesung.Requests;
import com.jesm3.newDualis.stupla.Wochenplan;
import com.jesm3.newDualis.stupla.Wochenplan.Days;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class DualisConnection {

	private DefaultHttpClient httpClient;
	private DualisParser dparse;
	private DualisLinks mlinks;
	private Backend backend;
	Utilities util;

	public DualisConnection(Backend aBackend) {
		httpClient = new DefaultHttpClient();
		dparse = new DualisParser();
		this.backend = aBackend;
		util = new Utilities();
	}

	/**
	 * Prüft ob die Benutzerdaten korrekt sind. TODO Prüfen ob ein vorheriger
	 * check der Logindaten mit einem richtigen Login zum Konflickt führt.
	 * 
	 * @param aUser
	 * @return
	 */
	public boolean simpleLoginCheck(User aUser) {
		try {
			// Erster Seitenaufruf um das Cookie zu bekommen.
			loadPage("https://dualis.dhbw.de/scripts/mgrqcgi?APPNAME=CampusNet&PRGNAME=EXTERNALPAGES&ARGUMENTS=-N000000000000001,-N000324,-Awelcome");

			// Zweiter Aufruf zum übertragen der Benutzerdaten.
			HttpPost loginPost = generateLoginPost(aUser.getUsername(),
					aUser.getPassword());
			HttpResponse loginResponse;
			loginResponse = httpClient.execute(loginPost);

			// Ist die Zweite Seite leer bis auf die Standard Html Konstrukte
			// war der Login erfolgreich.
			if (EntityUtils.toString(loginResponse.getEntity()).length() < 100) {
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean login(User aUser) {
		try {
			httpClient = new DefaultHttpClient();
			// Erster Seitenaufruf um das Cookie zu bekommen.
			loadPage("https://dualis.dhbw.de/scripts/mgrqcgi?APPNAME=CampusNet&PRGNAME=EXTERNALPAGES&ARGUMENTS=-N000000000000001,-N000324,-Awelcome");

			// Zweiter Aufruf zum übertragen der Benutzerdaten.
			HttpPost loginPost = generateLoginPost(aUser.getUsername(),
					aUser.getPassword());
			HttpResponse loginResponse;
			loginResponse = httpClient.execute(loginPost);

			// BISHER ArrayOutOfBoundException bei falschen Logindaten da nicht
			// existierender Header abgefragt wird
			// Erster Redirect der aus dem Header des zweiten Response gelesen
			// wird. Im Value stehen vor dem Link noch ein "0; URL=" deshalb
			// substring 7.
			if (loginResponse.getHeaders("REFRESH").length < 1) {
				return false;
			}
			HttpGet firstRedirect = new HttpGet("https://dualis.dhbw.de"
					+ loginResponse.getHeaders("REFRESH")[0].getValue()
							.substring(7));
			HttpResponse firstRedirectResponse = httpClient
					.execute(firstRedirect);

			// Zweiter Redirect der aus dem Header des ersten Redirect gelesen
			// wird. Im Value stehen vor dem Link noch ein "0;URL=" deshalb
			// substring 6.
			HttpGet secondRedirect = new HttpGet("https://dualis.dhbw.de"
					+ firstRedirectResponse.getHeaders("REFRESH")[0].getValue()
							.substring(6));
			HttpResponse secondRedirectResponse = httpClient
					.execute(secondRedirect);
			HttpEntity redirectEntity = secondRedirectResponse.getEntity();

			// Parse Hauptmen�links von Startseite
			getMainMenuLinks(EntityUtils.toString(redirectEntity));

			return true;
			// Erfolgreich eingeloggt.
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	

	// Parse Hauptmen�links von Startseite
	public void getMainMenuLinks(String startPageContent) {
		mlinks = new DualisLinks();
		mlinks.setStundenPlan(dparse.parseLink(startPageContent, ".link000028"));
		mlinks.setNoten(dparse.parseLink(startPageContent, ".link000307"));
	}

	public void loadNoten() {
		String notenContent = getPage("https://dualis.dhbw.de" + mlinks.getNoten());
		String[][] semester = dparse.parseSemesterLinks(notenContent);
		ArrayList<Note> noten = new ArrayList<Note>();
		for(int k=0 ; k < semester.length ; k++) {
			String realNotenContent = getPage("https://dualis.dhbw.de" + mlinks.getNoten() + ",-N" + semester[k][0]);
			Semester aktSemester = dparse.parseNoten(realNotenContent);
			Log.d("noten", "Semester:"+aktSemester.toString());
			noten.addAll(aktSemester.getNoten());
		}
		this.backend.getNotenManager().setNoten(noten);
	}
	
	// Geht zur Monats�bersicht und parst den Stundenplan
	public ArrayList<Vorlesung> loadStundenplan(int weeks) {
		String stundenplanContent = getPage("https://dualis.dhbw.de"
				+ mlinks.getStundenPlan());

		String parseLink = dparse.parseLink(stundenplanContent, ".link000031");
		String monatsansichtContent = getPage("https://dualis.dhbw.de"
				+ parseLink);
		ArrayList<Wochenplan> wl = dparse.parseMonth(monatsansichtContent);
		int monthsToGo = util.calcMonthsToGo(weeks);
		
		GregorianCalendar gcnow = new GregorianCalendar();
		int kalenderWocheNow = gcnow.get(GregorianCalendar.WEEK_OF_YEAR);
		
		if (wl.get(0).getAnfangsDatum()==null&&wl.get(0).getKalenderwoche()==kalenderWocheNow){ //prüfe ob woche überhaupt derzeitige kalenderwoche!
			Log.d("parsetest", "Erste Wochenhälfte fehlt, springe einen Monat zurück!!!");
			parseLink = dparse.parseLink(monatsansichtContent, ".img_arrowLeft");
			monatsansichtContent = getPage("https://dualis.dhbw.de" + parseLink);
			monthsToGo++;
			wl = dparse.parseMonth(monatsansichtContent);
			for(int i=0;i<wl.size()-1;i++){
				wl.remove(0);
			}
		}
		else {
			if(wl.get(0).getAnfangsDatum()==null){
				wl.remove(0);
			}
			else{
				if(util.dateToString(wl.get(0).getAnfangsDatum()).matches("01.([0-9].){1}.[0-9]*")==false) {
					wl.remove(0);
				}
			}
		}
		Log.d("parsetest", "Monate die geladen werden müssen: "+monthsToGo);
		for (int i=0; i<monthsToGo; i++) {
			parseLink = dparse.parseLink(monatsansichtContent, ".img_arrowRight");
			Log.d("parsetest", "Nächster Monat: "+parseLink);
			monatsansichtContent = getPage("https://dualis.dhbw.de" + parseLink); // GEHT AUS IRGENDEINEM GRUND AUF DEN NÄCHSTEN TAG???
			ArrayList<Wochenplan> wl2 = dparse.parseMonth(monatsansichtContent);
			Wochenplan mergeWeek = new StundenplanGenerator().mergeWeeks(wl.get(wl.size()-1), wl2.get(0));
			if(mergeWeek==null){
				wl.addAll(wl2);
			}
			else{
				mergeWeek.setKalenderwoche(wl.get(wl.size()-1).getKalenderwoche());
				wl.remove(wl.size()-1);
				wl2.remove(0);
				wl.add(mergeWeek);
				wl.addAll(wl2);
			}
		}
		wl.remove(wl.size()-1); // Letzte halbe Woche wieder löschen!
		
		SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(backend.getCustomApplication());
		thePrefs.edit().putInt(StundenplanGenerator.LAST_LOADED_WEEK, wl.get(wl.size()-1).getKalenderwoche()).commit();
		
		ArrayList<Vorlesung> alleVorlesungen = new ArrayList<Vorlesung>();
		for (Wochenplan eachWoche : wl) {
			if(eachWoche.getEndDatum()==null){
				Date anfangsDatum = eachWoche.getAnfangsDatum();
				Date endDatum = util.addDaysToDate(anfangsDatum, 7);
				eachWoche.setEndDatumDate(endDatum);
			}
			if(eachWoche.getAnfangsDatum()==null){
				Date endDatum = eachWoche.getEndDatum();
				Date anfangsDatum = util.addDaysToDate(endDatum, -7);
				eachWoche.setAnfangsDatumDate(anfangsDatum);
			}
			eachWoche = util.addDateToFreedays(eachWoche);
			alleVorlesungen.addAll(util.vorlesungenToList(eachWoche));
		}
//		backend.getDbManager().deleteVorlesungen(Requests.REQUEST_ALL);
//		backend.getDbManager().insertVorlesungen(alleVorlesungen);
//		List<Wochenplan> parsedWeeks= new StundenplanGenerator().generateWochenplaene(backend.getDbManager().getVorlesungen(Requests.REQUEST_ALL), backend.getCustomApplication());
//		List<Wochenplan> parsedWeeks= new StundenplanGenerator().generateWochenplaene(alleVorlesungen);
		for (Wochenplan eachWoche : wl) {
			this.backend.getVorlesungsplanManager().addWochenplan(eachWoche);
//			new StundenplanGenerator().generateWochenplaene(eachWoche.getDay(Days.MONTAG));
		}
		return alleVorlesungen;
	}

	// L�d Seite ohne HTML Code zur�ckzugeben
	public void loadPage(String url) {
		HttpGet startseite = new HttpGet(url);
		try {
			httpClient.execute(startseite);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// L�d Seite und gibt deren HTML Code als String zur�ck
	public String getPage(String url) {
		String pageContent = "";
		try {
			HttpGet target = new HttpGet(url);
			HttpResponse pageResponse = httpClient.execute(target);
			HttpEntity pageEntity = pageResponse.getEntity();
			pageContent = EntityUtils.toString(pageEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pageContent;
	}

	// Generiert POST f�r Login
	public HttpPost generateLoginPost(String user, String pw) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("usrname", user));
		nameValuePairs.add(new BasicNameValuePair("pass", pw));
		nameValuePairs.add(new BasicNameValuePair("APPNAME", "CampusNet"));
		nameValuePairs.add(new BasicNameValuePair("PRGNAME", "LOGINCHECK"));
		nameValuePairs.add(new BasicNameValuePair("ARGUMENTS",
				"clino,usrname,pass,menuno,menu_type,browser,platform"));
		nameValuePairs.add(new BasicNameValuePair("clino", "000000000000001"));
		nameValuePairs.add(new BasicNameValuePair("menuno", "000324"));
		nameValuePairs.add(new BasicNameValuePair("menu_type", "classic"));
		HttpPost loginPost = new HttpPost(
				"https://dualis.dhbw.de/scripts/mgrqcgi");
		try {
			loginPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return loginPost;
	}
}
