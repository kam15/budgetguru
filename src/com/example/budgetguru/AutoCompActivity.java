package com.example.budgetguru;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AutoCompActivity extends Activity {
	AutoCompleteTextView auto;
	ArrayAdapter<String> autoadapt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autocomp);
		auto = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		DatabaseHandler database = new DatabaseHandler(this);
		List<Finance> list = database.getAllRecords();
		autoadapt = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		final Context mContext = this;
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Finance f = list.get(i);

				autoadapt.add("Date : " + f.getDate() + "\nDescription : "
						+ f.getDescription() + "\nAmount : " + f.getExpense()
						+ "\nCategory : " + f.getCategory());

			}

		} else
			System.out.println("empty list");

		auto.setAdapter(autoadapt);
		// auto.setTextColor(1);

		auto.setOnItemClickListener(new OnItemClickListener() {
			//AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// String display=auto.getText().toString();
				// System.out.println("dISPLAY"+display);
				// System.out.println("onclick");
				// LayoutInflater inflater = (LayoutInflater) mContext
				// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// View npView = inflater.inflate(R.layout.dialog_specificmonth,
				// null);
				// alert.setTitle("Details");
				// alert.setMessage(display);
				//
				//
				//
				//
				// alert.setPositiveButton("OK",
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog,
				// int whichButton) {
				// auto.setText(null);

				TextView title = new TextView(mContext);
				// You Can Customise your Title here
				title.setText("Details");
				// title.setBackgroundColor(Color.DKGRAY);
				title.setPadding(10, 10, 10, 10);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.BLACK);
				title.setTextSize(20);

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setCustomTitle(title);
				// builder.setIcon(R.drawable.alert_36);

				String display = auto.getText().toString();
				builder.setMessage(display);

				builder.setCancelable(false);
				builder.setNegativeButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								auto.setText(null);
							}

						});

				AlertDialog alert = builder.show();
				TextView messageText = (TextView) alert
						.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				messageText.setTextColor(Color.BLACK);

			}

		});

		// alert.setView(npView).show();
	}// onclick

	// TODO Auto-generated method stub

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.auto_comp, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
