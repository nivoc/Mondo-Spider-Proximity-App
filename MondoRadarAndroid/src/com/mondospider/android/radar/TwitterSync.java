package com.mondospider.android.radar;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mondospider.android.lib.LibHTTP;

/**
 * Use: s = new TwitterSync(APIURL) s.start();
 * 
 * call s.waitAfterCurrCall() to sleep in background call s.notify() to
 * reactivate it
 * 
 * Reqister with addTwitterListener(CLASS) all classes that should be notified
 * with updates. This classes must implement the interface TwitterListener();
 */
public class TwitterSync extends Daemon {
	private String twitterApiUrl;
	private ArrayList<TwitterListener> listenerCollection = new ArrayList<TwitterListener>();

	public TwitterSync(String twitterApiUrl) {
		super();

		Check.isNotNull(twitterApiUrl);
		Check.isLongerThan3(twitterApiUrl);

		this.twitterApiUrl = twitterApiUrl;
	}

	protected void theActualWork() {
		try {
			Log.d(LOGTAG, "Send http req for twitter update - Wait Time (nextSyncIn) was: " + nextSyncIn);
			String responseString = LibHTTP.get(twitterApiUrl);
			// R.string.spidertweeturl

			JSONArray jsons = new JSONArray(responseString);
			ArrayList<HashMap<String, String>> tweetList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < jsons.length(); i++) {
				JSONObject o = jsons.getJSONObject(i);

				// int id = o.getInt("id");
				String text = o.getString("text");
				String createdAt = o.getString("created_at");

				HashMap<String, String> items = new HashMap<String, String>();
				items.put("tweet_date", createdAt);
				items.put("tweet_text", text);
				tweetList.add(items);
			}

			fireUpdate(tweetList);

		} catch (Exception ex) {
			ex.printStackTrace();
			// make sure that it waits a bit after an error
			nextSyncIn = 60;
		}
	}

	void fireUpdate(ArrayList<HashMap<String, String>> tweet_list) {
		for (TwitterListener listener : listenerCollection) {
			listener.onTwitterUpdate(tweet_list);
		}
	}

	void addTwitterListener(TwitterListener l) {
		listenerCollection.add(l);
	}

	interface TwitterListener {
		void onTwitterUpdate(ArrayList<HashMap<String, String>> tweet_list);
	}

}
