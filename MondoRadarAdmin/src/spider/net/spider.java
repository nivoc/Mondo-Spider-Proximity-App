package spider.net;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;
import java.net.URLConnection;
import android.util.Log;

public class spider extends Activity implements LocationListener{
	
    private static final String ENCODING = "UTF-8";
    private static final String SECRET_KEY = "DR3ceCRewAdrarUd";
    private static final String URL_STRING = "https://mondospiderwebapp.appspot.com/spiderlocation/set?pwd=" + SECRET_KEY + "&lat=";

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
        
        text1 = (TextView)findViewById(R.id.dest);
        
        locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        best = locManager.getBestProvider(new Criteria(), true);
        oldLocation = locManager.getLastKnownLocation(best);
        text1.setText("Last Location: lat: " + oldLocation.getLatitude() + "lng: " + oldLocation.getLongitude());
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
    	url.append(location.getLatitude());
    	url.append("&lng=" + location.getLongitude());
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