package com.example.budgetguru;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ChooseCategory extends Activity {

	Spinner selectcategory_spinner;
	ArrayAdapter<String> spinadapt;
	String selecteditem;
	Button submitbtn, backbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_category);
		submitbtn = (Button) findViewById(R.id.catsubmit_button);
		backbtn = (Button) findViewById(R.id.back_button);
		selectcategory_spinner = (Spinner) findViewById(R.id.selectcategory_spinner);
		spinadapt = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item);

		spinadapt.add("Money");
		spinadapt.add("Books");
		spinadapt.add("Stationery");

		SQLiteDatabase database = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();
		Cursor c = database.rawQuery("select Category from Categories", null);

		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {
				spinadapt.add(c.getString(0));
				c.moveToNext();
			}
		}

		selectcategory_spinner.setAdapter(spinadapt);
		c.close();
		database.close();

		submitbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String val = selectcategory_spinner.getSelectedItem()
						.toString();
				Intent i = new Intent(getApplicationContext(),
						ViewByCategories.class);
				i.putExtra("title", val);
				finish();
				startActivity(i);

			}
		});

		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Intent i = new Intent(getApplicationContext(),
						ForgetMeNot.class);
				startActivity(i);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_category, menu);
		return true;
	}

}
