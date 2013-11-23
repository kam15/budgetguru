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

import com.example.budgetguru.SimpleGestureFilter.SimpleGestureListener;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class PieChart_Activity extends Activity implements OnDateSetListener,SimpleGestureListener {

	SQLiteDatabase database;
	Cursor c;
	private SimpleGestureFilter detector;
	// ArrayList<Finance> pieChartlabels=new ArrayList<Finance>();

	private static int[] COLORS = new int[] { Color.YELLOW, Color.BLUE,
			Color.GREEN, Color.CYAN,Color.GREEN,Color.RED,Color.YELLOW};
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	private GraphicalView mChartView;
	private TextView textView1;
	Button button_dialog_specificDate;
	EditText editText_specificDate;

	int flag = 1, databaseflag = 1, year, month, day;
	String changedDate;
	NumberPicker np;
	String mdate;
	AlertDialog.Builder alert;
	String getMonthFromnumberpicker;
	String getcategoryofclicked = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pie_chart);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		detector = new SimpleGestureFilter(this,this);
		final Context mContext = this;
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

		Calendar d = new GregorianCalendar();
		// Date d = new Date(System.currentTimeMillis());
		System.out
				.println("date=" + d.get(Calendar.DATE) + "month="
						+ d.get(Calendar.DAY_OF_MONTH) + "year="
						+ d.get(Calendar.YEAR));
		int yr = d.get(Calendar.YEAR);

		if (yr > 99) {
			mdate = d.get(Calendar.DATE) + "/" + d.get(Calendar.MONTH) + "/20"
					+ (yr % 100);
		} else {
			mdate = d.get(Calendar.DATE) + "/" + d.get(Calendar.DAY_OF_MONTH)
					+ "/19" + (yr % 100);
		}
		System.out.println("date is " + mdate);
		drawPieForSpecificDate(mdate);
		editText_specificDate = (EditText) this
				.findViewById(R.id.editText_specificDate);
		editText_specificDate.setCursorVisible(false);
		editText_specificDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// custom dialog
				flag = 1;

				final Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						mContext, PieChart_Activity.this, year, month, day);

				datePickerDialog.show();
				flag=1;
				        

			}
		});// button_specificDate setListener
		
		if (flag == 1)
			drawPieForSpecificDate(mdate);

		Button button_specificMonth = (Button) this
				.findViewById(R.id.button_specificMonth);
		alert = new AlertDialog.Builder(this);
    
//		button_specificMonth.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// custom dialog
//
//				LayoutInflater inflater = (LayoutInflater) getApplicationContext()
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				View npView = inflater.inflate(R.layout.dialog_specificmonth,
//						null);
//				alert.setTitle("Select Specific Month");
//
//				np = (NumberPicker) npView.findViewById(R.id.numberPicker1);
//				np.setMaxValue(12);
//				np.setMinValue(1);
//				// np.setBackgroundColor(Color.BLUE);
//				np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//
//				
//				alert.setPositiveButton("Set",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								getMonthFromnumberpicker = String.valueOf(np
//										.getValue());
//								drawPieForSpecificMonth(getMonthFromnumberpicker);
//								System.out.println("getmonth"
//										+ getMonthFromnumberpicker);
//							}
//						});
//
//				alert.setNegativeButton("Cancel",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								// Cancel.
//
//							}
//						});
//
//				alert.setView(npView).show();
//			}// onclick
//
//		});
		button_specificMonth.setVisibility(View.GONE);
	
//		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//		
//		lp.copyFrom(this.getWindow().getAttributes());
//		lp.width = 290;
//		lp.height = 400;
//		lp.x=-0;
//		lp.y=70;
//		this.getWindow().setAttributes(lp);
	}

	
	
	
	void drawPieForSpecificDate(String mdate) {

		DatabaseHandler db = new DatabaseHandler(this);
		List<Finance> list = db.getuniqueRecords(mdate);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		System.out.println("in side inside ");
		if (databaseflag == 0) {
			if (mRenderer != null)
				mRenderer.removeAllRenderers();
			if (mSeries != null)
				mSeries.clear();
		}
		if (!list.isEmpty()) {
			System.out.println("in side ");
			databaseflag = 0;
			for (int i = 0; i < list.size(); i++) {

				Finance f = list.get(i);

				mSeries.add("" + f.getCategory(),
						f.getExpense());
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
			textView1.setText("Hey you have not added any data\n\t\tPlease add some");
		}

	}

	void drawPieForSpecificMonth(String month) {

		DatabaseHandler db = new DatabaseHandler(this);
		List<Finance> list = db.getmonthRecords(month);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		System.out.println("in side inside ");
		if (databaseflag == 0) {
			if (mRenderer != null)
				mRenderer.removeAllRenderers();
			if (mSeries != null)
				mSeries.clear();
		}
		if (!list.isEmpty()) {
			System.out.println("in side ");
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
	protected void onResume() {
		super.onResume();

		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setClickEnabled(true);

			mChartView.setOnLongClickListener(new View.OnLongClickListener() {
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
							getcategoryofclicked = mSeries.getCategory(seriesSelection.getPointIndex());
							

						}
						mChartView.repaint();

						Intent intent = new Intent(getApplicationContext(),
								PieChartDetails.class);
						intent.putExtra("selectedCategory",
								getcategoryofclicked);
						intent.putExtra("selectedDate", mdate);
						System.out.println("date is" + mdate);
						System.out.println("in on resume");
						startActivity(intent);

					}
					return true;
				}

			});

			// mChartView.setOnClickListener(new View.OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// SeriesSelection seriesSelection = mChartView
			// .getCurrentSeriesAndPoint();
			// if (seriesSelection == null) {
			// // Toast.makeText(PieChart_Activity.this,
			// // "No chart element selected", Toast.LENGTH_SHORT)
			// // .show();
			// mChartView.repaint();
			//
			// } else {
			// for (int i = 0; i < mSeries.getItemCount(); i++) {
			// mRenderer.getSeriesRendererAt(i).setHighlighted(
			// i == seriesSelection.getPointIndex());
			//
			//
			//
			// }
			// mChartView.repaint();
			// Toast.makeText(
			// PieChart_Activity.this,
			// "Chart data point index "
			// + seriesSelection.getPointIndex()
			// + " selected" + " point value="
			// + seriesSelection.getValue(),
			// Toast.LENGTH_SHORT).show();
			// }
			// }
			// });
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			if (mSeries.getItemCount() == 0)
				textView1
						.setText("Hey you have not added any data\n\t\tPlease add some");
		} else {
			mChartView.repaint();

		}
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
		editText_specificDate.setText(new StringBuilder().append(day)
				.append("-").append(month + 1).append("-").append(year)
				.append(""));
		flag = 0;
		if (year > 99) {
			changedDate = day + "/" + month + "/20" + (year % 100);
		} else {
			changedDate = day + "/" + month + "/19" + (year % 100);
		}

		System.out.println("----------------------" + changedDate);

		drawPieForSpecificDate(changedDate);

	}
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
