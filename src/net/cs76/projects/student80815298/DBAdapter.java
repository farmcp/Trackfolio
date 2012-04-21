/*author: Christopher Farm
 * HUID: 80815298*/

package net.cs76.projects.student80815298;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	public static final String KEY_STOCKID = "_id";
	public static final String KEY_STOCKNAME = "stockName";
	public static final String KEY_PURCHASE_PRICE = "purchasePrice";
	public static final String KEY_QTY = "qtyStock";

	private static final String DB_NAME = "stock";
	private static final String DB_TABLE = "stocks";
	private static final int DB_VER = 1; // increment this monotonically
	private static final String DB_CREATE = "CREATE TABLE stocks"
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, stockName TEXT NOT NULL, qtyStock REAL, purchasePrice REAL);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		// Class constructor
		DatabaseHelper(Context c) {
			// instantiate a SQLiteOpenHelper 
			super(c, DB_NAME, null, DB_VER);
		}

		
		public void onCreate(SQLiteDatabase db) {
			// create the databse
			db.execSQL(DB_CREATE);
		}

		
		public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
			// writeover old database
			db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
			onCreate(db);
		}

	}

	// define variables to use in the class
	private final Context context;
	private DatabaseHelper helper;
	private SQLiteDatabase db;

	public DBAdapter(Context c) {
		this.context = c;
	}

	public DBAdapter open() throws SQLException {
		helper = new DatabaseHelper(context);
		
		//get the database
		db = helper.getWritableDatabase();

		return this;
	}

	//close the database
	public void close() {
		helper.close();
	}

	public long insertTicker(String s) {
		
		//insert the ticker into the database
		ContentValues val = new ContentValues();
		val.put(KEY_STOCKNAME, s);
		return db.insert(DB_TABLE, null, val);
	}

	public int udpateStockInfo(String stock, double qty, double purchasePrice) {
		//update the purchase price and the quantity held for a specific stock
		ContentValues val = new ContentValues();
		val.put(KEY_QTY, qty);
		val.put(KEY_PURCHASE_PRICE, purchasePrice);
		return db.update(DB_TABLE, val, KEY_STOCKNAME + "=?",
				new String[] { stock });

	}

	public int removeStock(String s) {
		//delete a row in the table
		return db.delete(DB_TABLE, KEY_STOCKNAME + "=?", new String[] { s });
	}

	public ArrayList<String> getStocks() {
		
		//return the list of tickers in the database
		ArrayList<String> stock_list = new ArrayList<String>();
		Cursor cursor = db.query(DB_TABLE, new String[] { KEY_STOCKNAME },
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			stock_list.add(cursor.getString(0));
			cursor.moveToNext();
		}
		return stock_list;
	}

	public double getPurchasePrice(String ticker) {
		//get a purchase price for a specific ticker
		double pprice = 0;
		Cursor cur = db
				.query(DB_TABLE, new String[] { KEY_PURCHASE_PRICE },
						KEY_STOCKNAME + "=?", new String[] { ticker }, null,
						null, null);
		cur.moveToFirst();
		pprice = cur.getDouble(0);
		return pprice;
	}
	
	public double getQtyShares(String ticker){
		//get the quantity for a specific ticker
		double shares = 0;
		Cursor c = db.query(DB_TABLE, new String[] {KEY_QTY}, KEY_STOCKNAME + "=?", new String[]{ticker}, null, null, null);
		c.moveToFirst();
		shares = c.getDouble(0);
		return shares;
	}

}
