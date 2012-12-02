package com.hackrgt.katanalocate;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Vector;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class InboxViewActivity extends Activity implements OnItemClickListener {// extends ListActivity {

	
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
        
        data = new ArrayList<Map<String, String>>();
        
        final DataBaseHelper dbhelper = new DataBaseHelper(this);
        //final List<MessageTable> messages = new ArrayList<MessageTable>();
        final List<MessageTable> messages = new ArrayList();
        GetSelfData fbhelper = new GetSelfData();
        
        
        /*DisplayItems = new HashMap<String, String>();
        DisplayItems.put("Sender", "DJ");
        DisplayItems.put("Subject", "Test Subject2");
        data.add(DisplayItems);*/
        
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"Sender", "Subject"}, new int[] {android.R.id.text1, android.R.id.text2});
        InboxList.setAdapter(adapter);
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
