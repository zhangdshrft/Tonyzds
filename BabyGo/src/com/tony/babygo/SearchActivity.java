package com.tony.babygo;

import java.util.HashMap;
import java.util.Map;

import com.tony.babygo.adapter.BusinessAdapter;
import com.tony.babygo.data.ApiUrl;
import com.tony.babygo.data.DianpingUserData;
import com.tony.babygo.data.LocationData;
import com.tony.babygo.model.Businesses;
import com.tony.babygo.tool.DemoApiTool;
import com.tony.babygo.utils.json.JsonUtils;
import com.tony.babygo.utils.log.IWLog;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends BaseActivity {
	public static final String TAG = "SearchActivity";

	private TextView cancel;

	private TextView search;
	
	private EditText searchEditText;  
	
	private ListView businessListView;
	
	Businesses businesses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		initView();
	}

	@SuppressLint("NewApi")
	private void initView() {
		cancel = (TextView) findViewById(R.id.search_cancel);
		search = (TextView) findViewById(R.id.search_do);

		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"fontawesome-webfont.ttf");
		cancel.setTypeface(font);
		search.setTypeface(font);
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		searchEditText = (EditText) findViewById(R.id.search_editText);
		businessListView =  (ListView) findViewById(R.id.business_list);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		final String apiUrl = ApiUrl.NAME_SPACE + ApiUrl.BUSINESS_FIND_BUSINESSES;
		final Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", LocationData.latitude);
		paramMap.put("longitude", LocationData.longitude);
		paramMap.put("limit", "20");
		paramMap.put("radius", "5000");
		paramMap.put("offset_type", "0");
//		paramMap.put("has_coupon", "1");
//		paramMap.put("has_deal", "1");
		paramMap.put("sort", "2");
		paramMap.put("format", "json");
		
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String keyWord = searchEditText.getText().toString();
				if (!keyWord.equalsIgnoreCase("")) {
					paramMap.put("keyword", keyWord);
					String requestResult = DemoApiTool.requestApi(apiUrl, DianpingUserData.appKey, DianpingUserData.secret, paramMap);
			        IWLog.i(TAG, "requestResult:" + requestResult);  
			        businesses = JsonUtils.parseBusinessesFromJson(requestResult);  
			        BusinessAdapter businessAdapter = new BusinessAdapter(null, SearchActivity.this, businesses, businessListView);
			        businessListView.setAdapter(businessAdapter);
				} else {
					Toast.makeText(getApplicationContext(), "«Î ‰»ÎÀ—À˜ƒ⁄»›", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		businessListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent goBrowserIntent = new Intent(SearchActivity.this, MyBrowserActivity.class);
				goBrowserIntent.putExtra("html5url", businesses.getBusinesses().get(arg2).getBusiness_url());
				startActivity(goBrowserIntent);
			}
		});
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
