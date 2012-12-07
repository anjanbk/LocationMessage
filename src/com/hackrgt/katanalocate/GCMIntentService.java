package com.hackrgt.katanalocate;
 
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
 
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMBaseIntentService;

import static com.hackrgt.katanalocate.CommonUtilities.SENDER_ID;
import static com.hackrgt.katanalocate.CommonUtilities.TAG;
import static com.hackrgt.katanalocate.CommonUtilities.displayMessage;
 
public class GCMIntentService extends GCMBaseIntentService {
 
    private static final String TAG = "GCMIntentService";
 
    public GCMIntentService() {
        super(SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "From GCM: device successfully registered!");
        Session session = Session.getActiveSession();
        final Context mContext = context;
        final String mId = registrationId;
        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					ServerUtilities.register(mContext, user.getId() , mId);
				}
			}
		});
    	Request.executeBatchAsync(request);
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        ServerUtilities.unregister(context, registrationId);
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        //Get passed in data
        int id = intent.getExtras().getInt("id");
        String message = intent.getExtras().getString("message");
        String dateTime = intent.getExtras().getString("DateTime");
        double locLat = intent.getExtras().getDouble("latitude");
        double locLong = intent.getExtras().getDouble("longitude");
        String subject = intent.getExtras().getString("subject");
        int type = intent.getExtras().getInt("type");
        String senderId = intent.getExtras().getString("senderId");
        String receiverId = intent.getExtras().getString("receiverId");
        String senderName = intent.getExtras().getString("senderName");
        String receiverName = intent.getExtras().getString("receiverName");
        String rGcmId = intent.getExtras().getString("receiverGcmId");
        String sGcmId = intent.getExtras().getString("senderId");
        MessageTable messageObj = new MessageTable(id, dateTime, locLat, locLong, subject, message, type);
        UserTable sender = new UserTable(senderId, senderName, sGcmId);
        UserTable receiver = new UserTable(receiverId, receiverName, rGcmId);
        
        //Add to table of received messages
        DataBaseHelper helper = new DataBaseHelper(this);
        helper.addMessage(messageObj, sender, receiver);
        //Start a service to poll
        Intent serviceIntent = new Intent(getApplicationContext(), NotificationService.class);
        serviceIntent.putExtra("latitude", locLat);
        serviceIntent.putExtra("longitude", locLong);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //Set alarm every 3 minutes to call service to check if user is in location for a message
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 180*1000, pintent);
        Log.d(TAG, "Received Message: " + message);
        
        displayMessage(context,message);
        
        generateNotification(context, message);
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        /*
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
        */
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        /*
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
                */
        return super.onRecoverableError(context, errorId);
    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.com_facebook_button_check, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }
 
}
