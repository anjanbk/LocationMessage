package com.hackrgt.katanalocate;

import java.util.Calendar;
import java.util.Formatter;

import com.hackrgt.katanalocate.friendslist.FriendListActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class SendMessageActivity extends Activity implements OnClickListener, OnTimeSetListener {

	private EditText messageSubject, messageBody;
	private Button chooseRecepient, chooseLocation, chooseTime, submitButton;
	private TimePickerDialog timePicker;
	private Calendar calendar;
	
	private String msgRecipientName, msgTime, msgLocation, msgSubject, msgBody, msgRecipientId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_view);
        
        //messageTo = (EditText) findViewById(R.id.messageTo);
        messageSubject = (EditText) findViewById(R.id.messageSubject);
        //locationLabel = (TextView) findViewById(R.id.locationLabel);
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
        	if (msgLocation != null)
        		chooseLocation.setText("Choose Location: ("+msgLocation+")");
        	
        	msgRecipientId = extras.getString("user_id");
        	msgRecipientName = extras.getString("user_name");
        	if (msgRecipientName != null)
        		chooseRecepient.setText("To: ("+msgRecipientName+")");
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
			String subject = messageSubject.getText().toString();
			String body = messageBody.getText().toString();
			
			Intent activity = new Intent(this, MainActivity.class);
			startActivity(activity);
		}
	}

	public void onTimeSet(TimePicker view, int hour, int minute) {
		calendar.set(Calendar.HOUR_OF_DAY, hour);
    	calendar.set(Calendar.MINUTE, minute);
    	updateTime();
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
}
