/**
 * @author dingdj
 * Date:2014-6-19上午10:43:43
 *
 */
package com.ddj.mycurrency.model;

/**
 * @author dingdj
 * Date:2014-6-19上午10:43:43
 *
 */
public class FavoriteCurrency {
	
	public static final int UNKNOWID = -1;
	
	public static final int BUY_TYPE = 1;
	
	public static final int FAVORITE_TYPE = 0;
	
	
	//数据库中的唯一ID
	private int id = UNKNOWID;
	
	//币种
	private String currencyType;
	
	//买入的价格
	private String buyRate;
	
	//买入的时间
	private String buyTime;
	
	//type = 0 未买提醒
	//type = 1 已买提醒
	private int type;
	
	
	

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the currencyType
	 */
	public String getCurrencyType() {
		return currencyType;
	}

	/**
	 * @param currencyType the currencyType to set
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	/**
	 * @return the buyRate
	 */
	public String getBuyRate() {
		return buyRate;
	}

	/**
	 * @param buyRate the buyRate to set
	 */
	public void setBuyRate(String buyRate) {
		this.buyRate = buyRate;
	}

	/**
	 * @return the buyTime
	 */
	public String getBuyTime() {
		return buyTime;
	}

	/**
	 * @param buyTime the buyTime to set
	 */
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	

	
	
}
