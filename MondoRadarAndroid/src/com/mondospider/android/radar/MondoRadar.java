package com.mondospider.android.radar;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
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
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.mondospider.android.overlay.SpiderItemizedOverlay;

public class MondoRadar extends MapActivity implements LocationListener
		{
	static MondoRadar mondoradar;
	static double lat=0;
	static double lon=0;
	static double alt=0;
	static double spe=0;
	static double dis=0;
	static double bea=0;
	static double north=0;
	static float north_float = 0;
	static float last_north=0;
	static double last_bea=0;
	static float last_degree=0;
	static float bea_float = 0;
	static double mondspider_lat = 40.707189;
	static double mondospider_lon = -74.19342;
	
	static final long M_TIME=0;
	static final float M_DISTANCE=0;
	static LocationManager locationmanager;
	static Location mondo_spider_location;
	static Location last_location;
	
    private static final String TAG = "Compass";

    private static float[] mValues;
	
    static LinearLayout LinearCompass;
    static ImageView ImageViewCompass;
    static SeListener seListener;
    static RotateAnimation rotate;
    static RotateAnimation rotate_map;
    static ImageView radar_spin;
    static Button btn_close;
    
    static SensorManager sensormanager;
    static Sensor sensor;

    static MapController mapctrl;
    static MapView mapview;
    
    static TextView dis_m;
    static SeekBar seekBar;
    
    static SpiderSync spider_sync;
    static TwitterSync twitter_sync;
    
    static SpiderItemizedOverlay spiderOverlay;
    
    static GeoPoint geoPoint;
    static GeoPoint spiderPoint;
    final static Handler thread_handler = new Handler();
    
    static SlidingDrawer slidenews;
    static SlidingDrawer slideinfo;
	static ImageButton news_button;
	static ImageButton info_button;
	static ImageButton news_button_01;
	static ImageButton info_button_01;
	static ImageButton news_button_02;
	static ImageButton info_button_02;

	private ViewFlipper layoutswitcher;
	private float oldTouchValue;
	private Animation in_from_left;
	private Animation out_from_left;
	private Animation in_from_right;
	private Animation out_from_right;
	
	private static ListView twitter_listview;
	private static ArrayList<HashMap<String, String>> tweet_current_list;
	static Toast unconnectToast;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.main);
        
        
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
      	int DisplayWidth = display.getWidth();
    	int DisplayHeight = display.getHeight();
    	ImageView radar_background = (ImageView) findViewById(R.id.radar);
    	if(DisplayWidth == 480 && DisplayHeight == 800){
    		radar_background.setImageDrawable( getResources().getDrawable( R.drawable.radar_800_480 ) );
    	}

    	news_button = (ImageButton) findViewById(R.id.news_button);
   		info_button = (ImageButton) findViewById(R.id.info_button);
    	news_button_01 = (ImageButton) findViewById(R.id.news_button_01);
   		info_button_01 = (ImageButton) findViewById(R.id.info_button_01);
    	news_button_02 = (ImageButton) findViewById(R.id.news_button_02);
   		info_button_02 = (ImageButton) findViewById(R.id.info_button_02);
        slidenews = (SlidingDrawer) findViewById(R.id.slidenews);
        slideinfo = (SlidingDrawer) findViewById(R.id.slideinfo);
        
        news_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isSliderOpen()) return;
				slidenews.animateOpen();
			}
		});
        info_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isSliderOpen()) return;
				slideinfo.animateOpen();
			}
		});
        news_button_01.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				slidenews.animateClose();
			}
		});
        info_button_01.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				slidenews.animateClose();
				slideinfo.animateOpen();
			}
		});
        news_button_02.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				slideinfo.animateClose();
				slidenews.animateOpen();
			}
		});
        info_button_02.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				slideinfo.animateClose();
			}
		});
    	unconnectToast = Toast.makeText(
				this, 
				R.string.unconnectwarning ,
				Toast.LENGTH_SHORT
				);
    	
  	    radar_spin = (ImageView) findViewById(R.id.radar_spin);
  	    
  	    AnimationSet set = new AnimationSet(true);
  	  
  	    AlphaAnimation alpha = new AlphaAnimation(0, 0.6f);
  	    alpha.setDuration(800);
  	    alpha.setRepeatCount( -1 );
  	    set.addAnimation(alpha);

  	    Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely); 
  	    set.addAnimation(animation);
  	    set.setInterpolator( new LinearInterpolator() );
  	    
  	    radar_spin.startAnimation( set );
  	  
        seekBar = (SeekBar) findViewById(R.id.seekbar_zoom);
        seekBar.setMax(10);
        seekBar.setProgress(6);
        seekBar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isSliderOpen()) return;
			}
		});
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
            public void onProgressChanged(
                    SeekBar seekBar,
                    int progress,
                    boolean fromTouch) {
                Log.d("onProgressChanged()",
                    String.valueOf(progress) + ", " +
                    String.valueOf(fromTouch));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("onStartTrackingTouch()",
                    String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("onStopTrackingTouch()",
                    String.valueOf(seekBar.getProgress()));
                MondoRadar.mapctrl.setZoom( seekBar.getProgress() + 10 );
            }
        });
        spider_sync = new SpiderSync(this);
        spider_sync.start();
                
	    dis_m = (TextView) findViewById(R.id.dis_m);
    	
		mapview = (MapView) findViewById(R.id.mapview);
		Drawable spider_point = getResources().getDrawable( R.drawable.spider_point_00 );

		spiderOverlay = new SpiderItemizedOverlay( spider_point );
				
		MondoRadar.mapctrl = mapview.getController();
		MondoRadar.mapctrl.setZoom(16);

        mondoradar = this;
        seListener = new SeListener();
        sensormanager = (SensorManager)getSystemService("sensor");
        sensor = sensormanager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensormanager.registerListener(seListener, sensor, 1);

        MondoRadar.mapview.setSatellite(false);
        /*
        btn_map.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (MondoRadar.mapview.isSatellite()) {
					MondoRadar.mapview.setSatellite(false);
				}
			}
		});
        btn_satellite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					MondoRadar.mapview.setSatellite(true);
			}
		});
		*/
        ImageViewCompass = (ImageView) findViewById(R.id.compass);
        
        mondo_spider_location = new Location("mondo_spider");
        mondo_spider_location.setLatitude(mondspider_lat);
        mondo_spider_location.setLongitude(mondospider_lon);
        
		locationmanager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location lc = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (lc == null) {
			lc = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if( lc != null ){
    		onLocationChanged(lc);
    	}
		
		locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, M_TIME, M_DISTANCE, this);
		
		if( lc != null ){
	        lat = lc.getLatitude();
	        lon = lc.getLongitude();
		}
		MondoRadar.thread();
		
		String[] tweet_date_dummy = new String[] {
				"01 Apr 2010 10:10am",
				"02 Apr 2010 12:10pm",
				"03 Apr 2010 03:10pm",
				"04 Apr 2010 04:10pm",
				"05 Apr 2010 07:10pm",
				"06 Apr 2010 04:10pm"
				};
		String[] tweet_text_dummy = new String[] {
				"This is sample tweet",
				"Update from twitter",
				"List view can do many things",
				"Android is a interesing platform",
				"Android devices are lovely.",
				"The mascot is cute;)"
				};
		twitter_listview = (ListView) findViewById(R.id.twitter_listview);
