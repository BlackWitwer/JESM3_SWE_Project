package com.jesm3.newDualis.activities;

public class Note {
	private String name;
	private String note;
	private String credits;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param name
	 * @param note
	 * @param credits
	 */
	public Note(String name, String note, String credits) {
		super();
		this.name = name;
		this.note = note;
		this.credits = credits;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the credits
	 */
	public String getCredits() {
		return credits;
	}
	/**
	 * @param credits the credits to set
	 */
	public void setCredits(String credits) {
		this.credits = credits;
	}
	
	

}
