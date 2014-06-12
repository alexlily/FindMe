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

public class MainActivityOld extends Activity implements LocationListener{
	//public static String HELP_MESSAGE = "com.example.findme.MESSAGE";
	private LocationManager locationManager;
	private String provider;
	Handler h = new Handler();
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		//contactList=new HashMap<String,String>();
		//HELP_MESSAGE = getString(R.string.message);
		
		/*h.post(new Runnable(){

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

	    });*/
		try{
			SharedPreferences prefs =
					   PreferenceManager.getDefaultSharedPreferences(MainActivityOld.this);
			String message = prefs.getString(getString(R.string.message),getString(R.string.message));
			//Intent intent = getIntent();
	        //String message = intent.getStringExtra(MessagePage.EXTRA_MESSAGE);
	        TextView tv = (TextView) findViewById(R.id.mainPageText);
	        tv.setText(message);
		}catch(Exception e){
		
		}
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
	    new doTask().execute();
	    //new FindLocationTask().execute();
	    /*
	    // Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the location provider -> use
	    // default
	    
	    LocationListener locationListener = new LocationListener() {
	        public void onLocationChanged(Location location) {
	          // Called when a new location is found by the network location provider.
	          updateWithNewLocation(location);
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
	      onLocationChanged(location);
	    } else {
	    	updateWithNewLocation(null);
	    }
		*/
	    
