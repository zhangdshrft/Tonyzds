/*
 * Copyright 2014 trinea.cn All right reserved. This software is the
 * confidential and proprietary information of trinea.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with trinea.cn.
 */
package com.tony.babygo.adapter;

import java.util.List;

import com.tony.babygo.MyBrowserActivity;
import com.tony.babygo.data.DianpingUserData;
import com.tony.babygo.model.Business;
import com.tony.babygo.model.Businesses;
import com.tony.babygo.pic.Bimp;

import cn.trinea.android.common.util.ListUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;  

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context       context;
    private List<Integer> imageIdList;
    private Businesses businesses;

    public ImagePagerAdapter(Context context, List<Integer> imageIdList, Businesses businesses){
        this.context = context;
        this.imageIdList = imageIdList;
        this.businesses = businesses;
    }

    @Override
    public int getCount() {
    	
    		if (businesses.getBusinesses().size() >= 4) {
    			return 4;
    		} else {
    			return businesses.getBusinesses().size();
    		}
    	
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        
        Business business = businesses.getBusinesses().get(position);
        Bitmap bitmap = Bimp.getBitmapFromMemoryCache(business.getPhoto_url());
        if (bitmap != null) {
        		imageView.setImageBitmap(bitmap);
        } else {
        	 	imageView.setImageResource(imageIdList.get(0));
        }
        
        ((ViewPager)container).addView(imageView, 0);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent goBrowserIntent = new Intent(context, MyBrowserActivity.class);
				goBrowserIntent.putExtra("html5url", businesses.getBusinesses().get(DianpingUserData.cur_scroll_page_index).getBusiness_url());
				context.startActivity(goBrowserIntent);
			}
		});
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((ImageView)object);
    }
}
