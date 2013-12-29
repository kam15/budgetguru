package com.example.budgetguru;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewByCategories extends Activity {

	ArrayAdapter<String> catadapter;
	ListView categorylv;
	String selecteditem;
	Button backbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_by_categories);
		Intent i = getIntent();
		backbtn = (Button) findViewById(R.id.done_button);
		selecteditem = i.getStringExtra("title");

		catadapter = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1);

		categorylv = (ListView) findViewById(R.id.category_listview);

		final SQLiteDatabase database = new SQLiteHelper(
				getApplicationContext()).getReadableDatabase();

		Cursor c1 = database.rawQuery(
				"select Description from Reminders where Category='"
						+ selecteditem + "'", null);

		if (c1.moveToFirst()) {
			while (!c1.isAfterLast()) {
				catadapter.add(c1.getString(0));

				c1.moveToNext();
			}
		} else {
			catadapter
					.add("You haven't added any entries for this category yet");
			categorylv.setEnabled(false);
		}

		c1.close();

		categorylv.setAdapter(catadapter);

		database.close();
		
		categorylv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String title = (String) parent.getItemAtPosition(position);
				// Launching new Activity on selecting single List Item
				finish();
				Intent i = new Intent(getApplicationContext(),
						ViewReminder.class);
				System.out.println("title is=" + title);
				i.putExtra("title", title);
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
		getMenuInflater().inflate(R.menu.view_by_categories, menu);
		return true;
	}

}
