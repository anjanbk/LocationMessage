package com.hackrgt.katanalocate;

import com.facebook.SessionState;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements OnClickListener{

	private Button inboxButton, sentMessagesButton, sendMessageButton;

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.main_view, container, false);

	    inboxButton = (Button) view.findViewById(R.id.inboxButton);
	    inboxButton.setOnClickListener(this);
	    sentMessagesButton = (Button) view.findViewById(R.id.sentMessagesButton);
	    sentMessagesButton.setOnClickListener(this);
	    sendMessageButton = (Button) view.findViewById(R.id.sendMessageButton);
	    sendMessageButton.setOnClickListener(this);

	    return view;
	}
	
	public void onSessionStateChange(SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	   //Show the main UI if user is logged in
	          inboxButton.setVisibility(View.VISIBLE);
	          sentMessagesButton.setVisibility(View.VISIBLE);
	          sendMessageButton.setVisibility(View.VISIBLE);
	    } else if (state.isClosed()) {
	    	 //Hide the main UI if user is not logged in
	    	 inboxButton.setVisibility(View.GONE);
	         sentMessagesButton.setVisibility(View.GONE);
	         sendMessageButton.setVisibility(View.GONE);
	    }
	}
	
	public void onClick(View v) {
		int viewId = v.getId();
		
		if (viewId == inboxButton.getId()) {
			Intent inboxActivity = new Intent(this.getActivity(), InboxViewActivity.class);
			startActivity(inboxActivity);
		}
		else if (viewId == sentMessagesButton.getId()) {
			Intent sentMessagesActivity = new Intent(this.getActivity(), SentMessagesActivity.class);
			startActivity(sentMessagesActivity);
		}
		else if (viewId == sendMessageButton.getId()) {
			Intent sendMessageActivity = new Intent(this.getActivity(), SendMessageActivity.class);
			startActivity(sendMessageActivity);
		}
	}
	
}
