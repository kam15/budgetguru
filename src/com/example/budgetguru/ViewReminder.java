package com.example.budgetguru;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewReminder extends Activity {

	String desc, type;
	TextView desctv, category, date1, time1, date2, time2;
	Button delbtn, editbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		desc = i.getStringExtra("title");

		SQLiteDatabase database = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();
		Cursor c = database.rawQuery(
				"select type from Reminders where Description='" + desc + "'",
				null);

		if (c.moveToFirst())
			type = c.getString(0);

		if (type.equals("I took"))
			setContentView(R.layout.viewborrowed);
		else
			setContentView(R.layout.view_lent);

		c.close();
		database.close();

		SQLiteDatabase db = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();

		desctv = (TextView) findViewById(R.id.viewdesc);
		category = (TextView) findViewById(R.id.viewcategory);
		date1 = (TextView) findViewById(R.id.viewdate);
		time1 = (TextView) findViewById(R.id.viewtime);
		date2 = (TextView) findViewById(R.id.viewdate2);
		time2 = (TextView) findViewById(R.id.viewtime2);
		delbtn = (Button) findViewById(R.id.delete_button);
		editbtn = (Button) findViewById(R.id.edit_button);

		Cursor c1 = db.rawQuery("select * from Reminders where Description='"
				+ desc + "'", null);
		c1.moveToFirst();

		if (c1 != null) {
			desctv.setText(c1.getString(0));
			category.setText(c1.getString(1));
			date1.setText(c1.getString(3));
			time1.setText(c1.getString(4));
			date2.setText(c1.getString(5));
			time2.setText(c1.getString(6));

			c1.moveToNext();
		}

		c1.close();
		db.close();

		delbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// System.out.println("Title="+title);
				SQLiteDatabase database = new SQLiteHelper(
						getApplicationContext()).getWritableDatabase();
				String table_name = "Reminders";
				String where = "Description = '" + desc + "'";

				database.delete(table_name, where, null);

				database.close();
				finish();
				Intent disp_list = new Intent(getApplicationContext(),
						ForgetMeNot.class);
				startActivity(disp_list);
			}
		});

		editbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				finish();
				Intent edit_reminder = new Intent(getApplicationContext(),
						EditReminder.class);
				edit_reminder.putExtra("title", desc);
				startActivity(edit_reminder);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_reminder, menu);
		return true;
	}

}
