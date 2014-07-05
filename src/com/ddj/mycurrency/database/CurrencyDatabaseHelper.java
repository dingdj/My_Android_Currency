/**
 * @author dingdj
 * Date:2014-6-19上午10:20:47
 *
 */
package com.ddj.mycurrency.database;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ddj.commonkit.FileUtil;
import com.ddj.mycurrency.util.BackupUtil;

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

	public final static String SELECT = "select * from " + DATABASE_NAME;
	
	public final static String DELETE = "delete from " + DATABASE_NAME;

	public String createTableSql = "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CURRENCY_TYPE_COLUMN_NAME + " TEXT, " + BUY_RATE_COLUMN_NAME + " REAL, " + BUY_DATE_COLUMN_NAME + " TEXT, " + TYPE_COLUMN_NAME
			+ " INTEGER)";

	public final static String deleteCurrencySql = "DELETE From " + TABLE_NAME + " where " + ID_COLUMN_NAME + " = ";

	private final static String INSERT = "insert into currency(ID, CURRENCY_TYPE, BUY_RATE, BUY_DATE, TYPE) values(%d, '%s', %f, '%s', %d)";
	
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

	/**
	 * 备份
	 * @author dingdj
	 * Date:2014-7-5上午9:40:46
	 *  @param ctx
	 */
	public static void backupCurrencyTable(Context ctx) {
		FileUtil.delFile(BackupUtil.BACK_UP_FILE);// 删除之前可能已经存在的备份文件
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		CurrencyDatabaseHelper helper = CurrencyDatabaseHelper.getInstance(ctx);
		Cursor cursor = null;
		try {
			cursor = DataBaseOperation.query(helper.getReadableDatabase(), SELECT);
			boolean flag = cursor.moveToNext();
			if (!flag || cursor.getCount() == 0)
				return;
			JSONArray total = new JSONArray();
			while (flag) {
				JSONObject obj = new JSONObject();
				obj.put(ID_COLUMN_NAME, cursor.getInt(0));
				obj.put(CURRENCY_TYPE_COLUMN_NAME, cursor.getString(1));
				obj.put(BUY_RATE_COLUMN_NAME, cursor.getString(2));
				obj.put(BUY_DATE_COLUMN_NAME, cursor.getString(3));
				obj.put(TYPE_COLUMN_NAME, cursor.getInt(4));
				total.put(obj);
				flag = cursor.moveToNext();
			}

			if (0 == total.length())
				return;

			String jsonString = total.toString();
			FileUtil.writeFile(BackupUtil.BACK_UP_FILE, jsonString, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (helper != null) {
				helper.close();
			}
			// 关闭流
			try {
				if (null != bis)
					bis.close();
				if (null != fis)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 恢复备份
	 * @author dingdj
	 * Date:2014-7-5上午9:42:10
	 *  @param ctx
	 */
	public static void restoreAppTable(Context ctx) {
		if (!FileUtil.isFileExits(BackupUtil.BACK_UP_FILE))
			return;
		// 解析json
		String jsonString = FileUtil.getStringFromFileContent(BackupUtil.BACK_UP_FILE);
		if (null == jsonString) {
			FileUtil.delFile(BackupUtil.BACK_UP_FILE);// 放在最后
			return;
		}
		CurrencyDatabaseHelper helper = CurrencyDatabaseHelper.getInstance(ctx);
		try {
			JSONTokener jsonParser = new JSONTokener(jsonString);
			JSONArray array = (JSONArray) jsonParser.nextValue();
			if (null == array || array.length() == 0)
				return;

			String[] sqls = new String[array.length()+1];
			// 清空AppTable表的数据 delete from AppTable
			sqls[0] = DELETE;
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = (JSONObject) array.get(i);
				int ID = obj.getInt(ID_COLUMN_NAME);
				String currency_type = obj.getString(CURRENCY_TYPE_COLUMN_NAME);
				double buy_rate = obj.getDouble(BUY_RATE_COLUMN_NAME);
				String buy_date = obj.getString(BUY_DATE_COLUMN_NAME);
				int type = obj.getInt(TYPE_COLUMN_NAME);

				sqls[i+1] = String.format(INSERT, ID, currency_type, buy_rate, buy_date, type);
			}
			// 插入数据库
			DataBaseOperation.execBatchSQL(helper.getWritableDatabase(), sqls, true);
		} catch (Exception e) {
			FileUtil.delFile(BackupUtil.BACK_UP_FILE);// 删除失效的备份文件
			e.printStackTrace();
		} finally {
			if (null != helper)
				helper.close();
		}
	}
	
	
}
