package net.cs76.projects.student80815298;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class input extends Activity implements OnClickListener {
	DBAdapter db;
	String stockString;
	EditText purchasePriceText;
	EditText qtyText;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input);
		
		//declare variables in the xml file for inputs and set the listeners
		purchasePriceText = (EditText)findViewById(R.id.sharePrice);
		qtyText = (EditText)findViewById(R.id.sharesPurchased);
		purchasePriceText.setOnClickListener(this);
		qtyText.setOnClickListener(this);
		Button submitButton = (Button)findViewById(R.id.inputButton);
		submitButton.setOnClickListener(this);
		
		//open database
		db = new DBAdapter(this);
		db.open();
		
		//get the ticker that was sent from previous activity
		Intent intent = getIntent();
		stockString = intent.getStringExtra("ticker");
		
		Button remove = (Button)findViewById(R.id.removeButton);
		remove.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.sharePrice:
			purchasePriceText.setText("");
			break;
		
		case R.id.sharesPurchased:
			qtyText.setText("");
			break;
			
		case R.id.inputButton:
			//update data table from the new inputs
			double qty = Double.parseDouble(qtyText.getText().toString());
			double pprice = Double.parseDouble(purchasePriceText.getText().toString());
			db.udpateStockInfo(stockString, qty, pprice);
			
			//close the keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(qtyText.getWindowToken(), 0);
			finish();//fires the onactivityresult method
			setResult(RESULT_OK);
			//startActivity(in);
			
			break;
			
		case R.id.removeButton:
			db.removeStock(stockString);
			
			finish(); //finish fires the onactivityresult method
			setResult(RESULT_OK);
			//sends to force close on back press
			break;
		}
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		db.close();
	}
	
	

}
