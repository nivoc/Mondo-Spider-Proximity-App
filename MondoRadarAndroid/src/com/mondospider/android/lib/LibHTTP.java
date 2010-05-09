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

package com.mondospider.android.lib;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpVersion;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;


public class LibHTTP {
	final HttpParams httpParams = new BasicHttpParams();
	public LibHTTP(){
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
	}

	public static String get(String url){    	
		String ReturnHTML = "";
//		Log.d("LibHTTP->get->url",url);
        try{ 
          URLConnection urlConn = new URL( url ).openConnection(); 
          InputStream is = urlConn.getInputStream(); 
          BufferedInputStream bis = new BufferedInputStream(is,16000); 
          ByteArrayBuffer baf = new ByteArrayBuffer(50);
          int current = 0; 
          while((current = bis.read()) != -1){ 
                baf.append((byte)current); 
          } 
          ReturnHTML = new String(baf.toByteArray()); 
        }catch(Exception e){ 
        	ReturnHTML = e.getMessage(); 
        }
//        Log.d("LibHTTP->get->ReturnHTML", ReturnHTML);
		return ReturnHTML;
	}
}
