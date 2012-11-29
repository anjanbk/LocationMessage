package com.hackrgt.katanalocate.friendslist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
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

public class FacebookFriendsTask extends AsyncTask<String, Void, Boolean>{

	private ProgressDialog dialog;
	private ArrayList<Friend> friends;
	private FriendListActivity callingActivity;
	
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

	        System.out.println("json Response: "+ obj.toString());
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
	                
	                //Get image and set bitmap
	                /*URL imgUrl = new URL("https://graph.facebook.com/"+id+"/picture");
	                URI imgUri = imgUrl.toURI();
					File file = new File(imgUri.getPath());
					Bitmap imgBitmap = decodeFile(file);
					friend.setImgBitmap(imgBitmap);*/
	                
	                friends.add(friend);
	                System.out.println(name+" "+id);
	            }
	        } 
	        
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
		return true;
	}
	
	//decodes image and scales it to reduce memory consumption
	//http://stackoverflow.com/questions/3956702/java-lang-outofmemoryerror-bitmap-size-exceeds-vm-budget
    private Bitmap decodeFile(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
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