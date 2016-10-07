package fitness.classmate.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import fitness.classmate.model.ClassmateClass;

import java.util.ArrayList;

public class DatabaseHelper {

	private static DatabaseHelper helper;
	private ContentResolver mContentResolver;
	private Context mContext;

	private DatabaseHelper(Context context) {
		//Use application context to avoid any leaks
		mContext = context.getApplicationContext();
		mContentResolver = mContext.getContentResolver();
	}

	public static DatabaseHelper getInstance(Context context) {
		if(helper == null)
			helper = new DatabaseHelper(context);

		return helper;
	}

	public synchronized boolean insertValues(Uri uri, ContentValues values) {
		return mContentResolver.insert(uri, values) != null;
	}

	public synchronized int bulkInsertValues(Uri uri, ArrayList<ContentValues> allValues) {
		return mContentResolver.bulkInsert(uri, allValues.toArray(new ContentValues[allValues.size()]));
	}

	public ArrayList<ClassmateClass> getClasses() {
		Cursor cursor = mContentResolver.query(Tables.ClassmateClasses.CONTENT_URI, null, null, null, null);
		try {
			ArrayList<ClassmateClass> classes = new ArrayList<>();
			while(cursor != null && cursor.moveToNext()) {

			}

			return classes;
		} finally {
			cursor.close();
		}
	}

}
