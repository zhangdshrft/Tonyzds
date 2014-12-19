package com.tony.babygo;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.tony.babygo.data.LocationData;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

public class SplashActivity extends BaseActivity implements
		AMapLocationListener {
	public static final String TAG = "SplashActivity";
	
	private LocationManagerProxy mLocationManagerProxy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		MobclickAgent.updateOnlineConfig( SplashActivity.this );
		MobclickAgent.setDebugMode( false );
		UmengUpdateAgent.update(this);
		init();  
		initView();
	}
	
	/**
	 * 初始化定位  
	 */
	private void init() {
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		//在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 60*1000, 15, this);
		

	}
	
	private void initView() {
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {

		if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			LocationData.city = amapLocation.getCity();
			LocationData.city_cur = amapLocation.getCity();
			LocationData.latitude = String.valueOf(amapLocation.getLatitude());
			LocationData.longitude = String.valueOf(amapLocation.getLongitude());
			LocationData.latitude_cur = String.valueOf(amapLocation.getLatitude());
			LocationData.longitude_cur = String.valueOf(amapLocation.getLongitude());
			LocationData.region = amapLocation.getDistrict();
			Intent goMainIntent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(goMainIntent);
			finish();
		} else {
			Intent goMainIntent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(goMainIntent);
			finish();
		}

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashActivity");
		MobclickAgent.onPause(this);
		//移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}

	protected void onDestroy() {
		super.onDestroy();
		
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashActivity");
		MobclickAgent.onResume(this);
	}

}
