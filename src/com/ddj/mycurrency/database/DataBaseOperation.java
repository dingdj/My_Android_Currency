/**
 * @author dingdj
 * Date:2014-7-5上午9:02:18
 *
 */
package com.ddj.mycurrency.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author dingdj Date:2014-7-5上午9:02:18
 * 
 */
public class DataBaseOperation {
	static final String TAG = "DataBaseOperation";


	/**
	 * 关闭数据库连接。
	 */
	public static boolean close(SQLiteOpenHelper helper, SQLiteDatabase mDb) {
		try {
			helper.close();
			if (mDb != null) {
				mDb.close();
			}
			return true;
		} catch (SQLException s) {
			Log.e(TAG, "close db error" + s.toString());
			return false;
		}
	}

	/**
	 * 查询数据。
	 */
	public static Cursor query(SQLiteDatabase mDb, String sql) {
		return mDb.rawQuery(sql, null);
	}

	/**
	 * 查询数据
	 */
	public static Cursor query(SQLiteDatabase mDb, String sql, String[] selectionArgs) {
		return mDb.rawQuery(sql, selectionArgs);
	}

	/**
	 * 
	 * @Title: query
	 * @Description: 查询单个条件
	 * @author linyt
	 * @date 2012-8-8
	 * @param table
	 *            表名
	 * @param key
	 *            查询字段
	 * @param value
	 *            查询字段值
	 * @param sort
	 *            排序
	 * @return
	 */
	public static Cursor query(SQLiteDatabase mDb, String table, String key, String value, String sort) {
		Cursor cursor = mDb.query(table, null, key + "=?", new String[] { value }, null, null, sort);
		return cursor;
	}

	/**
	 * 
	 * @Title: query
	 * @Description: 查询给定表所有数据
	 * @author linyt
	 * @date 2012-8-8
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public static Cursor query(SQLiteDatabase mDb, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		Cursor cursor = mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}

	/**
	 * 修改数据。 cfb add
	 */
	public static int update(SQLiteDatabase mDb, String table, ContentValues values, String whereClause, String[] whereArgs) {
		return mDb.update(table, values, whereClause, whereArgs);
	}

	/**
	 * 增加数据。 cfb add
	 */
	public static long insertOrThrow(SQLiteDatabase mDb, String table, String nullColumnHack, ContentValues values) {
		return mDb.insertOrThrow(table, nullColumnHack, values);
	}

	/**
	 * 执行SQL语句。
	 */
	public static boolean execSQL(SQLiteDatabase mDb, String sql) {
		try {
			mDb.execSQL(sql);
			return true;
		} catch (SQLException s) {
			Log.e(TAG, "execSQL:" + sql + " ex:" + s.toString());
			return false;
		}
	}

	/**
	 * 执行SQL语句。
	 */
	public static boolean delete(SQLiteDatabase mDb, String table) {
		try {
			mDb.delete(table, null, null);
			return true;
		} catch (SQLException s) {
			Log.e(TAG, "ERROR when delete " + table);
			return false;
		}
	}

	/**
	 * 执行SQL语句。
	 */
	public static boolean delete(SQLiteDatabase mDb, String table, String where, String[] whereValue) {
		try {
			mDb.delete(table, where, whereValue);
			return true;
		} catch (SQLException s) {
			Log.e(TAG, "ERROR when delete " + table);
			return false;
		}
	}


	/**
	 * 执行SQL语句。
	 */
	public static boolean execSQL(SQLiteDatabase mDb, String sql, Object[] obj) {
		try {
			mDb.execSQL(sql, obj);
			return true;
		} catch (SQLException s) {
			Log.e(TAG, "execSQL:" + sql + " ex:" + s.toString());
			return false;
		}
	}

	/**
	 * 批量执行sql语句。
	 */
	public static boolean execBatchSQL(SQLiteDatabase mDb, String[] sqls, boolean transaction) {
		if (transaction) {
			mDb.beginTransaction();
		}

		for (String sql : sqls) {
			try {
				if (sql != null) {
					mDb.execSQL(sql);
				}
			} catch (Exception e) {
				Log.e(TAG, "execSQL:[" + sql + "] ex:" + e);
				if (transaction) {
					mDb.endTransaction();
				}
				return false;
			}
		}

		if (transaction) {
			mDb.setTransactionSuccessful();
			mDb.endTransaction();
		}

		return true;
	}

	/**
	 * 增加新的记录
	 * 
	 * @param table
	 * @param values
	 * @return
	 */
	public static long add(SQLiteDatabase mDb, String table, ContentValues values) {
		return mDb.insert(table, null, values);
	}

}
