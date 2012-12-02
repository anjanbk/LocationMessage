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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.os.Bundle;

public class SentMessagesActivity extends Activity implements OnClickListener {
    
	private Map<String, String> DisplayItems;
	private List<Map<String, String>> data;
    private ListView sentMessagesList;
    private ArrayList<String> itemArray;
    private ArrayList<String> itemArray2;
    private ArrayAdapter<String> itemAdapter;
    final DataBaseHelper dbhelper = new DataBaseHelper(this);
    
	List<MessageTable> messages;
	private String Id = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_messages_view);

        
     
        itemArray = new ArrayList<String>();
        itemArray.clear();
        itemArray2 = new ArrayList<String>();
        itemArray2.clear();
        
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArray);
       
        
        final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request request = Request.newMeRequest(
				      session,
				      new Request.GraphUserCallback() {
				        // callback after Graph API response with user object
				        public void onCompleted(GraphUser user, Response response) {
				          if (user != null) {
				        	  Log.d("SentMessagesActivity", "Facebook ID:" + user.getId());
				        	  Id = user.getId();
				        	  messages = dbhelper.fillsentMessages("Chandim");
				          	  
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

        itemAdapter.notifyDataSetChanged();
	}

	public void populatelistview()
	{
		SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"Sender", "Subject"}, new int[] {android.R.id.text1, android.R.id.text2});
		sentMessagesList.setAdapter(adapter);
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
