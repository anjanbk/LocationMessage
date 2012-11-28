package com.hackrgt.katanalocate;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

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
	private static final String FAKE_SUBJECT = "This is a fake message subject";
	private static final String FAKE_TIME = "10:30";
	private static final String FAKE_LOCATION = "Georgia Tech TSRB";
	private static final String FAKE_SENDER = "Not a real sender";
	private static final String FAKE_MESSAGE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus mi lectus, vehicula et commodo eu, sagittis ut risus. Nullam erat urna, elementum eget lobortis at, rutrum eget est. Donec interdum, erat eu tristique porta, felis neque euismod libero, non bibendum sapien mi quis leo.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_view);
		final TextView subject = (TextView) findViewById(R.id.subject);
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request request = Request.newMeRequest(
				      session,
				      new Request.GraphUserCallback() {
				        // callback after Graph API response with user object
				        public void onCompleted(GraphUser user, Response response) {
				          if (user != null) {
				        	  subject.setText(user.getName());
				          }
				        }
				      }
				    );
				    Request.executeBatchAsync(request); 
		}
		//final TextView subject = (TextView) findViewById(R.id.subject);
		//subject.setText(FAKE_SUBJECT);
		final TextView time = (TextView) findViewById(R.id.time);
		time.setText(FAKE_TIME);
		final TextView location = (TextView) findViewById(R.id.location);
		location.setText(FAKE_LOCATION);
		final TextView from = (TextView) findViewById(R.id.from);
		from.setText(FAKE_SENDER);
		final TextView message = (TextView) findViewById(R.id.message);
		message.setText("\n" + FAKE_MESSAGE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.message_view, menu);
		return true;
	}

}
