package com.hackrgt.katanalocate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.facebook.Session;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;

public class SentMessagesActivity extends Activity {
    
	private Map<String, String> DisplayItems;
	private List<Map<String, String>> data;
    private ListView sentMessagesList;
    final DataBaseHelper dbhelper = new DataBaseHelper(this);
    
	List<MessageTable> messages;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_messages_view);
       
        sentMessagesList = (ListView) findViewById(R.id.sentMessagesList);
        data = new ArrayList<Map<String, String>>();
        
        final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request request = Request.newMeRequest(
				      session,
				      new Request.GraphUserCallback() {
				        // callback after Graph API response with user object
				        public void onCompleted(GraphUser user, Response response) {
				          if (user != null) {
				        	  Log.d("SentMessagesActivity", "Facebook ID:" + user.getId());
				        	  messages = dbhelper.fillsentMessages(user.getId());
				          	  
				        	  for (MessageTable msg: messages)
				        	  {
				        	  	Log.d("SentMessagesActivity", "Sender " + msg.getUser());
				        	  	Log.d("SentMessagesActivity", "Subject " + msg.getSubject());
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
        sentMessagesList.setAdapter(adapter);
        sentMessagesList.setOnItemClickListener(new OnItemClickListener() {

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
		Log.d("William Part", Integer.toString(MessageId));
		viewMessageActivity.putExtra("MessageID", MessageId);
		viewMessageActivity.putExtra("sentreceive", 1);
		startActivity(viewMessageActivity);
	}

}
