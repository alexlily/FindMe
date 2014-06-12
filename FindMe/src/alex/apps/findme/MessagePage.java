package alex.apps.findme;
import alex.apps.findme.R;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import android.preference.PreferenceManager;

public class MessagePage extends ActionBarActivity {
	public static final String EXTRA_MESSAGE = "merp";
	//Handler h = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_page);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		try{
			SharedPreferences prefs =
					   PreferenceManager.getDefaultSharedPreferences(MessagePage.this);
			String message = prefs.getString(getString(R.string.message),getString(R.string.message));
			//Intent intent = getIntent();
			//TextView tv = (TextView) findViewById(R.id.curr_message);
			//String message = intent.getStringExtra(MessagePage.EXTRA_MESSAGE);
	        //tv.setText(message);
		}catch (Exception e){
			
		}
		/*h.post(new Runnable(){

	        @Override
	        public void run() {
	            // call your function
	            h.postDelayed(this,500);
	            try{
	            	TextView tv = (TextView) findViewById(R.id.curr_message);
	        		//tv.setText(((MyApp)getApplication()).jsonstring);
	            	SharedPreferences prefs =
	     				   PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	            	String message = prefs.getString(getString(R.string.message),getString(R.string.message));
	            	tv.setText(message);
	            }
	            catch(NullPointerException e){}
	        }

	    });*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_page, menu);
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
	public void resetMessage(View view){
		//MainActivity.HELP_MESSAGE = getString(R.string.message);
		//TextView textview = (TextView) findViewById(R.id.curr_message);
		//textview.setText(R.string.defaultmessage);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		MainActivity.clearForm(editText);
    	SharedPreferences prefs =
				   PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		Editor editor = prefs.edit();
		editor.putString(getString(R.string.message), getString(R.string.defaultmessage));
		editor.commit();
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(EXTRA_MESSAGE, getString(R.string.defaultmessage));
		startActivity(intent);
		
	}
	public void saveMessage(View view){
		EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	if (message.length() > 0){
	    	SharedPreferences prefs =
					   PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			Editor editor = prefs.edit();
			editor.putString(getString(R.string.message), message);
			editor.commit();
			
			//TextView textview = (TextView) findViewById(R.id.curr_message);
			//message = prefs.getString(getString(R.string.message),getString(R.string.message));
			//textview.setText(message);
	    	
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra(EXTRA_MESSAGE, message);
			startActivity(intent);
    	}
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
			View rootView = inflater.inflate(R.layout.fragment_message_page,
					container, false);
			return rootView;
		}
	}

}
