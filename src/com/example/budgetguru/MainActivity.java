package com.example.budgetguru;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.budgetguru.SimpleGestureFilter.SimpleGestureListener;

@SuppressLint("newapi")
public class MainActivity extends Activity implements SimpleGestureListener {

	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence title;
	private String new_date = "";
	Spinner spinner;
	ArrayAdapter<String> adapter;
	int flag;
	private String cat, amount, des, old_cat, old_amt, old_des, tday_dt;
	private int intentflag;
	private int initial_amount, food_amount, travel_amount, entertain_amount,
			clothing_amount, bill_amount, medical_amount, other_amount;

	private SimpleGestureFilter detector;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (Build.VERSION.SDK_INT < 11) {
			Intent i = new Intent(this, LowerMain.class);
			startActivity(i);
			finish();
		} else {
			detector = new SimpleGestureFilter(this, this);

			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			int amt = db.getBal();

			if (amt <= 0) {

				Fragment fragment = new InitialAmount();
				Bundle args = new Bundle();
				args.putString(InitialAmount.ARG_OS, "" + 1);
				if (android.os.Build.VERSION.SDK_INT >= 11) {
					fragment.setArguments(args);
					getActionBar().setTitle(("Initial Amount"));
					// Insert the fragment by replacing any existing fragment
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();

				}
			}

			title = getActionBar().getTitle();
			mPlanetTitles = getResources().getStringArray(R.array.drawer_items);
			System.out.println(mPlanetTitles.length);
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.left_drawer);

