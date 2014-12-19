package com.tony.babygo.tool;

import java.util.List;

import com.tony.babygo.model.Business;
import com.tony.babygo.model.Businesses;

public class CommonUtils {
	public static final String TAG = "CommonUtils";
	public static void addBusinessesMethod(Businesses businesses, Businesses businessesADD) {
		
		List<Business> b = businesses.getBusinesses();
		for (int i = 0; i < businessesADD.getCount(); i++) {
			Business B_add = businessesADD.getBusinesses().get(i);
			b.add(B_add);
		}
		businesses.setBusinesses(b);
	}
}
