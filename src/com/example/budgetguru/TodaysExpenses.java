package com.example.budgetguru;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@SuppressLint("newapi")
public class TodaysExpenses extends Fragment {
	public static final String ARG_OS = "OS";
	public static final String DATE = "";
	private String string;
	private String date;
	private String TAG_CAT = "category";
	private String TAG_AMT = "amount";
	private String TAG_DES = "descripiton";
	private int selectpos = -1;
	private View selectview;
	private String cat, amt, des;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.todays_expenses, null);

		DatabaseHandler db = new DatabaseHandler(view.getContext());
		DatePicker dp = new DatePicker(view.getContext());

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

		ListAdapter adapter = new SimpleAdapter(view.getContext(), contactList,
				R.layout.detail_list,
				new String[] { TAG_DES, TAG_AMT, TAG_CAT }, new int[] {
						R.id.label2, R.id.label4, R.id.label3 });

		ListView detail_list = (ListView) view.findViewById(R.id.listView1);
		detail_list.setAdapter(adapter);
		detail_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		detail_list.setDrawSelectorOnTop(true);
		detail_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

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

			}
		});

		Button open = (Button) view.findViewById(R.id.add);
		open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(v.getContext(), MainActivity.class);
				startActivity(i);
			}
		});

		Button edit = (Button) view.findViewById(R.id.edit);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cat != null) {
					Intent i = new Intent(v.getContext(), MainActivity.class);
					i.putExtra("intentflag", 1);
					i.putExtra("cat", cat);
					i.putExtra("amt", amt);
					i.putExtra("des", des);
					i.putExtra("date", date);
					startActivity(i);
				}
			}
		});

		Button delete = (Button) view.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cat != null) {
					Context context = view.getContext();
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
											view.getContext());
									db.delete(cat, Integer.parseInt(amt), des,
											date);
									List<Finance> list = db.getRecords(date);

									for (Finance f : list) {
										String log = "Date: " + f.getDate()
												+ " ,Amount: " + f.getExpense()
												+ " ,Category: "
												+ f.getCategory()
												+ " ,Description: "
												+ f.getDescription();

									}// for

									Fragment fragment = new TodaysExpenses();
									Bundle args = new Bundle();
									args.putString(TodaysExpenses.ARG_OS, ""
											+ string);
									Calendar d = new GregorianCalendar();

									args.putString(TodaysExpenses.DATE, date);
									fragment.setArguments(args);

									// Insert the fragment by replacing any
									// existing fragment
									FragmentManager fragmentManager = getFragmentManager();
									fragmentManager
											.beginTransaction()
											.replace(R.id.content_frame,
													fragment).commit();

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

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void setArguments(Bundle args) {
		string = args.getString(ARG_OS);
		date = args.getString(DATE);
	}

}