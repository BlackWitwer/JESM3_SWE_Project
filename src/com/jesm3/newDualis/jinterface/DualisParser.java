package com.jesm3.newDualis.jinterface;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.res.Resources;
import android.text.Html;

public class DualisParser {
	Resources r;
	
	public DualisParser(Resources r){
		this.r = r;
	}
	
	// JSoup HTML Parser, schnell aber noch kein Parsing mit xsl files 
	public String parseHtml(String aContent){
		Document doc = Jsoup.parse(aContent);
		
		// Siehe auch http://jsoup.org/apidocs/org/jsoup/select/Selector.html
		Elements monthel = doc.select(".apmntLink"); // Alle Elemente mit class="apmntLink"
		
		String out = "";
		for(int i=0; i<monthel.size();i++){
			out=out+monthel.get(0).toString();
		}
		return out;
	}
	
	// Methode die den Link mit xslt herausparst, weiterverwenden?
	public String parseLink(String aContent, int xslSystemId){
		String parseResult = parse(aContent, xslSystemId);
		return Html.fromHtml(parseResult.substring(parseResult.indexOf("/"))).toString();
	}
	
	// XSLT, leider sehr langsam
	public String parse(String aContent, int xslSystemId) {
		CustomOutput out = new CustomOutput();
		try {
			Source xmlSource = new StreamSource(new StringReader(aContent));
			Source xsltSource = new StreamSource(r.openRawResource(xslSystemId));
			
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans;
			trans = transFact.newTransformer(xsltSource);
			StreamResult result = new StreamResult(out);
			trans.transform(xmlSource,result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return out.getResult();
	}
	
	class CustomOutput extends OutputStream {
		private String result = "";
		@Override
		public void write(int oneByte) throws IOException {
			result = result + new String(new byte[]{(byte) oneByte});
		}
		public String getResult() {
			return result;
		}
	}
}
