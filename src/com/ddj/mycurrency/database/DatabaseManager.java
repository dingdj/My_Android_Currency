/**
 * @author dingdj
 * Date:2014-6-19上午10:36:10
 *
 */
package com.ddj.mycurrency.database;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ddj.mycurrency.model.FavoriteCurrency;
import com.ddj.mycurrency.util.SqlUtil;

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
                String buyRateStr = cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.BUY_RATE_COLUMN_NAME));
             
                double buyRate = Double.parseDouble(buyRateStr);
                String buyDate = cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.BUY_DATE_COLUMN_NAME));
                int type = cursor.getInt(cursor.getColumnIndex(CurrencyDatabaseHelper.TYPE_COLUMN_NAME));
                
                FavoriteCurrency favoriteCurrency = new FavoriteCurrency();
                favoriteCurrency.setId(id);
                favoriteCurrency.setBuyRate(buyRate+"");
                favoriteCurrency.setBuyTime(buyDate);
                favoriteCurrency.setType(type);
                favoriteCurrency.setCurrencyType(currencyType);
                
                favoriteCurrencies.add(favoriteCurrency);
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
	
	/**
	 * 增加或删除类型
	 * @param currency
	 */
	public static void addOrUpdateFavoriteCurrency(Context mContext, FavoriteCurrency currency){
		CurrencyDatabaseHelper mDbHelper = null;
		if(currency.getId() == FavoriteCurrency.UNKNOWID){//新增
			try{
				SQLiteDatabase db = getDatabaseHelper(mContext).getWritableDatabase();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put(CurrencyDatabaseHelper.CURRENCY_TYPE_COLUMN_NAME, currency.getCurrencyType());
				map.put(CurrencyDatabaseHelper.BUY_RATE_COLUMN_NAME, Double.parseDouble(currency.getBuyRate()));
				map.put(CurrencyDatabaseHelper.BUY_DATE_COLUMN_NAME, System.currentTimeMillis());
				map.put(CurrencyDatabaseHelper.TYPE_COLUMN_NAME, currency.getType());
				db.execSQL(SqlUtil.getInsertSql(CurrencyDatabaseHelper.TABLE_NAME, map));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(mDbHelper != null){
					mDbHelper.close();
				}
			}
		}else{//更新
			try{
				SQLiteDatabase db = getDatabaseHelper(mContext).getWritableDatabase();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put(CurrencyDatabaseHelper.CURRENCY_TYPE_COLUMN_NAME, currency.getCurrencyType());
				map.put(CurrencyDatabaseHelper.BUY_RATE_COLUMN_NAME, Double.parseDouble(currency.getBuyRate()));
				map.put(CurrencyDatabaseHelper.BUY_DATE_COLUMN_NAME, System.currentTimeMillis());
				
				
				LinkedHashMap<String, Object> whereMap = new LinkedHashMap<String, Object>();
				whereMap.put(CurrencyDatabaseHelper.ID_COLUMN_NAME, currency.getId());
				
				db.execSQL(SqlUtil.getUpdateSql(CurrencyDatabaseHelper.TABLE_NAME, map, whereMap));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(mDbHelper != null){
					mDbHelper.close();
				}
			}
		}
	}
	
	/**
	 * 删除指定的类型
	 * @param id
	 */
	public static void deleteCurrency(Context mContext, int id){
		CurrencyDatabaseHelper mDbHelper = null;
		try{
			SQLiteDatabase db = getDatabaseHelper(mContext).getWritableDatabase();
			db.execSQL(CurrencyDatabaseHelper.deleteCurrencySql + id);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(mDbHelper != null){
				mDbHelper.close();
			}
		}
	}

}
