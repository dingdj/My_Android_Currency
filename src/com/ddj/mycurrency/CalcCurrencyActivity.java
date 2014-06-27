/**
 * @author dingdj
 * Date:2014-6-27下午5:04:32
 *
 */
package com.ddj.mycurrency;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author dingdj
 * Date:2014-6-27下午5:04:32
 *
 */
public class CalcCurrencyActivity extends Activity {
	
	EditText cur_rate;
	EditText diancha;
	EditText multiple;
	EditText price;
	EditText shoushu;
	
	Button calc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc_forex);
		cur_rate = (EditText)findViewById(R.id.cur_rate);
		diancha = (EditText)findViewById(R.id.diancha);
		multiple = (EditText)findViewById(R.id.multiple);
		price = (EditText)findViewById(R.id.price);
		shoushu = (EditText)findViewById(R.id.shoushu);
		final TextView result = (TextView)findViewById(R.id.result);
		calc = (Button)findViewById(R.id.calc);
		
		
		calc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				double d_rate = Double.parseDouble(cur_rate.getText().toString());
				double d_diancha = Double.parseDouble(diancha.getText().toString());
				double d_multiple = Double.parseDouble(multiple.getText().toString());
				double d_price = Double.parseDouble(price.getText().toString());
				double d_shoushu = Double.parseDouble(shoushu.getText().toString());
				
				double d_result = d_rate - (d_diancha*d_shoushu) - ((1/d_multiple)*d_shoushu*100000*d_price);
				int hudongResult = (int) (d_result/10.0);
				
				result.setText(String.format(CalcCurrencyActivity.this.getResources().getString(R.string.calc_dian_result),hudongResult+""));
			}
		});
		
	}
	
	

}
