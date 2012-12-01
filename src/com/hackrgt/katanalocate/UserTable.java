package com.hackrgt.katanalocate;

public class UserTable {
	private String UserID;
    private String GcmRegId;
    
    public UserTable(String A, String B)
    {
    	UserID = A;
    	GcmRegId = B;
    }
    
    public UserTable()
    {
    	
    }
    
    public String getUserID()
    {
    	return UserID;
    }
    
    public String getGcmRegId()
    {
    	return GcmRegId; 
    }
}
