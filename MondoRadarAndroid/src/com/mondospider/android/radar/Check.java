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