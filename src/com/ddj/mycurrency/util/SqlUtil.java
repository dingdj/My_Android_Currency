package com.ddj.mycurrency.util;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 
 * @author dingdj
 *
 */
public class SqlUtil {
	
	/**
	 * 获得插入sql
	 * @param tableName
	 * @param map
	 * @return
	 */
	public static String getInsertSql(String tableName, LinkedHashMap<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("insert into ").append(tableName).append(" (");
		Set<String> set = map.keySet();
		int length = set.size();
		for (String key : set) {
			length--;
			builder.append(key);
			if(length != 0){
				builder.append(",");
			}else{
				builder.append(")");
			}
		}
		
		
		
		builder.append(" VALUES(");
		
		length = set.size();
		for (String key : set) {
			length--;
			Object obj = map.get(key);
			if(obj instanceof String){
				builder.append("\"").append(obj).append("\"");
				if(length != 0){
					builder.append(",");
				}else{
					builder.append(")");
				}
			}else{
				builder.append(obj);
				if(length != 0){
					builder.append(",");
				}else{
					builder.append(")");
				}
			}
			
		}
		return builder.toString();
	}
	
	/**
	 * 获得更新语句
	 * @param tableName
	 * @param map
	 * @param whereMap
	 * @return
	 */
	public static String getUpdateSql(String tableName, LinkedHashMap<String, Object> map, LinkedHashMap<String, Object> whereMap ){
		StringBuilder builder = new StringBuilder();
		builder.append("update ").append(tableName).append(" set ");
		Set<String> set = map.keySet();
		int length = set.size();
		for (String key : set) {
			length--;
			Object obj = map.get(key);
			builder.append(key).append(" = ");
			if(obj instanceof String){
				builder.append("\"").append(obj).append("\"");
				if(length != 0){
					builder.append(",");
				}
			}else{
				builder.append(obj);
				if(length != 0){
					builder.append(",");
				}
			}
		}
		
		set = whereMap.keySet();
		length = set.size();
		builder.append(" where ");
		for (String key : set) {
			length--;
			Object obj = whereMap.get(key);
			builder.append(key).append(" = ");
			if(obj instanceof String){
				builder.append("\"").append(obj).append("\"");
				if(length != 0){
					builder.append(" and ");
				}
			}else{
				builder.append(obj);
				if(length != 0){
					builder.append(" and ");
				}
			}
		}
		
		return builder.toString();
	}
	
	
	
	public static void main(String[] args){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("buy_rate", "623.113");
		map.put("buy_date", System.currentTimeMillis()+"");
		map.put("type", 1);
		
		LinkedHashMap<String, Object> whereMap = new LinkedHashMap<String, Object>();
		whereMap.put("id", 1);
		whereMap.put("currency_type", "USD");
		
		System.out.println(getUpdateSql("currency", map, whereMap));
	}

}
