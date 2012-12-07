package com.hackrgt.katanalocate;

import java.util.List;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.hackrgt.katanalocate.MyLocation.LocationResult;

import static com.hackrgt.katanalocate.CommonUtilities.SENDER_ID;
import static com.hackrgt.katanalocate.CommonUtilities.displayMessage;


/**
 * Background service that polls the local database.
 *
 */
public class NotificationService extends Service {
	private LocationManager mLocationManager;
	private Location mLocation;
	private final IBinder mBinder = new LocalBinder();
	private static final String TAG = "NotificationService";
	
	private Looper mServiceLooper;
	private static final int TWO_MINUTES = 1000*60*2;
	private ServiceHandler mServiceHandler;
	private boolean result;
	  // Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          super(looper);
	      }
	      
	      /*
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
	       */
	  }
	  
	  private void queryMessages() {
		  final DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
		  List<String> messages;
		  
		  // Run spatial query
		  messages = helper.getMessages(mLocation.getLatitude(),mLocation.getLongitude());
		  
		  if (messages.size() > 0) {
			  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("New Message!")
		        .setContentText("Found " + (messages.size()/2) + ((messages.size()/2) > 1 ? " messages " : " message ") + " near you!");
			  Intent resultIntent = new Intent(this, MainActivity.class);
			  int mId=0;
			  
			// The stack builder object will contain an artificial back stack for the
				// started Activity.
				// This ensures that navigating backward from the Activity leads out of
				// your application to the Home screen.
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
				// Adds the Intent that starts the Activity to the top of the stack
				stackBuilder.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent =
				        stackBuilder.getPendingIntent(
				            0,
				            PendingIntent.FLAG_UPDATE_CURRENT
				        );
				mBuilder.setContentIntent(resultPendingIntent);
			  
			  NotificationManager mNotificationManager =
					    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					// mId allows you to update the notification later on.
					mNotificationManager.notify(mId, mBuilder.build());
		  }
		  
	  }

	  @Override
	  public void onCreate() {
		  Log.d(TAG, "Service Created");
		  mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		  final boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		  final boolean networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		  
		  if (gpsEnabled) {
			  Log.d(TAG, "Getting location from GPS");
			  mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						10000,   // 10000 seconds
						100,      // 100 meters
						listener);
		  } else if (networkEnabled) {
			  Log.d(TAG, "Getting location from Network");
			  mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					  	10000,
					  	100,
					  	listener);
		  }
		  
		  
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
	  
	  public class LocalBinder extends Binder {
		  	NotificationService getService() {
				return NotificationService.this;
			}
		}

	  @Override
	  public IBinder onBind(Intent intent) {
	      return mBinder;
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
	  
	  protected boolean isBetterLocation(Location location, Location currentBestLocation) {
			if (currentBestLocation == null)
				return true;

			long timeDelta = location.getTime() - currentBestLocation.getTime();
			boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
			boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
			boolean isNewer = timeDelta > 0;

			if (isSignificantlyNewer)
				return true;
			else if (isSignificantlyOlder)
				return false;

			// Check whether the new location fix is more or less accurate
		    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		    boolean isLessAccurate = accuracyDelta > 0;
		    boolean isMoreAccurate = accuracyDelta < 0;
		    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		    // Check if the old and new location are from the same provider
		    boolean isFromSameProvider = isSameProvider(location.getProvider(),
		            currentBestLocation.getProvider());

		    // Determine location quality using a combination of timeliness and accuracy
		    if (isMoreAccurate) {
		        return true;
		    } else if (isNewer && !isLessAccurate) {
		        return true;
		    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
		        return true;
		    }
		    return false;

		}
	  
	  private boolean isSameProvider(String provider1, String provider2) {
			if (provider1 == null)
				return provider2 == null;
			return provider1.equals(provider2);
		}
	    
	  private final LocationListener listener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (isBetterLocation(location, mLocation)) {
					mLocation = location;
					Log.d(TAG, "Notification Changed");
					
					queryMessages();
					// Query messages
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}
		};

}