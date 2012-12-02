package com.hackrgt.katanalocate;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
//import java.util.Vector;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class InboxViewActivity extends Activity{// implements OnItemClickListener {// extends ListActivity {

	
	private Map<String, String> DisplayItems;
	private List<Map<String, String>> data;
	private String selectedMessageID;
	List<MessageTable> messages;
	private String Id = null;
	
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

        messages = new ArrayList<MessageTable>();
        
        //GetSelfData fbhelper = new GetSelfData();
        
        final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request request = Request.newMeRequest(
				      session,
				      new Request.GraphUserCallback() {
				        // callback after Graph API response with user object
				        public void onCompleted(GraphUser user, Response response) {
				          if (user != null) {
				        	  Log.d("InboxViewActivity", "Facebook ID:" + user.getId());
				        	  Id = user.getId();
				        	  messages = dbhelper.fillInbox("Chandim");
				          	  
				        	  for (MessageTable msg: messages)
				        	  {
				        	  	Log.d("InboxViewActivity", "Sender " + msg.getUser());
				        	  	Log.d("InboxViewActivity", "Subject " + msg.getSubject());
				        	   	DisplayItems = new HashMap<String, String>();
				        	   	DisplayItems.put("Sender", msg.getUser());
				        	   	DisplayItems.put("Subject", msg.getSubject());
				        	   	data.add(DisplayItems);
				        	  }
				        	  populatelistview();
				          }
				        }
				      }
				    );
				    Request.executeBatchAsync(request); 
		}
		
    }
	
	public void populatelistview()
	{
		SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"Sender", "Subject"}, new int[] {android.R.id.text1, android.R.id.text2});
        InboxList.setAdapter(adapter);
        InboxList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				int MessageId = messages.get((int) id).getId();
				switchactivities(MessageId);
				//Toast.makeText(getBaseContext(), MessageSubject, Toast.LENGTH_LONG).show();
			}
        	
        });
	}
	
	public void switchactivities(int MessageId)
	{
		Intent viewMessageActivity = new Intent(this, ViewMessageActivity.class);
		viewMessageActivity.putExtra("MessageID", MessageId);
		viewMessageActivity.putExtra("sentreceive", 0);
		startActivity(viewMessageActivity);
	}
	

	

}
