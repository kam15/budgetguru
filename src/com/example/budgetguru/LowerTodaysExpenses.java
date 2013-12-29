package com.example.budgetguru;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LowerTodaysExpenses extends Activity {

	public static final String DATE = "";
	private String string;
	private String date;
	private String TAG_CAT = "category";
	private String TAG_AMT = "amount";
	private String TAG_DES = "descripiton";
	private int selectpos = -1;
	private View selectview;
	private String cat, amt, des;
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todays_expenses);
		context = this;

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		DatePicker dp = new DatePicker(getApplicationContext());

		Intent intent = getIntent();
		date = intent.getStringExtra("date");
		List<Finance> list = db.getRecords(date);
		final ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

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

		ListAdapter adapter = new SimpleAdapter(getApplicationContext(),
				contactList, R.layout.detail_list, new String[] { TAG_DES,
						TAG_AMT, TAG_CAT }, new int[] { R.id.label2,
						R.id.label4, R.id.label3 });

		ListView detail_list = (ListView) findViewById(R.id.listView1);
		detail_list.setAdapter(adapter);
		detail_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		detail_list.setDrawSelectorOnTop(true);
		detail_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				System.out.println("inside onclick");

				if (selectpos == -1) {
					// no item is selected, mark as selected
					selectview = v;
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					selectpos = position;
				} else if (selectpos == position) {
					// the same item is selected as the previous one, deselect
					selectview = null;
					v.setBackgroundResource(R.drawable.list_selector);
					selectpos = -1;
				} else {
					// another item is selected, deselect the previous and
					// select the new one
					selectview.setBackgroundResource(R.drawable.list_selector);
					selectview = v;
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					selectpos = position;
				}
				HashMap<String, String> map = contactList.get(position);
				cat = map.get(TAG_CAT);
				amt = map.get(TAG_AMT);
				des = map.get(TAG_DES);
				System.out.println("cat=" + cat + "amt=" + amt + "des=" + des);
			}
		});

		Button open = (Button) findViewById(R.id.add);
		open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(v.getContext(), LowerMain.class);
				startActivity(i);
			}
		});

		Button edit = (Button) findViewById(R.id.edit);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cat != null) {
					Intent i = new Intent(v.getContext(), LowerMain.class);
					i.putExtra("intentflag", 1);
					i.putExtra("cat", cat);
					i.putExtra("amt", amt);
					i.putExtra("des", des);
					i.putExtra("date", date);
					startActivity(i);

				}
			}
		});

		Button delete = (Button) findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cat != null) {

					String title = "Select Option";
					String message = "Delete transaction?";
					String button1String = "YES";
					String button2String = "NO";
					AlertDialog.Builder ad = new AlertDialog.Builder(context);
					ad.setTitle(title);
					ad.setMessage(message);

					ad.setPositiveButton(button1String,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									DatabaseHandler db = new DatabaseHandler(
											getApplicationContext());
									db.delete(cat, Integer.parseInt(amt), des,
											date);
									List<Finance> list = db.getRecords(date);
									System.out.println(list.size() + " "
											+ list.size());
									for (Finance f : list) {
										String log = "Date: " + f.getDate()
												+ " ,Amount: " + f.getExpense()
												+ " ,Category: "
												+ f.getCategory()
												+ " ,Description: "
												+ f.getDescription();
									}// for

									Intent i = new Intent(
											getApplicationContext(),
											LowerTodaysExpenses.class);
									i.putExtra("date", date);
									startActivity(i);
									finish();

								}
							});

					ad.setNegativeButton(button2String,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// finish();
								}
							});
					ad.setCancelable(true);
					ad.setOnCancelListener(new OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
						}
					});
					ad.show();
				}
			}
		});

	}

}