package com.example.budgetguru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LowerInitialAmount extends Activity {

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial_amount);
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		int balance = db.getBal();
		TextView textView = (TextView) findViewById(R.id.balance);
		textView.setText("" + balance);
		Button add = (Button) findViewById(R.id.addbutton);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView amt = (TextView) findViewById(R.id.addeditText);
				if(amt.getText().toString().equals(""))
					amt.setError("Amount not Entered");
				else
				{
				DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				db.addTrans(new Finance("",0, "", "",Integer.parseInt(amt.getText().toString())));

				Toast.makeText(getApplicationContext(), "Balance=" + db.getBal(),
						Toast.LENGTH_LONG).show();
				Intent page = new Intent(getApplicationContext(), LowerMain.class);
				startActivity(page);
				}
			}
		});
		Button deduct = (Button) findViewById(R.id.deductbutton);
		deduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView amt = (TextView) findViewById(R.id.addeditText);
				if(amt.getText().toString().equals(""))
					amt.setError("Amount not Entered");
				else
				{
				DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				db.addTrans(new Finance("",Integer.parseInt(amt.getText().toString()), "", ""));

				Toast.makeText(getApplicationContext(), "Balance=" + db.getBal(),
						Toast.LENGTH_LONG).show();
				Intent page = new Intent(getApplicationContext(), LowerMain.class);
				startActivity(page);
				}
			}
		});
		
	}


}