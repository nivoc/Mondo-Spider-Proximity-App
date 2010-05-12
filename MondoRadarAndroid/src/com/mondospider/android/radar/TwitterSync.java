/*
 * Copyright (C) 2010 The Mondospider Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mondospider.android.radar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mondospider.android.lib.LibHTTP;
import com.mondospider.android.lib.ReadableDates;
import com.mondospider.android.lib.Util;

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

	SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss ZZZZ yyyy");
	ReadableDates rd = new ReadableDates();

	public TwitterSync(String twitterApiUrl) {
		super();

		Check.isNotNull(twitterApiUrl);
		Check.isLongerThan3(twitterApiUrl);

		this.twitterApiUrl = twitterApiUrl;
	}

	protected void theActualWork() {
		try {
			Log.d(TAG, "Send http req for twitter update - Wait Time (nextSyncIn) was: " + nextSyncIn);
			String responseString = LibHTTP.get(twitterApiUrl);
			// R.string.spidertweeturl

			JSONArray jsons = new JSONArray(responseString);
			ArrayList<HashMap<String, String>> tweetList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < jsons.length(); i++) {
				JSONObject o = jsons.getJSONObject(i);

				// int id = o.getInt("id");
				String text = o.getString("text");
				String createdAt = o.getString("created_at");

				Date dt = format.parse(createdAt);
				createdAt=rd.ReadbleDate(dt);
				
				HashMap<String, String> items = new HashMap<String, String>();
				items.put("tweet_date", createdAt);
				items.put("tweet_text", Util.entity2html(text));
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
