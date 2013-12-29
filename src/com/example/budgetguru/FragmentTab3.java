package com.example.budgetguru;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetguru.SimpleGestureFilter.SimpleGestureListener;

@SuppressLint("newAPI")
public class FragmentTab3 extends Fragment implements SimpleGestureListener  {

	SQLiteDatabase database;
	Cursor c;

	private static int[] COLORS = new int[] { Color.YELLOW, Color.BLUE,
			Color.GREEN, Color.CYAN, Color.GREEN, Color.RED, Color.YELLOW };
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	private GraphicalView mChartView;
	private TextView textView1;
	Button button_dialog_specificDate;
	EditText editText_specificDate;
	Context mContext;
	View rootView;

	int flag = 1, databaseflag = 1, year, month, day;
	String changedDate;
	NumberPicker np;
	String mdate;
	AlertDialog.Builder alert;
	String getMonthFromnumberpicker;
	String getcategoryofclicked = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragmenttab3, container, false);
		mContext = rootView.getContext();

		return rootView;
	}

	void drawPieForSpecificDate(String mdate) {

		DatabaseHandler db = new DatabaseHandler(mContext);
		List<Finance> list = db.getuniqueRecords(mdate);
		textView1 = (TextView) rootView.findViewById(R.id.textView1);

		if (databaseflag == 0) {
			if (mRenderer != null)
				mRenderer.removeAllRenderers();
			if (mSeries != null)
				mSeries.clear();
		}
		if (!list.isEmpty()) {

			databaseflag = 0;
			for (int i = 0; i < list.size(); i++) {

				Finance f = list.get(i);

				mSeries.add("" + f.getCategory(), f.getExpense());
				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();

				renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
						% COLORS.length]);
				mRenderer.addSeriesRenderer(renderer);

				if (mChartView != null) {
					mChartView.repaint();

					textView1.setText("");
				}

			}
		} else {
			if (mChartView != null)
				mChartView.repaint();
			textView1
					.setText("Hey you have not added any data\n\t\tPlease add some");
		}

	}

	void drawPieForSpecificMonth(String month) {

		DatabaseHandler db = new DatabaseHandler(mContext);
		List<Finance> list = db.getSumExpense(month);
		List<Finance> list1 = db.getSumMissing(month);
		textView1 = (TextView) rootView.findViewById(R.id.textView1);

		if (databaseflag == 0) {
			if (mRenderer != null)
				mRenderer.removeAllRenderers();
			if (mSeries != null)
				mSeries.clear();
		}
		if (!list.isEmpty()) {
			System.out.println("list is not empty ");
			databaseflag = 0;

			{

				Finance f = list.get(0);
				Finance f1 = list1.get(0);

				mSeries.add("" + "Expense", f.getExpense());
				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
				renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
						% COLORS.length]);
				mRenderer.addSeriesRenderer(renderer);
				if (mChartView != null) {
					mChartView.repaint();

					textView1.setText("");
				}

				mSeries.add("" + "Missing", f1.getExpense());
				renderer = new SimpleSeriesRenderer();
				renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
						% COLORS.length]);
				mRenderer.addSeriesRenderer(renderer);
				if (mChartView != null) {
					mChartView.repaint();
					textView1.setText("");
				}

			}
		} else {
			if (mChartView != null)
				mChartView.repaint();
			textView1
					.setText("Hey you have not added any data\n\t\tPlease add some");

		}

	}

	@Override
	public void onResume() {
		super.onResume();

		mRenderer.setApplyBackgroundColor(true);
		// mRenderer.setBackgroundColor(Color.argb(100, 80, 80, 50));
		mRenderer.setChartTitleTextSize(15);
		mRenderer.setLabelsTextSize(20);
		mRenderer.setLabelsColor(color.black);
		mRenderer.setDisplayValues(true);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setLegendTextSize(20);
		mRenderer.setShowLabels(true);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(180);
		mRenderer.setZoomEnabled(true);
		mRenderer.setPanEnabled(false);

		Calendar d = new GregorianCalendar();

		int yr = d.get(Calendar.YEAR);

		if (yr > 99) {
			mdate = d.get(Calendar.DATE) + "/" + d.get(Calendar.MONTH) + "/20"
					+ (yr % 100);
		} else {
			mdate = d.get(Calendar.DATE) + "/" + d.get(Calendar.DAY_OF_MONTH)
					+ "/19" + (yr % 100);
		}

		drawPieForSpecificMonth("" + d.get(Calendar.MONTH));

		{
			LinearLayout layout = (LinearLayout) rootView
					.findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(rootView.getContext(),
					mSeries, mRenderer);
			mRenderer.setClickEnabled(true);

			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			if (mSeries.getItemCount() == 0)
				textView1
						.setText("Hey you have not added any data\n\t\tfor this month");
		}
	}

	

	
@Override
 public void onSwipe(int direction) {
  String str = "";
  
  switch (direction) {
  
  case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
  											/*Intent i1=new Intent(this,MainActivity.class);
  											startActivity(i1);*/
                                           break;
  case SimpleGestureFilter.SWIPE_LEFT: str="Swipe left";
	Intent i2= new Intent(rootView.getContext(),Summary.class);
	startActivity(i2);
	break;
  } 
   Toast.makeText(rootView.getContext(), str, Toast.LENGTH_SHORT).show();
 }

}