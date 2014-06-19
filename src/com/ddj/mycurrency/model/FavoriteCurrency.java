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
	
	//数据库中的唯一ID
	private int id;
	
	//币种
	private String currencyType;
	
	//买入的价格
	private String buyRate;
	
	//买入的时间
	private String buyTime;
	
	
	

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
	

	
	
}
