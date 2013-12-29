package com.example.budgetguru;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Change_Password extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		// Show the Up button in the action bar.

		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change__password, menu);
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

	public void saveNewPassword(View view) {
		@SuppressWarnings("unused")
		boolean error_flag = false;
		EditText et1, et2, et3;
		String oldpwd, newpwd, confirmpwd, password = "", format = "";

		et1 = (EditText) findViewById(R.id.oldpwdtxt);
		et2 = (EditText) findViewById(R.id.newpwdtxt);
		et3 = (EditText) findViewById(R.id.confirmpwdtxt);
		oldpwd = et1.getText().toString();
		newpwd = et2.getText().toString();
		confirmpwd = et3.getText().toString();

		if (oldpwd.matches("")) {
			et1.setError("Old Password not entered");
			error_flag = true;
		}

		if (newpwd.matches("")) {
			et2.setError("New Password not entered");
			error_flag = true;
		}

		if (confirmpwd.matches("")) {
			et3.setError("Confirm Password not entered");
			error_flag = true;
		}
		RingerHandler db = new RingerHandler(getApplicationContext());
		List<Ringer> list = db.getRecords();
		for (Ringer f : list) {
			password = f.getPassword();
			format = f.getFormat();

		}

		if (error_flag = false) {
			if (oldpwd.equals(password)) {
				if (newpwd.equals(confirmpwd)) {

					long val = db.addTrans(new Ringer(newpwd, format));
					if (val != -1) {
						Toast.makeText(getApplicationContext(),
								"Password changed", Toast.LENGTH_LONG).show();
						Intent i = new Intent(this, MainActivity.class);
						startActivity(i);
					} else {
						Toast.makeText(getApplicationContext(),
								"Error in transaction", Toast.LENGTH_LONG)
								.show();
					}
				} else {
					Toast.makeText(
							getApplicationContext(),
							"New password and confirmed password did not match",
							Toast.LENGTH_LONG).show();
					et1.setText("");
					et2.setText("");
					et3.setText("");
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Old password is incorrect", Toast.LENGTH_LONG).show();
				et1.setText("");
				et2.setText("");
				et3.setText("");
			}

		}
	}

}
