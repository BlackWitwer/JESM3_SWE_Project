package com.jesm3.newDualis.noten;

import com.jesm3.newDualis.generatedDAO.AbstractNote;

public class Note extends AbstractNote {
	
	public Note(AbstractNote aNote) {
		super(aNote.getId(), aNote.getTitel(), aNote.getNote(), aNote
				.getCredits());
	}

	public Note() {
		this("","","","");
	}
	
	public Note(String titel) {
		this("",titel,"","");
	}

	public Note(String kennnummer, String titel, String note, String credits) {
		super(null, titel, note, credits);
	}

	@Override
	public String toString() {
		return "Note [kennnummer=" + getId() + ", titel=" + getTitel()
				+ ", note=" + getNote() + ", credits=" + getCredits() + "]";
	}
	
}
