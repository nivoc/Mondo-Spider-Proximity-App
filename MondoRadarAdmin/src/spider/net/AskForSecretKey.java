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

package spider.net;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AskForSecretKey extends Activity implements OnClickListener {

	public static final String PREFS_NAME = "GlobalPrefs";
	public static final String LOG = "ButtonFight";

    private Button mSubmitButton = null;
	private EditText mSecretKeyInput;
	private SharedPreferences mSettings;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.ask_for_secret_key);


    	mSubmitButton = (Button) findViewById(R.id.submit);
    	mSecretKeyInput = (EditText) findViewById(R.id.secretKeyEdit);
    	

        mSettings = getSharedPreferences(PREFS_NAME, 0);
        mSecretKeyInput.setText(mSettings.getString("secretKey", ""));
        
        
    	mSubmitButton.setOnClickListener(this);
//    	

    }

	@Override
	public void onClick(View v) {
 	 // Store preferences
     
      
      
      // Save user preferences. We need an Editor object to
      // make changes. All objects are from android.context.Context
      SharedPreferences.Editor editor = mSettings.edit();
      editor.putString("secretKey", mSecretKeyInput.getText().toString().trim());

      // Don't forget to commit your edits!!!
      editor.commit();

      
      finish();
	}

}