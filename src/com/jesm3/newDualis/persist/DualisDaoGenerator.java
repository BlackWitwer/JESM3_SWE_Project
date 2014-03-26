package com.jesm3.newDualis.persist;

import java.io.File;
import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DualisDaoGenerator {
	
	public static void main(String[] args) throws IOException, Exception {
		Schema theSchema = new Schema(3, "com.jesm3.newDualis.generatedDAO");
		
		addVorlesung(theSchema);
		addNoten(theSchema);
		
		File theFile = new File("./src-gen");
		if (!theFile.exists()) {
			theFile.mkdir();
		}
		
		new DaoGenerator().generateAll(theSchema, "./src-gen");
	}
	
	private static void addVorlesung(Schema aSchema) {
		Entity theVorlesung = aSchema.addEntity("AbstractVorlesung");
		theVorlesung.addIdProperty().autoincrement();
		theVorlesung.addStringProperty("name");
		theVorlesung.addStringProperty("dozent");
		theVorlesung.addDateProperty("uhrzeitVon");
		theVorlesung.addDateProperty("uhrzeitBis");
		theVorlesung.addStringProperty("raum");
	}
	
	private static void addNoten(Schema aSchema) {
		Entity theNote = aSchema.addEntity("AbstractNote");
		theNote.addIdProperty().autoincrement();
		theNote.addStringProperty("titel");
		theNote.addStringProperty("note");
		theNote.addStringProperty("credits");
	}
}
