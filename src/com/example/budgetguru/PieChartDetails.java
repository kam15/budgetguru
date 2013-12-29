package com.example.budgetguru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class PieChartDetails extends Activity {
	public static final String ARG_OS = "OS";
	public static final String DATE = "";
	private String string;

	private String TAG_CAT = "category";
	private String TAG_AMT = "amount";
	private String TAG_DES = "descripiton";
	Spinner spinner1;
	String category, date;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.todays_expenses1);

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		Intent intent = getIntent();
		category = intent.getStringExtra("selectedCategory");
		date = intent.getStringExtra("selectedDate");

		DatabaseHandler database = new DatabaseHandler(getApplicationContext());

		ArrayList<String> categ = database.getAllCategories();
		categ.remove(0);
		ArrayAdapter<String> adaptr = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item, categ);

		spinner1.setAdapter(adaptr);
		spinner1.setSelection(categ.indexOf(category));

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String selectedItem = spinner1.getSelectedItem().toString();

				category = selectedItem;
				getDetails();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				category = " ";
				getDetails();

			}
		});

		System.out.println("date and category" + date + "" + category);
		getDetails();

	}

	void getDetails() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		DatePicker dp = new DatePicker(getApplicationContext());

		List<Finance> list = db.getDetailTrans(date, category);
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		for (Finance f : list) {
			String log = " Amount: " + f.getExpense() + " ,Description: "
					+ f.getDescription();
			// Writing Contacts to log
			System.out.println(log);

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(TAG_CAT, "" + "");
			map.put(TAG_AMT, "" + f.getExpense());
			map.put(TAG_DES, "" + f.getDescription());
			// adding HashList to ArrayList
			contactList.add(map);

		}// for

		ListAdapter adapter = new SimpleAdapter(getApplicationContext(),
				contactList, R.layout.detail_list, new String[] { TAG_DES,
						TAG_CAT, TAG_AMT }, new int[] { R.id.label2,
						R.id.label4, R.id.label3 });

		ListView detail_list = (ListView) findViewById(R.id.listView1);

		detail_list.setAdapter(adapter);

	}
}