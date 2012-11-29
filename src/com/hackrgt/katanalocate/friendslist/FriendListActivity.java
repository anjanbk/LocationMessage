package com.hackrgt.katanalocate.friendslist;

import java.util.ArrayList;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.hackrgt.katanalocate.R;
import com.hackrgt.katanalocate.SendMessageActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FriendListActivity extends Activity implements OnItemClickListener {

	private ListView friendsListView;
	private ArrayList<Friend> friends;
	private ArrayList<String> appUsersId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_view);
        
        AlertMessage.setActivity(this);
        appUsersId = getAppUsersId();
        
        Session session = Session.getActiveSession();
		if (session.isOpened()) {
			//Fetch friends from Facebook
			FacebookFriendsTask fbFriends = new FacebookFriendsTask(this, this);
			String token = session.getAccessToken();
			String url = "https://graph.facebook.com/me/friends";
			fbFriends.execute(new String[] {token, url});
		}
	}
	
	public void getFacebookFriendsResult(ArrayList<Friend> friends) {
		this.friends = friends;
		
		//Fill friends list
        friendsListView = (ListView) findViewById(R.id.friendsListView);
        FriendListItemAdapter friendsAdapter = new FriendListItemAdapter(this, 
    			android.R.layout.simple_list_item_1, 
    			friends);
        friendsAdapter.setUsersId(appUsersId);
        friendsListView.setAdapter(friendsAdapter);
        friendsListView.setOnItemClickListener(this);
	}
	
	private ArrayList<String> getAppUsersId() {
		//TODO: Fetch user ids from database
		ArrayList<String> users = new ArrayList<String>();
		users.add("11111");
		return users;
	}

	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		Friend friend = friends.get(position);
		
		if (friend.isAppUser()) {
			System.out.println(friend.getName()+" is app user");
			Intent sendMsgActivity = new Intent(this, SendMessageActivity.class);
			sendMsgActivity.putExtra("user_id", friend.getId());
			sendMsgActivity.putExtra("user_name", friend.getName());
			startActivity(sendMsgActivity);
		}
		else {
			//Send invite to friend
			System.out.println(friend.getName()+" is not an app user");
			sendRequestDialog(friend.getId());
		}
	}
	
	private void sendRequestDialog(String userId) {
        Bundle params = new Bundle();
        params.putString("message", "Send items to your friends when they arrive at the location.");
        params.putString("to", userId);
        WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(this, Session.getActiveSession(), params))
        		.setOnCompleteListener(new OnCompleteListener() {
                    
                    public void onComplete(Bundle values, FacebookException error) {
                    	if (values == null)
                    		return;
                        final String requestId = values.getString("request");
                        if (requestId != null) {
                            Toast.makeText(getApplicationContext(), 
                                "Request sent",  
                                Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), 
                                "Request cancelled", 
                                Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        
        requestsDialog.show();
    }

	
}
