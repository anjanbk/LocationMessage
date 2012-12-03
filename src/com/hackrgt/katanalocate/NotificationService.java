package com.hackrgt.katanalocate;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.hackrgt.katanalocate.MyLocation.LocationResult;

import static com.hackrgt.katanalocate.CommonUtilities.SENDER_ID;
import static com.hackrgt.katanalocate.CommonUtilities.displayMessage;


/**
 * Background service that pulls data from the server and then simultaneously polls the local database.
 *
 */
public class NotificationService extends Service {
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private boolean result;
	  // Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          super(looper);
	      }
	      @Override
	      public void handleMessage(Message msg) {
	          final DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
	          //Check if location is a match
	          LocationResult locationResult = new LocationResult(){
	        	    @Override
	        	    public void gotLocation(Location location){
	        	    	//Check location against DB
	        	        result = helper.checkLocation(location.getLatitude(), location.getLongitude());
	        	    }
	        	};
	        	//Get location
	        	MyLocation myLocation = new MyLocation();
	        	myLocation.getLocation(getApplicationContext(), locationResult);
	          if (result) {
	        	  //Display notification
	        	  generateNotification(getApplicationContext(), "You have a new message!");
	        	  //Cancel alarm. should be able to cancel with equivalent PendingIntent. This probably wont work
	        	  Intent intent = new Intent(getApplicationContext(), NotificationService.class);
	        	  PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
	        	  AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	        	  alarmManager.cancel(pintent);
	          }
	          
	          stopSelf(msg.arg1);
	      }
	  }

	  @Override
	  public void onCreate() {
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
		
	    HandlerThread thread = new HandlerThread("ServiceStartArguments",Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();
	    
	    // Get the HandlerThread's Looper and use it for our Handler 
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	  }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;
	      mServiceHandler.sendMessage(msg);
	      
	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
	      // We don't provide binding, so return null
	      return null;
	  }
	  
	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
	  }
	  
	  private static void generateNotification(Context context, String message) {
	        int icon = R.drawable.ic_launcher;
	        long when = System.currentTimeMillis();
	        NotificationManager notificationManager = (NotificationManager)
	                context.getSystemService(Context.NOTIFICATION_SERVICE);
	        Notification notification = new Notification(icon, message, when);
	 
	        String title = context.getString(R.string.app_name);
	 
	        Intent notificationIntent = new Intent(context, MainActivity.class);
	        // set intent so it does not start a new activity
	        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        PendingIntent intent =
	                PendingIntent.getActivity(context, 0, notificationIntent, 0);
	        notification.setLatestEventInfo(context, title, message, intent);
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	 
	        // Play default notification sound
	        notification.defaults |= Notification.DEFAULT_SOUND;
	 
	        // Vibrate if vibrate is enabled
	        notification.defaults |= Notification.DEFAULT_VIBRATE;
	        notificationManager.notify(0, notification);      
	 
	    }
	    


}