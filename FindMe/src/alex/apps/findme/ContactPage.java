package alex.apps.findme;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import alex.apps.findme.R;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Profile;

public class ContactPage extends ActionBarActivity {
	Handler h = new Handler();
	//private static HashMap<String,String> contactdict;
	private static final int PICK_CONTACT = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_page);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		h.post(new Runnable(){

	        @Override
	        public void run() {
	            // call your function
	            h.postDelayed(this,500);
	            try{
	            	TextView tv = (TextView) findViewById(R.id.contact_list);
	            	tv.setText(((MyApp)getApplication()).contactdict.toString());
	            }
	            catch(NullPointerException e){}
	        }

	    });
		
	}
	
	public void addContact(View view){
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT);	
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		if ( reqCode==PICK_CONTACT && resultCode == Activity.RESULT_OK) {
			Uri contactData = data.getData();
			String[]mProjection = null;//new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
			Cursor c =  getContentResolver().query(contactData, mProjection, null, null, null);
			if (c.moveToFirst()) {
				Long id = c.getLong(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID)); //ContactsContract.CommonDataKinds.Phone.NUMBER)
				// Profile.DISPLAY_NAME
				String number = getContactPhone(String.valueOf(id));		
				String name = c.getString(c.getColumnIndexOrThrow(Profile.DISPLAY_NAME));
				((MyApp)this.getApplication()).contactdict.put(name,number);
				//((MyApp)this.getApplication()).jsonstring = putToJSON(name, number);
				//updateContacts();		
				TextView tv = (TextView) findViewById(R.id.contact_list);
            	tv.setText(((MyApp)getApplication()).contactdict.toString());
			}
		}
	}

	
	private String getContactPhone(String contactID) {
	    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	    String[] projection = null;
	    String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?";
	    String[] selectionArgs = new String[] { contactID };
	    String sortOrder = null;
	    Cursor result = getContentResolver().query(uri, projection, where, selectionArgs, sortOrder);
	    if (result.moveToFirst()) {
	        String phone = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        if (phone == null) {
	            result.close();
	            return null;
	        }
	        result.close();
	        return phone;
	    }
	    result.close();
	    return null;
	}
	
	
	/*public void updateContacts(){
		
		try {
			JSONObject jobj = new JSONObject(((MyApp)this.getApplication()).jsonstring);
			JSONObject jObject  = new JSONObject(((MyApp)this.getApplication()).jsonstring);
		    JSONObject  menu = jObject.getJSONObject("menu");

		    Map<String,String> map = new HashMap<String,String>();
		    Iterator<String> iter = menu.keys();
		    while(iter.hasNext()){
		        String key = (String)iter.next();
		        String value = menu.getString(key);
		        map.put(key,value);
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*String contactList = "";
		for (String key: MainActivity.contactList.keySet()){
			contactList = contactList + 
					" " + key + " " + MainActivity.contactList.get(key) + "\n";
		}
		TextView tv = (TextView) findViewById(R.id.contact_list);
		tv.setText(contactList);
		*/
		//SharedPreferences prefs =
		//		   PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		//String number = prefs.getString(getString(R.string.new_contact_number), num);
		//TextView tv = (TextView) findViewById(R.id.contact_list);
		//tv.setText(number);
		//return number;
	//}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void clearContacts(View view){
		/*Dialog dialog = new Dialog(getBaseContext());	
		dialog.setContentView(R.layout.dialog_clear_contact_confirm);
		dialog.show();*/
		((MyApp)this.getApplication()).contactdict = new HashMap<String,String>();
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_contact_page,
					container, false);
			return rootView;			
		}
	}
	
	public static class Background implements Runnable{
		@Override
		public void run(){
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			
		}
	}

}
