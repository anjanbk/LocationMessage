package com.hackrgt.katanalocate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	private static String DB_PATH = "/data/data/com/hackrgt/katanalocate/databases/";
	 
    private static String DB_NAME = "DatabaseLocation"; // DataBase Name
    
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
                + USER_USERID + " TEXT," + USER_GCMREGID + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
        String CREATE_SENDRECEIVE_TABLE = "CREATE TABLE " + TABLE_SENDRECEIVE + "("
                + SENDRECEIVE_SENDERID + " TEXT," + SENDRECEIVE_RECEIVERID + " TEXT," + 
                SENDRECEIVE_MESSAGEID + " NUMERIC" + ")";
        db.execSQL(CREATE_SENDRECEIVE_TABLE);
        String CREATE_READUNREAD_TABLE = "CREATE TABLE " + TABLE_READUNREAD + "("
                + READUNREAD_MESSAGEID + " INTEGER PRIMARY KEY," + READUNREAD_ISREAD + " NUMERIC" + ")";
        db.execSQL(CREATE_READUNREAD_TABLE);
	}
    
    
    void addUser(String UserID, String GCMRegID) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(USER_USERID, UserID); // Contact Name
        values.put(USER_GCMREGID, GCMRegID); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }
    
    String fetchUser(String UserID)
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
    
    /*
    a
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * 
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * 
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
	
    public void openDataBase() throws SQLException{
    	 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
    	    super.close();
 
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
}