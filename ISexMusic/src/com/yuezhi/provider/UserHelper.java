package com.yuezhi.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserHelper extends SQLiteOpenHelper{
          
	private static final String DB_NAME = "user.db";
	private static final int DB_VERSION = 2;
	public static final String TABLE_NAME = "shop";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_PASSWORD = "password";

	public static final String[] COLUMNS = { COLUMN_ID,COLUMN_USERNAME,COLUMN_PASSWORD };
	
	public UserHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
				+ " INTEGER PRIMARY KEY," + COLUMN_USERNAME + " TEXT," + COLUMN_PASSWORD+
				" TEXT);");
		init(db);
	}

	private void init(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "lixue");
        values.put(COLUMN_PASSWORD, "lixue");
        db.insert(TABLE_NAME, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, "haha");
		values.put(COLUMN_PASSWORD, 25);
		db.insert(TABLE_NAME, null, values);
	}

}


