package com.mondospider.android.radar;

import android.util.Log;

/**
 * 
 * 
 */
abstract public class Daemon extends Thread {
	public static String LOGTAG = MondoRadar.LOGTAG;
	private boolean pause = false;
	// inital in 60 sec
	protected int nextSyncIn = 60 * 1000;
	
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
				Thread.sleep(nextSyncIn);

			} catch ( InterruptedException e ) { 
			    // do nothing - It's a normal wakeup
		    } catch (Exception ex) {
				ex.printStackTrace();
				Log.e(LOGTAG, "MEMEM");
				// make sure that it waits a bit after an error
				nextSyncIn=60;
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
