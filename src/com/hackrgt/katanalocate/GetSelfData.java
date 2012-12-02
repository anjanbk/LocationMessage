package com.hackrgt.katanalocate;

import com.facebook.Request.GraphUserCallback;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class GetSelfData implements GraphUserCallback {
	private String ID;
	private String name;
	
	public String getID()
	{
		return ID;
	}
	
	public String getname()
	{
		return name;
	}
	
	public void onCompleted(GraphUser user, Response response) {
		// TODO Auto-generated method stub
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request request = Request.newMeRequest(
				      session,
				      new Request.GraphUserCallback() {
				        // callback after Graph API response with user object
				        public void onCompleted(GraphUser user, Response response) {
				          if (user != null) {
				        	  ID = user.getId();
				        	  name = user.getName();
				          }
				        }
				      }
				    );
				    Request.executeBatchAsync(request); 
		}
	}

}
