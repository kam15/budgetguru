package com.example.budgetguru;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper{

	public static final String DB = "test.sqlite";
	public static final String DB_TABLE = "Reminders";
	public static final String DB_ATTR1 = "Description";
	public static final String DB_ATTR2="Category";
	public static final String DB_ATTR3="Type";
	public static final String DB_ATTR4="startdate";
	public static final String DB_ATTR5="starttime";
	public static final String DB_ATTR6="enddate";
	public static final String DB_ATTR7="endtime";
	public static final String CREATE_TABLE = "create table "+DB_TABLE+"("+DB_ATTR1+" text,"+DB_ATTR2+" text,"+DB_ATTR3+" text,"+DB_ATTR4+" date default CURRENT_DATE,"+DB_ATTR5+" time default CURRENT_TIMESTAMP,"+DB_ATTR6+" date,"+DB_ATTR7+ " time)"; 
	

	public static final String DB_TABLE2 = "Categories";
	public static final String DB_ATTR = "Category";
	public static final String CREATE_TABLE2 = "create table "+DB_TABLE2+"("+DB_ATTR+" text)";
//	public static final String insert1="insert into Categories values('Money')";
//	public static final String insert2="insert into Categories values('Books')";
//	public static final String insert3="insert into Categories values('Stationery')";
	
	
	public SQLiteHelper(Context context)
	{	
		super(context, DB,null , 1); 
	}
	
	public void close(){
		  this.close();
		 }
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE);
		db.execSQL(CREATE_TABLE2);
		db.beginTransaction();
//		db.rawQuery(insert1, null);
//		db.rawQuery(insert2, null);
//		db.rawQuery(insert3, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
}

