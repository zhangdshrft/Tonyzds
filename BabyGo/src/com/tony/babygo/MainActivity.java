package com.tony.babygo;

import java.util.HashMap;
import java.util.Map;

import com.tony.babygo.adapter.MyFragmentPageAdapter;
import com.tony.babygo.data.ApiUrl;
import com.tony.babygo.data.BabyGoPreference;
import com.tony.babygo.data.DataCenter;
import com.tony.babygo.data.DianpingUserData;
import com.tony.babygo.data.LocationData;
import com.tony.babygo.pic.Bimp;
import com.tony.babygo.tool.DemoApiTool;
import com.tony.babygo.utils.log.IWLog;
import com.tony.babygo.views.ScrollingTabs;
import com.tony.babygo.views.ScrollingTabs.TabAdapter;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

	public static final String TAG = "MainActivity";

	private TextView placeBtn;

	private TextView placeText;

	private TextView searchBtn;

	private TextView feedBackBtn;
	
	private TextView topTitleText;

	private ViewPager mViewPager;

	private ScrollingTabs mScrollingTabs;

	private MyFragmentPageAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		
		UmengUpdateAgent.update(this);  

		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8;
		Bimp.mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				return bitmap.getByteCount() / 1024;
			}
		};

		findView();
		initView();
	}

	private void findView() {
		mViewPager = (ViewPager) findViewById(R.id.vp);
		mScrollingTabs = (ScrollingTabs) findViewById(R.id.stv);
	}

	private void initView() {
		topTitleText = (TextView) findViewById(R.id.top_title);
		placeBtn = (TextView) findViewById(R.id.place_btn);
		placeText = (TextView) findViewById(R.id.place_text);
		searchBtn = (TextView) findViewById(R.id.search_btn);
		feedBackBtn = (TextView) findViewById(R.id.feed_back_btn);
		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"fontawesome-webfont.ttf");
		placeBtn.setTypeface(font);
		searchBtn.setTypeface(font);
		feedBackBtn.setTypeface(font);
		
		topTitleText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent goAboutIntent = new Intent(MainActivity.this,
						AboutActivity.class);
				startActivity(goAboutIntent);
			}
		});

		placeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent goCitySelIntent = new Intent(MainActivity.this,
						CitySelActivity.class);
				startActivity(goCitySelIntent);
			}
		});
		
		placeText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent goCitySelIntent = new Intent(MainActivity.this,
						CitySelActivity.class);
				startActivity(goCitySelIntent);
			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent goSearchIntent = new Intent(MainActivity.this,
						SearchActivity.class);
				startActivity(goSearchIntent);
			}
		});

		feedBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
				agent.startFeedbackActivity();
			}
		});

		initCityListJsonMethod();
		
		initViewPageMethod();
		
	}

	private void initCityListJsonMethod() {
		new Thread() {
			@Override
			public void run() {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("format", "json");
				String apiUrl = ApiUrl.NAME_SPACE
						+ ApiUrl.METADATA_GET_CITIES_WITH_BUSINESSES;
				String requestResult = DemoApiTool.requestApi(apiUrl,
						DianpingUserData.appKey, DianpingUserData.secret,
						paramMap);
				IWLog.i(TAG, "requestResult:" + requestResult);
				BabyGoPreference.setCityListJson(getApplicationContext(),
						requestResult);
			}
		}.start();
	}

	public void onResume() {
		IWLog.i(TAG, "onResume()");

		if (DataCenter.FROM_CITY_SEL_FLAG == true) {
			initViewPageMethod();
			DataCenter.FROM_CITY_SEL_FLAG = false;
		}
		
		placeText.setText(LocationData.city);
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	private void initViewPageMethod() {
		FragmentManager fm = getSupportFragmentManager();
		mAdapter = new MyFragmentPageAdapter(fm, MainActivity.this);
		mViewPager.setAdapter(mAdapter);
		
		mScrollingTabs.setEqualWidth(true);
		mScrollingTabs.setViewPager(mViewPager);
		mScrollingTabs.setTabAdapter(new TabAdapter() {

			@Override
			public View getView(int position) {
				LayoutInflater inflater = MainActivity.this.getLayoutInflater();
				final View tab = (View) inflater.inflate(R.layout.tab, null);

				TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);

				final String[] mTitles = new String[] { "推荐", "摄影", "游乐", "教育",
						"购物" };

				if (position < mTitles.length)
					tv.setText(mTitles[position]);

				return tab;
			}

			@Override
			public View getSeparator() {
				View view = new ImageView(MainActivity.this);
				view.setLayoutParams(new LayoutParams(0,
						LayoutParams.MATCH_PARENT));
				view.setBackgroundColor(Color.RED);
				return view;
			}

			@Override
			public void onTabSelected(int position, ViewGroup mContainer) {
				View tab = (View) mContainer.getChildAt(position);
				TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);
				ImageView iv = (ImageView) tab.findViewById(R.id.iv_tabs);

				tv.setTextColor(Color.RED);
				iv.setVisibility(View.VISIBLE);
			}

			@Override
			public void onTabUnSelected(int position, ViewGroup mContainer) {
				View tab = (View) mContainer.getChildAt(position);
				TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);
				ImageView iv = (ImageView) tab.findViewById(R.id.iv_tabs);

				tv.setTextColor(Color.BLACK);
				iv.setVisibility(View.INVISIBLE);
			}
		});
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("确定退出宝贝GO?")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									setResult(RESULT_OK);
									System.exit(0);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).show();
		}
		return true;
	};

}
