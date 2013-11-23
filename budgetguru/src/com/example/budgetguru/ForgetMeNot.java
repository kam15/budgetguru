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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ForgetMeNot extends Activity {

	ListView lentlv;
	ArrayAdapter<String> adapter;
	Button addreminder, categories, viewborrowed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_lent_reminders_list);

		lentlv = (ListView) findViewById(R.id.lent_listview);
		addreminder=(Button) findViewById(R.id.addreminder_button);
		categories=(Button)findViewById(R.id.viewcategories_button);
		viewborrowed=(Button)findViewById(R.id.viewborrowedbutton);
		adapter=new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1);
		
		SQLiteDatabase database = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();
		Cursor c = database.rawQuery("select Description from Reminders  where type='I gave' group by enddate", null);
		
		if(c.moveToFirst())
		{while (!c.isAfterLast()) {
			adapter.add(c.getString(0));
			c.moveToNext();
		}
		}
		else
		{	adapter.add("You haven't lent anything to anyone yet.");
			lentlv.setEnabled(false);
		}
		
		lentlv.setAdapter(adapter);
		
		c.close();
		database.close();
		
		lentlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String title = (String) parent.getItemAtPosition(position);
				// Launching new Activity on selecting single List Item
				finish();
				Intent i = new Intent(getApplicationContext(),
						ViewReminder.class);
				System.out.println("title is="+title);
				i.putExtra("title", title);
				startActivity(i);
				
			}
			
			
		});
		
		viewborrowed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(getApplicationContext(),DisplayBorrowedReminders.class);
				finish();
				startActivity(i);
			}
		});
		
		addreminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent(getApplicationContext(),EnterReminder.class);
				finish();
				startActivity(i);
			}
		});
		
		categories.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent(getApplicationContext(),ChooseCategory.class);
				finish();
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_reminders_list, menu);
		return true;
	}

}
