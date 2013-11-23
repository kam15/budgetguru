package com.example.budgetguru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class PieChartDetails extends Activity {
	public static final String ARG_OS = "OS";
	public static final String DATE = "";
	private String string;
//	private String date;
	private String TAG_CAT = "category";
	private String TAG_AMT = "amount";
	private String TAG_DES = "descripiton";
	Spinner spinner1;
	String category,date;


	@Override
	public void onCreate(Bundle savedInstanceState) {
				// TextView textView = (TextView) view.findViewById(R.id.textView1);
		// textView.setText(string);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todays_expenses1);
		
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		Intent intent=getIntent();
		category=intent.getStringExtra("selectedCategory");
		date=intent.getStringExtra("selectedDate");
	//	SQLiteDatabase database = new SQLiteOpenHelper(ctxt).getWritableDatabase();
		DatabaseHandler database=new DatabaseHandler(getApplicationContext());
		/*Cursor c = database.rawQuery("select category from budget", null);
		c.moveToFirst();

		while (!c.isAfterLast()) {
			System.out.println("Data : " + c.getString(0));
			category.add(c.getString(0));
			c.moveToNext();
		}*/
	/* et=(EditText)findViewById(R.id.selectid);
	 
	 et.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getApplicationContext(),
					TodaysExpenses.class);
			intent.putExtra("selectedCategory",category);
			intent.putExtra("selectedDate", date);
			System.out.println("date is" + date);
			
//			startActivity(intent);
		}
	});*/
		ArrayList<String> categ=database.getAllCategories();

		ArrayAdapter<String> adaptr = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item, categ);
		//c.close();
		//database.close();
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adaptr);
		spinner1.setSelection(categ.indexOf(category));
		System.out.println("pos="+spinner1.getSelectedItemPosition()+" cat="+category+" index="+categ.indexOf(category));
	
		
		
		
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {			
				

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String selectedItem = spinner1.getSelectedItem().toString();	
				
		
				category=selectedItem;
				getDetails();
				/*Intent intent = new Intent(getApplicationContext(),
						TodaysExpenses.class);
				intent.putExtra("selectedCategory",selectedItem);
				intent.putExtra("selectedDate", date);*/
				System.out.println("date is" + date + selectedItem);
				
				//startActivity(intent);
				//finish();
			}
			

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				category=" ";
				getDetails();
				
			}
		});
		
		
		//et.setText(category);
		System.out.println("date and category"+date+""+category);
		getDetails();
		/*DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		DatePicker dp = new DatePicker(getApplicationContext());
		 //String date=dp.getDayOfMonth()+"/"+dp.getMonth()+"/"+dp.getYear();
		List<Finance> list = db.getDetailTrans(date, category);
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		 System.out.println(list.size() + " list " + contactList.size());
		for (Finance f : list) {
			String log = " Amount: " + f.getExpense() + " ,Description: "
					+ f.getDescription();
			// Writing Contacts to log
			 System.out.println(log);

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			//map.put(TAG_CAT, "" + f.getCategory());
			map.put(TAG_AMT, "" + f.getExpense());
			map.put(TAG_DES, "" + f.getDescription());
			// adding HashList to ArrayList
			contactList.add(map);
			  
 
		}// for

		ListAdapter adapter = new SimpleAdapter(getApplicationContext(), contactList,
				R.layout.detail_list,
				new String[] { TAG_DES, TAG_AMT }, new int[] {
						R.id.label2, R.id.label3 });

		ListView detail_list = (ListView) findViewById(R.id.listView1);
		
		detail_list.setAdapter(adapter);
*/
		
	}







void getDetails()
{
	DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	DatePicker dp = new DatePicker(getApplicationContext());
	 //String date=dp.getDayOfMonth()+"/"+dp.getMonth()+"/"+dp.getYear();
	List<Finance> list = db.getDetailTrans(date, category);
	ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

	 System.out.println(list.size() + " list " + contactList.size());
	for (Finance f : list) {
		String log = " Amount: " + f.getExpense() + " ,Description: "
				+ f.getDescription();
		// Writing Contacts to log
		 System.out.println(log);

		HashMap<String, String> map = new HashMap<String, String>();
		// adding each child node to HashMap key => value
		//map.put(TAG_CAT, "" + f.getCategory());
		map.put(TAG_AMT, "" + f.getExpense());
		map.put(TAG_DES, "" + f.getDescription());
		// adding HashList to ArrayList
		contactList.add(map);
		  

	}// for

	ListAdapter adapter = new SimpleAdapter(getApplicationContext(), contactList,
			R.layout.detail_list,
			new String[] { TAG_DES, TAG_AMT }, new int[] {
					R.id.label2, R.id.label3 });

	ListView detail_list = (ListView) findViewById(R.id.listView1);
	
	detail_list.setAdapter(adapter);

}
}