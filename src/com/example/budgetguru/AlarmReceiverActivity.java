package com.example.budgetguru;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlarmReceiverActivity extends Activity {
	private MediaPlayer mMediaPlayer;
	String title, date, m3, m4, p;
	Calendar cal = Calendar.getInstance();
	int m, m2, month;
	String desc, type;
	TextView desctv, category, date1, time1, date2, time2;
	Button delbtn, editbtn;

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent i = getIntent();
		desc = i.getStringExtra("title");

		SQLiteDatabase database = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();
		Cursor c = database.rawQuery(
				"select type from Reminders where Description='" + desc + "'",
				null);

		if (c.moveToFirst())
			type = c.getString(0);

		if (type.equals("I took"))
			setContentView(R.layout.viewborrowed);
		else
			setContentView(R.layout.view_lent);

		c.close();
		database.close();

		SQLiteDatabase db = new SQLiteHelper(getApplicationContext())
				.getReadableDatabase();

		desctv = (TextView) findViewById(R.id.viewdesc);
		category = (TextView) findViewById(R.id.viewcategory);
		date1 = (TextView) findViewById(R.id.viewdate);
		time1 = (TextView) findViewById(R.id.viewtime);
		date2 = (TextView) findViewById(R.id.viewdate2);
		time2 = (TextView) findViewById(R.id.viewtime2);
		delbtn = (Button) findViewById(R.id.delete_button);
		editbtn = (Button) findViewById(R.id.edit_button);

		Cursor c1 = db.rawQuery("select * from Reminders where Description='"
				+ desc + "'", null);
		c1.moveToFirst();

		if (c1 != null) {
			desctv.setText(c1.getString(0));
			category.setText(c1.getString(1));
			date1.setText(c1.getString(3));
			time1.setText(c1.getString(4));
			date2.setText(c1.getString(5));
			time2.setText(c1.getString(6));

			c1.moveToNext();
		}

		c1.close();
		db.close();

		delbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// System.out.println("Title="+title);
				SQLiteDatabase database = new SQLiteHelper(
						getApplicationContext()).getWritableDatabase();
				String table_name = "Reminders";
				String where = "Description = '" + desc + "'";

				database.delete(table_name, where, null);

				database.close();
				finish();
				Intent disp_list = new Intent(getApplicationContext(),
						ForgetMeNot.class);
				startActivity(disp_list);
			}
		});

		editbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				finish();
				Intent edit_reminder = new Intent(getApplicationContext(),
						EditReminder.class);
				edit_reminder.putExtra("title", desc);
				startActivity(edit_reminder);

			}
		});

		delbtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mMediaPlayer.stop();
				finish();
				return false;
			}
		});

		editbtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mMediaPlayer.stop();
				finish();
				return false;
			}
		});

		playSound(this, getAlarmUri());
	}

	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IOException e) {
			System.out.println("OOPS");
		}
	}

	// Get an alarm sound. Try for an alarm. If none set, try notification,
	// Otherwise, ringtone.
	private Uri getAlarmUri() {
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null) {
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}
}
