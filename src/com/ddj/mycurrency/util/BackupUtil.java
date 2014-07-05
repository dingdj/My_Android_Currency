/**
 * @author dingdj
 * Date:2014-7-5上午8:41:45
 *
 */
package com.ddj.mycurrency.util;

import java.io.File;

import com.ddj.commonkit.DateUtil;

import android.os.Environment;

/**
 * 备份还原
 * @author dingdj
 * Date:2014-7-5上午8:41:45
 *
 */
public class BackupUtil {
	
	
	private static final String BACK_UP_PATH = Environment.getExternalStorageDirectory() + "/MY_CURRENCY/";
	public static final String BACK_UP_FILE = BACK_UP_PATH + "currency";
	
	/**
	 * 是否存在备份数据
	 * @author dingdj
	 * Date:2014-7-5上午8:42:29
	 *  @return
	 */
	public static boolean isExistBackUpFile(){
		File file = new File(BACK_UP_FILE);
		return file.exists();
	}

	/**
	 * 获取备份文件时间
	 * @author dingdj
	 * Date:2014-7-5上午8:52:02
	 *  @return
	 */
	public static String getBackUpFileDate(){
		File file = new File(BACK_UP_FILE);
		if(file.exists()){
			return DateUtil.parseLongToTime(file.lastModified(), "yyyy-MM-dd HH:mm:ss");
		}else{
			return "";
		}
	}
	
	
	/**
	 * 创建备份目录
	 * @author dingdj
	 * Date:2014-7-5上午8:46:29
	 */
	public static void createBackupDir(){
		try{
			File file = new File(BACK_UP_PATH);
			if(!file.exists()){
				file.mkdir();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
