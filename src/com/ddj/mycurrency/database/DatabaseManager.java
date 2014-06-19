/**
 * @author dingdj
 * Date:2014-6-19上午10:36:10
 *
 */
package com.ddj.mycurrency.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ddj.mycurrency.model.FavoriteCurrency;

/**
 * @author dingdj Date:2014-6-19上午10:36:10
 * 
 */
public class DatabaseManager {

	/**
	 * 获取到数据库的引用
	 * 
	 * @author dingdj Date:2014-6-19上午10:41:11
	 * @param mContext
	 * @return
	 */
	public static CurrencyDatabaseHelper getDatabaseHelper(Context mContext) {
		CurrencyDatabaseHelper mDbHelper = null;
		mDbHelper = CurrencyDatabaseHelper.getInstance(mContext);
		// 调用getReadableDatabase方法如果数据库不存在 则创建 如果存在则打开
		SQLiteDatabase mDb = null;
		mDb = mDbHelper.getReadableDatabase();
		return mDbHelper;
	}

	/**
	 * 获取数据库中的对象
	 * @author dingdj
	 * Date:2014-6-19上午10:53:02
	 *  @param mContext
	 *  @return
	 */
	public static List<FavoriteCurrency> getList(Context mContext) {
		List<FavoriteCurrency> favoriteCurrencies = new ArrayList<FavoriteCurrency>();
		CurrencyDatabaseHelper mDbHelper = null;
		try {
			SQLiteDatabase db = getDatabaseHelper(mContext).getReadableDatabase();
			Cursor cursor = db.query(CurrencyDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
			while(cursor.moveToNext()){  
                int id  = cursor.getInt(cursor.getColumnIndex(CurrencyDatabaseHelper.ID_COLUMN_NAME));  
                String currencyType = cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.CURRENCY_TYPE_COLUMN_NAME));  
                String buyRate = cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.BUY_RATE_COLUMN_NAME));
                String buyDate = cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.BUY_DATE_COLUMN_NAME));
                Log.e("DatabaseManager", "id："+id+" "+"currencyType："+currencyType+" "+"buyRate："+buyRate); 
            }  
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(mDbHelper != null){
				mDbHelper.close();
			}
		}
		return favoriteCurrencies;
	}

}
