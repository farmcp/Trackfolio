/*author:Christopher Farm HUID:80815298*/

package net.cs76.projects.student80815298;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Trackfolio extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	EditText editText;
	Button addButton;
	Button editButton;
	ArrayList<String> stock_list;
	DBAdapter db;
	TableLayout tl;
	double totalValue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set content view then display the list of stocks in the database
		setContentView(R.layout.main);
		displayList();

	}

	public void displayList() {
		try {
			//open a database
			db = new DBAdapter(this);
			db.open();
			
			//get stock tickers in the database
			stock_list = new ArrayList<String>();
			stock_list = db.getStocks();
			
			// set the listeners for the textbox and the button
			editText = (EditText) findViewById(R.id.editText1);
			editText.setOnClickListener(this);
			addButton = (Button) findViewById(R.id.addButton);
			addButton.setOnClickListener(this);
			editButton = (Button) findViewById(R.id.editButton);
			editButton.setOnClickListener(this);
			
			//get the width of the display and allocate table col widths appropriately
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();

			tl = (TableLayout) findViewById(R.id.tableLayout1);
			tl.removeAllViewsInLayout();
			
			//create the first row in the table with headers
			TableRow headerRow = new TableRow(this);
			TextView tickHeader = new TextView(this);
			TextView priceHeader = new TextView(this);
			TextView gainHeader = new TextView(this);
			TextView allocHeader = new TextView(this);
			LayoutParams lp = new LayoutParams(width / 4,
					LayoutParams.WRAP_CONTENT);
			tickHeader.setLayoutParams(lp);
			priceHeader.setLayoutParams(lp);
			gainHeader.setLayoutParams(lp);
			allocHeader.setLayoutParams(lp);

			tickHeader.setGravity(Gravity.CENTER);
			tickHeader.setText("Ticker");
			tickHeader
					.setTextColor(getResources().getColor(R.color.colorBlack));
			priceHeader.setGravity(Gravity.RIGHT);
			priceHeader.setText("Price");
			priceHeader.setTextColor(getResources()
					.getColor(R.color.colorBlack));
			gainHeader.setGravity(Gravity.CENTER);
			gainHeader.setText("Gain");
			gainHeader
					.setTextColor(getResources().getColor(R.color.colorBlack));
			allocHeader.setGravity(Gravity.CENTER);
			allocHeader.setText("Allocation");
			allocHeader.setTextColor(getResources()
					.getColor(R.color.colorBlack));

			tl.addView(headerRow);
			headerRow.addView(tickHeader);
			headerRow.addView(priceHeader);
			headerRow.addView(gainHeader);
			headerRow.addView(allocHeader);
			
			ArrayList<Stock> stockList = new ArrayList<Stock>();
			
			//get the prices for each of the stocks and store information in stock objects
			for (int i = 0; i < stock_list.size(); i++) {
				String tempURI = getURI(stock_list.get(i));
				Stock tempStock = getStock(tempURI);
				stockList.add(tempStock);
			}

			totalValue = 0;
			
			//calculate the total value in the portfolio by incrementing the totalValue by the shareprice * shares
			for (int j = 0; j < stock_list.size(); j++) {
				double shares = db.getQtyShares(stockList.get(j).getTicker());
				double value = stockList.get(j).price * shares;
				totalValue += value;
			}
			
			// for each row get the prices, gains and allocations and add them to the table
			for (int i = 0; i < stock_list.size(); i++) {
				
				//create a new row
				TableRow row = new TableRow(this);
				
				//tv represents the ticker textview
				TextView tv = new TextView(this);

				// set formatting for each of the text views
				tv.setLayoutParams(new LayoutParams(width / 4,
						LayoutParams.WRAP_CONTENT));
				tv.setGravity(Gravity.CENTER_VERTICAL);
				
				//tv2 represents the price of the ticker
				TextView tv2 = new TextView(this);
				tv2.setLayoutParams(new LayoutParams(width / 4,
						LayoutParams.WRAP_CONTENT));
				tv2.setGravity(Gravity.CENTER_VERTICAL);
				tv2.setGravity(Gravity.RIGHT);
				
				//tv3 represents the gains made
				TextView tv3 = new TextView(this);
				tv3.setLayoutParams(new LayoutParams(width / 4,
						LayoutParams.WRAP_CONTENT));
				tv3.setGravity(Gravity.CENTER);
				
				//tv4 represents the allocation of assets
				TextView tv4 = new TextView(this);
				tv4.setLayoutParams(new LayoutParams(width / 4,
						LayoutParams.WRAP_CONTENT));
				tv4.setGravity(Gravity.CENTER);

				// set the colors of each textview
				tv.setTextColor(getResources().getColor(R.color.colorBlack));
				tv2.setTextColor(getResources().getColor(R.color.colorBlack));

				// calculate the value of each stock in the portfolio and subsequently the allocation to the rest of the 
				//portfolio. Get the purchase price of the stock and then calculate the gain
				double pp = db.getPurchasePrice(stockList.get(i).getTicker());
				double share = db.getQtyShares(stockList.get(i).getTicker());
				double val = stockList.get(i).price * share;
				double tempAlloc = val / totalValue;
				
				//calculate the gain of each stock using the purchase price and the current price
				double gain = (stockList.get(i).getPrice() / pp - 1);
				
				//set colors for the gains
				if (gain < 0) {
					tv3.setTextColor(getResources().getColor(R.color.colorRed));
				} else
					tv3.setTextColor(getResources()
							.getColor(R.color.colorGreen));

				tv4.setTextColor(getResources().getColor(R.color.colorBlack));
				// set the text size of each of the views
				tv.setTextSize(20);
				tv2.setTextSize(15);
				tv3.setTextSize(15);
				tv4.setTextSize(15);

				// make every other line blue
				if (i % 2 != 0) {
					row.setBackgroundResource(R.color.colorLightBlue);
				}

				// format each of the numbers
				tv.setText(stock_list.get(i));
				tv2.setText(customFormat("$###,###,###.##",
						Double.toString(stockList.get(i).getPrice())));
				if (pp == 0) {
					tv3.setText("NA");
				} else {
					tv3.setText(customFormat("###.#%", Double.toString(gain)));
				}

				if (Double.isNaN(tempAlloc)) {
					tv4.setText("NA");
				} else {
					tv4.setText(customFormat("###.#%",
							Double.toString(tempAlloc)));
				}

				// add the rows to the layout and add the views to the rows
				tl.addView(row);
				row.addView(tv);
				row.addView(tv2);
				row.addView(tv3);
				row.addView(tv4);

			}
			

		} catch (Exception e) {
			//handle the exception -> usually fires when not connected to the internet and can't get the stock information
			Toast.makeText(this,
					"Not connected to the internet. Cannot display stocks.",
					Toast.LENGTH_LONG).show();
		}
	}

	private CharSequence customFormat(String pattern, String value) {
		// format the string value according to the pattern indicated
		DecimalFormat formatter = new DecimalFormat(pattern);
		String output = formatter.format(Double.parseDouble(value));
		return output;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//close the database once the activity is destroyed
		db.close();
	}

	public boolean stockExistsInDB(String ticker) {
		
		//go through the tickers and check to see if the parameter ticker is in the database
		//if it is return true, else return false
		ArrayList<String> tempList = db.getStocks();
		for (int i = 0; i < tempList.size(); i++) {
			if (tempList.get(i).equals(ticker)) {
				return true;
			}
		}
		return false;
	}

	public boolean stockExists(String ticker) {
		
		//check to see if the stock exists in the market -> if it does then return true, else if the price ==0 then return false
		String uri = getURI(ticker);
		Stock tempStock = getStock(uri);
		if (tempStock.price == 0) {
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// Do different things with different buttons
		switch (v.getId()) {
		case R.id.editText1:
			editText.setText("");
			break;
		case R.id.addButton: //close the keyboard when click the add button
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			
			//get the ticker that was added to the edit text element
			tl = (TableLayout) findViewById(R.id.tableLayout1);
			String ticker = editText.getText().toString();
			ticker = ticker.toUpperCase();

			// check if the stock exists in the database or in real life
			boolean tempCheck = stockExistsInDB(ticker);
			boolean tickCheck = stockExists(ticker);
			if (tempCheck == true || tickCheck == false) {
				Toast.makeText(
						this,
						"This ticker already exists in the database or does not exist on the market. Did not insert stock into tracker.",
						Toast.LENGTH_LONG).show();
			}

			else {
				db.insertTicker(ticker); //insert the ticker into the database
				String uri = getURI(ticker);
				Stock s = getStock(uri);
				s.setTicker(ticker);
				finish();
				Intent i = new Intent(this, Trackfolio.class); //refresh the screen

				startActivity(i);
			}

			break;

		case R.id.editButton:

			Intent intent = new Intent(this, editList.class);
			//start new activity -> when returned refresh the screen in onActivityResult(requestcode, resultcode, data);
			startActivityForResult(intent, 0);
			break;

		default:
			// do nothing
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data){
		super.onActivityResult(requestcode, resultcode, data);
		displayList();
	}

	public String getURI(String stock) {
		
		//build the string to get data from yahoo
		StringBuilder uri = new StringBuilder();
		uri.append("http://download.finance.yahoo.com/d/quotes.csv");
		uri.append("?s=").append(stock);
		uri.append("&f=sl1p2");
		return uri.toString();
	}

	public Stock getStock(String uri) {

		Stock stock = new Stock();
		try {

			// need to call yahoo api and get csv -> parse csv for most recent
			// price and price change
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse response = httpClient.execute(httpGet, localContext);
			String result = "";

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = reader.readLine()) != null) {

				result += line + ",";
				String[] RowData = result.split(",");

				String name = RowData[0];
				String price = RowData[1];
				String change = RowData[2];

				name = name.replaceAll("\"", "");
				change = change.replace("\"", "");

				stock.setPrice(Double.parseDouble(price));
				stock.setTicker(name);
				stock.setChange(change);

			}

		} catch (Exception e) {
			// write exception handler

		}
		return stock;
	}
}
