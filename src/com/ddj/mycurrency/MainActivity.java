package com.ddj.mycurrency;

import com.ddj.commonkit.android.system.SystemEnvUtil;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TabHost mTabHost = getTabHost();
		mTabHost.addTab(mTabHost.newTabSpec("allCurrency").setIndicator("所有汇率").setContent(new Intent(this, AllCurrencyActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("myCurrency").setIndicator("自选汇率").setContent(new Intent(this, FavoriteCurrencyActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("calcCurrency").setIndicator("保证金计算").setContent(new Intent(this, CalcCurrencyActivity.class)));
		mTabHost.setCurrentTab(0);
		
		if(!SystemEnvUtil.isNetworkAvailable(getBaseContext())){
			Toast.makeText(getBaseContext(), "请检查你的网络状态..", 2000).show();
		}
	}
}
