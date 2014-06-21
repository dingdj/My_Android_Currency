/**
 * @author dingdj
 * Date:2014-6-19上午10:20:47
 *
 */
package com.ddj.mycurrency.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author dingdj Date:2014-6-19上午10:20:47
 * 
 */
public class CurrencyDatabaseHelper extends SQLiteOpenHelper {

	public final static String DATABASE_NAME = "currency";
	
	public final static String TABLE_NAME = "CURRENCY";

	public final static int DATABASE_VERSION = 1;
	
	public final static String ID_COLUMN_NAME = "ID";
	
	public final static String CURRENCY_TYPE_COLUMN_NAME = "CURRENCY_TYPE";
	
	public final static String BUY_RATE_COLUMN_NAME = "BUY_RATE";
	
	public final static String BUY_DATE_COLUMN_NAME = "BUY_DATE";
	
	public final static String TYPE_COLUMN_NAME = "TYPE";
	

	public String createTableSql = "CREATE TABLE "+TABLE_NAME+" ("+ ID_COLUMN_NAME+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CURRENCY_TYPE_COLUMN_NAME+" TEXT, "+BUY_RATE_COLUMN_NAME +" REAL, "+BUY_DATE_COLUMN_NAME+" TEXT, "+TYPE_COLUMN_NAME+" INTEGER)";

	public final static String deleteCurrencySql = "DELETE From " + TABLE_NAME + " where " + ID_COLUMN_NAME + " = ";
	
	
	private static CurrencyDatabaseHelper mInstance = null;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private CurrencyDatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public static synchronized CurrencyDatabaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new CurrencyDatabaseHelper(context);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * 删除数据库
	 * 
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase(DATABASE_NAME);
	}

}
