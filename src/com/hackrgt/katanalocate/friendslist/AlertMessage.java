package com.hackrgt.katanalocate.friendslist;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

public class AlertMessage {
	private static Activity activity;
	
	public static void setActivity(Activity activity) {
		AlertMessage.activity = activity;
	}
	
	//Show toast message that disappear after 3 seconds
	public static void showToastMessage(String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
	
	//Create Alert Dialog to be used for confirmation
	public static AlertDialog.Builder getConfirmDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    builder.setMessage(message);
	    return builder;
	}
}
