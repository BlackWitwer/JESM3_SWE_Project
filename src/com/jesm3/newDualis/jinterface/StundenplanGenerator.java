package com.jesm3.newDualis.jinterface;

import com.jesm3.newDualis.stupla.Stundenplan;
import com.jesm3.newDualis.stupla.Vorlesung;

public class StundenplanGenerator {
	public Stundenplan getStd() {
		return std;
	}
	Stundenplan std= new Stundenplan();
	public StundenplanGenerator() {
		std =  new Stundenplan();
	}
	public void addVorlesung(int day, Vorlesung v){
		switch (day) {
		case 0:  std.addMontag(v);
        break;
		case 1:  std.addDienstag(v);
        break;
		case 2:  std.addMittwoch(v);
        break;
		case 3:  std.addDonnerstag(v);
        break;
		case 4:  std.addFreitag(v);
        break;
		case 5:  std.addSamstag(v);
        break;
        default: break;
		}
	}
}