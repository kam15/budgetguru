package com.example.budgetguru;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.budgetguru.SimpleGestureFilter.SimpleGestureListener;


@SuppressLint("newAPI")
public class PieChartActivity extends Activity implements SimpleGestureListener{
	// Declare Tab Variable
	ActionBar.Tab Tab1, Tab2, Tab3;
	Fragment fragmentTab1 = new FragmentTab1();
	Fragment fragmentTab2 = new FragmentTab2();
	Fragment fragmentTab3 = new FragmentTab3();
	private SimpleGestureFilter detector;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_piechart);
		detector = new SimpleGestureFilter(this,this);
		ActionBar actionBar = getActionBar();

		// Hide Actionbar Icon
		actionBar.setDisplayShowHomeEnabled(false);

		// Hide Actionbar Title
		actionBar.setDisplayShowTitleEnabled(false);

		// Create Actionbar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set Tab Icon and Titles
	//	Tab1 = actionBar.newTab().setIcon(R.drawable.date);
		Tab1 = actionBar.newTab().setText("Date");
		Tab2 = actionBar.newTab().setText("Month");
		Tab3 = actionBar.newTab().setText("Missing");

		// Set Tab Listeners
		Tab1.setTabListener(new TabListener(fragmentTab1));
		Tab2.setTabListener(new TabListener(fragmentTab2));
		Tab3.setTabListener(new TabListener(fragmentTab3));
  
		// Add tabs to actionbar
		actionBar.addTab(Tab1);
		actionBar.addTab(Tab2);
		actionBar.addTab(Tab3);
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
  
  case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
  											Intent i1=new Intent(this,MainActivity.class);
  											startActivity(i1);
                                           break;
  case SimpleGestureFilter.SWIPE_LEFT: str="Swipe left";
	Intent i2= new Intent(this,Summary.class);
	startActivity(i2);
	break;
  } 
   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
 }

}
