package com.example.budgetguru;

import com.example.budgetguru.SimpleGestureFilter.SimpleGestureListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LowerMain extends Activity implements SimpleGestureListener{
	Spinner spinner,spinner2;
	ArrayAdapter<String> adapter,adapter2;
	int flag;
	private String  old_cat,old_amt,old_des,tday_dt;
	private int intentflag;
	private SimpleGestureFilter detector;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lower_main);
		DatabaseHandler db=new DatabaseHandler(getApplicationContext());
		int amt=db.getBal();
	System.out.println("amt="+amt);
		//	Log.i("Amount = "+amt, null);
		if(amt<=0)
		{
			Intent i=new Intent(this,LowerInitialAmount.class);
			startActivity(i);
		}
		
	
		
		String category[] = { "Food", "Travel", "Entertainment", "Clothing",
				"Bills", "Medical", "Other" };
		String page[] = { "Home", "Initial amount", "Todays Expense", "Specified date expense",
				"Reports", "Search","Lent/Borrowed", "About" };
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

	            String s=((TextView)view).getText().toString();
	            if(s.equals("Initial amount"))
	                startActivity(new Intent(view.getContext(),LowerInitialAmount.class));
	            if(s.equals("Todays Expense"))
	                startActivity(new Intent(view.getContext(),LowerTodaysExpenses.class));
	            if(s.equals("Specified date expense"))
	                startActivity(new Intent(view.getContext(),LowerTodaysExpenses.class));
	            //if(s.equals("Reports"))
	              //  startActivity(new Intent(view.getContext(),Reports.class));
	            if(s.equals("Search"))
	                startActivity(new Intent(view.getContext(),AutoCompActivity.class));
	            if(s.equals("Lent/Borrowed"))
	                startActivity(new Intent(view.getContext(),ForgetMeNot.class));
	            if(s.equals("About"))
	                startActivity(new Intent(view.getContext(),About.class));
	            
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {
	            // TODO Auto-generated method stub

	        }
		});       
				Intent i=getIntent();
		intentflag=i.getIntExtra("intentflag",0);
		if(intentflag==1)
		{
			old_cat=i.getStringExtra("cat");
			old_amt=i.getStringExtra("amt");
			old_des=i.getStringExtra("des");
			tday_dt=i.getStringExtra("date");
			EditText ed1=(EditText)findViewById(R.id.l_editText1);
			ed1.setText(""+old_des);
			EditText ed2=(EditText)findViewById(R.id.l_editText2);
			ed2.setText(""+old_amt);
			
			spinner.setSelection(adapter.getPosition(old_cat));
			System.out.println("In intent flag = 1");
			System.out.println("cat = "+old_cat);
			System.out.println("amt = "+old_amt);
			System.out.println("description = "+old_des);
		
		}
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
  case SimpleGestureFilter.SWIPE_LEFT: str="Swipe Left";
	Intent i= new Intent(this,PieChart_Activity.class);
	startActivity(i);
	break;
  case SimpleGestureFilter.SWIPE_RIGHT: str="Swipe Right";
	Intent i1= new Intent(this,Summary.class);
	startActivity(i1);
	break;

  
  } 
  	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

  
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.mainmenu, menu);
		// return true;
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
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
				//db.delAll();
				DatePicker dp = new DatePicker(getApplicationContext());
				String date = dp.getDayOfMonth() + "/" + dp.getMonth() + "/"
						+ dp.getYear();
				int new_bal=db.getBal()-Integer.parseInt(amount);
				if(new_bal>=0)
				{
					
					if(intentflag==1){
						db.addTrans(new Finance(tday_dt, Integer.parseInt(amount),
								category, description,0));
						db.delete(old_cat, Integer.parseInt(old_amt), old_des, tday_dt);
						Toast.makeText(getApplicationContext(),
								"Saved Your Transaction!!", Toast.LENGTH_LONG).show();
						Intent intent = getIntent();
					    finish();
					    startActivity(intent);
					}
					
					else if ((db.addTrans(new Finance(date, Integer.parseInt(amount),
						category, description,0)) != -1))
				{
					Toast.makeText(getApplicationContext(),
							"Saved Your Transaction!!", Toast.LENGTH_LONG).show();
				
					Intent intent = getIntent();
				    finish();
				    startActivity(intent);
				}	
				else
					Toast.makeText(getApplicationContext(),
							"Error in Transaction!!", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "NO SUFFICIENT BALANCE",Toast.LENGTH_LONG).show();
					Intent i=new Intent(this,LowerInitialAmount.class);
					startActivity(i);
					
				}
			}

	}
	
	
}
