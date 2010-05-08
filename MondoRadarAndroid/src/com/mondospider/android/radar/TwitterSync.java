package com.mondospider.android.radar;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mondospider.android.lib.LibHTTP;

import android.content.Context;
import android.util.Log;

public class TwitterSync {
	public static Thread thread;
	public static boolean sync;
	private static int interval = 30 * 1000;
	private static Context context;
	private static ArrayList<HashMap<String, String>> tweet_list;
	private static HashMap<String, String> items;
	public TwitterSync(Context cont){
		TwitterSync.context = cont;
		tweet_list = new ArrayList<HashMap<String, String>>();
		thread = new Thread(){
			@Override
			public void run() {
				while(TwitterSync.sync){
					try{
						String json = LibHTTP.get( TwitterSync.context.getString(R.string.spidertweeturl) );
						/*
						String sycnData = "{"
							 + "\"latitude\":35.728926,"
							 + "\"longitude\":139.71038,"
							 + "\"datemodified\":\"Thu Mar 25 06:59:21 UTC 2010\","
							 + "}";
						 */
						HashMap<String, String> items;
						if(json.indexOf("Host is unresolved") >= 0){
							MondoRadar.unconnectToast.show();
						}
//						Log.d("Debug",json);
						JSONArray jsons = new JSONArray(json);
						
						tweet_list.clear();
						for (int i = 0; i < jsons.length(); i++) {
						    JSONObject jsonObj = jsons.getJSONObject(i);
						    int id = jsonObj.getInt("id");
						    String text = jsonObj.getString("text");
						    String created_at = jsonObj.getString("created_at");
						    
//						    Log.d("Debug",String.valueOf(id));
//						    Log.d("Debug",text);
//						    Log.d("Debug",created_at);
//						    Log.d("Debug","------------------------------------");
//						    Log.d("Debug","------------------------------------");
//						    Log.d("Debug","------------------------------------");
//						    Log.d("Debug","------------------------------------");
						    
							items = new HashMap<String, String>();
							items.put("tweet_date", created_at );
							items.put("tweet_text", text );
							tweet_list.add(items);
						}
						MondoRadar.setSpiderTweet(tweet_list);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
					try{
						Thread.sleep( interval );
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
				}
			};
		};

	}
	public void start(){
		if(thread != null){
			TwitterSync.sync = true;
			TwitterSync.thread.start();
		}
	}
	public void stop(){
		if(thread != null){
			TwitterSync.sync = false;
			TwitterSync.thread.stop();
		}
	}
}
