/**
 * @author dingdj
 * Date:2014-6-18下午5:07:02
 *
 */
package com.ddj.mycurrency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ddj.mycurrency.model.Currency;
import com.ddj.mycurrency.notify.INotify;
import com.ddj.mycurrency.util.BackupUtil;

/**
 * @author dingdj Date:2014-6-18下午5:07:02
 * 
 */
public class CurrencyApplication extends Application {

	public RequestQueue requestQueue;

	public static CurrencyApplication application;

	public Currency[] currencys;

	public HashMap<String, Currency> currencyMap = new HashMap<String, Currency>();

	public List<INotify> notifys = new ArrayList<INotify>();

	@Override
	public void onCreate() {
		super.onCreate();
		requestQueue = Volley.newRequestQueue(this);
		application = this;
		
		Intent intent = new Intent();
		intent.setClassName(this, GetCurrencyService.class.getName());
		startService(intent);
		BackupUtil.createBackupDir();
	}

	public void registerNotify(INotify notify) {
		if (notify != null)
			notifys.add(notify);
	}

	public void unRegisterNotify(INotify notify) {
		if (notify != null)
			notifys.remove(notify);
	}

	public void dispatchNotify() {
		for (INotify notify : notifys) {
			notify.nofityDataChange();
		}
	}

}
