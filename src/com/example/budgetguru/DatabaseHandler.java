package com.example.budgetguru;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite Database handler class To create datbase table and perform opertions
 * on it
 * 
 * 
 * @version 1.1
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "mcsproj";

	// Finance table name
	private static final String TABLE_FINANCE = "budget";

	// Finance Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_DESC = "description";
	private static final String KEY_INCOME = "income";
	private static final String KEY_EXPENSE = "expense";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTICES_TABLE = "CREATE TABLE " + TABLE_FINANCE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
				+ KEY_EXPENSE + " INTEGER," + KEY_CATEGORY + " TEXT,"
				+ KEY_DESC + " TEXT," + KEY_INCOME + " INTEGER" + ")";
		System.out.println("Query= " + CREATE_NOTICES_TABLE);
		db.execSQL(CREATE_NOTICES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINANCE);

		// Create tables again
		onCreate(db);

	}

	/**
	 * Adding a new transaction info
	 * 
	 * @param finance
	 * @return rowid of inserted row, -1 if error
	 */
	long addTrans(Finance finance) {
		System.out.println("Adding in database");
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, finance.getDate());
		values.put(KEY_EXPENSE, finance.getExpense());
		values.put(KEY_CATEGORY, finance.getCategory());
		values.put(KEY_DESC, finance.getDescription());
		values.put(KEY_INCOME, finance.getIncome());

		// Inserting Row
		long return_val = db.insert(TABLE_FINANCE, null, values);

		db.close(); // Closing database connection
		return return_val;
	}

	/**
	 * Get all transactions
	 * 
	 * @return List of transactions
	 */
	public List<Finance> getAllRecords() {
		List<Finance> transList = new ArrayList<Finance>();
		// Select All Query
		String selectQuery = "SELECT date,category,description,expense FROM budget";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Finance fobj = new Finance();

					fobj.setDate(cursor.getString(0));
					fobj.setCategory(cursor.getString(1));
					fobj.setDescription(cursor.getString(2));
					fobj.setExpense(cursor.getInt(3));
					// Adding contact to list
					transList.add(fobj);

				} while (cursor.moveToNext());
			}
		}
		db.close();
		return transList;
	}

	/**
	 * Get unique Categories entered so far
	 * 
	 * @return list of categories
	 */
	public ArrayList<String> getAllCategories() {
		ArrayList<String> transList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT distinct category FROM budget";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					transList.add(cursor.getString(0));

				} while (cursor.moveToNext());
			}
		}
		db.close();
		return transList;
	}

	/**
	 * Gettting transaction info of particular date
	 * 
	 * @param date
	 * @return List of transactions
	 */
	public List<Finance> getRecords(String date) {
		List<Finance> recordList = new ArrayList<Finance>();
		// Select All Query
		String selectQuery = "SELECT date,category,description,expense FROM budget where date=\""
				+ date + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Finance fobj = new Finance();

					fobj.setDate(cursor.getString(0));
					fobj.setExpense(cursor.getInt(3));
					fobj.setCategory(cursor.getString(1));
					fobj.setDescription(cursor.getString(2));
					// Adding contact to list
					recordList.add(fobj);

				} while (cursor.moveToNext());
			}
		}
		db.close();
		return recordList;
	}

	/**
	 * Getting transaction records of particular month
	 * 
	 * @param month
	 * @return List of transactions
	 */
	public List<Finance> getmonthRecords(String month) {
		List<Finance> recordList = new ArrayList<Finance>();
		String selectQuery = "SELECT date,sum(expense),category,description FROM budget where date like '%"
				+ month + "%' group by category";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					// cursor.moveToNext();// get the month
					String date = cursor.getString(0);
					/*
					 * String date_split[] = date.split("/");
					 * System.out.println("dateeee in db="+date_split[1]); //
					 * check the month if (date_split[1].equals(month))
					 */{
						Finance fobj = new Finance();
						fobj.setDate(date);
						fobj.setExpense(cursor.getInt(1));
						fobj.setCategory(cursor.getString(2));
						fobj.setDescription(cursor.getString(3));
						// Adding to list
						recordList.add(fobj);
					}

				} while (cursor.moveToNext());
			}
		}

		db.close();
		return recordList;
	}

	/**
	 * Get unique category and corresponding summed amount
	 * 
	 * @param date
	 * @return List of transactions
	 */
	public List<Finance> getuniqueRecords(String date) {
		System.out.println("ddddtttt in db=" + date);
		List<Finance> recordList = new ArrayList<Finance>();
		String selectQuery = "SELECT sum(expense) ,category FROM budget where date=\""
				+ date + "\" group by category";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					Finance fobj = new Finance();
					fobj.setExpense(cursor.getInt(0));
					fobj.setCategory(cursor.getString(1));
					System.out.println("ddd " + cursor.getInt(0) + " "
							+ cursor.getString(1));
					recordList.add(fobj);

				} while (cursor.moveToNext());
			}
		}
		db.close();
		return recordList;
	}

	/**
	 * Get sum of all expenses except missing values
	 * 
	 * @param month
	 * @return sum in Finance object
	 */
	public List<Finance> getSumExpense(String month) {
		System.out.println("ddddtttt in db=" + month);
		List<Finance> recordList = new ArrayList<Finance>();
		String selectQuery = "SELECT sum(expense) FROM budget where date like '%"
				+ month + "%' and category not like '%other%'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					Finance fobj = new Finance();
					fobj.setExpense(cursor.getInt(0));
					recordList.add(fobj);

				} while (cursor.moveToNext());
			}
		}
		db.close();
		return recordList;
	}

	/**
	 * Get sum of all missing values
	 * 
	 * @param month
	 * @return sum in Finance Object
	 */
	public List<Finance> getSumMissing(String month) {
		System.out.println("ddddtttt in db=" + month);
		List<Finance> recordList = new ArrayList<Finance>();
		String selectQuery = "SELECT sum(expense) FROM budget where date like '%"
				+ month + "%' and category=\"Other\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					Finance fobj = new Finance();

					System.out.println(" in sql missing sum="
							+ cursor.getInt(0));
					fobj.setExpense(cursor.getInt(0));
					recordList.add(fobj);

				} while (cursor.moveToNext());
			}
		}
		db.close();
		return recordList;
	}

	/**
	 * Get current balance
	 * 
	 * @return balance
	 */
	public int getBal() {

		String selectQuery = "SELECT sum(income),sum(expense) FROM budget";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {

			if (cursor.moveToNext()) {
				// bal=income-expense
				return (cursor.getInt(0) - cursor.getInt(1));

			}
		}

		db.close();
		return 0;
	}

	/**
	 * Get details of a particular transaction
	 * 
	 * @param date
	 * @param cate
	 * @return List of transactions
	 */
	public List<Finance> getDetailTrans(String date, String cate) {
		List<Finance> recordList = new ArrayList<Finance>();

		// Select All Query
		String selectQuery = "SELECT expense,description FROM budget where date=\""
				+ date + "\" and category=\"" + cate + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Finance fobj = new Finance();
					fobj.setExpense(cursor.getInt(0));
					fobj.setDescription(cursor.getString(1));
					// Adding to list
					recordList.add(fobj);

				} while (cursor.moveToNext());
			}
		}
		db.close();

		return recordList;
	}

	/**
	 * Delete transactions of particular date
	 * 
	 * @param date
	 */
	public void delAllTrans(String date) {

		// Select All Query
		String selectQuery = "select * FROM budget where date=\"" + date + "\"";

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					// get all and match ID
					Finance fobj = new Finance();
					fobj.setId(Integer.parseInt(cursor.getString(0)));
					db.delete(TABLE_FINANCE, KEY_ID + " = ?",
							new String[] { String.valueOf(fobj.getId()) });
					db.rawQuery(selectQuery, null);

				} while (cursor.moveToNext());
			}
		}

	}

	/**
	 * Delete a particular transaction
	 * 
	 * @param category
	 * @param amount
	 * @param desc
	 * @param date
	 */
	public void delete(String category, int amount, String desc, String date) {

		// Select All Query
		String selectQuery = "select * FROM budget where date=\"" + date
				+ "\" and category=\"" + category + "\" and expense=" + amount
				+ " and description=\"" + desc + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		System.out.println("Deleting");
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {

				// get and match ID
				Finance fobj = new Finance();
				fobj.setId(Integer.parseInt(cursor.getString(0)));
				db.delete(TABLE_FINANCE, KEY_ID + " = ?",
						new String[] { String.valueOf(fobj.getId()) });
				db.rawQuery(selectQuery, null);

			}
		}

	}

	/**
	 * Empty database
	 */
	public void delAll() {

		// Select All Query
		String selectQuery = "select * FROM budget";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and matching ID
			if (cursor.moveToFirst()) {
				do {
					Finance fobj = new Finance();
					fobj.setId(Integer.parseInt(cursor.getString(0)));
					db.delete(TABLE_FINANCE, KEY_ID + " = ?",
							new String[] { String.valueOf(fobj.getId()) });
					db.rawQuery(selectQuery, null);

				} while (cursor.moveToNext());
			}
		}
	}

}
