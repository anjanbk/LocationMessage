package com.hackrgt.katanalocate;

import com.facebook.FacebookActivity;
import com.facebook.SessionState;
import com.hackrgt.katanalocate.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FacebookActivity implements OnClickListener {
	
    private Button inboxButton, sentMessagesButton, sendMessageButton;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        this.openSession();
        
        //Testing send_message_view
        //Intent map = new Intent(this, MapLocateActivity.class);
		//startActivity(map);
        
        inboxButton = (Button) findViewById(R.id.inboxButton);
        inboxButton.setOnClickListener(this);
        sentMessagesButton = (Button) findViewById(R.id.sentMessagesButton);
        sentMessagesButton.setOnClickListener(this);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(this);
    }
    
    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onClick(View v) {
		int viewId = v.getId();
		
		if (viewId == inboxButton.getId()) {
			Intent inboxActivity = new Intent(this, InboxViewActivity.class);
			startActivity(inboxActivity);
		}
		else if (viewId == sentMessagesButton.getId()) {
			Intent sentMessagesActivity = new Intent(this, SentMessagesActivity.class);
			startActivity(sentMessagesActivity);
		}
		else if (viewId == sendMessageButton.getId()) {
			Intent sendMessageActivity = new Intent(this, SendMessageActivity.class);
			startActivity(sendMessageActivity);
		}
	}

    /*public void buttonSendMessage(View v){
    	 setContentView(R.layout.send_message_view);
    }
    public void buttonSentMessages(View v){
    	setContentView(R.layout.sent_messages_view);
    }
    public void buttonInbox(View v){
      	 setContentView(R.layout.inbox_view);
    }
    public void buttonBack(View v){
    	setContentView(R.layout.main_view);
    }*/
    
}
