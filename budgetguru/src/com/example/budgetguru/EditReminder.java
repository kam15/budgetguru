package com.example.budgetguru;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditReminder extends Activity {

	EditText descEditText;
	Spinner category_spinner, type_spinner;
	DatePicker date_picker;
	TimePicker time_picker;
	Button submit_btn, reset_btn;
	String categories[]={"Money","Books","Stationery"};
	String types[] = { "I took", "I gave" },olddesc;
	ArrayAdapter<String> spinadapt, typeadapt;

	public void addreminder(String s)
	{

		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR,date_picker.getYear());
		date.set(Calendar.MONTH,date_picker.getMonth());
		date.set(Calendar.DAY_OF_MONTH, date_picker.getDayOfMonth());
		date.set(Calendar.HOUR_OF_DAY,time_picker.getCurrentHour());
		date.set(Calendar.MINUTE,time_picker.getCurrentMinute());
		//System.out.println("Date-"+date);
		
		Intent intent = new Intent(this, AlarmReceiverActivity.class);
		intent.putExtra("title", s);
		
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
            12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = 
            (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),
                pendingIntent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_reminder);
		
		descEditText = (EditText) findViewById(R.id.editdesc_et);
		category_spinner = (Spinner) findViewById(R.id.editcategory_spinner);
		type_spinner = (Spinner) findViewById(R.id.edittype_spinner);
		date_picker = (DatePicker) findViewById(R.id.editdate_picker);
		time_picker = (TimePicker) findViewById(R.id.edittime_picker);
		submit_btn = (Button) findViewById(R.id.editsubmit_button);
		reset_btn = (Button) findViewById(R.id.editreset_button);

		spinadapt = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item);
		
		typeadapt = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item,types);
		
		spinadapt.add("Money");
		spinadapt.add("Books");
		spinadapt.add("Stationery");
		
		SQLiteDatabase database = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();
		Cursor c = database.rawQuery("select Category from Categories", null);
		
		if(c.moveToFirst())
		{while (!c.isAfterLast()) {
			spinadapt.add(c.getString(0));
			c.moveToNext();
		}}
		
		category_spinner.setAdapter(spinadapt);
		c.close();
		
		Cursor c1=database.rawQuery("select * from Reminders", null);
		c1.moveToFirst();
		while(!c1.isAfterLast())
		{
			descEditText.setText(c1.getString(0));
			olddesc=c1.getString(0);
			category_spinner.setSelection(spinadapt.getPosition(c1
					.getString(1)));
			type_spinner.setSelection(typeadapt.getPosition(c1.getString(2)));
			date_picker.init(date_picker.getYear(),date_picker.getMonth(),date_picker.getDayOfMonth(),null);
			time_picker.getCurrentHour();
			time_picker.getCurrentMinute();
			c1.moveToNext();
		}
		
		c1.close();
		database.close();
		
		
		submit_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String desc = null, category = null, type = null, alarmdate, alarmtime = null;

				if (descEditText.getText().toString().trim().equals("")) {
					descEditText.setError("Description is required!");
					Toast t = Toast.makeText(EditReminder.this,
							"Please enter the description", Toast.LENGTH_LONG);
					t.show();
				} else

					desc = descEditText.getText().toString();

				if (category_spinner.getSelectedItem().toString().trim()
						.equals("")) {
					Toast t = Toast.makeText(EditReminder.this,
							"Please select the category", Toast.LENGTH_LONG);
					t.show();
				} else

					category = category_spinner.getSelectedItem().toString();

				if (type_spinner.getSelectedItem().toString().trim().equals("")) {
					Toast t = Toast.makeText(EditReminder.this,
							"Please select the type of reminder", Toast.LENGTH_LONG);
					t.show();
				} else

					type = type_spinner.getSelectedItem().toString();
				alarmdate = String.valueOf(date_picker.getYear()) + "-"
						+ String.valueOf(date_picker.getMonth()) + "-"
						+ String.valueOf(date_picker.getDayOfMonth());

				if (alarmdate.trim().equals("")) {
					Toast t = Toast.makeText(EditReminder.this,
							"Please select the date for the reminder", Toast.LENGTH_LONG);
					t.show();
				} else
					alarmtime = String.valueOf(time_picker.getCurrentHour())
							+ ":"
							+ String.valueOf(time_picker.getCurrentMinute())
							+ ":00";

				if (alarmtime.trim().equals("")) {
					Toast t = Toast.makeText(EditReminder.this,
							"Please select the time for the reminder", Toast.LENGTH_LONG);
					t.show();
				}

				ContentValues cv = new ContentValues();
				cv.put("Description", desc);
				cv.put("Category", category);
				cv.put("Type", type);
				cv.put("enddate", alarmdate);
				cv.put("endtime", alarmtime);
				

				addreminder(desc);
				
				SQLiteDatabase database = new SQLiteHelper(getApplicationContext()).getWritableDatabase();
				String table_name = "Reminders";
				String where = "Description = '"+olddesc+"'";
				
				database.delete(table_name, where, null);
				
				
				database.insert("Reminders",null, cv);
				//System.out.println("insert : "+a);
				database.close();
				//System.out.println("After db.close");
				finish();
				Intent show_list=new Intent(getApplicationContext(),ForgetMeNot.class);
				startActivity(show_list);
				

			}
		});
		
		reset_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				if (descEditText != null) descEditText.setText("");
				Calendar today = Calendar.getInstance();
				date_picker.init (today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE),null);
				time_picker.setCurrentHour(time_picker.getCurrentHour());
				time_picker.setCurrentMinute(time_picker.getCurrentMinute());
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_reminder, menu);
		return true;
	}

}
