package com.yuezhi.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class UserProvider extends ContentProvider {
	private static final String authority = "com.lixue.user";

	private static final int QUERY_ALL_CODE = 1;
	private static final int QUERY_BY_ID_CODE = 5;
	private static final int ADD_CODE = 2;
	private static final int DELETE_CODE = 3;
	private static final int MODIFY_CODE = 4;
	
	private static final UriMatcher URI_MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	
	static {
		// 如果一个URI是：content://authority/query,匹配时返回QUERY_ALL_CODE
		URI_MATCHER.addURI(authority, "query", QUERY_ALL_CODE);
		// 如果一个URI是：content://authority/query/5（任意整数）,匹配时返回QUERY_BY_ID_CODE
		URI_MATCHER.addURI(authority, "query/#", QUERY_BY_ID_CODE);
		//URI_MATCHER.addURI(authority, "query2/#",QUERY_BY_NAME_CODE);
		//content://com.niit.person/add
		URI_MATCHER.addURI(authority, "add", ADD_CODE);
		URI_MATCHER.addURI(authority, "delete", DELETE_CODE);
		URI_MATCHER.addURI(authority, "modify", MODIFY_CODE);
	}

	private UserHelper helper;

	// 资源定位符 URI：content://authority/path
	// 实际例子： content://contacts/phone/5
	@Override
	public boolean onCreate() {
		Log.d("yuanbin", "创建ShopProvider");
		helper = new UserHelper(getContext());
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		int code = URI_MATCHER.match(uri);
		SQLiteDatabase database = helper.getReadableDatabase();
		switch (code) {
		case QUERY_ALL_CODE:
			result = database.query(UserHelper.TABLE_NAME, null, null, null,
					null, null, sortOrder);
			break;
		case QUERY_BY_ID_CODE:
			result = database.query(UserHelper.TABLE_NAME, null,
					UserHelper.COLUMN_USERNAME + "=?", new String[] { uri
							.getPathSegments().get(1) }, null, null, sortOrder);
			break;
		
		}
		return result;
	}
	
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri result = null;
		int code = URI_MATCHER.match(uri);
		SQLiteDatabase database = helper.getWritableDatabase();
		switch (code) {
		case ADD_CODE:
			long id = database.insert(UserHelper.TABLE_NAME, null, values);
			result = Uri.parse("content://com.lixue.user/query/"+id);
			break;
		}
		return result;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = 0;
		int code = URI_MATCHER.match(uri);
		SQLiteDatabase database = helper.getWritableDatabase();
		switch (code) {
		case DELETE_CODE:
			result = database.delete(UserHelper.TABLE_NAME, selection, selectionArgs);
			break;
		}
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int result = 0;
		int code = URI_MATCHER.match(uri);
		SQLiteDatabase database = helper.getWritableDatabase();
		switch (code) {
		case MODIFY_CODE:
			result = database.update(UserHelper.TABLE_NAME, values, selection, selectionArgs);
			break;
		}
		return result;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}		

}
