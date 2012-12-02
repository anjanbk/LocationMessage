package com.hackrgt.katanalocate;


import static com.hackrgt.katanalocate.CommonUtilities.SENDER_ID;
import static com.hackrgt.katanalocate.CommonUtilities.SERVER_URL;
import static com.hackrgt.katanalocate.CommonUtilities.TAG;
import static com.hackrgt.katanalocate.CommonUtilities.DISPLAY_MESSAGE_ACTION;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.facebook.FacebookActivity;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class RegisterActivity extends FacebookActivity {
	String fbName;
	
    // UI elements
    EditText txtName;
 
    // Register button
    Button btnRegister;

    private AsyncTask<Void, Void, Void> mRegisterTask;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting activity");
        setContentView(R.layout.activity_register);
 
        // Check if Internet present
        // Check if GCM configuration is set
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
        	Log.d(TAG, "Configuration Error!" + " Please set your Server URL and GCM Sender ID");
            // stop executing code by return
             return;
        }
 
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	
        	Log.d(TAG, "Session is opened, session: " + Session.getActiveSession().getAccessToken());
        	/*
        	Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
						Log.d(TAG, "AsyncRequest completed: " + user.getId());
						fbName = user.getId();
					}
				}
			});
        	Request.executeBatchAsync(request);
			*/
        	fbName = "632583495";
        
	        btnRegister = (Button) findViewById(R.id.btnRegister);
	 
	        /*
	         * Click event on Register button
	         * */
	        if (fbName != null) {
	        	Log.d(TAG, "FB Name worked!");
	        	btnRegister.setOnClickListener(new RegisterButtonOnClickListener(this, fbName));
	        } else {
	        	Log.d(TAG, "FB Name was null");
	        }
        }
    }
    
    private class RegisterButtonOnClickListener implements View.OnClickListener {
    	private Context context;
    	private String fbName;
    	
    	public RegisterButtonOnClickListener(Context context, String facebookUserId) {
    		this.context = context;
    		fbName = facebookUserId;
    	}

		@Override
		public void onClick(View v) {
           	// Register the device with the GCM server, if not already registered.
			Log.d(TAG, "Clicked");
			
           	GCMRegistrar.checkDevice(context);
           	//GCMRegistrar.checkManifest(context);
           	registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
           	final String regId = GCMRegistrar.getRegistrationId(context);
           	
           	if (regId.equals("")) {
                // Registration is not present, register now with GCM
           		Log.d(TAG, "Not registered");
                GCMRegistrar.register(context, SENDER_ID);
            } else {
                // Device is already registered on GCM
                if (GCMRegistrar.isRegisteredOnServer(context)) {
                    // Skips registration.
                    Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
                } else {
                    // Try to register again, but not in the UI thread.
                    // It's also necessary to cancel the thread onDestroy(),
                    // hence the use of AsyncTask instead of a raw thread.
                    final Context context = this.context;
                    final String name = this.fbName;
                    mRegisterTask = new AsyncTask<Void, Void, Void>() {
     
                        @Override
                        protected Void doInBackground(Void... params) {
                            // Register on our server
                        	Log.d(TAG, "Registering...");
                            ServerUtilities.register(context,name,regId);
                            return null;
                        }
     
                        @Override
                        protected void onPostExecute(Void result) {
                            mRegisterTask = null;
                        }
     
                    };
                    mRegisterTask.execute(null, null, null);
                }
            }
		}
    	
    }
    
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString("message");
            // Waking up mobile if it is sleeping
            //WakeLocker.acquire(getApplicationContext());
 
            // Showing received message
            Log.d(TAG, newMessage);
 
            // Releasing wake lock
            //WakeLocker.release();
        }
    };
 
    @Override
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
}