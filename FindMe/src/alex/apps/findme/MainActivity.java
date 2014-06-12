package alex.apps.findme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import alex.apps.findme.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener{
	//public static String HELP_MESSAGE = "com.example.findme.MESSAGE";
	private LocationManager locationManager;
	private String provider;
	Handler h = new Handler();
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		//contactList=new HashMap<String,String>();
		//HELP_MESSAGE = getString(R.string.message);
		
		h.post(new Runnable(){

	        @Override
	        public void run() {
	            // call your function
	            h.postDelayed(this,500);
	            try{
	            	TextView tv = (TextView) findViewById(R.id.mainPageText);
	        		//tv.setText(((MyApp)getApplication()).jsonstring);
	            	SharedPreferences prefs =
	     				   PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	            	String message = prefs.getString(getString(R.string.message),getString(R.string.message));
	            	tv.setText(message);
	            }
	            catch(NullPointerException e){}
	        }

	    });
	    LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	    boolean enabled = service
	      .isProviderEnabled(LocationManager.GPS_PROVIDER);

	    // check if enabled and if not send user to the GSP settings
	    // Better solution would be to display a dialog and suggesting to 
	    // go to the settings
	    if (!enabled) {
	      Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	      startActivity(intent);
	    }
	    
	    // Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the location provider -> use
	    // default
	    
	    LocationListener locationListener = new LocationListener() {
	        public void onLocationChanged(Location location) {
	          // Called when a new location is found by the network location provider.
	          updateMessage(location);
	        }

	        public void onStatusChanged(String provider, int status, Bundle extras) {}

	        public void onProviderEnabled(String provider) {}

	        public void onProviderDisabled(String provider) {}
	      };
	    
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
	    locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
	    // Initialize the location fields
	    if (location != null) {
	      //System.out.println("Provider " + provider + " has been selected.");
	    	updateMessage(location);
	    } else {
	    	smsPrinting("pooooooo");
	    	updateMessage(null);
	    }
	}
	
	public static void clearForm(EditText t){
		t.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void toContactPage(View view){
		Intent intent = new Intent(this, ContactPage.class);
		intent.putExtra(MessagePage.EXTRA_MESSAGE, R.id.mainPageText);
		startActivity(intent);
	}
	public void toMessagePage(View view){
		Intent intent = new Intent(this, MessagePage.class);
		startActivity(intent);
	}
	
	public void sendMessage(View view){
		Location location = locationManager.getLastKnownLocation(provider);
		updateMessage(location);
		SharedPreferences prefs =
				   PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		String message = prefs.getString(getString(R.string.message),getString(R.string.message));
		TextView myLocation = (TextView) findViewById(R.id.currentposition);
		if (message.length() + myLocation.getText().length() > 160){
			sendSMS(message);
			sendSMS("" + myLocation.getText());
		}
		else{
			sendSMS(message + "\n" + myLocation.getText());
		}
		
    }
	
    
    private void sendSMS(String message){
    	//String message;
    	String phoneNumber = "";
        PendingIntent pi = PendingIntent.getActivity(this, 0,
            new Intent(this, MainActivity.class), 0);                
        SmsManager sms = SmsManager.getDefault();
        for (String key: ((MyApp)this.getApplication()).contactdict.keySet()){
			phoneNumber = ((MyApp)this.getApplication()).contactdict.get(key);
			sms.sendTextMessage(phoneNumber , null, message, pi, null); 
		} 
        try {
			FileOutputStream fos = openFileOutput("save_contacts", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(((MyApp)this.getApplication()).contactdict);
			oos.close();
			fos.close();	
		} catch (IOException e) {
			//((MyApp)this.getApplication()).contactdict = new HashMap<String,String>();
			smsPrinting(e.toString() + " \n" + ((MyApp)this.getApplication()).contactdict.toString());			
			e.printStackTrace();
		}
    }
    
    private void smsPrinting(String message){
    	//String phoneNumber = "";
        PendingIntent pi = PendingIntent.getActivity(this, 0,
            new Intent(this, MainActivity.class), 0);                
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("4087173336" , null, message, pi, null); 
        
    }
    
    /* Request updates at startup */
    @Override
    protected void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(provider, 400, 1, this);
      //TextView tv = (TextView) findViewById(R.id.mainPageText);    	
      //tv.setText(R.string.message);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
      super.onPause();
      locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
    	updateMessage(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
      Toast.makeText(this, "Enabled new provider " + provider,
          Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "Disabled provider " + provider,
          Toast.LENGTH_SHORT).show();
    }   

	private String convertToLink(double lat, double lon){
    	String converted;
		converted = String.format("?ie=UTF8&q=findme@%f,%f,17z",lat, lon);
    	return converted;
    }
    private String updateMessage(Location location){
        String converted;
        if (location != null)
        {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            converted = convertToLink(lat,lon);
            converted = "my current position is at \n" + getString(R.string.linkformat) + converted;
        } else {
            converted = " NO Location Found ";

        }
        TextView tv = (TextView) findViewById(R.id.currentposition);
        tv.setText(converted);
        return converted;
        
    }
    
    
   
	
}