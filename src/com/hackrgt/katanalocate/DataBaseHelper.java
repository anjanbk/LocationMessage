package com.hackrgt.katanalocate;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    public static String DB_NAME = "DatabaseLocation"; // DataBase Name
    
    //Table names
    private static final String TABLE_MESSAGE = "Message";
    private static final String TABLE_USER = "User";
    private static final String TABLE_SENDRECEIVE = "SendReceive";
    private static final String TABLE_READUNREAD = "ReadUnread";
    
    //Table_Message Column Names
    private static final String MESSAGE_ID = "ID";
    private static final String MESSAGE_TIMESTAMP = "TimeStamp";
    private static final String MESSAGE_LOCATION = "Location";
    private static final String MESSAGE_SUBJECT = "Subject";
    private static final String MESSAGE_TEXT = "Text";
    private static final String MESSAGE_TYPEID = "TypeID";
    
    //Table User Column Names
    private static final String USER_USERID = "UserID";
    private static final String USER_GCMREGID = "GcmRegId";
    private static final String USER_NAME = "Name";
    
    //Table SendReceive Names
    private static final String SENDRECEIVE_SENDERID = "SenderID";
    private static final String SENDRECEIVE_RECEIVERID = "ReceiverID";
    private static final String SENDRECEIVE_MESSAGEID = "MessageID";
    
    //Table ReadUnread Names
    private static final String READUNREAD_MESSAGEID = "MessageID";
    private static final String READUNREAD_ISREAD = "IsRead";
    
    
    @Override
	public void onCreate(SQLiteDatabase db) {
    	Log.d("Reached Create Database: ", "Reached Create Database ..");
    	String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
                + MESSAGE_ID + " INTEGER PRIMARY KEY," + MESSAGE_TIMESTAMP + " NUMERIC,"
                + MESSAGE_LOCATION + " TEXT," + MESSAGE_SUBJECT + "TEXT," +  MESSAGE_TEXT + "TEXT," 
                + MESSAGE_TYPEID + "NUMERIC" + ")";
        db.execSQL(CREATE_MESSAGE_TABLE);
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + USER_USERID + " TEXT," + USER_GCMREGID + " TEXT," + USER_NAME + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
        String CREATE_SENDRECEIVE_TABLE = "CREATE TABLE " + TABLE_SENDRECEIVE + "("
                + SENDRECEIVE_SENDERID + " TEXT," + SENDRECEIVE_RECEIVERID + " TEXT," + 
                SENDRECEIVE_MESSAGEID + " NUMERIC" + ")";
        db.execSQL(CREATE_SENDRECEIVE_TABLE);
        String CREATE_READUNREAD_TABLE = "CREATE TABLE " + TABLE_READUNREAD + "("
                + READUNREAD_MESSAGEID + " INTEGER PRIMARY KEY," + READUNREAD_ISREAD + " NUMERIC" + ")";
        db.execSQL(CREATE_READUNREAD_TABLE);
	}
    
    
    void addUser(String UserID, String GCMRegID, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(USER_USERID, UserID); // Contact Name
        values.put(USER_GCMREGID, GCMRegID); // Contact Phone
        values.put(USER_NAME, name);
 
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }
    
    String fetchUserGCMID(String UserID)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
        Cursor cursor = db.query(TABLE_USER, new String[] { USER_USERID,
        		USER_GCMREGID}, USER_USERID + "=?",
                new String[] { UserID }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // return contact
        return cursor.getString(1);
    }
    
    List<String> fetchallUsers()
    {
    	List<String> allUsers = new ArrayList<String>();
    	String selectQuery = "SELECT " + USER_USERID + " FROM " + TABLE_USER;
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
        	do
        	{
        		allUsers.add(cursor.getString(0));
        	} while (cursor.moveToNext());
        }
    	
    	return allUsers;
    }
    
	/**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    
    
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        Log.d("Enters Constructor: ", "Enters Constructor ..");
    }
    
    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
    
    public MessageTable getMessage(String id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
        Cursor cursor = db.rawQuery("SELECT M.TimeStamp, M.Location, M.Subject, M.Text, U.Name FROM Message M, SendReceive S, User U WHERE M.ID = ? " +
        		"AND M.ID = S.MessageID AND S.SenderId = U.UserID", new String[]{id});
        if (cursor != null)
            cursor.moveToFirst();
     
        MessageTable message = new MessageTable(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return message
        return message;
    
    }
}
