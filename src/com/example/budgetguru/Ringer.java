package com.example.budgetguru;

public class Ringer {
	// private variables
	int id;
	String password;
	String format;

	// Empty constructor
	public Ringer() {

	}

	public Ringer(int id, String password, String format) {
		super();
		this.id = id;
		this.password = password;
		this.format = format;
	}

	public Ringer(String password, String format) {
		super();
		this.password = password;
		this.format = format;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
