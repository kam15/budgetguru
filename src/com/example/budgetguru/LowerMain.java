package com.example.budgetguru;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LowerMain extends Activity implements OnDateSetListener {
	Spinner spinner, spinner2;
	ArrayAdapter<String> adapter, adapter2;
	int flag, year, month, day;
	String new_date;
	private String old_cat, old_amt, old_des, tday_dt;
	private int intentflag;
	String changedDate;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lower_main);
		final Context mContext = this;
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		int amt = db.getBal();
		db.close();

		if (amt <= 0) {
			Intent i = new Intent(this, LowerInitialAmount.class);
			startActivity(i);
		}

		String category[] = { "Food", "Travel", "Entertainment", "Clothing",
				"Bills", "Medical", "Other" };
		String page[] = { "Home", "Initial amount", "Todays Expense",
				"Specified date expense", "Reports", "Search", "Lent/Borrowed",
				"About" };
		spinner = (Spinner) findViewById(R.id.l_spinner);
		adapter = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_spinner_item, category);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner2 = (Spinner) findViewById(R.id.l_spinner2);
		adapter2 = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_spinner_item, page);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				String s = ((TextView) view).getText().toString();
				if (s.equals("Initial amount"))
					startActivity(new Intent(view.getContext(),
							LowerInitialAmount.class));
				if (s.equals("Todays Expense")) {
					Intent i = new Intent(view.getContext(),
							LowerTodaysExpenses.class);
					Calendar d = new GregorianCalendar();

					String new_date = d.get(Calendar.DAY_OF_MONTH) + "/"
							+ d.get(Calendar.MONTH) + "/"
							+ d.get(Calendar.YEAR);
					i.putExtra("date", new_date);
					startActivity(i);
				}
				if (s.equals("Specified date expense")) {
					LayoutInflater inflater = (LayoutInflater) view
							.getContext().getSystemService(
									Context.LAYOUT_INFLATER_SERVICE);
					View npView = inflater.inflate(
							R.layout.lower_dialog_specificdate, null);
					Builder alert = new AlertDialog.Builder(view.getContext());
					alert.setTitle("Select Specific Date");
					final Calendar c = Calendar.getInstance();
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);
					DatePickerDialog datePickerDialog = new DatePickerDialog(
							mContext, LowerMain.this, year, month, day);

					datePickerDialog.show();

				}
				if (s.equals("Reports"))
					startActivity(new Intent(view.getContext(),
							PieChart_lower.class));
				if (s.equals("Search"))
					startActivity(new Intent(view.getContext(),
							AutoCompActivity.class));
				if (s.equals("Lent/Borrowed"))
					startActivity(new Intent(view.getContext(),
							ForgetMeNot.class));
				if (s.equals("About"))
					startActivity(new Intent(view.getContext(), About.class));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		Intent i = getIntent();
		intentflag = i.getIntExtra("intentflag", 0);
		if (intentflag == 1) {
			old_cat = i.getStringExtra("cat");
			old_amt = i.getStringExtra("amt");
			old_des = i.getStringExtra("des");
			tday_dt = i.getStringExtra("date");
			EditText ed1 = (EditText) findViewById(R.id.l_editText1);
			ed1.setText("" + old_des);
			EditText ed2 = (EditText) findViewById(R.id.l_editText2);
			ed2.setText("" + old_amt);

			spinner.setSelection(adapter.getPosition(old_cat));
			System.out.println("In intent flag = 1");
			System.out.println("cat = " + old_cat);
			System.out.println("amt = " + old_amt);
			System.out.println("description = " + old_des);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		this.year = year;
		this.month = monthOfYear;
		this.day = dayOfMonth;
		updateDisplay();
	}

	private void updateDisplay() { /* * Hide virtual keyboard */

		flag = 0;
		if (year > 99) {
			changedDate = day + "/" + month + "/20" + (year % 100);
		} else {
			changedDate = day + "/" + month + "/19" + (year % 100);
		}

		System.out.println("----------------------" + changedDate);
		startActivity(new Intent(getApplicationContext(),
				LowerTodaysExpenses.class).putExtra("date", changedDate));

	}

	public void Valid(View view) {

		boolean error_flag = false;
		EditText et1, et2;
		et1 = (EditText) findViewById(R.id.l_editText1);
		String description = et1.getText().toString();
		if (description.matches("")) {
			et1.setError("Description not entered");
			error_flag = true;
		}
		et2 = (EditText) findViewById(R.id.l_editText2);
		String amount = et2.getText().toString();
		if (amount.matches("")) {
			et2.setError("Amount not entered");
			error_flag = true;
		}
		if (error_flag == false) {
			String category = (String) spinner.getSelectedItem();
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			DatePicker dp = new DatePicker(getApplicationContext());
			String date = dp.getDayOfMonth() + "/" + dp.getMonth() + "/"
					+ dp.getYear();
			int new_bal = db.getBal() - Integer.parseInt(amount);
			if (new_bal >= 0) {

				if (intentflag == 1) {
					db.addTrans(new Finance(tday_dt, Integer.parseInt(amount),
							category, description, 0));

					db.delete(old_cat, Integer.parseInt(old_amt), old_des,
							tday_dt);
					Toast.makeText(getApplicationContext(),
							"Saved Your Transaction!!", Toast.LENGTH_LONG)
							.show();
					Intent intent = getIntent();
					finish();
					startActivity(intent);
				}

				else if ((db.addTrans(new Finance(date, Integer
						.parseInt(amount), category, description, 0)) != -1)) {

					Toast.makeText(getApplicationContext(),
							"Saved Your Transaction!!", Toast.LENGTH_LONG)
							.show();

					Intent intent = getIntent();
					finish();
					startActivity(intent);
				} else
					Toast.makeText(getApplicationContext(),
							"Error in Transaction!!", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"NO SUFFICIENT BALANCE", Toast.LENGTH_LONG).show();
				Intent i = new Intent(this, LowerInitialAmount.class);
				startActivity(i);

			}
		}

	}

}
