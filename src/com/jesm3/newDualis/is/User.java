package com.jesm3.newDualis.is;

public class User {

	private String username;
	private String password;
	
	public User(String aUsername, String aPassword) {
		this.username = aUsername;
		this.password = aPassword;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
