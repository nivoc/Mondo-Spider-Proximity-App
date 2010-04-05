package com.mondospider.android.radar;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;


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
    
    static Button btn_close;
    static ImageButton btn_map;
    static ImageButton btn_satellite;
    
    static SensorManager sensormanager;
    static Sensor sensor;
    private GrayScaleOverlay overlay;  
    
    static MapController mapctrl;
    static MapView mapview;
    
    static EditText e_dis;
    static TextView dis_m;
    static TextView dis_km;
    static TextView dis_inch;
    static TextView dis_mile;
    static SeekBar seekBar;
    
    static SpiderSync sync;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        /*
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Log.e("XXXX", String.valueOf( metrics.densityDpi ));
        switch( metrics.densityDpi ){
	        case DisplayMetrics.DENSITY_HIGH:
//	        	metrics.setTo(metrics);
	        	break;
	        case DisplayMetrics.DENSITY_MEDIUM:
	           	break;
	        case DisplayMetrics.DENSITY_LOW:
	        	break;
        }
        */
        btn_map = (ImageButton) findViewById(R.id.btn_map);
        btn_satellite = (ImageButton) findViewById(R.id.btn_satellite);
        seekBar = (SeekBar) findViewById(R.id.seekbar_zoom);
        seekBar.setMax(15);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(
                    SeekBar seekBar,
                    int progress,
                    boolean fromTouch) {
                Log.v("onProgressChanged()",
                    String.valueOf(progress) + ", " +
                    String.valueOf(fromTouch));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v("onStartTrackingTouch()",
                    String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.v("onStopTrackingTouch()",
                    String.valueOf(seekBar.getProgress()));
                MondoRadar.mapctrl.setZoom( seekBar.getProgress() + 5 );
            }
        });
        sync = new SpiderSync(this);
        sync.start();
        
	    e_dis = (EditText) findViewById(R.id.dis);
	    dis_m = (TextView) findViewById(R.id.dis_m);
    	dis_km = (TextView) findViewById(R.id.dis_km);
    	dis_inch = (TextView) findViewById(R.id.dis_inch);
    	dis_mile = (TextView) findViewById(R.id.dis_mile);
    	
		mapview = (MapView) findViewById(R.id.mapview);
		
//		overlay = new GrayScaleOverlay( this, mapview );
//		List<Overlay> list = mapview.getOverlays();  
//		list.add(overlay);  
		
		MondoRadar.mapctrl = mapview.getController();
		MondoRadar.mapctrl.setZoom(10);
