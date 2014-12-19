package com.tony.babygo.adapter;

import com.tony.babygo.R;
import com.tony.babygo.cache.AsyncImageLoader;
import com.tony.babygo.cache.AsyncImageLoader.ImageCallback;
import com.tony.babygo.model.Business;
import com.tony.babygo.model.Businesses;
import com.tony.babygo.pic.Bimp;
import com.tony.babygo.pic.PicTool;
import com.tony.babygo.utils.log.IWLog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BusinessAdapter extends BaseAdapter {

	public static final String TAG = "BusinessAdapter";

	private Fragment f;  
	private Context context;
	private Businesses businesses;
	private ListView businessListView;
	private LayoutInflater mInflater;
	private AsyncImageLoader asyncImageLoader;

	public BusinessAdapter(Fragment f, Context context,
			Businesses businesses, ListView businessListView) {
		IWLog.i(TAG, "BusinessAdapter");
		this.f = f;
		this.context = context;
		this.businesses = businesses;
		this.businessListView = businessListView;
		this.mInflater = LayoutInflater.from(context);
		asyncImageLoader = new AsyncImageLoader();
	}

	@Override
	public int getCount() {
		return businesses.getBusinesses().size();
	}

	@Override
	public Object getItem(int position) {
		if (businesses != null && businesses.getBusinesses().size() > 0) {
			return businesses.getBusinesses().get(position);
		}
		return null;
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
			convertView = mInflater.inflate(R.layout.business_list_item, null);  
			holder.addressText = (TextView) convertView.findViewById(R.id.address_text);
			holder.nameText = (TextView) convertView.findViewById(R.id.name_text);
			holder.avgPriceText = (TextView) convertView.findViewById(R.id.avg_price_text);
			holder.sPhotoImage = (ImageView) convertView.findViewById(R.id.s_photo_image);
			holder.starsImage = (ImageView) convertView.findViewById(R.id.stars_image);
			holder.tuanImage = (ImageView) convertView.findViewById(R.id.tuan_image);
			holder.dingImage = (ImageView) convertView.findViewById(R.id.ding_image);
			holder.huiImage = (ImageView) convertView.findViewById(R.id.hui_image);
			convertView.setTag(holder);  
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tuanImage.setVisibility(View.VISIBLE);  
		holder.dingImage.setVisibility(View.VISIBLE);  
		holder.huiImage.setVisibility(View.VISIBLE);  
		
		Business business = businesses.getBusinesses().get(position);
		holder.addressText.setText(business.getAddress());
		holder.nameText.setText(business.getName());
		holder.avgPriceText.setText(String.valueOf(business.getAvg_price()) + "Ԫ");  
		
		if (business.getHas_deal() == 0) {
			holder.tuanImage.setVisibility(View.GONE);   
		}
		
		if (business.getHas_online_reservation() == 0) {
			holder.dingImage.setVisibility(View.GONE);   
		}
		
		if (business.getHas_coupon() == 0) {
			holder.huiImage.setVisibility(View.GONE);   
		}
		
		Bitmap bitmap = Bimp.getBitmapFromMemoryCache(business.getS_photo_url());
		
		if (bitmap != null) {
			holder.sPhotoImage.setImageBitmap(bitmap);
		} else {
			String sPhotoImageUrl = business.getS_photo_url();
			String sPhotoImageTag = business.getS_photo_url();
			holder.sPhotoImage.setTag(sPhotoImageTag);
			asyncImageLoader.loadDrawable(sPhotoImageTag, sPhotoImageUrl,
					sPhotoImageUrl, new ImageCallback() {
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl, String imageTag) {
							ImageView imageViewByTag = (ImageView) businessListView
									.findViewWithTag(imageTag);
							if (imageViewByTag != null) {
								Bitmap bitmap = PicTool.getRoundedCornerBitmap(PicTool.drawableToBitmap(imageDrawable), 10);
								imageViewByTag.setImageBitmap(bitmap);
							}
						}
					});
		}
		
		Bitmap bitmapR = Bimp.getBitmapFromMemoryCache(business.getRating_s_img_url());
		
		if (bitmapR != null) {
			holder.starsImage.setImageBitmap(bitmapR);
		} else {
			String starsImageUrl = business.getRating_s_img_url();
			String starsImageTag = business.getRating_s_img_url();
			holder.starsImage.setTag(starsImageTag);         
			Drawable cachedStarsImage = asyncImageLoader.loadDrawable2(
					starsImageTag, starsImageUrl, starsImageUrl, new ImageCallback() {
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl, String imageTag) {
							ImageView imageViewByTag = (ImageView) businessListView
									.findViewWithTag(imageTag);
							if (imageViewByTag != null) {  
								imageViewByTag.setImageDrawable(imageDrawable);
							}  
						}
					});
			if (cachedStarsImage == null) {
			} else if (cachedStarsImage != null) {  
				holder.starsImage.setImageDrawable(cachedStarsImage);   
				
			}
		}
		
		return convertView;
	}
	
	class ViewHolder {
		private ImageView sPhotoImage;
		private ImageView starsImage;
		private TextView nameText;
		private TextView addressText;
		private TextView avgPriceText;
		private ImageView tuanImage;
		private ImageView dingImage;
		private ImageView huiImage;
	}

}
