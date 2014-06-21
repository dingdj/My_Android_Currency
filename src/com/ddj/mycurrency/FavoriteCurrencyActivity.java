/**
 * @author dingdj
 * Date:2014-6-19上午10:08:05
 *
 */
package com.ddj.mycurrency;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ddj.commonkit.StringUtils;
import com.ddj.mycurrency.AllCurrencyActivity.AllCurrencyAdapter;
import com.ddj.mycurrency.database.DatabaseManager;
import com.ddj.mycurrency.model.FavoriteCurrency;
import com.ddj.mycurrency.notify.INotify;
import com.ddj.mycurrency.util.Constant;

/**
 * @author dingdj
 * Date:2014-6-19上午10:08:05
 *
 */
public class FavoriteCurrencyActivity extends ListActivity implements INotify{

	FavoriteCurrencyAdapter adapter;

	LayoutInflater inflater;
	
    public static final int ADD_ID = Menu.FIRST;// 添加命令对应ID值  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new FavoriteCurrencyAdapter();
		List<FavoriteCurrency> currencys = DatabaseManager.getList(this);
		getListView().setAdapter(adapter);
		adapter.setCurrencys(currencys);
		inflater = getLayoutInflater();
	}
	
	

	
	class FavoriteCurrencyAdapter extends BaseAdapter {

		List<FavoriteCurrency> currencys;

		@Override
		public int getCount() {
			if (currencys != null) {
				return currencys.size();
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
				FavoriteCurrency currency = currencys.get(position);
				
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
					holder.id = currency.getId();

					convertView.setTag(holder);

				} else {
					ViewHolder holder = (ViewHolder) convertView.getTag();
					
					iconView = holder.iconView;
					bankBuyRate = holder.bankBuyRate;
					bankSaleRaTe = holder.bankSaleRaTe;
					currencyType = holder.currencyType;
					updateTime = holder.updateTime;
					holder.id = currency.getId();
				}
				//阈值
				bankBuyRate.setText(String.format(FavoriteCurrencyActivity.this.getString(R.string.bank_buy_rate), currency.getBuyRate()));
				//类型
				if(currency.getType() == FavoriteCurrency.BUY_TYPE){
					bankSaleRaTe.setText("已买入");
					bankSaleRaTe.setTextColor(Color.RED);
				}else{
					bankSaleRaTe.setText("未买入");
				}
				
				String id = currency.getCurrencyType();
				String toHumanreadCurrency = Constant.toHumanRead.get(id);
				if(StringUtils.isNotEmpty(toHumanreadCurrency)){
					id = toHumanreadCurrency;
				}
				currencyType.setText(String.format(FavoriteCurrencyActivity.this.getString(R.string.currency_type), id));
				updateTime.setText(String.format(FavoriteCurrencyActivity.this.getString(R.string.update_time), currency.getBuyTime()));
				
				return convertView;
			}
			return null;
		}

		/**
		 * @param currencys
		 *            the currencys to set
		 */
		public void setCurrencys(List<FavoriteCurrency> currencys) {
			this.currencys = currencys;
		}

		class ViewHolder {
			public ImageView iconView;
			public TextView bankBuyRate;
			public TextView bankSaleRaTe;
			public TextView currencyType;
			public TextView updateTime;
			public int id;
		}

	}

	@Override
	public void nofityDataChange() {
		//价格超过阈值 发出通知
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean rtn = super.onCreateOptionsMenu(menu);
		boolean b = super.onCreateOptionsMenu(menu);
        
        menu.add(Menu.NONE, ADD_ID, Menu.NONE, "增加汇率");
		return rtn;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case ADD_ID:
			// 弹出增加的dialog
			final Dialog addDialog = new Dialog(this);
			View view = inflater.inflate(R.layout.currency, null);
			addDialog.setContentView(view);
			addDialog.setTitle("增加汇率");
			addDialog.show();
		
			final Spinner currencyType = (Spinner)view.findViewById(R.id.select_currency);
			final Spinner type = (Spinner)view.findViewById(R.id.favorite_type);
			final EditText buyRate = (EditText)view.findViewById(R.id.buy_rate);
			final EditText notifyRate = (EditText)view.findViewById(R.id.notify_rate);
			notifyRate.setVisibility(View.GONE);
			
			Button positiveButton = (Button)view.findViewById(R.id.positiveButton);
			Button negativeButton = (Button)view.findViewById(R.id.negativeButton);
			String[] strs = new String[Constant.toHumanRead.entrySet().size()];
			int i= 0;
			for(String key : Constant.toHumanRead.keySet()){
				strs[i] = Constant.toHumanRead.get(key);
				i++;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, strs);
			currencyType.setAdapter(adapter);
			
			String[] types = new String[]{"买入", "通知"};
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, types);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			type.setAdapter(adapter);
			type.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(position == 0){
						notifyRate.setVisibility(View.GONE);
						buyRate.setVisibility(View.VISIBLE);
					}else if(position == 1){
						notifyRate.setVisibility(View.VISIBLE);
						buyRate.setVisibility(View.GONE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			positiveButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String currencyStr = Constant.toHumanReadReverse.get((currencyType.getSelectedItem()).toString());
					String typeStr = type.getSelectedItem().toString();
					int type = 0;
					if("买入".equals(typeStr)){
						type = 1;
					}else if("通知".equals(typeStr)){
						type = 0;
					}
					String buyRateStr = "";
					if(type == 1){
						buyRateStr = buyRate.getText().toString();
					}else{
						buyRateStr = notifyRate.getText().toString();
					}
					FavoriteCurrency favoriteCurrency = new FavoriteCurrency();
					favoriteCurrency.setBuyRate(buyRateStr);
					favoriteCurrency.setCurrencyType(type+"");
					favoriteCurrency.setCurrencyType(currencyStr);
					DatabaseManager.addOrUpdateFavoriteCurrency(FavoriteCurrencyActivity.this, favoriteCurrency);
					addDialog.dismiss();
					List<FavoriteCurrency> currencys = DatabaseManager.getList(FavoriteCurrencyActivity.this);
					FavoriteCurrencyActivity.this.adapter.setCurrencys(currencys);
					FavoriteCurrencyActivity.this.adapter.notifyDataSetChanged();
				}
			});
			
			negativeButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					addDialog.dismiss();
				}
			});
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
