package com.example.twittershort;

public class Notes {
	int id;
	String note;
	
	public Notes () {}
	
	public Notes(int id, String note) {
		this.id = id;
		this.note = note;
	}
	
	public Notes (String note) {
		this.note = note;
	}
	
	public int getID () {
		return this.id;
	}
	
	public void setID (int id) {
		this.id = id;
	}
	
	public String getNote () {
		return this.note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
}
