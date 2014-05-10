package com.example.homwassg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class RssParser {
	private ArrayList<RssItem> items = new ArrayList<RssItem>();
	private RssItem currentItem = null;
	
	public RssFeed parse(String url) throws XmlPullParserException, IOException{
		RssFeed rssfeed = null;

		url = "http://rss.cnn.com/rss/cnn_topstories.rss";
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		xpp.setInput(new URL(url).openStream(),null);
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if(eventType == XmlPullParser.START_DOCUMENT) {
				Log.i("MyPullParser","Start document");
			} else if(eventType == XmlPullParser.START_TAG) {
				if(xpp.getName().equals("item")){
					currentItem = new RssItem();
				}else if(xpp.getName().equals("title") && currentItem != null){
					currentItem.Title = xpp.nextText();
				}else if(xpp.getName().equals("description") && currentItem != null){
					currentItem.Description = xpp.nextText();
				}else if(xpp.getName().equals("link") && currentItem !=null){
					currentItem.Link = xpp.nextText();
				}else if(xpp.getName().equals("pubdate") && currentItem !=null){
					currentItem.Pubdate = xpp.nextText();
				}
			} else if(eventType == XmlPullParser.END_TAG) {
				String tag = xpp.getName();
				if(xpp.getName().equals("item")){
					items.add(currentItem);
				}
			}
			eventType = xpp.next();
		}
		Log.i("MyPullParser","End document");
		Log.i("MyPullParser","We received: "+items.size());
		return rssfeed;
		
	}
	@Override
	public String toString(){
		String prettyPrint = "";
		for(int i = 0; i < items.size(); i++){
			prettyPrint = prettyPrint+
					"\n"+
					"---Title: "+items.get(i).getTitle()+"\n"+
					"---Description: "+items.get(i).getDescription()+ "\n"+
					"---Link: "+items.get(i).getLink()+ "\n" +
					"---Pubdate: "+items.get(i).getPubdate();
		}
		return prettyPrint;
	}
	public ArrayList<RssItem> getItems(){
		return items;
	}
}