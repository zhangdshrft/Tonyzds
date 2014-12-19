package com.tony.babygo.cache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.tony.babygo.pic.Bimp;
import com.tony.babygo.pic.PicTool;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageTag, final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageTag)) {
			SoftReference<Drawable> softReference = imageCache.get(imageTag);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl,
						imageTag);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageTag, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	public Drawable loadDrawable(final String imageTag, final String imageUrl, final String imageKey,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageTag)) {
			SoftReference<Drawable> softReference = imageCache.get(imageTag);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl,
						imageTag);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				
				Bitmap bitmap0 = Bimp.drawableToBitmap(drawable);
				
				if (bitmap0 != null) {
					Bitmap b1 = PicTool.getRoundedCornerBitmap(bitmap0, 10);
					Bimp.addBitmapToMemoryCache(imageKey, b1);
				} 
				
				imageCache.put(imageTag, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	public Drawable loadDrawable2(final String imageTag, final String imageUrl, final String imageKey,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageTag)) {
			SoftReference<Drawable> softReference = imageCache.get(imageTag);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl,
						imageTag);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				
				Bitmap bitmap0 = Bimp.drawableToBitmap(drawable);
				
				if (bitmap0 != null) {
					Bimp.addBitmapToMemoryCache(imageKey, bitmap0);
				} 
				
				imageCache.put(imageTag, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl,
				String imageTag);
	}

}
