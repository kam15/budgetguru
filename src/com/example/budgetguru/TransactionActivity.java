package com.example.budgetguru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TransactionActivity extends Activity {
	private String TAG_CAT = "category";
	private String TAG_AMT = "amount";
	private String TAG_DES = "descripiton";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todays_expenses);
		Intent page = getIntent();
		String date = page.getStringExtra("date");
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		List<Finance> list = db.getRecords(date);
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		for (Finance f : list) {
			String log = "Date: " + f.getDate() + " ,Amount: " + f.getExpense()
					+ " ,Category: " + f.getCategory() + " ,Description: "
					+ f.getDescription();

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(TAG_CAT, "" + f.getCategory());
			map.put(TAG_AMT, "" + f.getExpense());
			map.put(TAG_DES, "" + f.getDescription());
			// adding HashList to ArrayList
			contactList.add(map);

		}// for

		ListAdapter adapter = new SimpleAdapter(this, contactList,
				R.layout.detail_list,
				new String[] { TAG_CAT, TAG_AMT, TAG_DES }, new int[] {
						R.id.label2, R.id.label4, R.id.label3 });

		ListView detail_list = (ListView) findViewById(R.id.listView1);
		detail_list.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transaction, menu);
		return true;
	}

}
