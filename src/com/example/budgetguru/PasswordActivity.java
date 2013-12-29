package com.example.budgetguru;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		String db_pswd = "";
		RingerHandler db = new RingerHandler(getApplicationContext());
		List<Ringer> list = db.getRecords();
		for (Ringer f : list) {
			db_pswd = f.getPassword();

		}
		if (db_pswd.equals("1111")) {
			TextView t = (TextView) findViewById(R.id.defaulttext);
			t.setText("(Default password is 1111)");
		}
	}

	public void validate(View view) {
		boolean error_flag = false;
		EditText pwd = (EditText) findViewById(R.id.passwordtxt);
		String password = pwd.getText().toString();

		if (password.matches("")) {
			pwd.setError("Password not entered");
			error_flag = true;
		}
		String db_pswd = "";
		RingerHandler db = new RingerHandler(getApplicationContext());
		List<Ringer> list = db.getRecords();
		for (Ringer f : list) {
			db_pswd = f.getPassword();

		}
		System.out.println("password**" + db_pswd);
		if (error_flag == false) {
			if (password.equals(db_pswd)) {
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Incorrect password",
						Toast.LENGTH_LONG).show();
				pwd.setText("");
				// Intent i=new Intent(this,PasswordActivity.class);
				// startActivity(i);
			}
		}
	}

}