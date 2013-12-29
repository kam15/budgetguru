package com.example.budgetguru;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("newAPI")
public class FragmentTab2 extends Fragment implements OnDateSetListener {

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
	String mdate, mMonth;
	AlertDialog.Builder alert;
	String getMonthFromnumberpicker;
	String getcategoryofclicked = null;
	Calendar d;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragmenttab2, container, false);
		mContext = rootView.getContext();

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
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setStartAngle(90);

		d = new GregorianCalendar();
		
		int yr = d.get(Calendar.YEAR);

		if (yr > 99) {
			mdate = d.get(Calendar.DATE) + "/" + d.get(Calendar.MONTH) + "/20"
					+ (yr % 100);
		} else {
			mdate = d.get(Calendar.DATE) + "/" + d.get(Calendar.DAY_OF_MONTH)
					+ "/19" + (yr % 100);
		}
		mMonth = String.valueOf(d.get(Calendar.MONTH));
		
		drawPieForSpecificMonth(mMonth);

		final EditText editText_specificMonth = (EditText) rootView
				.findViewById(R.id.editText_specificMonth);
		alert = new AlertDialog.Builder(mContext);
		editText_specificMonth.setCursorVisible(false);

		editText_specificMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// custom dialog
				
				LayoutInflater inflater = (LayoutInflater) rootView
						.getContext().getSystemService(
								Context.LAYOUT_INFLATER_SERVICE);
				View npView = inflater.inflate(R.layout.dialog_specificmonth,
						null);
				alert.setTitle("Select Specific Month");

				np = (NumberPicker) npView.findViewById(R.id.numberPicker1);
				np.setMaxValue(12);
				np.setMinValue(1);

				String displayValues[] = { "Jan", "Feb", "Mar", "Apr", "May",
						"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
				np.setDisplayedValues(displayValues);
				np.setValue(d.get(Calendar.MONTH));

				// np.setBackgroundColor(Color.BLUE);
				np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

				alert.setPositiveButton("Set",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								getMonthFromnumberpicker = String.valueOf(np
										.getValue() - 1);
								drawPieForSpecificMonth(getMonthFromnumberpicker);
								
								editText_specificMonth
										.setText(getMonthFromnumberpicker);
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Cancel.

							}
						});

				alert.setView(npView).show();
			}// onclick

		});

		return rootView;
	}

	void drawPieForSpecificMonth(String month) {
		DatabaseHandler db = new DatabaseHandler(mContext);
		List<Finance> list = db.getmonthRecords(month);
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

	@Override
	public void onResume() {
		super.onResume();
		
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.chart);
		mChartView = ChartFactory.getPieChartView(rootView.getContext(),
				mSeries, mRenderer);
		mRenderer.setClickEnabled(true);

	/*	mChartView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				SeriesSelection seriesSelection = mChartView
						.getCurrentSeriesAndPoint();
				if (seriesSelection == null) {
					mChartView.repaint();
				} else {
					for (int i = 0; i < mSeries.getItemCount(); i++) {
						mRenderer.getSeriesRendererAt(i).setHighlighted(
								i == seriesSelection.getPointIndex());
						getcategoryofclicked = mSeries
								.getCategory(seriesSelection.getPointIndex());

					}
					mChartView.repaint();

					Intent intent = new Intent(rootView.getContext(),
							PieChartDetails.class);
					intent.putExtra("selectedCategory", getcategoryofclicked);
					intent.putExtra("selectedDate", mdate);
					
					startActivity(intent);

				}
				return true;
			}

		});
*/
		
		layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		if (mSeries.getItemCount() == 0)
			textView1
					.setText("Hey you have not added any data\n\t\tfor this month");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		this.year = year;
		this.month = monthOfYear;
		this.day = dayOfMonth;
		updateDisplay();
	}

	private void updateDisplay() { /* * Hide virtual keyboard */
		editText_specificDate.setText(new StringBuilder().append(month));
		flag = 0;
		if (year > 99) {
			changedDate = day + "/" + month + "/20" + (year % 100);
		} else {
			changedDate = day + "/" + month + "/19" + (year % 100);
		}

			drawPieForSpecificMonth(String.valueOf(month));

	}
}