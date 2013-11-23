package com.example.budgetguru;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends Activity {

	EditText newcat_et;
	Button addbutton,resetbutton;
	String newcategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_category);
		
		newcat_et=(EditText)findViewById(R.id.addcategory_et);
		addbutton=(Button)findViewById(R.id.addcat_button);
		resetbutton=(Button)findViewById(R.id.resetcat_button);
		
		addbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (newcat_et.getText().toString().trim().equals("")) {
					newcat_et.setError("Category name is required!");
					Toast t = Toast.makeText(AddCategory.this,
							"Please enter the name of the new category", Toast.LENGTH_LONG);
					t.show();
				} else

					newcategory = newcat_et.getText().toString();
				
				SQLiteDatabase database = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();
				
				ContentValues cv=new ContentValues();
				cv.put("Category", newcategory);
				
				database.insert("Categories", null, cv);
				database.close();
				
				finish();
				Intent i=new Intent(getApplicationContext(),EnterReminder.class);
				startActivity(i);
				
			}
		});
		
		resetbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				if (newcat_et != null) newcat_et.setText("");
				
			}
			
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_category, menu);
		return true;
	}

}
