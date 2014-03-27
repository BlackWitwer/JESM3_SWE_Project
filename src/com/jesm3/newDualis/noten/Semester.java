package com.jesm3.newDualis.noten;
import java.util.ArrayList;

public class Semester {
	String name;
	ArrayList<Note> noten = new ArrayList<Note>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Note> getNoten() {
		return noten;
	}
	public void setNoten(ArrayList<Note> noten) {
		this.noten = noten;
	}
	public Semester(String name) {
		this.name = name;
	}
	public void addNote(Note neueNote){
		noten.add(neueNote);
	}
	
	public String toString(){
		return this.name+"\n"+noten.toString();
	}
	
}
