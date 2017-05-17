package com.exercise.mycarfinder1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class GpsDbAdapter {

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGEPATH = "imagePath";
    public static final String KEY_DATE = "date";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "GpsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
        
    /**
     * Database creation sql statement
     */
    
    private static final String DATABASE_CREATE =
        "create table gpslocation (_id integer primary key autoincrement, "
        + "latitude text not null, longitude text not null, description text not null, imagePath text, date text not null, address text not null);";

    private static final String DATABASE_NAME = "Carlocation";
    private static final String DATABASE_TABLE = "gpslocation";
    private static final int DATABASE_VERSION = 4;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        
        
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS gpslocation");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */

	public GpsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the mileage database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public GpsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new record using the origin, destination and distance provided. If the record is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @return rowId or -1 if failed
     */
    
    public long createRecord(String latitude, String longitude, String description, String imagePath, String date, String address) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_IMAGEPATH, imagePath);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_ADDRESS, address);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the record with the given rowId
     * 
     * @return true if deleted, false otherwise
     */
  public boolean deleteRecord(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all records in the database
     * 
     * @return Cursor over all records
     */
    public Cursor fetchAllRecords() {
    	//adding date
//        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LATITUDE,
//                KEY_LONGITUDE, KEY_DESCRIPTION, KEY_IMAGEPATH}, null, null, null, null, null);
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LATITUDE,
                KEY_LONGITUDE, KEY_DESCRIPTION, KEY_IMAGEPATH, KEY_DATE, KEY_ADDRESS}, null, null, null, null, KEY_ROWID + " DESC");
        
        
    }

    /**
     * Return a Cursor positioned at the record that matches the given origin and destination
     * 
     * @return Cursor positioned to matching record, if found
     * @throws SQLException if note could not be found/retrieved
     */

//	public Cursor fetchRecord(String latitude, String longitude) throws SQLException {
    public Cursor fetchRecord(long rowId) throws SQLException {  	        	
		Cursor mCursor =

	            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
	            		KEY_LATITUDE, KEY_LONGITUDE, KEY_DESCRIPTION, KEY_IMAGEPATH, KEY_DATE, KEY_ADDRESS}, KEY_ROWID + "=" + rowId, null,
	                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;

    }
	
	public boolean Exists(String latitude, String longitude) {
		
		Cursor cursor=mDb.query(true, DATABASE_TABLE, null,
				KEY_LATITUDE + "= ?  and " + KEY_LONGITUDE + " = ?"
                        , new String[]{latitude, longitude}, null, null, null, null);
		   
		   boolean exists = (cursor.getCount() > 0);
		   
		   cursor.close();
		   return exists;
		}

    /**
     * Update the record using the details provided. The record to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    //adding date
    public boolean updateRecord(long rowId, String imagePath ) {
        ContentValues args = new ContentValues();
        args.put(KEY_IMAGEPATH, imagePath);
       
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
