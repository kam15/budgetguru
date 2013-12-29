package com.example.budgetguru;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RingerHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "mcproject";

	// Contacts table name
	private static final String TABLE_RINGER = "ringer";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_FORMAT = "format";

	public RingerHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RINGER_TABLE = "CREATE TABLE " + TABLE_RINGER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PASSWORD + " TEXT,"
				+ KEY_FORMAT + " TEXT" + ")";
		ContentValues values = new ContentValues();
		values.put(KEY_PASSWORD, "1111");
		values.put(KEY_FORMAT, "Change the phone profile");
		db.execSQL(CREATE_RINGER_TABLE);
		db.insert(TABLE_RINGER, null, values);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RINGER);
		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	long addTrans(Ringer ringer) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PASSWORD, ringer.getPassword());
		values.put(KEY_FORMAT, ringer.getFormat());
		// Updating Row

		long return_val = db.update(TABLE_RINGER, values, null, null);
		db.close(); // Closing database connection
		return return_val;
	}

	// Getting All Contacts
	public List<Ringer> getAllRecords() {
		List<Ringer> transList = new ArrayList<Ringer>();

		// Select All Query
		String selectQuery = "SELECT password,format FROM ringer";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			Log.i("LOg is null", "query null");
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				Log.i("OK", "Yes");
				do {
					Ringer f = new Ringer();

					f.setPassword(cursor.getString(0));
					f.setFormat(cursor.getString(1));
					// Adding contact to list
					transList.add(f);

				} while (cursor.moveToNext());
			}
		}
		db.close();

		return transList;
	}

	// Getting All Contacts
	public List<Ringer> getRecords() {
		List<Ringer> recordList = new ArrayList<Ringer>();

		// Select All Query
		String selectQuery = "SELECT password,format FROM ringer";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {

				do {
					Ringer f = new Ringer();

					f.setPassword(cursor.getString(0));
					f.setFormat(cursor.getString(1));
					// Adding contact to list
					recordList.add(f);

				} while (cursor.moveToNext());
			}
		}
		db.close();

		return recordList;
	}

	// Getting All Contacts
	public String getPassword() {
		List<Ringer> recordList = new ArrayList<Ringer>();
		String selectQuery = "SELECT password FROM ringer";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {

			if (cursor.moveToLast()) {

				return cursor.getString(0);

			}
		}
		db.close();

		return "";
	}

	// Getting All Contacts
	public String getFormat() {
		List<Ringer> recordList = new ArrayList<Ringer>();
		String selectQuery = "SELECT format FROM ringer";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {

			if (cursor.moveToLast()) {

				return cursor.getString(1);

			}
		}
		db.close();

		return "";
	}

	public void delAllTrans(String password) {

		// Select All Query
		String selectQuery = "select * FROM ringer where password=\""
				+ password + "\"";

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Ringer f = new Ringer();
					f.setId(Integer.parseInt(cursor.getString(0)));
					db.delete(TABLE_RINGER, KEY_ID + " = ?",
							new String[] { String.valueOf(f.getId()) }); // Cursor
																			// cursor
																			// =
					db.rawQuery(selectQuery, null);

				} while (cursor.moveToNext());
			}
		}

	}

	public void delAll() {

		// Select All Query
		String selectQuery = "select * FROM ringer";

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Ringer f = new Ringer();
					f.setId(Integer.parseInt(cursor.getString(0)));
					db.delete(TABLE_RINGER, KEY_ID + " = ?",
							new String[] { String.valueOf(f.getId()) }); // Cursor
																			// cursor
																			// =
					db.rawQuery(selectQuery, null);

				} while (cursor.moveToNext());
			}
		}
	}

}
