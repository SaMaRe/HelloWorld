package com.example.homwassg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RssDataBase extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "news_db";
    private static final String TABLE_NAME = "RssNewsReader";
	private static final String ID = "id";
	private static final String TITLE ="title";
	private static final String DESCRIPTION = "description";
	private static final String LINK ="link";
	private static final String RSSLINK ="rsslink";
	private static final String PUBDATE ="pubdate";
		
	private static final String CREATE_TABLE = "CREATE TABLE "+
			TABLE_NAME+" ("+
			ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
			TITLE+" TEXT, "+
			DESCRIPTION+" TEXT, "+
			LINK+" TEXT, "+
			PUBDATE+" TEXT)";
		
	public RssDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL( CREATE_TABLE);
		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP EXISTING TABLE " + TABLE_NAME);
		db.execSQL("DROP TABLE IF EXITS" + DATABASE_NAME );

		onCreate(db);	
	}
	
	private static final String [] COLUMNS = {ID, TITLE, DESCRIPTION, LINK, PUBDATE};
	
	public void insertNewsSite(NewsSite site){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TITLE, site.getTitle());
		cv.put(DESCRIPTION, site.getDescription());
		//cv.put(LINK, site.getLink());
		cv.put(PUBDATE, site.getPubdate());
		db.insert(TABLE_NAME, null, cv);
		
		updateSite(site);
		db.close();
	}
	public NewsSite getSite(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, COLUMNS, " id = ?", new String[] {String.valueOf(id) }, null, null, null, null);
		
		if (cursor!= null)
			cursor.moveToFirst();
		NewsSite ns = new NewsSite();
		ns.setId(Integer.parseInt(cursor.getString(0)));
		ns.setTitle(cursor.getString(1));
		ns.setDescription(cursor.getString(2));
		ns.setLink(cursor.getString(3));
		ns.setRssLink(cursor.getString(4));
		ns.setPubdate(cursor.getString(5));
		
		cursor.close();
		db.close();
		
		
		//Log.d("get news("+id+")", ns.toString()); // added today
		return ns;
		
	}
	
	public List<NewsSite> getSite(){
		List<NewsSite> newsList = new ArrayList<NewsSite>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME ;
		//SQLiteDatabase db = this.getReadableDatabase();
		SQLiteDatabase db = this.getWritableDatabase();

		//Cursor cursor = db.rawQuery(selectQuery, null);
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				NewsSite ns = new NewsSite();
				ns.setId(Integer.parseInt(cursor.getString(0)));
				ns.setTitle(cursor.getString(1));
				ns.setDescription(cursor.getString(2));
				ns.setLink(cursor.getString(3));
				ns.setRssLink(cursor.getString(4));
				ns.setPubdate(cursor.getString(5));
				newsList.add(ns);
			}while(cursor.moveToNext());
		}
		//Log.d("getSite()", newsList.toString());
		cursor.close();
		db.close();
		return newsList;
		
	}

	public int updateSite(NewsSite site) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv =  new ContentValues();
		cv.put(TITLE, site.getTitle());
		cv.put(DESCRIPTION, site.getDescription());
		cv.put(LINK, site.getLink());
		cv.put(PUBDATE, site.getPubdate());
		
		int update = db.update(TABLE_NAME, cv, ID + " = ?", 
				new String[]{String.valueOf(site.getId())});
		db.close();
		return update;	
	}

	
	

	public void deleteNewsSite(NewsSite site){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ID +"=?", new String[]{String.valueOf(site.getId()) });
		db.close();
		Log.d("deleteNews", site.toString());
	}
	

}
