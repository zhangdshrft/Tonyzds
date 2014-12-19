package com.tony.babygo.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.tony.babygo.MyBrowserActivity;
import com.tony.babygo.R;
import com.tony.babygo.adapter.BusinessAdapter;
import com.tony.babygo.adapter.ImagePagerAdapter;
import com.tony.babygo.data.ApiUrl;
import com.tony.babygo.data.DataCenter;
import com.tony.babygo.data.DianpingUserData;
import com.tony.babygo.data.LocationData;
import com.tony.babygo.model.Businesses;
import com.tony.babygo.pic.Bimp;
import com.tony.babygo.pic.PicTool;
import com.tony.babygo.tool.CommonUtils;
import com.tony.babygo.tool.DemoApiTool;
import com.tony.babygo.utils.json.JsonUtils;
import com.tony.babygo.utils.log.IWLog;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MyHomeFragment extends Fragment {
	public static final String TAG = "MyHomeFragment";
	
	static Context context;
	
	int mNum;

	public MyHomeFragment() {
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
	public static MyHomeFragment newInstance(int num, Context mContext) {
		MyHomeFragment f = new MyHomeFragment();
		
		context = mContext;  
		
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);
		
		return f;
	}
	
	private ListView businessListView;
	
	private AutoScrollViewPager viewPager;
	
	private View head;
	
	private View foot;
	
	private List<Integer> imageIdList;
	
	int page = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		IWLog.i(TAG, "onCreate mNum:" + mNum);
		View v = inflater.inflate(R.layout.my_home_fragment, container, false);
		businessListView =  (ListView) v.findViewById(R.id.business_list);
		
		head = LayoutInflater.from(context).inflate(R.layout.auto_scroll_view_pager, null);
		foot = LayoutInflater.from(context).inflate(R.layout.foot, null);  
		viewPager = (AutoScrollViewPager) head.findViewById(R.id.view_pager);
		businessListView.addHeaderView(head,null,true);    
		businessListView.addFooterView(foot, null, true);
		imageIdList = new ArrayList<Integer>();
		imageIdList.add(R.drawable.view_page_loading_bg);
//		imageIdList.add(R.drawable.banner2);
//		imageIdList.add(R.drawable.banner3);
//		imageIdList.add(R.drawable.banner4);
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
        paramMap.put("category", "亲子游乐");
//        paramMap.put("region", LocationData.region);
        paramMap.put("limit", "20");
        paramMap.put("radius", "5000");
        paramMap.put("offset_type", "0");
//        paramMap.put("has_coupon", "1");
//        paramMap.put("has_deal", "1");
        paramMap.put("sort", "2");
        paramMap.put("format", "json");
        paramMap.put("page", String.valueOf(page));
  
        String requestResult = DemoApiTool.requestApi(apiUrl, DianpingUserData.appKey, DianpingUserData.secret, paramMap);
        IWLog.i(TAG, "requestResult:" + requestResult);
        final Businesses businesses = JsonUtils.parseBusinessesFromJson(requestResult);  
        
        if (businesses.getBusinesses().size() > 0) {
	        	if (businesses.getTotal_count() <= 20) {
	        		businessListView.removeFooterView(foot);
	        }
	        
	        initTopPhotosMethod(businesses);
	        
	        viewPager.setAdapter(new ImagePagerAdapter(context, imageIdList, businesses));
	        viewPager.setInterval(2000);
	        viewPager.startAutoScroll();
	        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	        
	        final BusinessAdapter businessAdapter = new BusinessAdapter(MyHomeFragment.this, context, businesses, businessListView);
	        businessListView.setAdapter(businessAdapter);
	        
			businessListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent goBrowserIntent = new Intent(context, MyBrowserActivity.class);
					goBrowserIntent.putExtra("html5url", businesses.getBusinesses().get(arg2 - 1).getBusiness_url());
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
						BusinessAdapter businessAdapter = new BusinessAdapter(MyHomeFragment.this, context, businesses, businessListView);
				        businessListView.setAdapter(businessAdapter);
					}
					businessListView.setSelection(businesses.getBusinesses().size());
				}
			});
        }
	}
	
	private void initTopPhotosMethod(final Businesses businesses) {
		new Thread() {
			public void run() {
				
				if (businesses.getBusinesses().size() >= 4) {
					for (int i = 0; i < 4; i++) {    
						Bitmap b = Bimp.getHttpBitmap(businesses.getBusinesses().get(i).getPhoto_url());
						Bitmap b0 = PicTool.getRoundedCornerBitmap(b, 10);
						Bimp.addBitmapToMemoryCache(businesses.getBusinesses().get(i).getPhoto_url(), b0);
					}
				} else {
					for (int i = 0; i < businesses.getBusinesses().size(); i++) {    
						Bitmap b = Bimp.getHttpBitmap(businesses.getBusinesses().get(i).getPhoto_url());
						Bitmap b0 = PicTool.getRoundedCornerBitmap(b, 10);
						Bimp.addBitmapToMemoryCache(businesses.getBusinesses().get(i).getPhoto_url(), b0);
					}
				}
				
			}
		}.start();
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        		IWLog.i(TAG, "onPageSelected:" + position);  
        		DianpingUserData.cur_scroll_page_index = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
	
	@Override
	public void onPause() {
		viewPager.stopAutoScroll();
		MobclickAgent.onPageEnd("MyHomeFragment"); 
		super.onPause();
	}
	
	@Override
	public void onResume() {
		IWLog.i(TAG, "onResume()");
		initDataMethod();
		 viewPager.startAutoScroll();
		 MobclickAgent.onPageStart("MyHomeFragment"); //统计页面
		super.onResume();
	}
	
}
