package com.hackrgt.katanalocate;

import java.util.Calendar;
import java.util.Formatter;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.hackrgt.katanalocate.friendslist.AlertMessage;
import com.hackrgt.katanalocate.friendslist.FriendListActivity;
//import com.hackrgt.katanalocate.helper.ObjectSaver;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class SendMessageActivity extends Activity implements OnClickListener, OnTimeSetListener {

	private EditText messageSubject, messageBody;
	private Button chooseRecepient, chooseLocation, chooseTime, submitButton;
	private TimePickerDialog timePicker;
	private Calendar calendar;
	private DataBaseHelper dbHelper;
	
	private SharedPreferences prefs;
	private Editor editor;
	private String msgRecipientId, msgRecipientName, msgSubject, msgLocation, msgBody;
	private long msgTime;
	
	private final String STORED_TIME_FLAG = "timeSelected";
	private final String STORED_RECIEPIENT_FLAG = "userSelected";
	private final String STORED_LOCATION_FLAG = "locationSelected";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_view);
        
        AlertMessage.setActivity(this);
        dbHelper = new DataBaseHelper(getApplicationContext());
        
        //ObjectSaver.setActivity(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        
        messageSubject = (EditText) findViewById(R.id.messageSubject);
        messageBody = (EditText) findViewById(R.id.messageBody);
        
        //Set listeners on buttons
        chooseLocation = (Button) findViewById(R.id.chooseLocationButton);
        chooseLocation.setOnClickListener(this);
        chooseTime = (Button) findViewById(R.id.chooseTime);
        chooseTime.setOnClickListener(this);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        chooseRecepient = (Button) findViewById(R.id.receipient);
        chooseRecepient.setOnClickListener(this);
        
        //Setup time variables
        calendar = Calendar.getInstance();
        timePicker = new TimePickerDialog(this, this, 
        		calendar.get(Calendar.HOUR_OF_DAY), 
	    		calendar.get(Calendar.MINUTE), false);
        
        
        //Extract variables if passed in
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	msgLocation = extras.getString("location_name");
        	if (msgLocation != null) {
        		chooseLocation.setText("Choose Location: ("+msgLocation+")");
        		
        		editor.putBoolean(STORED_LOCATION_FLAG, true);
        		//editor.putString("locLat", msgRecipientId);
        		//editor.putString("locLong", msgRecipientName);
        		editor.putString("locName", msgLocation);
                editor.commit();
        	}
        	
        	msgRecipientId = extras.getString("user_id");
        	msgRecipientName = extras.getString("user_name");
        	if (msgRecipientId != null) {
        		chooseRecepient.setText("To: ("+msgRecipientName+")");
        		
        		editor.putBoolean(STORED_RECIEPIENT_FLAG, true);
        		editor.putString("userId", msgRecipientId);
        		editor.putString("userName", msgRecipientName);
                editor.commit();
        	}
        	//Set user again if previously set
        	else if (prefs.getBoolean(STORED_RECIEPIENT_FLAG, false)) {
        		msgRecipientId = prefs.getString("userId", null);
        		msgRecipientName = prefs.getString("userName", null);
        		if (msgRecipientId != null)
            		chooseRecepient.setText("To: ("+msgRecipientName+")");
        	}
        }
        
        //Set time again if previously set
        if (prefs.getBoolean(STORED_TIME_FLAG, false)) {
        	int hr = prefs.getInt("messageTimeHour", -1);
        	int min = prefs.getInt("messageTimeMinute", -1);
        	if (hr != -1 && min != -1) {
	        	calendar.set(Calendar.HOUR_OF_DAY, hr);
	        	calendar.set(Calendar.MINUTE, min);
	        	updateTime();
        	}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onClick(View view) {
		int viewId = view.getId();
		
		if (viewId == chooseRecepient.getId()) {
			Intent friendListActivity = new Intent(this, FriendListActivity.class);
			startActivity(friendListActivity);
		}
		else if (viewId == chooseLocation.getId()) {
			Intent mapActivity = new Intent(this, MapLocateActivity.class);
			startActivity(mapActivity);
		}
		else if (viewId == chooseTime.getId()) {
			timePicker.show();
		}
		else if (viewId == submitButton.getId()) {
			msgSubject = messageSubject.getText().toString();
			msgBody = messageBody.getText().toString();
			
			//This will call sendMessage
			getUserId(getApplicationContext());
		}
	}

	public void onTimeSet(TimePicker view, int hour, int minute) {
		calendar.set(Calendar.HOUR_OF_DAY, hour);
    	calendar.set(Calendar.MINUTE, minute);
    	
    	//Flag for checking if time was selected
        editor.putBoolean(STORED_TIME_FLAG, true);
        editor.putInt("messageTimeHour", hour);
        editor.putInt("messageTimeMinute", minute);
        editor.commit();
    	updateTime();
	}
	
	private boolean sendMessage(String userIdStr, String userName) {
		
		//Get recipient id and name
		if (prefs.getBoolean(STORED_RECIEPIENT_FLAG, false) == false) {
			AlertMessage.showToastMessage("Please select a friend");
			return false;
		}
		
		msgRecipientId = prefs.getString("userId", null);
		msgRecipientName = prefs.getString("userName", null);
		if (msgRecipientId == null) {
			AlertMessage.showToastMessage("Please select a friend");
			return false;
		}
		
		msgRecipientName = prefs.getString("userName", null);
		
		int type = 0;
		
		//Get location
		if (prefs.getBoolean(STORED_LOCATION_FLAG, false)) {
			msgLocation = prefs.getString("locName", null);
			type = 1;
		}
		
		//Get time
		Long timeStamp;
		if (prefs.getBoolean(STORED_TIME_FLAG, false)) {
			timeStamp = calendar.getTimeInMillis();
			if (type == 0)
				type = 2;
			else
				type = 3;
		}
		else
			timeStamp = null;
		
		//msgLocation = "test";
		if (timeStamp == null)
			timeStamp = Long.valueOf(-1);
		
		MessageTable msgTable = new MessageTable(0, timeStamp.longValue(), msgLocation, msgSubject, msgBody, type);
		
		String senderGcmRegId = "12345";
		UserTable senderTable = new UserTable(userIdStr, senderGcmRegId, userName);
		UserTable receiverTable = new UserTable(msgRecipientId, null, msgRecipientName);
		
		dbHelper.addMessage(msgTable, senderTable, receiverTable);
		return true;
	}
	
	private void updateTime() {
		chooseTime.setText("Time: ("+getTime()+")");
	}
	
	private String getTime() {
		Formatter fmt = new Formatter();
        fmt.format("%tl:%tM", calendar, calendar);
        String am_pm;
        if (calendar.get(Calendar.AM_PM) == 0)
        	am_pm = "AM";
        else
        	am_pm = "PM";
    	return fmt.toString()+" "+am_pm;
	}
	
	private void clearStoreVarFlags() {
		editor.putBoolean(STORED_TIME_FLAG, false);
		editor.putBoolean(STORED_RECIEPIENT_FLAG, false);
		editor.putBoolean(STORED_LOCATION_FLAG, false);
		editor.commit();
	}
	
	private String getUserId(final Context context) {
		final TextView idView = new TextView(getApplicationContext());
		final String[] strArray = {null};
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request request = Request.newMeRequest(
				      session,
				      new Request.GraphUserCallback() {
				    	String id = null;
				        // callback after Graph API response with user object
				        public void onCompleted(GraphUser user, Response response) {
				          if (user != null) {
				        	  /*strArray[0] = user.getId();
				        	  id = user.getId();
				        	  idView.setText(id);
				        	  System.out.println(id);*/
				        	  
				        	  if (sendMessage(user.getId(), user.getName()) == true) {
				  				clearStoreVarFlags();
				  				Intent activity = new Intent(context, MainActivity.class);
				  				startActivity(activity);
				  			}
				          }
				        }
				      }
				    );
				    Request.executeBatchAsync(request); 
		}
		
		return idView.getText().toString();
	}
}
