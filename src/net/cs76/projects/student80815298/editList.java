package net.cs76.projects.student80815298;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class editList extends Activity implements OnItemClickListener {

	ArrayList<String> stock_list;
	DBAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.edit);
			db = new DBAdapter(this);
			db.open();
			//get list of stocks
			stock_list = db.getStocks();
			
			//push to an array and set to the listview
			String[] stockArr = new String[stock_list.size()];
			stockArr = stock_list.toArray(stockArr);
			ListView lv = (ListView) findViewById(R.id.editView);
			lv.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, stockArr));

			lv.setOnItemClickListener(this);
		} 
		
		catch (Exception e) {
			// do nothing
		}
	}

	public void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
		//when item is clicked send to the next activity which stock was clicked on
		Intent intent = new Intent(this, input.class);
		intent.putExtra("ticker", stock_list.get(arg2));
		finish();
		startActivity(intent);

	}

}