//		twitter_listview.setClickable(false);
		tweet_current_list = new ArrayList<HashMap<String, String>>();
        twitter_sync = new TwitterSync(this);
        twitter_sync.start();

		final ArrayList<HashMap<String, String>> tweet_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> items;
		for(int i=0;i<=5;i++){
			items = new HashMap<String, String>();
			items.put("tweet_date", tweet_date_dummy[i]);
			items.put("tweet_text", tweet_text_dummy[i]);
			tweet_list.add(items);
		}
		final SimpleAdapter sa = new SimpleAdapter(
				this,
				tweet_list,
				R.layout.tweet_row,
	            new String[] {
						"tweet_date",
						"tweet_text"
						},
	            new int[] {
						R.id.tweet_date,
						R.id.tweet_text
						}
				);
		twitter_listview.setAdapter(sa);
		twitter_listview.setOnScrollListener(new AbsListView.OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Do nothings
			}

			public void onScroll(
					AbsListView view,
					int firstVisibleItem,
					int visibleItemCount,
					int totalItemCount) {
				// TODO
				// Do somethings
			}
		});

		

		
		
        layoutswitcher = (ViewFlipper)findViewById(R.id.layoutswitcher);
        
        in_from_left  = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
        out_from_left = AnimationUtils.loadAnimation(this, R.anim.out_from_left);
        in_from_right  = AnimationUtils.loadAnimation(this, R.anim.in_from_right);
        out_from_right = AnimationUtils.loadAnimation(this, R.anim.out_from_right);
        
    }

    private boolean isSliderOpen(){
		if( slideinfo.isMoving() || slideinfo.isOpened() || slidenews.isMoving() || slidenews.isOpened() )
			return true;
		else
			return false;
    }
    static private void thread(){
		new Thread(
			new Runnable() {
				public void run() {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					thread_handler.post(thread_Finished);
				}
			}
		).start();
    }
    final static Runnable thread_Finished = new Runnable() {
		public void run() {
			if(spiderOverlay != null)
				spiderOverlay.clearPoint();
			mapview.getOverlays().add( spiderOverlay );
			spiderOverlay.addPoint( MondoRadar.spiderPoint );
			MondoRadar.mapctrl.setCenter(MondoRadar.geoPoint);

			MondoRadar.thread();
		}
	};
    synchronized public static void setSpiderLocation(double latitude, double longitude){
//    	Log.d("setSpiderLocation->latitude", String.valueOf(latitude));
//    	Log.d("setSpiderLocation->longitude", String.valueOf(longitude));
    	mondspider_lat = latitude;
    	mondospider_lon = longitude;
    	mondo_spider_location = new Location("mondo_spider");
        mondo_spider_location.setLatitude(mondspider_lat);
        mondo_spider_location.setLongitude(mondospider_lon);
        if( last_location != null && mondo_spider_location != null ){
        	last_bea = bea;
        	dis = last_location.distanceTo(mondo_spider_location);
        	bea = Math.round( last_location.bearingTo(mondo_spider_location) );
        	bea_float = Float.valueOf( String.valueOf( bea ) ).floatValue();
        	MondoRadar.ChangeDirection();
        }
        MondoRadar.spiderPoint = new GeoPoint(
				(int) (mondspider_lat * 1E6),
				(int) (mondospider_lon * 1E6)
			);
    }
    static Handler tweetHandler = new Handler();
    synchronized public static void setSpiderTweet(ArrayList<HashMap<String, String>> tweet_list){
    	MondoRadar.tweet_current_list = tweet_list;
    	new Thread(new Runnable() {
    	    public void run() {
	        	final SimpleAdapter sa = new SimpleAdapter(
	    				MondoRadar.mondoradar,
	    				MondoRadar.tweet_current_list,
	    				R.layout.tweet_row,
	    	            new String[] {
	    						"tweet_date",
	    						"tweet_text"
	    						},
	    	            new int[] {
	    						R.id.tweet_date,
	    						R.id.tweet_text
	    						}
	    				);
	        	MondoRadar.tweetHandler.post(new Runnable() {
    	        public void run() {
    	        	twitter_listview.setAdapter(sa);
    	        	twitter_listview.invalidate();
    	        }
    	      });
    	    }
    	  }).start();
    }
    @Override
    protected void onStop() {
    	destroyListeners();
    	super.onStop();
    }
    @Override
    protected void onDestroy() {
    	destroyListeners();
    	super.onDestroy();
    }
    @Override
    protected void onPause() {
    	destroyListeners();
    	super.onPause();
    }
    synchronized public void destroyListeners(){
    	if(sensormanager != null && seListener != null)
    		sensormanager.unregisterListener(seListener);
    	if( locationmanager != null && mondoradar != null)
    		locationmanager.removeUpdates(mondoradar);
    	finish();
    }
    @Override
	public void onLocationChanged(Location location) {
    	last_location = location;
        lat = location.getLatitude();
        lon = location.getLongitude();
        alt = location.getAltitude();
        spe = location.getSpeed();
        north = location.getBearing();
        north_float = Float.valueOf( String.valueOf( north ) ).floatValue();
        if( mondo_spider_location != null ){
        	last_bea = bea;
        	dis = location.distanceTo(mondo_spider_location);
        	bea = Math.round( location.bearingTo(mondo_spider_location) );
        	bea_float = Float.valueOf( String.valueOf( bea ) ).floatValue();
//        	if (Config.LOGD) Log.d(TAG, String.valueOf( bea ) );
//        	if (Config.LOGD) Log.d(TAG, String.valueOf( bea_float ) );
        }
        MondoRadar.ChangeDirection();

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

    private class SeListener implements SensorEventListener{

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			mValues = event.values;
			MondoRadar.ChangeDirection();
		}
    	
    }
    synchronized 
    public static void ChangeDirection(){
		if(mValues == null)
			return;

		try{
			MondoRadar.geoPoint = new GeoPoint(
					(int) (MondoRadar.lat * 1E6),
					(int) (MondoRadar.lon * 1E6)
				);
			
			int l = Math.round( mValues[0] );
			int l2 =  Math.round( bea_float );
			int l3 =  Math.round( north_float );
			if(l >= 360)
				l -= 360;
			else if(l < 0)
				l += 360;
			
			int i1 = 360 - l + l2;
			if(i1 >= 360)
				i1 -= 360;
			else if(i1 < 0)
				i1 += 360;
	
			int i2 = 360 - l + l3;
			if(i2 >= 360)
				i2 -= 360;
			else if(i2 < 0)
				i2 += 360;
	
			// Map
			MondoRadar.rotate_map = new RotateAnimation(
	    			last_north,
	    			i2,
	    			MondoRadar.mapview.getMeasuredWidth() / 2,
	    			MondoRadar.mapview.getMeasuredHeight() / 2
	    			);
	    	rotate_map.setDuration(3000);
	    	rotate_map.setRepeatCount(1);
	    	MondoRadar.mapview.startAnimation(rotate_map);
		
			// Arrow
	    	MondoRadar.rotate = new RotateAnimation(
	    			last_degree,
	    			i1,
	    			ImageViewCompass.getMeasuredWidth() / 2,
	    			ImageViewCompass.getMeasuredHeight() / 2
	    			);
	    	rotate.setDuration(5000);
	    	rotate.setInterpolator( new AccelerateDecelerateInterpolator() );
	    	rotate.setRepeatCount(1);
	    	ImageViewCompass.startAnimation(rotate);
	
	
	    	last_degree = i1;
	    	last_north = i2;
		    	
		    int meter = (int) Math.round(dis);
		    dis_m.setText( String.valueOf(meter) + " m");
		}catch(NullPointerException e){
			Log.e("NullPointerException", e.toString());
		}catch(Exception e){
			Log.e("Exception", e.toString());
		}
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if( slidenews.isMoving() || slidenews.isOpened() ){
				slidenews.animateClose();
				return true;
			}
			if( slideinfo.isMoving() || slideinfo.isOpened() ){
				slideinfo.animateClose();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	static Handler infoHandler = new Handler();
	public void onClickPerv(final View view){
		layoutswitcher.setInAnimation( in_from_left );
    	layoutswitcher.setOutAnimation( out_from_left );
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
	public void onClickNext(final View view){
    	layoutswitcher.setInAnimation( in_from_right );
    	layoutswitcher.setOutAnimation( out_from_right );
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
}