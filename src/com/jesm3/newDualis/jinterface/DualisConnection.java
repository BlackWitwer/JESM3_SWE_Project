package com.jesm3.newDualis.jinterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import android.text.Html;
import android.widget.TextView;

public class DualisConnection {

	private DefaultHttpClient httpClient;
	private MainActivity main;
	private DualisParser dparse;
	private DualisLinks mlinks;
	
	public DualisConnection(MainActivity mainActivity){
		httpClient = new DefaultHttpClient();
		this.main = mainActivity;
		dparse = new DualisParser(mainActivity.getResources());  //Initialisiere Parser
	}
	
	
	public void login() throws IOException {
		//Erster Seitenaufruf um das Cookie zu bekommen.
		loadPage("https://dualis.dhbw.de/scripts/mgrqcgi?APPNAME=CampusNet&PRGNAME=EXTERNALPAGES&ARGUMENTS=-N000000000000001,-N000324,-Awelcome");

		//Zweiter Aufruf zum übertragen der Benutzerdaten.
		HttpPost loginPost = generateLoginPost("it12018@lehre.dhbw-stuttgart.de","EUERPW");
		HttpResponse loginResponse = httpClient.execute(loginPost);

		// BISHER ArrayOutOfBoundException bei falschen Logindaten da nicht existierender Header abgefragt wird
		//Erster Redirect der aus dem Header des zweiten Response gelesen wird. Im Value stehen vor dem Link noch ein "0; URL=" deshalb substring 7.
		HttpGet firstRedirect = new HttpGet("https://dualis.dhbw.de" + loginResponse.getHeaders("REFRESH")[0].getValue().substring(7));
		HttpResponse firstRedirectResponse = httpClient.execute(firstRedirect);

		//Zweiter Redirect der aus dem Header des ersten Redirect gelesen wird. Im Value stehen vor dem Link noch ein "0;URL=" deshalb substring 6.
		HttpGet secondRedirect = new HttpGet("https://dualis.dhbw.de" + firstRedirectResponse.getHeaders("REFRESH")[0].getValue().substring(6));
		HttpResponse secondRedirectResponse = httpClient.execute(secondRedirect);
		HttpEntity redirectEntity = secondRedirectResponse.getEntity();
		
		//Parse Hauptmen�links von Startseite
		getMainMenuLinks(EntityUtils.toString(redirectEntity));
		
		//Erfolgreich eingeloggt.
	}
	
	//Parse Hauptmen�links von Startseite
	public void getMainMenuLinks(String startPageContent){
		mlinks = new DualisLinks();
		mlinks.setStundenPlan(dparse.parseLink(startPageContent, ".link000028"));
	}
	
	// Geht zur Monats�bersicht und parst den Stundenplan
	public void loadStundenplan() {
		String stundenplanContent = getPage("https://dualis.dhbw.de" + mlinks.getStundenPlan());

		String parseLink = dparse.parseLink(stundenplanContent, ".link000031");
		String monatsansichtContent = getPage("https://dualis.dhbw.de" + parseLink);

		String vorlesungen = dparse.parseMonth(monatsansichtContent);
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
	public HttpPost generateLoginPost(String user, String pw){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("usrname", user));
		nameValuePairs.add(new BasicNameValuePair("pass", pw));
		nameValuePairs.add(new BasicNameValuePair("APPNAME", "CampusNet"));
		nameValuePairs.add(new BasicNameValuePair("PRGNAME", "LOGINCHECK"));
		nameValuePairs.add(new BasicNameValuePair("ARGUMENTS", "clino,usrname,pass,menuno,menu_type,browser,platform"));
		nameValuePairs.add(new BasicNameValuePair("clino", "000000000000001"));
		nameValuePairs.add(new BasicNameValuePair("menuno", "000324"));
		nameValuePairs.add(new BasicNameValuePair("menu_type", "classic"));
		HttpPost loginPost = new HttpPost("https://dualis.dhbw.de/scripts/mgrqcgi");
		try {
			loginPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return loginPost;
	}
}
