package com.hackrgt.katanalocate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import static com.hackrgt.katanalocate.CommonUtilities.SENDER_ID;
import static com.hackrgt.katanalocate.CommonUtilities.displayMessage;


/**
 * Background service that pulls data from the server and then simultaneously polls the local database.
 * @author anjan
 *
 */
public class NotificationService extends GCMBaseIntentService {
	private static final String TAG = "NotificationService";
	
	public NotificationService() {
		super(SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString("price");
      
		Log.i(TAG, "Received message: " + message);
        //displayMessage(context, message);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        //ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}