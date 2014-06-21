/**
 * @author dingdj
 * Date:2014-6-17上午11:36:56
 *
 */
package com.ddj.mycurrency.util;

import java.util.HashMap;

/**
 * @author dingdj
 * Date:2014-6-17上午11:36:56
 *
 */
public class Constant {
	
	public static final String URL = "http://s17.currency.procstel.net/api/3/icbc_currency_widget/v1//gzip:true";
	public static final String KEY = "YOURLONGSTRINGABCDEFGABCDEFG1234";
	
	public static final String NEW_CONTENT_NOTIFY = "NEW_CONTENT_NOTIFY";
	
	public static final HashMap<String, String> toHumanRead = new HashMap<String, String>();
	public static final HashMap<String, String> toHumanReadReverse = new HashMap<String, String>();
	
	static{
		toHumanRead.put("AUD", "澳大利亚元");
		toHumanRead.put("CAD", "加拿大元");
		toHumanRead.put("CHF", "瑞士法郎");
		toHumanRead.put("DKK", "丹麦克朗");
		toHumanRead.put("EUR", "欧元");
		toHumanRead.put("GBP", "英镑");
		toHumanRead.put("HKD", "港币");
		toHumanRead.put("JPY", "日元");
		toHumanRead.put("KRW", "韩国元");
		toHumanRead.put("MOP", "澳门元");
		toHumanRead.put("NOK", "挪威克朗");
		toHumanRead.put("NZD", "新西兰元");
		toHumanRead.put("PHP", "菲律宾比索");
		toHumanRead.put("SEK", "瑞典克朗");
		toHumanRead.put("SGD", "新加坡元");
		toHumanRead.put("THB", "泰国铢");
		toHumanRead.put("USD", "美元");
		toHumanRead.put("RUB", "俄罗斯卢布");
		
		for(String str : toHumanRead.keySet()){
			toHumanReadReverse.put(toHumanRead.get(str), str);
		}
	}

}
