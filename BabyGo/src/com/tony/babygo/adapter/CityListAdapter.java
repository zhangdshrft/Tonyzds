package com.tony.babygo.adapter;

import com.tony.babygo.R;
import com.tony.babygo.model.CityList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListAdapter extends BaseAdapter {

	public static final String TAG = "CityListAdapter";
	
	private Context context;
	
	private CityList cityList;
	
	private LayoutInflater mInflater;
	
	public CityListAdapter(Context context, CityList cityList) {
		this.context = context;
		this.cityList = cityList;
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cityList.getCities().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cityList.getCities().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.city_list_item_layout, null);  
			holder.cityNameText = (TextView) convertView.findViewById(R.id.city_name_text);
			convertView.setTag(holder);  
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cityNameText.setText(cityList.getCities().get(position));
		return convertView;  
	}
	
	class ViewHolder {
		private TextView cityNameText;
	}

}
