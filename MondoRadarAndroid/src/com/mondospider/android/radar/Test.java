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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Test extends Activity {

     /** Called when the activity is first created. */ 
    @Override 
    public void onCreate(Bundle icicle) { 
         super.onCreate(icicle); 
         setContentView(R.layout.test); 
 TextView tv = (TextView)findViewById(R.id.eat_art_description2);
 tv.setLinksClickable(true);
 /*
  * 
	    android:textColorLink="#5795FC"
	    android:textColorHighlight="#EEEEEE"
	    android:textColor="#FFFFFF"
  */
 tv.setClickable(false);
 tv.setLongClickable(false);
 
 		tv.setMovementMethod(LinkMovementMethod.getInstance());
    } 
} 