/**
 * @author dingdj
 * Date:2014-6-18下午4:35:57
 *
 */
package com.ddj.mycurrency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ddj.commonkit.StringUtils;
import com.ddj.mycurrency.model.Currency;
import com.ddj.mycurrency.util.Constant;
import com.ddj.mycurrency.util.DeEncryptUtil;

/**
 * @author dingdj Date:2014-6-18下午4:35:57
 * 
 */
public class GetCurrencyService extends Service {

	boolean flag = false;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					StringRequest stringRequest = new StringRequest(Constant.URL, listener, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("TAG", error.getMessage(), error);
						}
					});
					CurrencyApplication.application.requestQueue.add(stringRequest);
				}

			}, queryTimeIntervalInSecond * 1000);
		}

	};

	// 请求时间间隔
	private long queryTimeIntervalInSecond = 3600;
	
	Response.Listener<String> listener;

	@Override
	public void onCreate() {
		super.onCreate();
		// 常驻进程
		if (!flag) {
			startForeground(0XFFFF, new Notification());
			flag = true;
		}
		
		listener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				String content = DeEncryptUtil.deEncry(response);
				// 解析
				try {
					JSONArray jsonArray = new JSONArray(content);
					int length = jsonArray.length();
					Currency[] currencys = new Currency[length];
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						jsonObject = (JSONObject) jsonObject.get("Currency");
						Currency currency = new Currency();
						String id = jsonObject.getString("id");
						currency.setId(id);
						
						Log.e("GetCurrencyService", currency.getId());
						currency.setBuyRate(jsonObject.getString("buy_rate"));
						currency.setCashBuyRate(jsonObject.getString("cash_buy_rate"));
						currency.setSaleRate(jsonObject.getString("sell_rate"));
						currency.setUpdateTimeStr(jsonObject.getString("updated"));
						currencys[i] = currency;
						
						CurrencyApplication.application.currencyMap.put(currency.getId(), currency);
					}
					CurrencyApplication.application.currencys = currencys;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 通知有内容更新
				CurrencyApplication.application.dispatchNotify();
				
				// 发送一个空消息 进行下次的请求
				handler.sendEmptyMessage(0);
			}
		};
		handler.post(new Runnable() {

			@Override
			public void run() {
				StringRequest stringRequest = new StringRequest(Constant.URL, listener, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				});
				CurrencyApplication.application.requestQueue.add(stringRequest);
			}

		});

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
