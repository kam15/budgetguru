package com.example.budgetguru;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AutoCompActivity extends Activity {
AutoCompleteTextView auto;
ArrayAdapter<String> autoadapt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autocomp);
		auto=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		
		DatabaseHandler database=new DatabaseHandler(this);
		  List<Finance> list=database.getAllRecords();
		 autoadapt=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		 
		 if(!list.isEmpty())
		  {
			  for(int i=0;i<list.size();i++)
			  {
				  Finance f=list.get(i);
				  
				  autoadapt.add("Date : "+f.getDate()+"\nDescription : "+f.getDescription()+"\nAmount : "+f.getExpense()+"\nCategory : "+f.getCategory());
				  
			  }
			 
		  }
		  else
			  System.out.println("empty list");
		 
		 auto.setAdapter(autoadapt);
		// auto.setTextColor(1);
		 
	String display=auto.getText().toString();
	System.out.println("dISPLAY"+display);
	auto.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			// TODO Auto-generated method stub
			
		}
	});
	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.auto_comp, menu);
		return true;
	}

}
