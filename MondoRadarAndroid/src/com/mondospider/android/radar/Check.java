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

import android.util.Log;

public class Check {
	public final String LOGTAG = MondoRadar.LOGTAG;

	public static void isNotNull(Object o) {
		if (o == null) {
			Log.e("LOGTAG", "Null is not allowed");
			throw new IllegalArgumentException("Argument can not be null");
		}
	}
	
	public static boolean isWaiting(Thread t){
		return t.getState() == Thread.State.WAITING ;
	}

	public static void isLongerThan3(String spiderLocationApiUrl) {
		if (spiderLocationApiUrl.length() < 3) {
			Log.e("LOGTAG", "Argument is too short");
			throw new IllegalArgumentException("Argument is too short");
		}
	}
}