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
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.mondospider.android.overlay.SpiderItemizedOverlay;
import com.mondospider.android.radar.SpiderSync.SpiderListener;
import com.mondospider.android.radar.TwitterSync.TwitterListener;

public class MondoRadar extends MapActivity implements LocationListener,
		SpiderListener, TwitterListener, SensorEventListener, OnClickListener {

	// Debugging
	public static final String TAG = "Mondospider";

	// Layout Views
	private ImageView mRadarSpinView;
	private SeekBar mSeekBar;
	private SlidingDrawer mNewsDrawer;
	private SlidingDrawer mInfoDrawer;
	private ImageView mCompassImageView;
	private TextView mDistanceTextView;

	private static final long UPD_MIN_TIME = 0;
	private static final float UPD_MIN_DISTANCE = 0;
	private static double lat = 0;
	private static double lon = 0;
	private static double dis = 0;
	private static double bea = 0;
	private static double north = 0;
	private static float northFloat = 0;
	private static float lastNorth = 0;
	private static float lastDegree = 0;
	private static float beaFloat = 0;
	private static LocationManager locationmanager;
	private static Location mSpiderLoc = new Location("mondo_spider");
	private static Location mLastUserloc;

	private static float[] mValues;

	private static RotateAnimation rotate;
	private static RotateAnimation rotate_map;

	private static SensorManager sensormanager;
	private static Sensor sensor;

	private static MapController mapctrl;
	private static MapView mapview;

	private static SpiderSync spiderSync;
	private static TwitterSync twitterSync;

	private static SpiderItemizedOverlay spiderOverlay;

	private static GeoPoint geoPoint;
	private static GeoPoint mSpiderPoint;
	final static Handler thread_handler = new Handler();

	private ViewFlipper layoutswitcher;
	private Animation in_from_left;
	private Animation out_from_left;
	private Animation in_from_right;
	private Animation out_from_right;

	private static ListView twitter_listview;
	private static ArrayList<HashMap<String, String>> tweet_current_list;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		// Resize Background based on screen res
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int DisplayWidth = display.getWidth();
		int DisplayHeight = display.getHeight();
		ImageView radar_background = (ImageView) findViewById(R.id.radar);
		if (DisplayWidth == 480 && DisplayHeight == 800) {
			radar_background.setImageDrawable(getResources().getDrawable(
					R.drawable.radar_800_480));
		}

		// Registrer OnClicks
		findViewById(R.id.news_button).setOnClickListener(this);
		findViewById(R.id.info_button).setOnClickListener(this);
		findViewById(R.id.news_button_01).setOnClickListener(this);
		findViewById(R.id.info_button_01).setOnClickListener(this);
		findViewById(R.id.news_button_02).setOnClickListener(this);
		findViewById(R.id.info_button_02).setOnClickListener(this);
		findViewById(R.id.radar_info_backwards_arrow_01).setOnClickListener(this);
		findViewById(R.id.radar_info_backwards_arrow_02).setOnClickListener(this);

		//Set eatart links clickable
		((TextView)findViewById(R.id.eat_art_description)).setMovementMethod(LinkMovementMethod.getInstance());
		
		// Find UI-Elements
		mNewsDrawer = (SlidingDrawer) findViewById(R.id.slidenews);
		mInfoDrawer = (SlidingDrawer) findViewById(R.id.slideinfo);
		mRadarSpinView = (ImageView) findViewById(R.id.radar_spin);
		mDistanceTextView = (TextView) findViewById(R.id.dis_m);
		mSeekBar = (SeekBar) findViewById(R.id.seekbar_zoom);

		// Setup ZoomBar
		mSeekBar.setMax(10);
		mSeekBar.setProgress(6);
		mSeekBar.setOnClickListener(this);

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromTouch) {
				Log.d("onProgressChanged()", String.valueOf(progress) + ", "
						+ String.valueOf(fromTouch));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.d("onStartTrackingTouch()", String.valueOf(seekBar
						.getProgress()));
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d("onStopTrackingTouch()", String.valueOf(seekBar
						.getProgress()));
				MondoRadar.mapctrl.setZoom(seekBar.getProgress() + 10);
			}
		});

		AnimationSet set = new AnimationSet(true);

		AlphaAnimation alpha = new AlphaAnimation(0, 0.6f);
		alpha.setDuration(800);
		alpha.setRepeatCount(-1);
		set.addAnimation(alpha);

		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.rotate_indefinitely);
		set.addAnimation(animation);
		set.setInterpolator(new LinearInterpolator());

		mRadarSpinView.startAnimation(set);

		mapview = (MapView) findViewById(R.id.mapview);
		Drawable spider_point = getResources().getDrawable(
				R.drawable.spider_point_00);

		spiderOverlay = new SpiderItemizedOverlay(spider_point);

		MondoRadar.mapctrl = mapview.getController();
		MondoRadar.mapctrl.setZoom(16);

		sensormanager = (SensorManager) getSystemService("sensor");
		sensor = sensormanager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		MondoRadar.mapview.setSatellite(false);
		mCompassImageView = (ImageView) findViewById(R.id.compass);


		locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location lc = locationmanager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (lc == null) {
			lc = locationmanager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (lc != null) {
			onLocationChanged(lc);
		}

		if (lc != null) {
			lat = lc.getLatitude();
			lon = lc.getLongitude();
		}

		String[] tweet_date_dummy = new String[] { "News feed from Mondo Spider" };
		String[] tweet_text_dummy = new String[] { "Update the Mondospider news from twitter. You can see any moving." };
		twitter_listview = (ListView) findViewById(R.id.twitter_listview);
		// twitter_listview.setClickable(false);
		tweet_current_list = new ArrayList<HashMap<String, String>>();

		final ArrayList<HashMap<String, String>> tweet_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> items;
		for (int i = 0; i < tweet_date_dummy.length; i++) {
			items = new HashMap<String, String>();
			items.put("tweet_date", tweet_date_dummy[i]);
			items.put("tweet_text", tweet_text_dummy[i]);
			tweet_list.add(items);
		}
		final SimpleAdapter sa = new SimpleAdapter(this, tweet_list,
				R.layout.tweet_row,
				new String[] { "tweet_date", "tweet_text" }, new int[] {
						R.id.tweet_date, R.id.tweet_text });
		twitter_listview.setAdapter(sa);
		twitter_listview
				.setOnScrollListener(new AbsListView.OnScrollListener() {

					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// Do nothings
					}

					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// TODO
						// Do somethings
					}
				});

		layoutswitcher = (ViewFlipper) findViewById(R.id.layoutswitcher);

		in_from_left = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
		out_from_left = AnimationUtils
				.loadAnimation(this, R.anim.out_from_left);
		in_from_right = AnimationUtils
				.loadAnimation(this, R.anim.in_from_right);
		out_from_right = AnimationUtils.loadAnimation(this,
				R.anim.out_from_right);

	}

	@Override
	protected void onPause() {
		destroyListeners();
		spiderSync.waitAfterCurrCall();
		twitterSync.waitAfterCurrCall();

		super.onPause();
	}

	@Override
	protected void onResume() {
		sensormanager.registerListener(this, sensor, 1);
		locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				UPD_MIN_TIME, UPD_MIN_DISTANCE, this);
		//MondoRadar.thread();

		createOrWakeTwitterSync();
		createOrWakesSpiderSync();

		super.onResume();
	}

	private void createOrWakeTwitterSync() {
		// Create or weak the twitterSync thread
		if (twitterSync == null || !Check.isWaiting(twitterSync)) {
			if (twitterSync != null) {
				twitterSync.interrupt();
			}
			twitterSync = new TwitterSync(getString(R.string.spidertweeturl));
			twitterSync.start();
			twitterSync.addTwitterListener(this);
		} else {
			twitterSync.resumeWork();
		}
	}

	private void createOrWakesSpiderSync() {
		// Create or weak the spiderSync thread
		if (spiderSync == null || !Check.isWaiting(spiderSync)) {
			if (spiderSync != null) {
				spiderSync.interrupt();
			}
			spiderSync = new SpiderSync(getString(R.string.spiderlocation));
			spiderSync.start();
			spiderSync.addSpiderListener(this);
		} else {
			spiderSync.resumeWork();
		}
	}

	private boolean isSliderOpen() {
		if (mInfoDrawer.isMoving() || mInfoDrawer.isOpened()
				|| mNewsDrawer.isMoving() || mNewsDrawer.isOpened())
			return true;
		else
			return false;
	}


	final static Runnable thread_Finished = new Runnable() {
		public void run() {
			if (spiderOverlay != null)
				spiderOverlay.clearPoint();
			mapview.getOverlays().add(spiderOverlay);
			spiderOverlay.addPoint(MondoRadar.mSpiderPoint);
			MondoRadar.mapctrl.setCenter(MondoRadar.geoPoint);

			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					thread_handler.post(thread_Finished);
				}
			}).start();
		}
	};

	synchronized public void destroyListeners() {
		if (sensormanager != null)
			sensormanager.unregisterListener(this);
		if (locationmanager != null)
			locationmanager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		mLastUserloc = location;
		lat = location.getLatitude();
		lon = location.getLongitude();
		north = location.getBearing();
		northFloat = Float.valueOf(String.valueOf(north)).floatValue();
		if (mSpiderLoc != null) {
			dis = location.distanceTo(mSpiderLoc);
			bea = Math.round(location.bearingTo(mSpiderLoc));
			beaFloat = Float.valueOf(String.valueOf(bea)).floatValue();
			// if (Config.LOGD) Log.d(TAG, String.valueOf( bea ) );
			// if (Config.LOGD) Log.d(TAG, String.valueOf( bea_float ) );
		}
		changeDirection();

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	synchronized public void changeDirection() {
		if (mValues == null)
			return;

		try {
			MondoRadar.geoPoint = new GeoPoint((int) (MondoRadar.lat * 1E6),
					(int) (MondoRadar.lon * 1E6));

			int l = Math.round(mValues[0]);
			int l2 = Math.round(beaFloat);
			int l3 = Math.round(northFloat);
			if (l >= 360)
				l -= 360;
			else if (l < 0)
				l += 360;

			int i1 = 360 - l + l2;
			if (i1 >= 360)
				i1 -= 360;
			else if (i1 < 0)
				i1 += 360;

			int i2 = 360 - l + l3;
			if (i2 >= 360)
				i2 -= 360;
			else if (i2 < 0)
				i2 += 360;

			// Map
			MondoRadar.rotate_map = new RotateAnimation(lastNorth, i2,
					MondoRadar.mapview.getMeasuredWidth() / 2,
					MondoRadar.mapview.getMeasuredHeight() / 2);
			rotate_map.setDuration(3000);
			rotate_map.setRepeatCount(1);
			MondoRadar.mapview.startAnimation(rotate_map);

			// Arrow
			MondoRadar.rotate = new RotateAnimation(lastDegree, i1,
					mCompassImageView.getMeasuredWidth() / 2, mCompassImageView
							.getMeasuredHeight() / 2);
			rotate.setDuration(5000);
			rotate.setInterpolator(new AccelerateDecelerateInterpolator());
			rotate.setRepeatCount(1);

			mCompassImageView.startAnimation(rotate);

			lastDegree = i1;
			lastNorth = i2;

			int meter = (int) Math.round(dis);

			mDistanceTextView.setText("distance :: :: :: :: :: :: :: :: :: " + String.valueOf(meter) + " m");
		} catch (NullPointerException e) {
			Log.e("NullPointerException", e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("Exception", e.toString());
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mNewsDrawer.isMoving() || mNewsDrawer.isOpened()) {
				mNewsDrawer.animateClose();
				mSeekBar.setVisibility(View.VISIBLE);
				return true;
			}
			if (mInfoDrawer.isMoving() || mInfoDrawer.isOpened()) {
				mInfoDrawer.animateClose();
				mSeekBar.setVisibility(View.VISIBLE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	static Handler infoHandler = new Handler();

	public void onClickPerv(final View view) {
		layoutswitcher.setInAnimation(in_from_left);
		layoutswitcher.setOutAnimation(out_from_left);
		layoutswitcher.showPrevious();
		new Thread(new Runnable() {
			public void run() {
				MondoRadar.infoHandler.post(new Runnable() {
					public void run() {
						view.invalidate();
					}
				});
			}
		}).start();
	}

	public void onClickNext(final View view) {
		layoutswitcher.setInAnimation(in_from_right);
		layoutswitcher.setOutAnimation(out_from_right);
		layoutswitcher.showNext();
		new Thread(new Runnable() {
			public void run() {
				MondoRadar.infoHandler.post(new Runnable() {
					public void run() {
						view.invalidate();
					}
				});
			}
		}).start();
	}

	@Override
	synchronized public void onSpiderUpdate(double latitude, double longitude) {
		mSpiderLoc = new Location("mondo_spider");
		mSpiderLoc.setLatitude(latitude);
		mSpiderLoc.setLongitude(longitude);
		if (mLastUserloc != null && mSpiderLoc != null) {
			dis = mLastUserloc.distanceTo(mSpiderLoc);
			bea = Math.round(mLastUserloc.bearingTo(mSpiderLoc));
			beaFloat = Float.valueOf(String.valueOf(bea)).floatValue();
		}
		mSpiderPoint = new GeoPoint((int) (mSpiderLoc.getLatitude() * 1E6),
				(int) (mSpiderLoc.getLongitude() * 1E6));
	}

	@Override
	public void onTwitterUpdate(ArrayList<HashMap<String, String>> tweetList) {
		MondoRadar.tweet_current_list = tweetList;
		
		final SimpleAdapter sa = new SimpleAdapter(MondoRadar.this,
				tweetList, R.layout.tweet_row,
				new String[] { "tweet_date", "tweet_text" }, new int[] {
						R.id.tweet_date, R.id.tweet_text });
		runOnUiThread(new Runnable() {
			public void run() {
				twitter_listview.setAdapter(sa);
				twitter_listview.invalidate();
			}
		});
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		mValues = event.values;
		changeDirection();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_button:
			if (isSliderOpen())
				return;
			mNewsDrawer.open();
			mSeekBar.setVisibility(View.GONE);
			break;
		case R.id.info_button:
			if (isSliderOpen())
				return;
			mInfoDrawer.open();
			layoutswitcher.setAnimation(null);
			layoutswitcher.setInAnimation(null);
			layoutswitcher.setOutAnimation(null);
			layoutswitcher.setDisplayedChild(0);
			mSeekBar.setVisibility(View.GONE);
			break;
		case R.id.news_button_01:
			mNewsDrawer.close();
			mSeekBar.setVisibility(View.VISIBLE);
			break;
		case R.id.info_button_01:
			mNewsDrawer.close();
			mInfoDrawer.open();
			layoutswitcher.setAnimation(null);
			layoutswitcher.setInAnimation(null);
			layoutswitcher.setOutAnimation(null);
			layoutswitcher.setDisplayedChild(0);
			mSeekBar.setVisibility(View.GONE);
			break;
		case R.id.news_button_02:
			mInfoDrawer.close();
			mNewsDrawer.open();
			mSeekBar.setVisibility(View.GONE);
			break;
		case R.id.info_button_02:
			mInfoDrawer.close();
			mSeekBar.setVisibility(View.VISIBLE);
			break;
		case R.id.radar_info_backwards_arrow_01:
		case R.id.radar_info_backwards_arrow_02:
			mInfoDrawer.close();
			mNewsDrawer.close();
			mSeekBar.setVisibility(View.VISIBLE);
			break;
		default:
			Log.e(TAG, "No Action found for this click event :(.");
		}

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.startTestActivity:
//			startActivity(new Intent(this, Test.class));
//			return true; 
		case R.id.radarScreen:
			if (isSliderOpen()){
				mInfoDrawer.animateClose();
				mInfoDrawer.animateClose();
				mSeekBar.setVisibility(View.VISIBLE);
			}
			return true; 
		case R.id.aboutScreen:
			if (isSliderOpen())
				mNewsDrawer.animateClose();
			mInfoDrawer.animateOpen();
			mSeekBar.setVisibility(View.GONE);
			return true;
		case R.id.tweetsScreen:
			if (isSliderOpen())
				mInfoDrawer.animateClose();
			mNewsDrawer.animateOpen();
			mSeekBar.setVisibility(View.GONE);
			return true; 
		}

		return false; 
	}
	
	
}