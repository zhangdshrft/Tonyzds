package com.tony.babygo.fragments;

import java.util.HashMap;
import java.util.Map;

import com.tony.babygo.MyBrowserActivity;
import com.tony.babygo.R;
import com.tony.babygo.adapter.BusinessAdapter;
import com.tony.babygo.data.ApiUrl;
import com.tony.babygo.data.DataCenter;
import com.tony.babygo.data.DianpingUserData;
import com.tony.babygo.data.LocationData;
import com.tony.babygo.model.Businesses;
import com.tony.babygo.tool.CommonUtils;
import com.tony.babygo.tool.DemoApiTool;
import com.tony.babygo.utils.json.JsonUtils;
import com.tony.babygo.utils.log.IWLog;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MyGeneralFragment extends Fragment {
	public static final String TAG = "MyGeneralFragment";

	static Context context;
	
	private View foot;
	
	int page = 1;
	
	int mNum;

	public MyGeneralFragment() {
		super();
	}
	
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser == true) {
			IWLog.i(TAG, "isVisibleToUser == true" + mNum);
			DataCenter.PAGE_VIEW_CUR = mNum;
		} else if (isVisibleToUser == false) {
			IWLog.i(TAG, "isVisibleToUser == false" + mNum);
		}
	}

	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */
	public static MyGeneralFragment newInstance(int num, Context mContext) {
		MyGeneralFragment f = new MyGeneralFragment();

		context = mContext;

		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}
	
	private ListView businessListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		IWLog.i(TAG, "onCreate mNum:" + mNum);
		View v = inflater.inflate(R.layout.my_general_fragment, container, false);
		foot = LayoutInflater.from(context).inflate(R.layout.foot, null);  
		businessListView =  (ListView) v.findViewById(R.id.business_list);
		businessListView.addFooterView(foot, null, true);
		
		initDataMethod();
		
		return v;
	}
	
	private void initDataMethod() {
		page = 1;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        final String apiUrl = ApiUrl.NAME_SPACE + ApiUrl.BUSINESS_FIND_BUSINESSES;
        final Map<String, String> paramMap = new HashMap<String, String>();
//        paramMap.put("city", LocationData.city);
        paramMap.put("latitude", LocationData.latitude);
        paramMap.put("longitude", LocationData.longitude);
        
		switch (mNum) {
		case 1:  
			paramMap.put("category", "亲子摄影");
			break;
		case 2:
			paramMap.put("category", "亲子游乐");  
			break;
		case 3:
			paramMap.put("category", "早教中心");
			break;
		case 4:
			paramMap.put("category", "亲子购物");
			break;
		}
        
//		paramMap.put("region", LocationData.region);
        paramMap.put("limit", "20");
        paramMap.put("radius", "5000");
        paramMap.put("offset_type", "0");
//        paramMap.put("has_coupon", "1");
//        paramMap.put("has_deal", "1");  
        paramMap.put("sort", "2");
        paramMap.put("format", "json");
        
        String requestResult = DemoApiTool.requestApi(apiUrl, DianpingUserData.appKey, DianpingUserData.secret, paramMap);
        IWLog.i(TAG, "requestResult:" + requestResult);  
        final Businesses businesses = JsonUtils.parseBusinessesFromJson(requestResult);  
        
        if (businesses.getBusinesses().size() > 0) {
	        	
	       
	        
			if (businesses.getTotal_count() <= 20) {
				businessListView.removeFooterView(foot);
			}
	        
	        BusinessAdapter businessAdapter = new BusinessAdapter(MyGeneralFragment.this, context, businesses, businessListView);
	        businessListView.setAdapter(businessAdapter);
	        
	        businessListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent goBrowserIntent = new Intent(context, MyBrowserActivity.class);
					goBrowserIntent.putExtra("html5url", businesses.getBusinesses().get(arg2).getBusiness_url());
					startActivity(goBrowserIntent);
				}
			});
	        
	        foot.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					IWLog.i(TAG, "FOOT CLICK"); 
					page++;
					paramMap.put("page", String.valueOf(page));  
					String requestResult = DemoApiTool.requestApi(apiUrl, DianpingUserData.appKey, DianpingUserData.secret, paramMap);
					IWLog.i(TAG, "page:" + requestResult);
					Businesses businessesADD = JsonUtils.parseBusinessesFromJson(requestResult);  
					if (businessesADD.getCount() == 0) {
						businessListView.removeFooterView(foot);
					} else {
						CommonUtils.addBusinessesMethod(businesses, businessesADD);
						IWLog.i(TAG, "businesses size" + businesses.getBusinesses().size());
						BusinessAdapter businessAdapter = new BusinessAdapter(MyGeneralFragment.this, context, businesses, businessListView);
				        businessListView.setAdapter(businessAdapter);
					}
					businessListView.setSelection(businesses.getBusinesses().size());
				}
			});
        }
	}
	
	@Override
	public void onPause() {
		MobclickAgent.onPageEnd("MyGeneralFragment"); 
		super.onPause();
	}
	
	@Override
	public void onResume() {
		 MobclickAgent.onPageStart("MyGeneralFragment"); //统计页面
		super.onResume();
	}
}