			// Set the adapter for the list view
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
					R.layout.drawer_item, R.id.content, mPlanetTitles));
			// Set the list's click listener
			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

			mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.lines, /* nav drawer icon to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description */
			R.string.drawer_close /* "close drawer" description */) {

				/**
				 * Called when a drawer has settled in a completely closed
				 * state.
				 */

				public void onDrawerClosed(View view) {
					// getActionBar().setTitle(title);
				}

				/** Called when a drawer has settled in a completely open state. */

				public void onDrawerOpened(View drawerView) {
					// getActionBar().setTitle("Open Drawer");
				}
			};

			// Set the drawer toggle as the DrawerListener
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			getActionBar().setDisplayHomeAsUpEnabled(true);
			// added suppress lint 4 below line
			// getActionBar().setHomeButtonEnabled(true);
			// should come in child activity
			// ----------------------------------------------------------------------------
			// ----------------------------------------------------------------------------
			// getActionBar().setDisplayHomeAsUpEnabled(true);
			String category[] = { "Food", "Travel", "Entertainment",
					"Clothing", "Bills", "Medical", "Other" };
			spinner = (Spinner) findViewById(R.id.spinner);
			adapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_spinner_item, category);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);

			Intent i = getIntent();
			intentflag = i.getIntExtra("intentflag", 0);
			if (intentflag == 1) {
				old_cat = i.getStringExtra("cat");
				old_amt = i.getStringExtra("amt");
				old_des = i.getStringExtra("des");
				tday_dt = i.getStringExtra("date");
				EditText ed1 = (EditText) findViewById(R.id.editText1);
				ed1.setText("" + old_des);
				EditText ed2 = (EditText) findViewById(R.id.editText2);
				ed2.setText("" + old_amt);
				Spinner sp = (Spinner) findViewById(R.id.spinner);
				sp.setSelection(adapter.getPosition(old_cat));

			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		String str = "";

		switch (direction) {
		case SimpleGestureFilter.SWIPE_LEFT:
			str = "Swipe Left";
			Intent i = new Intent(this, PieChartActivity.class);
			startActivity(i);
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
			break;

		}

	}

	@SuppressLint("newapi")
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				switch (position) {
				case 0:
					Toast.makeText(getApplicationContext(), "Home clicked",
							Toast.LENGTH_LONG).show();
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MainActivity.class);
					startActivity(i);
					finish();
					break;

				case 1:
					Toast.makeText(getApplicationContext(),
							"Initial amount clicked", Toast.LENGTH_LONG).show();
					selectItem(1);
					break;

				case 2:
					Toast.makeText(getApplicationContext(),
							"View Today's Expenses clicked", Toast.LENGTH_LONG)
							.show();
					selectItem(2);
					break;

				case 3:

					flag = 0;
					LayoutInflater inflater = (LayoutInflater) view
							.getContext().getSystemService(
									Context.LAYOUT_INFLATER_SERVICE);
					View npView = inflater.inflate(
							R.layout.dialog_specificdate, null);
					Builder alert = new AlertDialog.Builder(view.getContext());
					alert.setTitle("Select Specific Date");

					final AlertDialog dialog = alert.setView(npView).create();
					dialog.show();

					CalendarView cv = (CalendarView) npView
							.findViewById(R.id.calendarView1);
					Button button_dialog_specificDate = (Button) npView
							.findViewById(R.id.button_dialog_specificDate);
					button_dialog_specificDate
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

									dialog.dismiss();

									if (flag == 0) {
										Calendar d = new GregorianCalendar();

										new_date = d.get(Calendar.DAY_OF_MONTH)
												+ "/" + d.get(Calendar.MONTH)
												+ "/" + d.get(Calendar.YEAR);

									}

									selectItem(3);
								}
							});
					cv.setClickable(true);

					cv.setOnDateChangeListener(new OnDateChangeListener() {

						@Override
						public void onSelectedDayChange(CalendarView view,
								int year, int month, int dayOfMonth) {
							new_date = dayOfMonth + "/" + month + "/" + year;
							flag = 1;

						}
					});

					break;

				case 4:
					Toast.makeText(getApplicationContext(), "Reports clicked",
							Toast.LENGTH_LONG).show();
					Intent i2 = new Intent();
					i2.setClass(getApplicationContext(),
							PieChart_Activity.class);
					startActivity(i2);
					break;
				case 5:
					Toast.makeText(getApplicationContext(), "Search clicked",
							Toast.LENGTH_LONG).show();
					Intent i1 = new Intent();
					i1.setClass(getApplicationContext(), AutoCompActivity.class);
					startActivity(i1);

					break;

				case 6:
					Toast.makeText(getApplicationContext(),
							"Lent/Borrowed clicked", Toast.LENGTH_LONG).show();
					Intent i3 = new Intent();
					i3.setClass(getApplicationContext(), ForgetMeNot.class);
					startActivity(i3);

					break;
				case 7:
					Intent i4 = new Intent();
					i4.setClass(getApplicationContext(), Change_Password.class);
					startActivity(i4);
					break;

				case 8:
					Toast.makeText(getApplicationContext(), "About clicked",
							Toast.LENGTH_LONG).show();
					Intent i5 = new Intent();
					i5.setClass(getApplicationContext(), About.class);
					startActivity(i5);
					break;
				}

			}
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/** Swaps fragments in the main content view */

	private void selectItem(int position) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			String date = "";
			if (position == 1) {
				Fragment fragment = new InitialAmount();
				Bundle args = new Bundle();
				args.putString(InitialAmount.ARG_OS, "" + position);

				fragment.setArguments(args);

				// Insert the fragment by replacing any existing fragment
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();

				// Highlight the selected item, update the title, and close the
				// drawer
				mDrawerList.setItemChecked(position, true);
				getActionBar().setTitle((mPlanetTitles[position]));
				mDrawerLayout.closeDrawer(mDrawerList);
			}

			if (position == 2) {
				Fragment fragment = new TodaysExpenses();
				Bundle args = new Bundle();
				args.putString(TodaysExpenses.ARG_OS, "" + position);
				Calendar d = new GregorianCalendar();
				date = d.get(Calendar.DAY_OF_MONTH) + "/"
						+ d.get(Calendar.MONTH) + "/" + d.get(Calendar.YEAR);
				args.putString(TodaysExpenses.DATE, date);
				if (android.os.Build.VERSION.SDK_INT >= 11) {
					fragment.setArguments(args);

					// Insert the fragment by replacing any existing fragment
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();

					// Highlight the selected item, update the title, and close
					// the drawer
					mDrawerList.setItemChecked(position, true);
					getActionBar().setTitle((mPlanetTitles[position]));
					mDrawerLayout.closeDrawer(mDrawerList);
				}

			} else if (position == 3) {

				Fragment fragment = new TodaysExpenses();
				Bundle args = new Bundle();
				args.putString(TodaysExpenses.ARG_OS, "" + position);
				System.out.println("in 2 date" + new_date);
				args.putString(TodaysExpenses.DATE, new_date);
				if (android.os.Build.VERSION.SDK_INT >= 11) {
					fragment.setArguments(args);

					// Insert the fragment by replacing any existing fragment
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();

					// Highlight the selected item, update the title, and close
					// the drawer
					mDrawerList.setItemChecked(position, true);
					getActionBar().setTitle((mPlanetTitles[position]));
					mDrawerLayout.closeDrawer(mDrawerList);
				}
			}
		}

	}

	// NEW
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// NEW
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_search:
			Toast.makeText(this, "Action search selected", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);

		// return true;
	}

	public void Validate(View view) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			boolean error_flag = false;
			EditText et1, et2;
			et1 = (EditText) findViewById(R.id.editText1);
			String description = et1.getText().toString();
			if (description.matches("")) {
				et1.setError("Description not entered");
				error_flag = true;
			}
			et2 = (EditText) findViewById(R.id.editText2);
			String amount = et2.getText().toString();
			if (amount.matches("")) {
				et2.setError("Amount not entered");
				error_flag = true;
			}
			if (error_flag == false) {
				String category = (String) spinner.getSelectedItem();
				DatabaseHandler db = new DatabaseHandler(
						getApplicationContext());
				DatePicker dp = new DatePicker(getApplicationContext());
				String date = dp.getDayOfMonth() + "/" + dp.getMonth() + "/"
						+ dp.getYear();
				int new_bal = db.getBal() - Integer.parseInt(amount);
				if (new_bal >= 0) {

					if (intentflag == 1) {
						db.addTrans(new Finance(tday_dt, Integer
							.parseInt(amount), category, description, 0));
						
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
								"Error in Transaction!!", Toast.LENGTH_LONG)
								.show();
				} else {
					Toast.makeText(getApplicationContext(),
							"NO SUFFICIENT BALANCE", Toast.LENGTH_LONG).show();
					Fragment fragment = new InitialAmount();
					Bundle args = new Bundle();
					args.putString(InitialAmount.ARG_OS, "" + 1);

					fragment.setArguments(args);
					getActionBar().setTitle(("Initial Amount"));
					// Insert the fragment by replacing any existing fragment
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();

				}
			}
		}

	}
}