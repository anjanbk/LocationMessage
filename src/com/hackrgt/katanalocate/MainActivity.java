package com.hackrgt.katanalocate;

import java.io.File;

import com.facebook.FacebookActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.hackrgt.katanalocate.R;

import android.os.Bundle;
import android.view.Menu;

/**
 * Logs the user in through Facebook by creating a session and displays the home 
 * screen upon successful login.
 *
 */
public class MainActivity extends FacebookActivity {
    private MainFragment mainFragment;
    private boolean isResumed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//open a facebook session
	
		this.openSession();
	    if (savedInstanceState == null) {
	        // Add the fragment
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Restore the fragment
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
    }
    
    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
    	super.onSessionStateChange(state, exception);
        if (isResumed) {
            mainFragment.onSessionStateChange(state, exception);
            if (state.isClosed()) {
            	//Default to SSO login and show dialog if Facebook App isnt installed
            	this.openSession();
            }
        }
        
        if (state.isOpened()) {
        	//Get database and check if it has been created
        	File database = getApplicationContext().getDatabasePath(DataBaseHelper.DB_NAME);
        	if (!database.exists()) {
        		//Get users Facebook Id
        		final Session session = Session.getActiveSession();
        		if (session != null && session.isOpened()) {
        			Request request = Request.newMeRequest(
        					session,
        					new Request.GraphUserCallback() {
        						// callback after Graph API response with user object
        						public void onCompleted(GraphUser user, Response response) {
        							if (user != null) {
        								final String userId = user.getId();
        								final String userName = user.getName();
        								//Add the user to the sqlite database
        								DataBaseHelper helper = new DataBaseHelper(MainActivity.this);
        								helper.addUser(userId, "", userName);
        							}
        						}
        					}
        					);
        			Request.executeBatchAsync(request); 
        		}
        	}
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
    }
    
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session.getState(), null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
