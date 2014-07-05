/**
 * @author dingdj
 * Date:2014-6-18下午4:35:57
 *
 */
package com.ddj.mycurrency;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ddj.commonkit.StringUtils;
import com.ddj.commonkit.android.system.SystemEnvUtil;
import com.ddj.mycurrency.database.DatabaseManager;
import com.ddj.mycurrency.model.Currency;
import com.ddj.mycurrency.model.FavoriteCurrency;
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
				
				//价格超过阈值 发出通知 发送提醒消息到通知栏
				List<FavoriteCurrency> currencys = DatabaseManager
						.getList(GetCurrencyService.this);
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				for (FavoriteCurrency favoriteCurrency : currencys) {
					try {

						String currencyType = favoriteCurrency
								.getCurrencyType();
						int type = favoriteCurrency.getType();
						double buyRate = Double.parseDouble(favoriteCurrency
								.getBuyRate());
						CurrencyApplication application = (CurrencyApplication) GetCurrencyService.this
								.getApplication();
						Currency currency = application.currencyMap
								.get(currencyType);

						if (type == FavoriteCurrency.BUY_TYPE) {
							if ((Double.parseDouble(currency.getBuyRate()) - buyRate) > 0) {
								Intent intent = new Intent(
										GetCurrencyService.this,
										MainActivity.class);
								PendingIntent pi = PendingIntent.getActivity(
										GetCurrencyService.this, 0, intent, 0);
								NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
										GetCurrencyService.this);
								mBuilder.setContentTitle("卖出")
										// 设置通知栏标题
										.setContentText(
												Constant.toHumanRead
														.get(currencyType)
														+ "价格："
														+ currency.getBuyRate()
														+ " 可卖出")
										.setContentIntent(pi) // 设置通知栏点击意图
										.setTicker("卖出") // 通知首次出现在通知栏，带上升动画效果的
										.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
										.setPriority(
												Notification.PRIORITY_DEFAULT) // 设置该通知优先级
										// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
										.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
										.setDefaults(
												Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
										// Notification.DEFAULT_ALL
										// Notification.DEFAULT_SOUND 添加声音 //
										// requires VIBRATE permission
										.setSmallIcon(R.drawable.ic_launcher);// 设置通知小ICON
								nm.notify(1, mBuilder.build());
							}
						} else if (type == FavoriteCurrency.FAVORITE_TYPE) {
							if (Double.parseDouble(currency.getSaleRate())
									- buyRate <= 0) {
								Intent intent = new Intent(
										GetCurrencyService.this,
										MainActivity.class);
								PendingIntent pi = PendingIntent.getActivity(
										GetCurrencyService.this, 0, intent, 0);
								NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
										GetCurrencyService.this);
								mBuilder.setContentTitle("买入")
										// 设置通知栏标题
										.setContentText(
												Constant.toHumanRead
														.get(currencyType)
														+ "价格："
														+ currency
																.getSaleRate()
														+ " 可买入")
										.setContentIntent(pi) // 设置通知栏点击意图
										.setTicker("买入") // 通知首次出现在通知栏，带上升动画效果的
										.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
										.setPriority(
												Notification.PRIORITY_DEFAULT) // 设置该通知优先级
										// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
										.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
										.setDefaults(
												Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
										// Notification.DEFAULT_ALL
										// Notification.DEFAULT_SOUND 添加声音 //
										// requires VIBRATE permission
										.setSmallIcon(R.drawable.ic_launcher);// 设置通知小ICON
								nm.notify(favoriteCurrency.getId(),
										mBuilder.build());
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 发送一个空消息 进行下次的请求
				handler.sendEmptyMessage(0);
			}
		};
		
		//注册网络变化广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		
		this.registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				if(SystemEnvUtil.isNetworkAvailable(context)){
					StringRequest stringRequest = new StringRequest(Constant.URL, listener, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("TAG", error.getMessage(), error);
						}
					});
					CurrencyApplication.application.requestQueue.add(stringRequest);
				}
			}
		}, intentFilter);

	}
	
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				Log.e("GetCurrencyService", "onStartCommand");
				StringRequest stringRequest = new StringRequest(Constant.URL, listener, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				});
				CurrencyApplication.application.requestQueue.add(stringRequest);
			}

		});
		return super.onStartCommand(intent, flags, startId);
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
