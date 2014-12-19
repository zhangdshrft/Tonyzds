package com.tony.babygo.adapter;

import com.tony.babygo.fragments.MyGeneralFragment;
import com.tony.babygo.fragments.MyHomeFragment;
import com.tony.babygo.utils.log.IWLog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyFragmentPageAdapter extends FragmentStatePagerAdapter {
	public static final String TAG = "MyFragmentPageAdapter";

	public static final int NUM_ITEMS = 5;

	private Context context;

	public MyFragmentPageAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int arg0) {
		IWLog.i(TAG, "getItem:" + arg0);
		if (arg0 == 0) {
			return MyHomeFragment.newInstance(arg0, context);
		} else {
			return MyGeneralFragment.newInstance(arg0, context);
		}
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return NUM_ITEMS;
	}
}
