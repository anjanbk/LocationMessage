package com.hackrgt.katanalocate.friendslist;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.android.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class FacebookFriendsTask extends AsyncTask<String, Void, Boolean>{

	private ProgressDialog dialog;
	private ArrayList<Friend> friends;
	private FriendListActivity callingActivity;
	private final String TAG = "Facebook Friend Loader";
	
	public FacebookFriendsTask(Context context, FriendListActivity callingActivity) {
		this.callingActivity = callingActivity;
		dialog = new ProgressDialog(context);
		friends = new ArrayList<Friend>();
	}
	
	@Override
	public void onPreExecute() {
		dialog.setMessage("Fetching friends...");
	    dialog.show();
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		Bundle parameters = new Bundle();
		
	    try {
	    	if (params.length < 2)
	    		return false;
	    	
	    	String token = params[0];
	    	String url = params[1];
	        parameters.putString("format", "json");
	        parameters.putString("access_token", token);
	        
	        String response = Util.openUrl(url, "GET", parameters);
	        JSONObject obj = Util.parseJson(response);
	        JSONArray array = obj.optJSONArray("data");

	        if (array != null) {
	        	friends = new ArrayList<Friend>();
	        	
	        	//Put name and id into sorted TreeMap
	        	TreeMap<String, String> friendsMap = new TreeMap<String, String>();
	            for (int i = 0; i < array.length(); i++) {
	                String name = array.getJSONObject(i).getString("name");
	                String id = array.getJSONObject(i).getString("id");
	                friendsMap.put(name, id);
	            }
	            
	            //Read name of friends in alphabetical order
	            for(Entry<String, String> entry : friendsMap.entrySet()) {
	            	  String name = entry.getKey();
	            	  String id = entry.getValue();

	                Friend friend = new Friend(id, name);
					
					//Bitmap imgBitmap = getUserPic(friend.getId());
					//friend.setImgBitmap(imgBitmap);
	                
	                friends.add(friend);
	            }
	        } 
	        
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
		return true;
	}
	
	public Bitmap getUserPic(String userID) {
	    String imageURL;
	    Bitmap bitmap = null;
	    Log.d(TAG, "Loading Picture");
	    imageURL = "http://graph.facebook.com/"+userID+"/picture?type=small";
	    try {
	        bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
	    } catch (Exception e) {
	        Log.d("TAG", "Loading Picture FAILED");
	        e.printStackTrace();
	    }
	    return bitmap;
	}
	
	protected void onPostExecute(Boolean success) {
		dialog.hide();
        if (!success)
        	AlertMessage.showToastMessage("Failed to fetch user's freinds!");
        else {
        	callingActivity.getFacebookFriendsResult(friends);
        }
    }
}