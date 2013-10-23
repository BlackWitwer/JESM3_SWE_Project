package com.jesm3.newDualis.jinterface;

public class DualisLinks {

	//Datenklasse für geparste Links, damit diese nicht erneut geparst werden müssen
	
	String stundenPlan;
	String leistung;
	String noten;
	String monatsAnsichtStundenplan;
	
	public String getStundenPlan() {
		return stundenPlan;
	}
	public void setStundenPlan(String stundenPlan) {
		this.stundenPlan = stundenPlan;
	}
	public String getLeistung() {
		return leistung;
	}
	public void setLeistung(String leistung) {
		this.leistung = leistung;
	}
	public String getNoten() {
		return noten;
	}
	public void setNoten(String noten) {
		this.noten = noten;
	}
	public String getMonatsAnsichtStundenplan() {
		return monatsAnsichtStundenplan;
	}
	public void setMonatsAnsichtStundenplan(String monatsAnsichtStundenplan) {
		this.monatsAnsichtStundenplan = monatsAnsichtStundenplan;
	}
	
}
