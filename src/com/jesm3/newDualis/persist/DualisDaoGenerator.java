package com.jesm3.newDualis.persist;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DualisDaoGenerator {
	
	public static void main(String[] args) throws IOException, Exception {
		Schema theSchema = new Schema(3, "com.jesm3.newDualis.stupla");
		
		addVorlesung(theSchema);
		
		new DaoGenerator().generateAll(theSchema, "./src-gen");
	}
	
	private static void addVorlesung(Schema aSchema) {
		Entity theVorlesung = aSchema.addEntity("AbstractVorlesung");
		theVorlesung.addIdProperty().autoincrement();
		theVorlesung.addStringProperty("name");
		theVorlesung.addStringProperty("dozent");
		theVorlesung.addStringProperty("datum");
		theVorlesung.addStringProperty("uhrzeitVon");
		theVorlesung.addStringProperty("uhrzeitBis");
		theVorlesung.addStringProperty("raum");
	}
}
