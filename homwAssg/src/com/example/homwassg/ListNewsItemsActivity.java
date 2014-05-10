
package com.example.homwassg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListNewsItemsActivity extends ListActivity{
	
	private ProgressDialog pd;
	//Array list for ListView
	ArrayList<HashMap<String, String>> NewsItemList = new ArrayList<HashMap<String,String>>();
	
	RssParser rssparser = new RssParser();
	
	List<RssItem> rssItems = new ArrayList<RssItem>();
	RssFeed rssfeed;
	private static String THE_TITLE = "title";
	private static String THE_DESCRIPTION = "description";
	private static String THE_LINK ="link";
	private static String THE_PUBDATE ="pubDate";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);
		Intent i = getIntent();
		Integer row_id = Integer.parseInt(i.getStringExtra("id"));
		RssDataBase rssdb = new RssDataBase(getApplicationContext());
		
		NewsSite ns = rssdb.getSite(row_id);
		String rsslink = ns.getRssLink();
		
		new loadNewsFeed().execute(rsslink);
	

		
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = new Intent(getApplicationContext() , DisplayingNewsPage.class);
				String url_link = ((TextView) view.findViewById(R.id.url_link)).getText().toString();
				Toast.makeText(getApplicationContext(), url_link, Toast.LENGTH_SHORT).show();
				i.putExtra("url_link", url_link);
				startActivity(i);
			}
			
		});
	
		

		
	}
	
	class loadNewsFeed extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(ListNewsItemsActivity.this);
			pd.setMessage("Updating recent news");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) { 
			String  url_link = args[0];
				rssItems = rssparser.getItems(); //problems!!
				for(RssItem item :rssItems){
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(THE_TITLE, item.getTitle());
				hashMap.put(THE_DESCRIPTION, item.getLink());
				hashMap.put(THE_PUBDATE, item.getPubdate());
				String description = item.getDescription();
				if (description.length()>100){
					description = description.substring(0, 80)+"...";
				}
				hashMap.put(THE_DESCRIPTION, description);
				NewsItemList.add(hashMap);	
				}
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						//Updating parsed news item into listView
						ListAdapter adapter = new SimpleAdapter(
								ListNewsItemsActivity.this,NewsItemList, R.layout.news_list_row,
								new String[] {THE_TITLE, THE_DESCRIPTION,THE_LINK, THE_PUBDATE},
								new int[] {R.id.url_link, R.id.title, R.id.pubdate, R.id.link});
						setListAdapter(adapter);
					}
					
				});
				return  null ;
				
		
			
		}
	
		
		protected void onPostExecute(String args){
			pd.dismiss();
		}
	}
}
 
 