//		MondoRadar.mapctrl.zoomToSpan(
//				(int) ( 35.66026 * 1E6 ), (int) ( 139.729528 ) );

		/*
		mapctrl.setCenter(
				new GeoPoint(
					(int) (35.45530345132602 * 1E6),
					(int) (139.6365491316008 * 1E6)
				)
			);
		*/
		/*
		MondoRadar.mapctrl.setCenter(
				new GeoPoint(
					(int) (mondspider_lat * 1E6),
					(int) (mondospider_lon * 1E6)
				)
			);
		*/
        mondoradar = this;
        seListener = new SeListener();
        sensormanager = (SensorManager)getSystemService("sensor");
        sensor = sensormanager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensormanager.registerListener(seListener, sensor, 1);

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

        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				destroyListeners();
			}
        });
        
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
		
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
	   	super.onCreateOptionsMenu(menu);
    	menu.add(0, 5100, Menu.NONE, "Statue of Liberty" );
    	menu.add(0, 5200, Menu.NONE, "Moscone Center" );
    	menu.add(0, 5300, Menu.NONE, "Key West" );
    	menu.add(0, 5400, Menu.NONE, "Chicago International Airport" );
    	menu.add(0, 5500, Menu.NONE, "Hawaii" );
    	menu.add(0, 5600, Menu.NONE, "White House" );
    	return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case 5100:
		        // Statue of Liberty 
		        mondspider_lat = 41.727255;
		        mondospider_lon = -87.297363;
				break;
			case 5200:
		        // Moscone Center 
		        mondspider_lat = 40.707189;
		        mondospider_lon = -74.19342;
				break;
			case 5300:
		        // Key West 
		        mondspider_lat = 24.54636;
		        mondospider_lon = -81.797507;
				break;
			case 5400:
		        // Chicago International Airport 
		        mondspider_lat = 41.976721;
		        mondospider_lon = -87.918606;
				break;
			case 5500:
		        // Hawaii 
		        mondspider_lat = 19.642588;
		        mondospider_lon = -155.566406;
				break;
			case 5600:
		        // White House 
		        mondspider_lat = 38.901053;
		        mondospider_lon = -77.036562;
				break;
    	}
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
    	return true;
    }
    synchronized public static void setSpiderLocation(double latitude, double longitude){
    	Log.d("setSpiderLocation->latitude", String.valueOf(latitude));
    	Log.d("setSpiderLocation->longitude", String.valueOf(longitude));
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
	
    }
    /*
    @Override
    protected void onPause() {
    	finish();
    	super.onPause();
    }
    */
    @Override
    protected void onStop() {
    	finish();
    	super.onStop();
    }
    @Override
    protected void onDestroy() {
    	destroyListeners();
    	super.onDestroy();
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
        north_float = Float.valueOf( String.valueOf( bea ) ).floatValue();
        if( mondo_spider_location != null ){
        	last_bea = bea;
        	dis = location.distanceTo(mondo_spider_location);
        	bea = Math.round( location.bearingTo(mondo_spider_location) );
        	bea_float = Float.valueOf( String.valueOf( bea ) ).floatValue();
        	if (Config.LOGD) Log.d(TAG, String.valueOf( bea ) );
        	if (Config.LOGD) Log.d(TAG, String.valueOf( bea_float ) );
        }
        MondoRadar.ChangeDirection();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

    private class SeListener implements SensorEventListener{

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			mValues = event.values;
			MondoRadar.ChangeDirection();
		}
    	
    }
    synchronized public static void ChangeDirection(){
		if(mValues == null)
			return;
		MondoRadar.mapctrl.setCenter(
				new GeoPoint(
					(int) (MondoRadar.lat * 1E6),
					(int) (MondoRadar.lon * 1E6)
				)
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

		try{
			MondoRadar.rotate_map = new RotateAnimation(
	    			last_north,
	    			i2,
	    			MondoRadar.mapview.getMeasuredWidth() / 2,
	    			MondoRadar.mapview.getMeasuredHeight() / 2
	    			);
	    	rotate_map.setDuration(3000);
	    	rotate_map.setRepeatCount(1);
	    	MondoRadar.mapview.startAnimation(rotate_map);
	
	    	MondoRadar.rotate = new RotateAnimation(
	    			last_degree,
	    			i1,
	    			ImageViewCompass.getMeasuredWidth() / 2,
	    			ImageViewCompass.getMeasuredHeight() / 2
	    			);
	    	rotate.setDuration(3000);
	    	rotate.setRepeatCount(1);
	    	ImageViewCompass.startAnimation(rotate);
		}catch(Exception e){
			Log.e("Exception", e.toString());
		}
    	last_degree = i1;
    	last_north = i2;
    	
	    	e_dis.setText( String.valueOf(dis) );
	    	
	    int meter = (int) Math.round(dis);
	    int kilo = Math.round( meter / 1000 );
	    	dis_m.setText( String.valueOf(meter));
	       	dis_km.setText( String.valueOf(kilo));
	       	
	    int inch = (int) Math.round( meter * 0.4 );
	    int mile = (int) Math.round( kilo * 0.62 );
	       	dis_inch.setText( String.valueOf(inch));
	    	dis_mile.setText( String.valueOf(mile));

	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


}