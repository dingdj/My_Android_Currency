package com.ddj.mycurrency;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TabHost mTabHost = getTabHost();
		mTabHost.addTab(mTabHost.newTabSpec("myCurrency").setIndicator("我的汇率").setContent(new Intent(this, FavoriteCurrencyActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("allCurrency").setIndicator("所有汇率").setContent(new Intent(this, AllCurrencyActivity.class)));
		mTabHost.setCurrentTab(0);
	}
}
