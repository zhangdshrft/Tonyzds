package com.tony.babygo.utils.json;

import com.google.gson.Gson;
import com.tony.babygo.model.Businesses;
import com.tony.babygo.model.CityList;
import com.tony.babygo.utils.log.IWLog;

public class JsonUtils {
	public static boolean verifyJsonData(String jsonData) {
		if (jsonData.startsWith("{") == true && jsonData.endsWith("}") == true) {
			return true;
		}  
		return false;
	}

	public static Businesses parseBusinessesFromJson(String jsonData) {
//		if (verifyJsonData(jsonData) == true) {
			Gson gson = new Gson();
			Businesses businesses = gson.fromJson(jsonData, Businesses.class);
			return businesses;
//		} else {
//			return null;
//		}
	}
	
	public static CityList parseCityListFromJson(String jsonData) {
			Gson gson = new Gson();
			CityList cityList = gson.fromJson(jsonData, CityList.class);
			return cityList;
	}
}
