package com.tony.babygo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class BabyGoPreference {
	public static final String CITY_LIST_JSON = "city_list_json";

	public static void setCityListJson(Context context, String cityListJson) {
		Editor ed = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		ed.putString(CITY_LIST_JSON, cityListJson);
		ed.commit();
	}

	public static String getCityListJson(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(CITY_LIST_JSON, "");

	}
}
