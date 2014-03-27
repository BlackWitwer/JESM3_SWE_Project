package com.jesm3.newDualis.noten;

public class Note {
	String kennnummer;
	String titel;
	String note;
	String credits;
	
	public Note() {
		this("","","","");
	}
	
	public Note(String titel) {
		this("",titel,"","");
	}

	public Note(String kennnummer, String titel, String note, String credits) {
		this.kennnummer = kennnummer;
		this.titel = titel;
		this.note = note;
		this.credits = credits;
	}

	public String getKennnummer() {
		return kennnummer;
	}

	public void setKennnummer(String kennnummer) {
		this.kennnummer = kennnummer;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCredits() {
		return credits;
	}

	public void setCredits(String credits) {
		this.credits = credits;
	}

	@Override
	public String toString() {
		return "Note [kennnummer=" + kennnummer + ", titel=" + titel
				+ ", note=" + note + ", credits=" + credits + "]";
	}
	
}
