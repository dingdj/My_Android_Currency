/**
 * @author dingdj
 * Date:2014-6-17上午11:20:14
 *
 */
package com.ddj.mycurrency;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddj.mycurrency.model.Currency;
import com.ddj.mycurrency.notify.INotify;

/**
 * @author dingdj Date:2014-6-17上午11:20:14
 * 
 */
public class AllCurrencyActivity extends ListActivity implements INotify {

	AllCurrencyAdapter adapter;

	LayoutInflater inflater;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setAdapter(new AllCurrencyAdapter());
		inflater = getLayoutInflater();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 进行一次界面刷新
		adapter = (AllCurrencyAdapter) getListView().getAdapter();
		if(CurrencyApplication.application.currencys != null){
			adapter.setCurrencys(CurrencyApplication.application.currencys);
			adapter.notifyDataSetChanged();
		}
		// 注册监听
		CurrencyApplication.application.registerNotify(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// 注销监听
		CurrencyApplication.application.unRegisterNotify(this);
	}

	class AllCurrencyAdapter extends BaseAdapter {

		Currency[] currencys;

		@Override
		public int getCount() {
			if (currencys != null) {
				return currencys.length;
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (currencys != null) {
				Currency currency = currencys[position];
				
				ImageView iconView;
				TextView bankBuyRate;
				TextView bankSaleRaTe;
				TextView currencyType;
				TextView updateTime;

				if (convertView == null) {
					convertView = inflater.inflate(R.layout.all_currency_layout, null);

					iconView = (ImageView) convertView.findViewById(R.id.imageView1);
					bankBuyRate = (TextView) convertView.findViewById(R.id.bank_buy_rate);
					bankSaleRaTe = (TextView) convertView.findViewById(R.id.bank_sell_rate);
					currencyType = (TextView) convertView.findViewById(R.id.type);
					updateTime = (TextView) convertView.findViewById(R.id.update_time);

					ViewHolder holder = new ViewHolder();
					holder.iconView = iconView;
					holder.bankBuyRate = bankBuyRate;
					holder.bankSaleRaTe = bankSaleRaTe;
					holder.currencyType = currencyType;
					holder.updateTime = updateTime;

					convertView.setTag(holder);

				} else {
					ViewHolder holder = (ViewHolder) convertView.getTag();
					
					iconView = holder.iconView;
					bankBuyRate = holder.bankBuyRate;
					bankSaleRaTe = holder.bankSaleRaTe;
					currencyType = holder.currencyType;
					updateTime = holder.updateTime;
				}
				
				bankBuyRate.setText(String.format(AllCurrencyActivity.this.getString(R.string.bank_buy_rate), currency.getBuyRate()));
				bankSaleRaTe.setText(String.format(AllCurrencyActivity.this.getString(R.string.bank_sell_rate), currency.getSaleRate()));
				currencyType.setText(String.format(AllCurrencyActivity.this.getString(R.string.currency_type), currency.getId()));
				updateTime.setText(String.format(AllCurrencyActivity.this.getString(R.string.update_time), currency.getUpdateTimeStr()));
				
				return convertView;
			}
			return null;
		}

		/**
		 * @param currencys
		 *            the currencys to set
		 */
		public void setCurrencys(Currency[] currencys) {
			this.currencys = currencys;
		}

		class ViewHolder {
			public ImageView iconView;
			public TextView bankBuyRate;
			public TextView bankSaleRaTe;
			public TextView currencyType;
			public TextView updateTime;
		}

	}

	@Override
	public void nofityDataChange() {
		if (adapter != null) {
			adapter.setCurrencys(CurrencyApplication.application.currencys);
			adapter.notifyDataSetChanged();
		}
	}
}
