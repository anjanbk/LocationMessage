package com.hackrgt.katanalocate;

import com.facebook.FacebookActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.hackrgt.katanalocate.R;

import android.os.Bundle;
import android.view.Menu;

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
