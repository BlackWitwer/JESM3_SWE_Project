package com.jesm3.newDualis.jinterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.jesm3.newDualis.is.Utilities;
import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Wochenplan;

public class DualisParser {
	Utilities util = new Utilities();
	
	public DualisParser() {
	}

	// JSoup HTML Parser, schnell aber noch kein Parsing mit xsl files
	public ArrayList<Wochenplan> parseMonth(String aContent) {
		ArrayList<Wochenplan> stdl = new ArrayList<Wochenplan>();
		StundenplanGenerator stdgr = new StundenplanGenerator();
		Document doc = Jsoup.parse(aContent);

		// Siehe auch http://jsoup.org/apidocs/org/jsoup/select/Selector.html
		Elements weeks = doc.select("tr"); // RAM???

		for (int i = 1; i < weeks.size(); i++) {
			boolean addWeek = true;
			boolean calenderSet = false;

			Elements days = weeks.get(i).select(".tbMonthDayCell");
			stdgr = new StundenplanGenerator();

			// Wochenafangsdatum auslesen und setzen
			Elements datelinks = null;

			if (days.size() > 6 && calenderSet == false) {
				datelinks = days.get(6).select("a");
				if (datelinks.size() > 0) {
					String wochenEnddatum = datelinks.get(0).attr("title");
					stdgr.getStd().setEndDatum(wochenEnddatum);
					stdgr.setKalenderwoche(wochenEnddatum);
				}
			}

			datelinks = days.get(0).select("a");
			if (datelinks.size() > 0) {
				String wochenAnfangsdatum = datelinks.get(0).attr("title");
				stdgr.getStd().setAnfangsDatum(wochenAnfangsdatum);
				stdgr.setKalenderwoche(wochenAnfangsdatum);
				calenderSet = true;

				GregorianCalendar gc = stdgr.stringToGreg(wochenAnfangsdatum);
				int kalenderWoche = gc.get(GregorianCalendar.WEEK_OF_YEAR);
				int jahr = gc.get(GregorianCalendar.YEAR);
				GregorianCalendar gcnow = new GregorianCalendar();
				int kalenderWocheNow = gcnow
						.get(GregorianCalendar.WEEK_OF_YEAR);
				int jahrNow = gcnow.get(GregorianCalendar.YEAR);
				Log.d("parsetest", "kalenderWoche:" + kalenderWoche
						+ " kalenderWocheNow" + kalenderWocheNow + "jahr"
						+ jahr + " jahrNow" + jahrNow);
				if (kalenderWoche < kalenderWocheNow && jahr == jahrNow) {
					addWeek = false;
				}
			}

			for (int j = 0; j < days.size(); j++) {
				ArrayList<Vorlesung> vorlesungen = generateVorlesungen(days
						.get(j));
				if (vorlesungen.size() == 0) {
					// Dummyvorlesung wenn Vorlesung in Monat nicht geladen
					Vorlesung dummy = new Vorlesung();
					dummy.setDozent("FREEDAY");
					stdgr.addVorlesung(j, dummy);
				}
				for (int k = 0; k < vorlesungen.size(); k++) {

					stdgr.addVorlesung(j, vorlesungen.get(k));
				}
			}
			for (int j = 0; j < 6; j++) {
				if (stdgr.getDays(stdgr.getStd()).get(j).size() == 0) { // Prüft
																		// ob
																		// eine
																		// Vorlesung
																		// an
																		// jedem
																		// Tag
																		// existiert
					Vorlesung dummy = new Vorlesung();
					dummy.setDozent("DRFAIL");
					stdgr.addVorlesung(j, dummy);
				}
			}
			if (addWeek) {
				stdl.add(stdgr.getStd());
			} else {
				Log.d("parsetest", "Wochenplan wird nicht geaddet");
			}
		}
		String out = "";
		for (int i = 0; i < stdl.size(); i++) {
			out = out + "Kalenderwoche\n" + stdl.get(i).toString() + "\n\n";
		}
		Log.d("parsetest", out.toString());
		return stdl;
		// Wenn Erste/Letzte Woche nicht komplett, diese Funktion erneut
		// ausf�hren und
		// mit erstem/letzten Stundenplan mergen
	}

	public ArrayList<Vorlesung> generateVorlesungen(Element day) {
		ArrayList<Vorlesung> vorlesungen = new ArrayList<Vorlesung>();
		Elements vlink = day.select(".apmntLink");
		for (int i = 0; i < vlink.size(); i++) {
			String title = vlink.get(i).attr("title");
			// Log ausgabe?
			System.out.println("TITLE: " + title);

			String[] splitTitle = title.split("/");
			String[] splitTime = splitTitle[0].split("-");
			Elements atags = day.select("a");
			String datum = "DD.MM.YYYY";
			if (atags.size() > 0) {
				datum = atags.get(0).attr("title");
			}//

			String uhrzeitVon = splitTime[0].trim();
			String uhrzeitBis = splitTime[1].trim();
			String dozent = "???";
			String raum = splitTitle[1].trim(); // TODO STG-RB41-4.14-TINF
												// "-TINF" notwendig??
			String name = splitTitle[2].trim();
			Date uhrZeitVonDate = util.dateAndTimeToDate(datum,uhrzeitVon);
			Date uhrZeitBisDate = util.dateAndTimeToDate(datum,uhrzeitBis);
			Vorlesung dayv = new Vorlesung(uhrZeitVonDate, uhrZeitBisDate, dozent, name, raum);

			vorlesungen.add(dayv);
		}
		return vorlesungen;
	}

	// Methode die den Link mit xslt herausparst, weiterverwenden?
	public String parseLink(String aContent, String selector) {
		Document doc = Jsoup.parse(aContent);
		Element link = doc.select(selector).first();
		String linkHref = link.attr("href");
		return linkHref;
	}

}
