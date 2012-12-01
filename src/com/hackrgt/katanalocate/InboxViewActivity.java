package com.hackrgt.katanalocate;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Vector;

public class InboxViewActivity extends Activity {// extends ListActivity {

	
	private Map<String, String> DisplayItems;
	private List<Map<String, String>> data;
	private String selectedMessageID;
	
	private ListView InboxList;
	
	public String getselectedMessageID()
	{
		return selectedMessageID;
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_view);
        
        InboxList = (ListView) findViewById(R.id.InboxList);
        
        DisplayItems = new HashMap<String, String>();
        data = new ArrayList<Map<String, String>>();
        DisplayItems.put("Sender", "Chandim");
        DisplayItems.put("Subject", "Test Subject");
        data.add(DisplayItems);
        DataBaseHelper dbhelper = new DataBaseHelper(this);
        DisplayItems = new HashMap<String, String> ();
        DisplayItems.put("Sender", "Chandim2");
        DisplayItems.put("Subject", dbhelper.fetchUserGCMID("Chandim2"));
        data.add(DisplayItems);
        /*DisplayItems = new HashMap<String, String>();
        DisplayItems.put("Sender", "DJ");
        DisplayItems.put("Subject", "Test Subject2");
        data.add(DisplayItems);*/
        
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"Sender", "Subject"}, new int[] {android.R.id.text1, android.R.id.text2});
        InboxList.setAdapter(adapter);
        
        
        //DisplaySets = new Vector<DisplayItems>();
        //DisplaySets.add(new DisplayItems("Chandim", "Test Message"));
    }
}
