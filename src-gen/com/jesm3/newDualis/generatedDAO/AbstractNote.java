package com.jesm3.newDualis.generatedDAO;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ABSTRACT_NOTE.
 */
public class AbstractNote {

    private Long id;
    private String titel;
    private String note;
    private String credits;

    public AbstractNote() {
    }

    public AbstractNote(Long id) {
        this.id = id;
    }

    public AbstractNote(Long id, String titel, String note, String credits) {
        this.id = id;
        this.titel = titel;
        this.note = note;
        this.credits = credits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
