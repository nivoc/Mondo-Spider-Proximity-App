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

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mondospider.android.lib.LibHTTP;

/**
 * Use: s = new SpiderSync(APIURL) s.start();
 * 
 * call s.waitAfterCurrCall() to sleep in background call s.notifyresumeWork()
 * to reactivate it
 * 
 * Reqister with addSpiderListener(CLASS) all classes that should be notified
 * with updates. This classes must implement the interface SpiderListener();
 */
public class SpiderSync extends Daemon {
	private String spiderLocationApiUrl;
	private ArrayList<SpiderListener> listenerCollection = new ArrayList<SpiderListener>();

	public SpiderSync(String spiderLocationApiUrl) {
		super();

		Check.isNotNull(spiderLocationApiUrl);
		Check.isLongerThan3(spiderLocationApiUrl);

		this.spiderLocationApiUrl = spiderLocationApiUrl;
	}

	protected void theActualWork() {
		try {
			Log.d(LOGTAG, "Send http req for spider update - Wait Time (nextSyncIn) was: " + nextSyncIn);
			String responseString = LibHTTP.get(spiderLocationApiUrl);

			/*
			 * String responseString = "{" + "\"latitude\":35.728926," +
			 * "\"longitude\":139.71038," +
			 * "\"datemodified\":\"Thu Mar 25 06:59:21 UTC 2010\"," + "}";
			 */

			JSONObject jsonObj = new JSONObject(responseString);

			final double latitude = jsonObj.getDouble("latitude");
			final double longitude = jsonObj.getDouble("longitude");
			int nextSyncIn2 = jsonObj.getInt("next_update_in") * 1000;
			nextSyncIn = nextSyncIn2;
			fireUpdate(latitude, longitude);

		} catch (Exception ex) {
			ex.printStackTrace();
			// make sure that it waits a bit after an error
			nextSyncIn = 60;
		}
	}

	void fireUpdate(double latitude, double longitude) {
		for (SpiderListener listener : listenerCollection) {
			listener.onSpiderUpdate(latitude, longitude);
		}
	}

	void addSpiderListener(SpiderListener l) {
		listenerCollection.add(l);
	}

	interface SpiderListener {
		void onSpiderUpdate(double latitude, double longitude);
	}
}
