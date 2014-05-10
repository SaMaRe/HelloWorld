package com.example.homwassg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisplayingNewsPage extends Activity {
	WebView wb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_news);
		Intent i = getIntent();
		String url_link = i.getStringExtra("url_link");
		
		wb = (WebView) findViewById(R.id.NewsPage);
		wb.getSettings().setJavaScriptEnabled(true);
		wb.loadUrl(url_link);
		wb.setWebViewClient(new DisplayingNewsPageClient());	
	}

	@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if ((keyCode == KeyEvent.KEYCODE_BACK) && wb.canGoBack()) {
	            wb.goBack();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	private class DisplayingNewsPageClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
