package com.jesm3.newDualis.jinterface;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jesm3.newDualis.stupla.Stundenplan;
import com.jesm3.newDualis.stupla.Vorlesung;

import android.content.res.Resources;
import android.text.Html;
import android.util.Log;

public class DualisParser {
	Resources r;
	
	public DualisParser(Resources r){
		this.r = r;
	}
	
	// JSoup HTML Parser, schnell aber noch kein Parsing mit xsl files 
	public String parseMonth(String aContent){
		ArrayList<Stundenplan> stdl = new ArrayList<Stundenplan>();
		StundenplanGenerator stdgr = new StundenplanGenerator();
		Document doc = Jsoup.parse(aContent);
		
		// Siehe auch http://jsoup.org/apidocs/org/jsoup/select/Selector.html
		Elements weeks = doc.select("tr"); //RAM???

		for(int i=1;i<weeks.size();i++){
			Elements days = weeks.get(i).select(".tbMonthDayCell");
			stdgr = new StundenplanGenerator();
			for(int j=0;j<days.size();j++){
				ArrayList<Vorlesung> vorlesungen = generateVorlesungen(days.get(j));
				for(int k=0;k<vorlesungen.size();k++){
					stdgr.addVorlesung(j, vorlesungen.get(k));
				}
			}
			stdl.add(stdgr.getStd());
		}
		String out = "";
		for (int i=0;i<stdl.size();i++){
			out=out+"Kalenderwoche\n"+stdl.get(i).toString()+"\n\n";
		}
		Log.d("parsetest", out.toString());
		return out;
	}
	
	
	
	public ArrayList<Vorlesung> generateVorlesungen(Element day){
		ArrayList<Vorlesung> vorlesungen = new ArrayList<Vorlesung>();
		Elements link = day.select(".apmntLink");
		for(int i=0;i<link.size();i++) {
			String title = link.get(i).attr("title");
			String[] splitTitle = title.split("/");
			String[] splitTime = splitTitle[0].split("-");
			String uhrzeitVon=splitTime[0].trim();
			String uhrzeitBis=splitTime[1].trim();
			String dozent="???";
			String name=splitTitle[2].trim();
			Vorlesung dayv = new Vorlesung(uhrzeitVon, uhrzeitBis, dozent, name);
			vorlesungen.add(dayv);
		}
		return vorlesungen;
	}
	
	// Methode die den Link mit xslt herausparst, weiterverwenden?
	public String parseLink(String aContent, String selector){
		Document doc = Jsoup.parse(aContent);
		Element link = doc.select(selector).first();
		String linkHref = link.attr("href");
		return linkHref;
	}
	
}
