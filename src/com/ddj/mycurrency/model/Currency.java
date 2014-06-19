/**
 * @author dingdj
 * Date:2014-6-18下午4:18:34
 *
 */
package com.ddj.mycurrency.model;

/**
 * @author dingdj
 * Date:2014-6-18下午4:18:34
 *
 */
public class Currency {
	
	private String id;
	
	private String buyRate;
	
	private String cashBuyRate;
	
	private String saleRate;
	
	private String updateTimeStr;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the updateTimeStr
	 */
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	/**
	 * @param updateTimeStr the updateTimeStr to set
	 */
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
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
	 * @return the cashBuyRate
	 */
	public String getCashBuyRate() {
		return cashBuyRate;
	}

	/**
	 * @param cashBuyRate the cashBuyRate to set
	 */
	public void setCashBuyRate(String cashBuyRate) {
		this.cashBuyRate = cashBuyRate;
	}

	/**
	 * @return the saleRate
	 */
	public String getSaleRate() {
		return saleRate;
	}

	/**
	 * @param saleRate the saleRate to set
	 */
	public void setSaleRate(String saleRate) {
		this.saleRate = saleRate;
	}
	
	
	

}
