package com.tony.babygo;

import com.tony.babygo.utils.log.IWLog;
import com.umeng.analytics.MobclickAgent;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyBrowserActivity extends BaseActivity {
	public static final String TAG = "MyBrowserActivity";

	private WebView webView;

	private ProgressBar progressBar;

	private TextView titleText;

	private TextView backText;

	private TextView homeText;

	private String html5Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	public void initView() {

		html5Url = getIntent().getExtras().getString("html5url");
		setContentView(R.layout.my_browser);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		titleText = (TextView) findViewById(R.id.web_title);
		backText = (TextView) findViewById(R.id.web_back);
		homeText = (TextView) findViewById(R.id.web_home);

		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"fontawesome-webfont.ttf");
		backText.setTypeface(font);
		homeText.setTypeface(font);

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		webView.loadUrl(html5Url);

		homeText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		backText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webView.canGoBack()) {
					webView.goBack();
				}
				if (!webView.canGoBack()) {
					finish();
				}
			}
		});

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}

		});

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String title) {
				if (!title.equalsIgnoreCase("")) {
					IWLog.i(TAG, "title:" + title);
					titleText.setText(title);
				}
				super.onReceivedTitle(view, title);

			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				IWLog.i(TAG, "newProgress=" + String.valueOf(newProgress));
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && !webView.canGoBack()) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyBrowserActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyBrowserActivity");
		MobclickAgent.onPause(this);
	}
}
