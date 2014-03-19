package com.jesm3.newDualis.generatedDAO;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ABSTRACT_VORLESUNG.
 */
public class AbstractVorlesung {

    private Long id;
    private String name;
    private String dozent;
    private String datum;
    private String uhrzeitVon;
    private String uhrzeitBis;
    private String raum;

    public AbstractVorlesung() {
    }

    public AbstractVorlesung(Long id) {
        this.id = id;
    }

    public AbstractVorlesung(Long id, String name, String dozent, String datum, String uhrzeitVon, String uhrzeitBis, String raum) {
        this.id = id;
        this.name = name;
        this.dozent = dozent;
        this.datum = datum;
        this.uhrzeitVon = uhrzeitVon;
        this.uhrzeitBis = uhrzeitBis;
        this.raum = raum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDozent() {
        return dozent;
    }

    public void setDozent(String dozent) {
        this.dozent = dozent;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getUhrzeitVon() {
        return uhrzeitVon;
    }

    public void setUhrzeitVon(String uhrzeitVon) {
        this.uhrzeitVon = uhrzeitVon;
    }

    public String getUhrzeitBis() {
        return uhrzeitBis;
    }

    public void setUhrzeitBis(String uhrzeitBis) {
        this.uhrzeitBis = uhrzeitBis;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

}
