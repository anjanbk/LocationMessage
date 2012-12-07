package com.hackrgt.katanalocate;

import static com.hackrgt.katanalocate.CommonUtilities.SENDER_ID;
import static com.hackrgt.katanalocate.CommonUtilities.TAG;

import java.io.File;

import com.facebook.FacebookActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.hackrgt.katanalocate.NotificationService.LocalBinder;
import com.hackrgt.katanalocate.R;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FacebookActivity {
	private NotificationService mService;
	private boolean mBound = false;
    private MainFragment mainFragment;
    private boolean isResumed = false;
    AsyncTask<Void, Void, Void> mRegisterTask;
    
    private void registerDevice(String fbid) {
    	GCMRegistrar.checkDevice(this);
       	//GCMRegistrar.checkManifest(context);
    	registerReceiver(mHandleMessageReceiver, new IntentFilter("android.intent.action.MAIN"));
	    final String regId = GCMRegistrar.getRegistrationId(this);
       	
       	if (regId.equals("")) {
            // Registration is not present, register now with GCM
       		Log.d(TAG, "Not registered");
            GCMRegistrar.register(this, SENDER_ID);
       	} else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
            	Log.d(TAG, "Already registered with GCM, regID: " + regId);
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                final String name = fbid;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                    	Log.d(TAG, "Registering...");
                        boolean register = ServerUtilities.register(context,name,regId);
                        
                        if (!register) {
                            GCMRegistrar.unregister(context);
                        }
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                    	mRegisterTask=null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
            }
       	}
    }

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
	    // Register the device with GCM, if not already registered
	    Session session = Session.getActiveSession();
	    if (session.isOpened()) {
	    	Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null)
				       	registerDevice(user.getId());
				}
			});
        	Request.executeBatchAsync(request);
	    }
	   
	    /*
	     * Testing Inbox View
	     */
	    Log.d("Main Activity", "Tried to create DBHelper");
	    DataBaseHelper dbhelper = new DataBaseHelper(this);
	    //dbhelper.addUser("Chandim", "Chandim", "Success");
	    //dbhelper.addUser("Diya", "Diya", "Dummy");
	    MessageTable message = new MessageTable(1, "10:30", 47.6097, -122.3331, "Message", "Hello, this is a message!", "Diya", 2);
	    UserTable Sender = new UserTable("586525603", "Chandim Chatterjee", "gcm");
	    UserTable Receiver = new UserTable("632583495", "Anjan Karanam", "Dummy");
	    dbhelper.addUser("586525603", "Chandim Chatterjee", "gcm");
	    dbhelper.addUser("632583495", "Anjan Karanam", "Dummy");
	    dbhelper.addMessage(message, Sender, Receiver);
	    //dbhelper.addMessage(message2, Receiver, Sender);
	    dbhelper.checkMessage();
	    dbhelper.checkUser();
	    dbhelper.checkSendReceive();
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	Intent intent = new Intent(this, NotificationService.class);
    	bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    
 // Handles the connection to the Android Service
    private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d(TAG,"Service Connected");

			LocalBinder binder = (LocalBinder)service;
			mService = binder.getService();
			mBound = true;
		}
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d(TAG, "Service Disconnected");

			mBound = false;
		}
    };
    
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	Log.d(TAG, "Message received");
            String newMessage = intent.getExtras().getString("message");
            WakeLocker.acquire(getApplicationContext());
 
            // Showing received message
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
            WakeLocker.release();
        }
    };
    
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
        	File database = getApplicationContext().getDatabasePath("DatabaseLocation");
        	if (!database.exists()) {
        		//Get users Facebook Id
        		Log.d("Chandim - Main Activity", "Database doesn't exist");
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
        								Log.d("Chandim - Main Activity", "Database created");
        								DataBaseHelper helper = new DataBaseHelper(MainActivity.this);
        								helper.addUser(userId, userName, "");
        								helper.addUser("Chandim", "Chandim", "Success");
        							    helper.addUser("Diya", "Diya", "Dummy");
        							    helper.addUser("Nathan", "Nathan", "Hurley");
        							    MessageTable message = new MessageTable(1, "1234567", 36, 54, "Hi", "It's been a while, how are you?", "Diya", 2);
        							    helper.sendMessage(message, "Nathan", user.getId());
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
    protected void onStop() {
    	super.onStop();
    	
    	if (mBound) {
    		unbindService(mConnection);
    		mBound = false;
    	}
    }
    
    public void onDestroy() {
    	if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
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
