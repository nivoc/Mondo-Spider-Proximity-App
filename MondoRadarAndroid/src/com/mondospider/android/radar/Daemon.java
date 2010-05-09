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

/**
 * 
 * 
 */
abstract public class Daemon extends Thread {
	public static String TAG = MondoRadar.TAG;
	private boolean pause = false;
	// inital in 60 sec
	protected int nextSyncIn = 60;
	
	public Daemon() {
		setDaemon(true);		
	}

	public void run() {
		while (! isInterrupted()) {
			try {
				if (pause == true) {
					synchronized( this ) {
						this.wait();
					}
				}	
				
				//do the work
				theActualWork();
				Thread.sleep(nextSyncIn*1000);

			} catch ( InterruptedException e ) { 
			    // do nothing - It's a normal wakeup
		    } catch (Exception ex) {
				ex.printStackTrace();
				Log.e(TAG, "");
				// make sure that it waits a bit after an error
				nextSyncIn=120;
			}
		}
	};

	protected abstract void theActualWork();

	public void waitAfterCurrCall() {
		pause = true;
	}
	
	public void resumeWork(){
		synchronized( this ) {
			pause = false;
			this.notify();
		}
	}


}
