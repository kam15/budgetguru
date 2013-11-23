package com.example.budgetguru;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InitialAmount extends Fragment {
	public static final String ARG_OS = "OS";
	private String string;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.initial_amount, null);
		DatabaseHandler db = new DatabaseHandler(view.getContext());
		int balance = db.getBal();
		TextView textView = (TextView) view.findViewById(R.id.balance);
		textView.setText("" + balance);
		Button add = (Button) view.findViewById(R.id.addbutton);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView amt = (TextView) view.findViewById(R.id.addeditText);
				if(amt.getText().toString().equals(""))
					amt.setError("Amount not Entered");
				else
				{
				DatabaseHandler db = new DatabaseHandler(view.getContext());
				db.addTrans(new Finance("",0, "", "",Integer.parseInt(amt.getText().toString())));

				Toast.makeText(view.getContext(), "Balance=" + db.getBal(),
						Toast.LENGTH_LONG).show();
				Intent page = new Intent(view.getContext(), MainActivity.class);
				startActivity(page);
				}
			}
		});
		Button deduct = (Button) view.findViewById(R.id.deductbutton);
		deduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView amt = (TextView) view.findViewById(R.id.addeditText);
				if(amt.getText().toString().equals(""))
					amt.setError("Amount not Entered");
				else
				{
				DatabaseHandler db = new DatabaseHandler(view.getContext());
				db.addTrans(new Finance("",Integer.parseInt(amt.getText().toString()), "", ""));

				Toast.makeText(view.getContext(), "Balance=" + db.getBal(),
						Toast.LENGTH_LONG).show();
				Intent page = new Intent(view.getContext(), MainActivity.class);
				startActivity(page);
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
	}

}