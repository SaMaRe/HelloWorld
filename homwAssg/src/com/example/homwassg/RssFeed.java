
package com.example.homwassg;

import java.util.List;

public class RssFeed {

    public String Title;
    public String Link;
    public String Description;
    public String RssLink;
    public String Pubdate;
    List<RssItem> items;

    public RssFeed(String title, String link,String rsslink, String description, String pubdate) {
       this.Title = title;
       this.Link = link;
       this.RssLink = rsslink;
       this.Description = description;
       this.Pubdate = pubdate;
       
        
    }
    public void setItems(List<RssItem> items){
    	this.items = items;
    }
    public List<RssItem> getItems(){
    	return this.items;
    }
    public String getTitle() {
        return this.Title;
    }

    public String getDescription() {
        return Description;
    }
    public String getLink() {
        return this.Link;
    }
    public String getRssLink() {
		return this.RssLink;
	}

    public String getPubdate(){
    	return this.Pubdate;
    }
	

}
