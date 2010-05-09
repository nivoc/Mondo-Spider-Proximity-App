package spider.net;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;
import java.net.URLConnection;
import android.util.Log;

public class spider extends Activity implements LocationListener{

	public static final String PREFS_NAME = "GlobalPrefs";
    private static final String ENCODING = "UTF-8";
    private static String mSecretKey;
    private static String URL_STRING = "https://mondospiderwebapp.appspot.com/spiderlocation/set";

	Button btn1;
	TextView text1;
	LocationManager locManager;
	String best;
	Location oldLocation;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		mSecretKey = settings.getString("secretKey", "");
        
		if (mSecretKey.length() < 6) {
			askForSecretKey();
		}
		
		
        text1 = (TextView)findViewById(R.id.dest);
        
        locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        best = locManager.getBestProvider(new Criteria(), true);
        oldLocation = locManager.getLastKnownLocation(best);
        text1.setText("Last Location: lat: " + oldLocation.getLatitude() + "lng: " + oldLocation.getLongitude());
    }
    


	private void askForSecretKey() {
		Intent i = new Intent(this, AskForSecretKey.class);
		startActivity(i);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		mSecretKey = settings.getString("secretKey", "");
	}
    

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.changeSecretKey:
			startActivity(new Intent(this, AskForSecretKey.class));
			return true; 
		}

		return false; 
	}
	
	
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	locManager.removeUpdates(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	final int REFRESH_RATE = 3000;
    	locManager.requestLocationUpdates(best, REFRESH_RATE, 0, this);
    }

    private void updateLocation(Location location) {
    	if (location != null) {
    		oldLocation = location;
    	}
    	
    	sendLocation(oldLocation);
    	
    	text1.setText("Last Location: lat: " + oldLocation.getLatitude() + "lng: " + oldLocation.getLongitude());
    }
    
    private void sendLocation(Location location)
    {
    	StringBuilder url = new StringBuilder(URL_STRING);
    	url.append("?pwd=" + mSecretKey + "&lat=") ;
    	url.append(location.getLatitude());
    	url.append("&lng=" + location.getLongitude());
    	Log.d("MondoRadarAdmin", " Send update: "+url);
    	try {
    		URLConnection conn = (new URL(url.toString())).openConnection();
    	} catch (Exception e) {
    		Log.e("sendLocation", e.getMessage());
    	}
    }
    
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		updateLocation(location);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}