	    /*public class FetchCordinates extends AsyncTask<String, Integer, String> {
	        ProgressDialog progDailog = null;

	        public double lati = 0.0;
	        public double longi = 0.0;

	        public LocationManager mLocationManager;
	        public VeggsterLocationListener mVeggsterLocationListener;

	        @Override
	        protected void onPreExecute() {
	            mVeggsterLocationListener = new VeggsterLocationListener();
	            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	            mLocationManager.requestLocationUpdates(
	                    LocationManager.NETWORK_PROVIDER, 0, 0,
	                    mVeggsterLocationListener);

	            progDailog = new ProgressDialog(FastMainActivity.this);
	            progDailog.setOnCancelListener(new OnCancelListener() {
	                @Override
	                public void onCancel(DialogInterface dialog) {
	                    FetchCordinates.this.cancel(true);
	                }
	            });
	            progDailog.setMessage("Loading...");
	            progDailog.setIndeterminate(true);
	            progDailog.setCancelable(true);
	            progDailog.show();

	        }

	        @Override
	        protected void onCancelled(){
	            System.out.println("Cancelled by user!");
	            progDialog.dismiss();
	            mLocationManager.removeUpdates(mVeggsterLocationListener);
	        }

	        @Override
	        protected void onPostExecute(String result) {
	            progDailog.dismiss();

	            Toast.makeText(FastMainActivity.this,
	                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
	                    Toast.LENGTH_LONG).show();
	        }

	        @Override
	        protected String doInBackground(String... params) {
	            // TODO Auto-generated method stub

	            while (this.lati == 0.0) {

	            }
	            return null;
	        }

	        public class VeggsterLocationListener implements LocationListener {

	            @Override
	            public void onLocationChanged(Location location) {

	                int lat = (int) location.getLatitude(); // * 1E6);
	                int log = (int) location.getLongitude(); // * 1E6);
	                int acc = (int) (location.getAccuracy());

	                String info = location.getProvider();
	                try {

	                    // LocatorService.myLatitude=location.getLatitude();

	                    // LocatorService.myLongitude=location.getLongitude();

	                    lati = location.getLatitude();
	                    longi = location.getLongitude();

	                } catch (Exception e) {
	                    progDailog.dismiss();
	                    // Toast.makeText(getApplicationContext(),"Unable to get Location"
	                    // , Toast.LENGTH_LONG).show();
	                }

	            }

	            @Override
	            public void onProviderDisabled(String provider) {
	                Log.i("OnProviderDisabled", "OnProviderDisabled");
	            }

	            @Override
	            public void onProviderEnabled(String provider) {
	                Log.i("onProviderEnabled", "onProviderEnabled");
	            }

	            @Override
	            public void onStatusChanged(String provider, int status,
	                    Bundle extras) {
	                Log.i("onStatusChanged", "onStatusChanged");

	            }

	        }

	    }*/
	    
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
		//Location location = locationManager.getLastKnownLocation(provider);
		//updateWithNewLocation(location);
		
		
		SharedPreferences prefs =
				   PreferenceManager.getDefaultSharedPreferences(MainActivityOld.this);
		String message = prefs.getString(getString(R.string.message),getString(R.string.message));
		TextView myLocation = (TextView) findViewById(R.id.currentposition);
		if (message.length() + myLocation.getText().length() > 160){
			sendSMS(message);
			sendSMS("" + myLocation.getText());
		}
		else{
			sendSMS(message + "\n" + myLocation.getText());
		}
    	//sendSMS(message+"\n" + myLocation.getText());
    	//TextView t = (TextView) findViewById(R.id.mainPageText);    	
    	//t.setText(message);
		
    }
	
    
    private void sendSMS(String message){
    	//String message;
    	String phoneNumber = "";
        PendingIntent pi = PendingIntent.getActivity(this, 0,
            new Intent(this, MainActivity.class), 0);                
        SmsManager sms = SmsManager.getDefault();
        /*SharedPreferences prefs =
		//		   PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		//phoneNumber = prefs.getString(getString(R.string.number), "4087173336");
       //phoneNumber="4087173336";
        message = "y u no work.";*/
        //sms.sendTextMessage(phoneNumber, null, message, pi, null); 
        for (String key: ((MyApp)this.getApplication()).contactdict.keySet()){
			phoneNumber = ((MyApp)this.getApplication()).contactdict.get(key);
			
			sms.sendTextMessage(phoneNumber , null, message, pi, null); 
		} 
        //sms.sendTextMessage("4087173336" , null, ((MyApp)this.getApplication()).contactdict.keySet().toString(), pi, null); 
    	
    	/*for (int i = 0; i < ((MyApp)this.getApplication()).contactlist.length(); i++) {
			try {
				phoneNumber = ((MyApp)this.getApplication()).contactlist.getString(i);
				phoneNumber = getNumber(phoneNumber);
				sms.sendTextMessage(phoneNumber, null, message, pi, null); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}*/    
        try {
			//File file = new File("save_contacts");
			FileOutputStream fos = openFileOutput("save_contacts", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(((MyApp)this.getApplication()).contactdict);
			oos.close();
			fos.close();
			//oos.close();
		//((MyApp)this.getApplication()).jsonstring = putToJSON(name, number);
		//updateContacts();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    
    private String getNumber(String phoneNumber){
    	String onlyNum = "";
    	for (int i = 0; i < phoneNumber.length(); i++){
    	    char c = phoneNumber.charAt(i);        
    	    if (Character.isDigit(c)){
    	    	onlyNum += c;
    	    }
    	}
    	return onlyNum;
    }
    /* Request updates at startup */
    @Override
    protected void onResume() {
      super.onResume();
      //locationManager.requestLocationUpdates(provider, 400, 1, this);
      //TextView tv = (TextView) findViewById(R.id.mainPageText);    	
      //tv.setText(R.string.message);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
      super.onPause();
      //locationManager.removeUpdates(this);

      
    }

    @Override
    public void onLocationChanged(Location location) {
    	//updateWithNewLocation(location);
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
    
    private String convertToDMS(double lat, double lon){
    	String converted;
		converted = String.format("?ie=UTF8&q=findme@%f,%f,17z",lat, lon);
    	return converted;
    }
    private void updateWithNewLocation(Location location)
    {
        String latLong;
        String converted;
        TextView myLocation;
        myLocation = (TextView) findViewById(R.id.currentposition);

        String addressString = "no address found";

        if (location != null)
        {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            converted = convertToDMS(lat,lon);
            latLong = "Lat:" + lat + "\nLong:" + lon;

            double lattitude = location.getLatitude();
            double longitude = location.getLongitude();

            Geocoder gc = new Geocoder(this, Locale.getDefault());
            try
            {
                List<Address> addresses = gc.getFromLocation(lattitude,
                        longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0)
                {
                    Address address = (Address) addresses.get(0);
                    sb.append(address.getAddressLine(0)).append("\n");
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                }
                addressString = sb.toString();
            } catch (Exception e)
            {
            }
        } else
        {
            latLong = " NO Location Found ";
            converted = " NO Location Found ";
        }
        myLocation.setText("my current position is at \n" +  getString(R.string.linkformat) + converted);// + "\n "  // getString(R.string.linkformat) +
             // + "\n\n"  + addressString);
    }
    
    
    class doTask extends AsyncTask<String, Void, String>{
    	@Override
    	protected String doInBackground(String...strings){
    		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    		LocationListener locationListener = new LocationListener() {
		        public void onLocationChanged(Location location) {
		          // Called when a new location is found by the network location provider.
		        	TextView myLocation = (TextView) findViewById(R.id.currentposition);
		    		String s = location.toString();  		
					myLocation.setText(s);
		        }

		        public void onStatusChanged(String provider, int status, Bundle extras) {}

		        public void onProviderEnabled(String provider) {}

		        public void onProviderDisabled(String provider) {}
		      };
		    
		    Criteria criteria = new Criteria();
		    provider = locationManager.getBestProvider(criteria, false);
		    Location location = locationManager.getLastKnownLocation(provider);
		    //locationManager.requestLocationUpdates(provider, 0, 0, locationListener); 
		    TextView myLocation = (TextView) findViewById(R.id.currentposition);
    		String s = updateMessage(location);  		
			myLocation.setText(s);
    		return "";
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
	            return "my current position is at \n" + getString(R.string.linkformat) + converted;
	        } else {
	            converted = " NO Location Found ";
	            return converted;
	        }
	        
	    }
    }
    
    /*class FindLocationTask extends AsyncTask<String, Void, String>{
    	private LocationManager locationManager;
    	
    	@Override
    	protected void onPreExecute(){
    		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
		      //onLocationChanged(location);
		      //return location;
		    } else {
		    	//updateWithNewLocation(null);
		    	//return null;
		    }  
    	}
    	
    	@Override
		protected String doInBackground(String...params) {				    
		    return "";
		}

		protected void onPostExecute(Location result) {			
			String newMessage = updateMessage(result);			
			TextView myLocation = (TextView) findViewById(R.id.mainPageText);
			myLocation.setText("meh");
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
	            return "my current position is at \n" + getString(R.string.linkformat) + converted;
	        } else {
	            converted = " NO Location Found ";
	            return converted;
	        }
	        
	    }
		
    	
    }*/
	
}