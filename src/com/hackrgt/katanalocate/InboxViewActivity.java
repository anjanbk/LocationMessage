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
	
	private ListView InboxList;
	//private Vector<DisplayItems> DisplaySets;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_view);
        
        InboxList = (ListView) findViewById(R.id.InboxList);
        
        DisplayItems = new HashMap<String, String>(1);
        data = new ArrayList<Map<String, String>>();
        DisplayItems.put("Chandim", "Test Subject");
        //data.add(DisplayItems);
        
        //SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"Chandim"}, new int[] {android.R.id.text1, android.R.id.text2});
        //InboxList.setAdapter(adapter);
        
        
        //DisplaySets = new Vector<DisplayItems>();
        //DisplaySets.add(new DisplayItems("Chandim", "Test Message"));
    }
}
