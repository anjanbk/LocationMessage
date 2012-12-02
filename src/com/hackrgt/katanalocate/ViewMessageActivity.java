package com.hackrgt.katanalocate;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

/**
 * Displays individual message for the user. Shows subject, time, location,
 * sender, and message.
 * 
 */
public class ViewMessageActivity extends Activity {

	/**
	 * Fake data for subject, time, location, sender, and message to populate
	 * fields
	 */
	private int MessageId;
	private MessageTable message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_view);
		Bundle extras = getIntent().getExtras();
		MessageId = extras.getInt("MessageID");
		int type = extras.getInt("sentreceive");
		DataBaseHelper dbhelper = new DataBaseHelper(this);
		message = dbhelper.getMessage(MessageId, type);
		final TextView subject = (TextView) findViewById(R.id.subject);
		subject.setText(message.getSubject());
		final TextView time = (TextView) findViewById(R.id.time);
		time.setText(Long.toString(message.getDateTime()));
		final TextView location = (TextView) findViewById(R.id.location);
		location.setText(message.getLatitude() + " " + message.getLongitude());
		final TextView from = (TextView) findViewById(R.id.from);
		from.setText(message.getUser());
		final TextView messageText = (TextView) findViewById(R.id.message);
		messageText.setText("\n" + message.getMessage());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.message_view, menu);
		//getMenuInflater().inflate(R.menu.activity_view_message, menu);
		return true;
	}

}
