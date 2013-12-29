package com.example.budgetguru;

public class Finance {
	// private variables
	int id;
	String date;
	String category;
	String description;
	int income;
	int expense;

	// default constructor
	public Finance() {
		super();
	}

	// parameterized constructor
	public Finance(int id, String date, int expense, String category,
			String description, int income) {
		super();
		this.id = id;
		this.date = date;
		this.category = category;
		this.description = description;
		this.income = income;
		this.expense = expense;
	}

	// parameterized constructor
	public Finance(String date, int expense, String category,
			String description, int income) {
		super();

		this.date = date;
		this.category = category;
		this.description = description;
		this.income = income;
		this.expense = expense;
	}

	// parameterized constructor
	public Finance(String date, int expense, String category, String description) {
		super();
		this.date = date;
		this.category = category;
		this.description = description;
		this.expense = expense;
	}

	// getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getExpense() {
		return expense;
	}

	public void setExpense(int expense) {
		this.expense = expense;
	}

	public Finance(String date, String category, String description,
			int income, int expense) {
		super();
		this.date = date;
		this.category = category;
		this.description = description;
		this.income = income;
		this.expense = expense;
	}

}