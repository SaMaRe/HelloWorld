package com.example.homwassg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;

public class MainActivity extends Activity {
	private static final int SERVICE_CONNECTION_ACK = 1;
	private static final int MESSAGE_RECEIVED = 2;
	
	private Messenger messenger = new Messenger(new Handler(){
		@Override
		public void handleMessage(Message  msg){
			Log.i("MainAcitivity","got message");
			if(msg.what==MESSAGE_RECEIVED){

				Bundle data =msg.getData();
				String message = data.getString("message");
				Log.i("MainActivity","received message" + message);
				TextView tv =(TextView) MainActivity.this.findViewById(R.id.message);
				tv.setText(message);
			}
		}
	});
	private ServiceConnection serviceConn = new ServiceConnection(){
		@Override
		public void onServiceConnected (ComponentName className , IBinder service){
			Log.i("MainActivity", "bound to service");
			Messenger serviceMessenger = new Messenger(service);
			Message message =Message.obtain();
			message.what = SERVICE_CONNECTION_ACK;
			message.replyTo = messenger;
			try{
				serviceMessenger.send(message);
			}catch(RemoteException e){
				e.printStackTrace();
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName className){
			
		}
	};
		private ProgressDialog pd;
		ArrayList<HashMap<String, String>> RssFeedList;
		RssParser rssparser = new RssParser();
		RssFeed rssfeed;
		
		Button BtnAddNewsSite;
		
		String[] sqliteIds;
		String THE_ID = "id";
		String THE_TITLE = "title";
		String THE_LINK = "link";
		String THE_PUBDATE = "pubDate";
		
		ListView lv;
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			Intent i = new Intent(this , MainService.class);
			startService(i);
			bindService(i, serviceConn, 0);
			
			BtnAddNewsSite = (Button) findViewById(R.id.BtnAddNewsSite);
			RssFeedList = new ArrayList<HashMap<String, String>>();
		
			new loadNewsSite().execute();
			lv = (ListView) findViewById(R.id.newslist);
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					String sqliteId = ((TextView) view.findViewById(R.id.sqliteId)).getText().toString();
					Intent i = new Intent(getApplicationContext() , ListNewsItemsActivity.class);
					i.putExtra(THE_ID , sqliteId);
					startActivity(i);
			}
			
		});
		//problems!!!
		BtnAddNewsSite.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext() , AddNewsSiteActivity.class);
				startActivityForResult(i, 100);
			}
		});
		
	}
		@Override
		protected void onActivityResult(int requestCode, int resultCode,Intent data){
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode==100){
				Intent i = getIntent();
				finish();
				startActivity(i);
			}
		}
		
		
		class loadNewsSite extends AsyncTask<String , String, String >{

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(MainActivity.this);
				pd.setMessage("Loading website..");
				pd.setIndeterminate(false);
				pd.setCancelable(false);
				pd.show();
			}
			
			@Override
			protected String doInBackground(String... args) {
				runOnUiThread(new Runnable(){
					public void run(){
						RssDataBase rssdb = new RssDataBase(
								getApplicationContext());
			
						List<NewsSite> newsList = rssdb.getSite();
						sqliteIds = new String[newsList.size()];
						for(int i = 0; i< newsList.size(); i++){
							NewsSite nS = newsList.get(i);
							HashMap<String, String> HMap = new HashMap<String, String>();
							HMap.put(THE_ID, nS.getId().toString());
							HMap.put(THE_TITLE, nS.getTitle());
							HMap.put(THE_LINK,  nS.getLink());
							RssFeedList.add(HMap);
							
							sqliteIds[i] = nS.getId().toString();
							
						}
						ListAdapter adapter = new SimpleAdapter(
							  MainActivity.this,RssFeedList, R.layout.activity_main1, new String[]{
								THE_ID, THE_TITLE,THE_LINK},
								new int[] { R.id.sqliteId, R.id.title, R.id.link});
						lv.setAdapter(adapter);
						registerForContextMenu(lv);
					}
				});
				return null;
						
			}
			
			protected void onPostExecute(String args){
				//pd.dismiss();
				pd.dismiss();
			}
			
		}
		@Override
		protected void onDestroy(){
			super.onDestroy();
			unbindService(serviceConn);
		}

}
