package com.jesm3.newDualis.jinterface;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.jesm3.newDualis.stupla.Vorlesung;
import com.jesm3.newDualis.stupla.Wochenplan;

public class DualisParser {

	public DualisParser() {
	}

	// JSoup HTML Parser, schnell aber noch kein Parsing mit xsl files
	public ArrayList<Wochenplan> parseMonth(String aContent) {
		ArrayList<Wochenplan> stdl = new ArrayList<Wochenplan>();
		StundenplanGenerator stdgr = new StundenplanGenerator();
		Document doc = Jsoup.parse(aContent);

		// Siehe auch http://jsoup.org/apidocs/org/jsoup/select/Selector.html
		Elements weeks = doc.select("tr"); // RAM???

		String datum = "DD.MM.YYYY";
		for (int i = 1; i < weeks.size(); i++) {
			Elements days = weeks.get(i).select(".tbMonthDayCell");
			stdgr = new StundenplanGenerator();
			for (int j = 0; j < days.size(); j++) {
				ArrayList<Vorlesung> vorlesungen = generateVorlesungen(days
						.get(j));
				for (int k = 0; k < vorlesungen.size(); k++) {
					stdgr.addVorlesung(j, vorlesungen.get(k));
				}
			}
			stdl.add(stdgr.getStd());
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
			String raum = splitTitle[1].trim(); // STG-RB41-4.14-TINF "-TINF"
												// notwendig??
			String name = splitTitle[2].trim();
			Vorlesung dayv = new Vorlesung(uhrzeitVon, uhrzeitBis, dozent,
					name, datum, raum);
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
