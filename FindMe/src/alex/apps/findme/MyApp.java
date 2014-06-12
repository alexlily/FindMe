package alex.apps.findme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.json.JSONArray;

import android.app.Application;

public class MyApp extends Application{
	protected JSONArray contactlist = new JSONArray();
	protected String jsonstring;
	protected HashMap<String,String> contactdict; //= new HashMap<String, String>();
	public MyApp(){
		try {
			//File file = new File("save_contacts");
			FileInputStream fin = new FileInputStream("save_contacts");
			ObjectInputStream ois = new ObjectInputStream(fin);
			contactdict = (HashMap<String,String>) ois.readObject();
			ois.close();
		//((MyApp)this.getApplication()).jsonstring = putToJSON(name, number);
		//updateContacts();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			contactdict = new HashMap<String,String>();
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			contactdict = new HashMap<String,String>();
			//contactdict.put("cries","4087173336");
			e.printStackTrace();
		}
	}
}
