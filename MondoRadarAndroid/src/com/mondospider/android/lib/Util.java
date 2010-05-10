package com.mondospider.android.lib;

public class Util {
	public static String entity2html(String str){
		return str
			.replaceAll("&lt;br ?/?&gt;","\n")
			.replaceAll("&lt;","<")
			.replaceAll("&gt;",">");
	}
}
