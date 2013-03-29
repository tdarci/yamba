package com.frenchfry.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class TweetRepository {

	public static final String TAG = TweetRepository.class.getSimpleName();
	
	private static final String DB_NAME = "WOOKIe";
	private static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "timeline";
	public static final String COL_ID = BaseColumns._ID;
	public static final String COL_CREATED_AT = "created_at";
	public static final String COL_SOURCE = "source";
	public static final String COL_TEXT = "txt";
	public static final String COL_USER = "user";
	
	private static final String GET_ALL_ORDER_BY = COL_CREATED_AT + " desc";
	private static final String[] MAX_CREATED_AT_COLUMNS = { "max(" + COL_CREATED_AT + ")" };
	private static final String[] DB_TEXT_COLUMNS = {COL_TEXT};
	
	private final DbHelper dbHelper;
	
	public TweetRepository(Context context) {
		this.dbHelper = new DbHelper(context);
		Log.d(TAG, "db initialized");
	}
	
	public void close() {
		this.dbHelper.close();
	}
	
	public void insertOrIgnore(ContentValues vals) {
		Log.d(TAG, "insert or ignore on: " + vals);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLE_NAME, null, vals, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}
	
	public Cursor getTweetUpdates() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(TABLE_NAME, null, null, null, null, null, GET_ALL_ORDER_BY);
	}
	
	public long getLatestTweetCreatedAtTime() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query( TABLE_NAME, MAX_CREATED_AT_COLUMNS, null, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}
	
	public String getTweetTextById( long id) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query( TABLE_NAME, DB_TEXT_COLUMNS, COL_ID + " = " + id, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getString(0) : null;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}

	// ============================================================
	// nested classes
	
	class DbHelper extends SQLiteOpenHelper {
		
		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + TABLE_NAME + " " + 
					"(" + 
					COL_ID + " int primary key, " + 
					COL_CREATED_AT + " int, " + 
					COL_SOURCE + " text, " + 
					COL_USER + " text, " +
					COL_TEXT + " text)"
					;
			
			db.execSQL(sql);
			Log.d(TAG, "Created db: " + sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "onUpgrade");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			this.onCreate(db);
		}

	}
	
}
