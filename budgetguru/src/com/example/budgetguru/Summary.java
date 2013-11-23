package com.example.budgetguru;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetguru.SimpleGestureFilter.SimpleGestureListener;

public class Summary extends Activity implements SimpleGestureListener{
	
	private int amount,cash=0,food_amount=0,travel_amount=0,entertain_amount=0, 
			clothing_amount=0,bill_amount=0,medical_amount=0, missing_amount=0,other_amount=0;
	Calendar cal=new GregorianCalendar();
	private SimpleGestureFilter detector;
	private String month,category;
	private int m;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		// Show the Up button in the action bar.
		setupActionBar();
		detector = new SimpleGestureFilter(this,this);
		month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		month = month.toUpperCase();
		m = cal.get(Calendar.MONTH);
		DatabaseHandler db=new DatabaseHandler(getApplicationContext());
		cash=db.getBal();
		System.out.println("cas"+cash+cal.get(Calendar.MONTH));
		List<Finance> list =db.getmonthRecords(Integer.toString(m));
		System.out.println("list size isssssas="+list.size());
		//list=db.getAllRecords();
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
System.out.println("list size"+list.size());
		// System.out.println(list.size() + " " + allList.size());
		for (Finance f : list) {
			  HashMap<String, String> map = new HashMap<String, String>();
		      // adding each child node to HashMap key => value
		      category=f.getCategory();
		      amount=f.getExpense();
		      System.out.println("-----------dateeeeee"+f.getDate());
		      if(category.equals("Food"))
				{
					food_amount =amount;
					System.out.println("in summary check food amt : "+food_amount);
				}
				else if(category.equals("Travel"))
					travel_amount =amount;
				else if(category.equals("Bills"))
					bill_amount = amount;
				else if(category.equals("Clothing"))
					clothing_amount = amount;
				else if(category.equals("Medical"))
					medical_amount = amount;
				else if(category.equals("Entertainment"))
					entertain_amount = amount;
				else if(category.equals("Missing"))
					missing_amount = amount;
				else if(category.equals("Other"))
					other_amount = amount;
			


		}// for
		
		TextView t1=(TextView)findViewById(R.id.monthsum);
		t1.setText(month);
		TextView t2=(TextView)findViewById(R.id.cashsum);
		t2.setText("Cash in Hand : "+cash);
		TextView t3=(TextView)findViewById(R.id.foodsum);
		t3.setText("Food : "+food_amount);
		TextView t4=(TextView)findViewById(R.id.travelsum);
		t4.setText("Travel : "+travel_amount);
		TextView t5=(TextView)findViewById(R.id.entertainsum);
		t5.setText("Entertainment : "+entertain_amount);
		TextView t6=(TextView)findViewById(R.id.clothsum);
		t6.setText("Clothing : "+clothing_amount);
		TextView t7=(TextView)findViewById(R.id.billsum);
		t7.setText("Bills : "+bill_amount);
		TextView t8=(TextView)findViewById(R.id.medsum);
		t8.setText("Medical : "+medical_amount);
		TextView t9=(TextView)findViewById(R.id.missingsum);
		t9.setText("Missing : "+missing_amount);
		TextView t10=(TextView)findViewById(R.id.othersum);
		t10.setText("Other : "+other_amount);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summary, menu);
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

	@Override 
    public boolean dispatchTouchEvent(MotionEvent me){ 
      this.detector.onTouchEvent(me);
     return super.dispatchTouchEvent(me); 
    }
@Override
 public void onSwipe(int direction) {
  String str = "";
  
  switch (direction) {
  
  case SimpleGestureFilter.SWIPE_LEFT : str = "Swipe Left";
  											Intent i1=new Intent(this,MainActivity.class);
  											startActivity(i1);
                                           break;                                       
  } 
   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
 }

}
