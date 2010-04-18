package com.mondospider.android.radar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class SpiderSync {
	public static Thread thread;
	public static boolean sync;
	private static int interval = 30 * 1000;
	private static Context context;
	public SpiderSync(Context cont){
		SpiderSync.context = cont;
		thread = new Thread(){
			public void run() {
				while(SpiderSync.sync){
					try{
						String sycnData = LibHTTP.get( SpiderSync.context.getString(R.string.spiderlocation) );
/*						
						String sycnData = "{"
							 + "\"latitude\":30.0,"
							 + "\"longitude\":-139.0,"
							 + "\"datemodified\":\"Thu Mar 25 06:59:21 UTC 2010\","
							 + "}";
*/
						String json = "[" + sycnData + "]";
						JSONArray jsons = new JSONArray(json);
						JSONObject jsonObj = jsons.getJSONObject( 0 );
						double latitude = jsonObj.getDouble("latitude");
						double longitude = jsonObj.getDouble("longitude");
						MondoRadar.setSpiderLocation(latitude,longitude);
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
			SpiderSync.sync = true;
			SpiderSync.thread.start();
		}
	}
	public void stop(){
		if(thread != null){
			SpiderSync.sync = false;
			SpiderSync.thread.stop();
		}
	}
}
