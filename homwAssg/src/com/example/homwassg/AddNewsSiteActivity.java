
package com.example.homwassg;


import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewsSiteActivity extends Activity{
	
	TextView txtmsg;
	EditText typeUrl;
	Button btngo;
	
	RssParser rssparser = new RssParser();
	RssFeed rssfeed;
	//List<RssItem> rssfeed;

	private ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_news_site);
		
		txtmsg = (TextView) findViewById(R.id.txtmsg);
		typeUrl = (EditText) findViewById(R.id.typeUrl);
		btngo = (Button) findViewById(R.id.btngo);
		
		btngo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = typeUrl.getText().toString();
				Log.d("URL length", "" + url.length());
				if (url.length()>0){
					txtmsg.setText("");
					String URLpattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
					if(url.matches(URLpattern)){
						new loadRssFeed().execute(url);
					}else{
						txtmsg.setText("Please try again");
					}
				}else{
					txtmsg.setText("Enter News Url");
				}
			}
		});
	}
	/*
	 * Background AsyncTask to get RSS data from url
	 */
		class loadRssFeed extends AsyncTask<String, String, String>{
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(AddNewsSiteActivity.this);
			pd.setMessage("Fetching information.");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		
			@Override
			protected String doInBackground(String... args) {
				String url = args[0];
				try {
					rssfeed = rssparser.parse(url);
					Log.d("rssfeed" , ""+ rssfeed); 
					if(rssfeed!=null){
						Log.e("rss url", rssfeed.getTitle() + " "+ rssfeed.getDescription() + " "+
							rssfeed.getLink());
						RssDataBase rssdb = new RssDataBase(getApplicationContext());
						NewsSite site = new NewsSite(rssfeed.getTitle(), rssfeed.getDescription(), rssfeed.getLink(), rssfeed.getRssLink(), rssfeed.getPubdate());
						rssdb.insertNewsSite(site);
						Intent i = getIntent();
						setResult(100, i);
						finish();
					}else{
						runOnUiThread(new Runnable(){ //updating ui from Background thread
							@Override
							public void run() {
								txtmsg.setText("try again..");
							}
	
						});
					}
					return null;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return url;

}   
		
		protected void onPostExecute(String args){
			pd.dismiss();
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					if(rssfeed!=null){
						
					}
				}	
			});
		}
	

}

}

