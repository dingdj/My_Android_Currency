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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ddj.commonkit.DateUtil;
import com.ddj.commonkit.StringUtils;
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
		//创建弹出式菜单
		registerForContextMenu(getListView());
		adapter.setCurrencys(currencys);
		inflater = getLayoutInflater();
	}
	
	

	
	class FavoriteCurrencyAdapter extends BaseAdapter {

		List<FavoriteCurrency> currencys;

		
		/**
		 * @return the currencys
		 */
		public List<FavoriteCurrency> getCurrencys() {
			return currencys;
		}

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
				updateTime.setText(String.format(FavoriteCurrencyActivity.this.getString(R.string.update_time), DateUtil.parseLongToTime(Long.parseLong(currency.getBuyTime()), "yyyy-MM-dd HH:mm:ss")));
				
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean rtn = super.onCreateOptionsMenu(menu);
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
			final View buyRateContainer = view.findViewById(R.id.buy_rate_container);
			final View notifyRateContainer = view.findViewById(R.id.notify_rate_container);
			notifyRateContainer.setVisibility(View.GONE);
			
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
						notifyRateContainer.setVisibility(View.GONE);
						buyRateContainer.setVisibility(View.VISIBLE);
					}else if(position == 1){
						notifyRateContainer.setVisibility(View.VISIBLE);
						buyRateContainer.setVisibility(View.GONE);
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
					favoriteCurrency.setType(type);
					favoriteCurrency.setCurrencyType(currencyStr);
					DatabaseManager.addOrUpdateFavoriteCurrency(FavoriteCurrencyActivity.this, favoriteCurrency);
					addDialog.dismiss();
					refresh();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		// 注册监听
		CurrencyApplication.application.registerNotify(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 注销监听
		CurrencyApplication.application.unRegisterNotify(this);
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
        try {
        	AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;   
            FavoriteCurrency currency = adapter.getCurrencys().get(info.position);
            menu.setHeaderTitle(Constant.toHumanRead.get(currency.getCurrencyType()));
        } catch (Exception e) {
            return;
        }
		menu.add(0, 1, 0, "删除");
		menu.add(0, 2, 0, "修改");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo itemInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		try {
			final FavoriteCurrency favoriteCurrency = adapter.getCurrencys().get(itemInfo.position);
			switch (item.getItemId()) {
			case 1:
				DatabaseManager.deleteCurrency(this, favoriteCurrency.getId());
				refresh();
				break;
			case 2:
				// 弹出修改的dialog
				final Dialog updateDialog = new Dialog(this);
				View view = inflater.inflate(R.layout.currency, null);
				updateDialog.setContentView(view);
				updateDialog.setTitle("修改汇率");
				updateDialog.show();
			
				final Spinner currencyType = (Spinner)view.findViewById(R.id.select_currency);
				final Spinner type = (Spinner)view.findViewById(R.id.favorite_type);
				final EditText buyRate = (EditText)view.findViewById(R.id.buy_rate);
				final EditText notifyRate = (EditText)view.findViewById(R.id.notify_rate);
				final View buyRateContainer = view.findViewById(R.id.buy_rate_container);
				final View notifyRateContainer = view.findViewById(R.id.notify_rate_container);
				
				if(favoriteCurrency.getType() == FavoriteCurrency.BUY_TYPE){
					notifyRateContainer.setVisibility(View.GONE);
					buyRate.setText(favoriteCurrency.getBuyRate());
				}else{
					buyRateContainer.setVisibility(View.GONE);
					notifyRate.setText(favoriteCurrency.getBuyRate());
				}
				
				Button positiveButton = (Button)view.findViewById(R.id.positiveButton);
				Button negativeButton = (Button)view.findViewById(R.id.negativeButton);
				String[] strs = new String[Constant.toHumanRead.entrySet().size()];
				int i= 0;
				int position = 0;
				for(String key : Constant.toHumanRead.keySet()){
					strs[i] = Constant.toHumanRead.get(key);
					if(key.equals(favoriteCurrency.getCurrencyType())){
						position = i;
					}
					i++;
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, strs);
				currencyType.setAdapter(adapter);
				currencyType.setSelection(position);
				
				
				String[] types = new String[]{"买入", "通知"};
				adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, types);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				type.setAdapter(adapter);
				if(favoriteCurrency.getType() == FavoriteCurrency.BUY_TYPE){
					type.setSelection(0);
				}else{
					type.setSelection(1);
				}
				
				type.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if(position == 0){
							notifyRateContainer.setVisibility(View.GONE);
							buyRateContainer.setVisibility(View.VISIBLE);
						}else if(position == 1){
							notifyRateContainer.setVisibility(View.VISIBLE);
							buyRateContainer.setVisibility(View.GONE);
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
						favoriteCurrency.setBuyRate(buyRateStr);
						favoriteCurrency.setType(type);
						favoriteCurrency.setCurrencyType(currencyStr);
						DatabaseManager.addOrUpdateFavoriteCurrency(FavoriteCurrencyActivity.this, favoriteCurrency);
						updateDialog.dismiss();
						refresh();
					}
				});
				
				negativeButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						updateDialog.dismiss();
					}
				});
				break;	
			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onContextItemSelected(item);
	}
	
	
	/**
	 * 重新加载数据库
	 */
	public void refresh(){
		List<FavoriteCurrency> currencys = DatabaseManager.getList(this);
		adapter.setCurrencys(currencys);
		adapter.notifyDataSetChanged();
	}
	
	
	

}
