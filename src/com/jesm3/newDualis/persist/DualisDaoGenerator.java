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
		addMail(theSchema);
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
	
	private static void addMail(Schema aSchema) {
		Entity theMail = aSchema.addEntity("AbstractMailContainer");
		
		theMail.addIdProperty().autoincrement();
		theMail.addStringProperty("from");
		theMail.addStringProperty("fromComplete");
		theMail.addStringProperty("to");
		theMail.addStringProperty("subject");
		theMail.addStringProperty("text");
		theMail.addDateProperty("date");
		theMail.addBooleanProperty("attachment");
		theMail.addBooleanProperty("seen");
		theMail.addBooleanProperty("html");
		theMail.addIntProperty("messageNumber");
		theMail.addLongProperty("uId");
	}
	
	private static void addNoten(Schema aSchema) {
		Entity theNote = aSchema.addEntity("AbstractNote");
		theNote.addIdProperty().autoincrement();
		theNote.addStringProperty("titel");
		theNote.addStringProperty("note");
		theNote.addStringProperty("credits");
	}
}
