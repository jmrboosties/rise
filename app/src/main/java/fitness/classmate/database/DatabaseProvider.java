package fitness.classmate.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import fitness.classmate.util.Print;

public class DatabaseProvider extends ContentProvider {

	public static final String AUTHORITY = "fitness.classmate.provider.internalDB";
	public static final String DATABASE_NAME = "Clsmte.db";
	public static final int DATABASE_VERSION = 1; //Ver 1 for pre-release

	private ClassmateDatabaseHelper mDatabaseHelper;

	private static final int RAW = 0;
	private static final int CLASSMATE_CLASS = 1;
	private static final int CLASSMATE_CLASS_COMPONENT = 2;
	private static final int CLASSMATE_CLASS_COMPONENT_LIST = 3;

	private static final UriMatcher mUriMatcher;
	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, Tables.RAW, RAW);
		mUriMatcher.addURI(AUTHORITY, Tables.ClassmateClasses.TABLE_NAME, CLASSMATE_CLASS);
		mUriMatcher.addURI(AUTHORITY, Tables.ClassmateClassComponents.TABLE_NAME, CLASSMATE_CLASS_COMPONENT);
		mUriMatcher.addURI(AUTHORITY, Tables.ClassmateClassComponentsList.TABLE_NAME, CLASSMATE_CLASS_COMPONENT_LIST);
	}

	public static class ClassmateDatabaseHelper extends SQLiteOpenHelper {

		private Context mContext;

		public ClassmateDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createClassmateClassesTable(db);
			createClassmateClassComponentsTable(db);
			createClassmateClassComponentsListTable(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

		private void createClassmateClassesTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + Tables.ClassmateClasses.TABLE_NAME + "("
					+ Tables.ClassmateClasses.ID + " INTEGER PRIMARY KEY,"
					+ Tables.ClassmateClasses.TITLE + " TEXT,"
					+ Tables.ClassmateClasses.CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
					+ Tables.ClassmateClasses.UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
					+ ");"
			);
		}

		private void createClassmateClassComponentsTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + Tables.ClassmateClassComponents.TABLE_NAME + "("
					+ Tables.ClassmateClassComponents.ID + " INTEGER PRIMARY KEY,"
					+ Tables.ClassmateClassComponents.NAME + " TEXT,"
					+ Tables.ClassmateClassComponents.PACE + " INTEGER,"
					+ Tables.ClassmateClassComponents.CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
					+ Tables.ClassmateClassComponents.UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
					+ ");"
			);
		}

		private void createClassmateClassComponentsListTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + Tables.ClassmateClassComponentsList.TABLE_NAME + "("
					+ Tables.ClassmateClassComponentsList.CLASSMATE_CLASS_ID + " INTEGER,"
					+ Tables.ClassmateClassComponentsList.CLASSMATE_CLASS_COMPONENT_ID + " INTEGER,"
					+ "PRIMARY KEY (" + Tables.ClassmateClassComponentsList.CLASSMATE_CLASS_ID + ", " + Tables.ClassmateClassComponentsList.CLASSMATE_CLASS_COMPONENT_ID + ")"
					+ ");"
			);
		}

	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new ClassmateDatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(@NonNull Uri uri) {
		switch(mUriMatcher.match(uri)) {
			case CLASSMATE_CLASS:
				return Tables.ClassmateClasses.CONTENT_ITEM_TYPE;
			case CLASSMATE_CLASS_COMPONENT:
				return Tables.ClassmateClassComponents.CONTENT_ITEM_TYPE;
			case CLASSMATE_CLASS_COMPONENT_LIST:
				return Tables.ClassmateClassComponentsList.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("uri not matched");
		}
	}

	/**
	 * Get the table that the FILE_PATH represents
	 *
	 * @param uri content uri
	 * @return string table name
	 */
	public static String getTable(Uri uri) {
		switch(mUriMatcher.match(uri)) {
			case CLASSMATE_CLASS:
				return Tables.ClassmateClasses.TABLE_NAME;
			case CLASSMATE_CLASS_COMPONENT:
				return Tables.ClassmateClassComponents.TABLE_NAME;
			case CLASSMATE_CLASS_COMPONENT_LIST:
				return Tables.ClassmateClassComponentsList.TABLE_NAME;
			default:
				throw new IllegalArgumentException("table name not matched");
		}
	}

	//TODO
	private ArrayList<String> getOnConflictConstraints(Uri uri) {
		return new ArrayList<>();
	}

	@Override
	public synchronized Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		if(uri.equals(Tables.RAW_QUERY))
			return db.rawQuery(selection, selectionArgs);
		else
			return db.query(getTable(uri), projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public synchronized int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] initialValues) {
		String table = getTable(uri);
		int rows = 0;

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		db.beginTransaction();

		for(ContentValues values : initialValues) {
			if(handleInsert(db, table, uri, values) >= 0)
				rows++;
		}

		db.setTransactionSuccessful();
		db.endTransaction();

		return rows;
	}

	@Override
	public synchronized Uri insert(@NonNull Uri uri, ContentValues values) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		String table = getTable(uri);

		long insertedId = handleInsert(db, table, uri, values);

		if(insertedId < 0)
			Print.exception(new Exception("inserted id: " + insertedId + " and was not caught"));

		if(insertedId > 0)
			return ContentUris.withAppendedId(uri, insertedId);
		else
			return null;
	}

	private long handleInsert(SQLiteDatabase db, String table, Uri uri, ContentValues values) {
		long insertedId = -1;
		switch(mUriMatcher.match(uri)) {
			default :
				try {
					insertedId = db.insertOrThrow(table, null, values);
				} catch(SQLiteConstraintException e) {
					//Try to update
					ArrayList<String> constraints = getOnConflictConstraints(uri);
					if(constraints.size() > 0) {
						StringBuilder selectionBuilder = new StringBuilder();
						for(int i = 0; i < constraints.size(); i++) {
							selectionBuilder.append(constraints.get(i));
							selectionBuilder.append(" = ");
							selectionBuilder.append(values.get(constraints.get(i)));

							if(i < constraints.size() - 1)
								selectionBuilder.append(" AND ");
						}

						String selection = selectionBuilder.toString();
						int updatedRows = db.update(table, values, selection, null);

						//Set the inserted id to 0 if it just updated, if there was an error it will be -1
						if(updatedRows == 1)
							insertedId = 0;
						else
							Print.exception(new Exception("update on conflict yielded result != 1. result: " + updatedRows));
					}
					else
						Print.log("missing sql constraints for uri: " + uri.toString());
				}
				break;
		}

		return insertedId;
	}

	@Override
	public synchronized int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		String table = getTable(uri);

		int count = db.delete(table, selection, selectionArgs);

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public synchronized int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		String table = getTable(uri);

		int count = db.update(table, values, selection, selectionArgs);

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

//	@Nullable
//	@Override
//	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//		return null;
//	}
//
//	@Nullable
//	@Override
//	public Uri insert(@NonNull Uri uri, ContentValues values) {
//		return null;
//	}
//
//	@Override
//	public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
//		return 0;
//	}
//
//	@Override
//	public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//		return 0;
//	}



}
