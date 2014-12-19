package com.tony.babygo;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tony.babygo.adapter.CityListAdapter;
import com.tony.babygo.data.BabyGoPreference;
import com.tony.babygo.data.DataCenter;
import com.tony.babygo.data.LocationData;
import com.tony.babygo.model.CityList;
import com.tony.babygo.utils.json.JsonUtils;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CitySelActivity extends BaseActivity implements OnGeocodeSearchListener{
	public static final String TAG = "CitySelActivity";
	
	private TextView citySelBack;
	
	private TextView citySelTitle;
	
	private ListView cityList;
	
	private TextView locCityNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_sel_layout);
		initView();
	}

	private void initView() {
		citySelBack = (TextView) findViewById(R.id.city_sel_back);
		citySelTitle = (TextView) findViewById(R.id.city_sel_title);
		
		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"fontawesome-webfont.ttf");
		citySelBack.setTypeface(font);
		citySelTitle.setTypeface(font);
		  
		cityList = (ListView) findViewById(R.id.city_list);
		locCityNameText = (TextView) findViewById(R.id.loc_city_name_text);
		locCityNameText.setText(LocationData.city_cur + "\tGPS定位");
		
		locCityNameText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				DataCenter.FROM_CITY_SEL_FLAG = true;
				
				LocationData.city = LocationData.city_cur;
				LocationData.latitude = LocationData.latitude_cur;
				LocationData.longitude = LocationData.longitude_cur;
				CitySelActivity.this.finish();
			}
		});
		  
		String cityListJson = BabyGoPreference.getCityListJson(getApplicationContext());
		final CityList cities = JsonUtils.parseCityListFromJson(cityListJson);
		CityListAdapter cityListAdapter = new CityListAdapter(getApplicationContext(), cities);
		cityList.setAdapter(cityListAdapter);
		
		cityList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				DataCenter.FROM_CITY_SEL_FLAG = true;
				
				cityName = cities.getCities().get(arg2);
				getLatlon(cityName);
			}
		});
		
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
	}
	
	String cityName;
	
	private GeocodeSearch geocoderSearch;
	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		GeocodeQuery query = new GeocodeQuery(name, name);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				String addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:" + address.getFormatAddress();
				Toast.makeText(CitySelActivity.this, addressName, Toast.LENGTH_SHORT).show();
				LocationData.latitude = String.valueOf(address.getLatLonPoint().getLatitude());
				LocationData.longitude = String.valueOf(address.getLatLonPoint().getLongitude());
				LocationData.city = cityName;
				CitySelActivity.this.finish();
			} else {
//				ToastUtil.show(CitySelActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
//			ToastUtil.show(CitySelActivity.this, R.string.error_network);
		} else if (rCode == 32) {
//			ToastUtil.show(CitySelActivity.this, R.string.error_key);
		} else {
//			ToastUtil.show(CitySelActivity.this, R.string.error_other);
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
