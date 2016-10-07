package fitness.classmate.database;

import android.database.Cursor;
import android.net.Uri;
import fitness.classmate.annotation.ClassUri;
import fitness.classmate.annotation.ColumnName;
import fitness.classmate.util.Print;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseIO {

	public static void fromCursor(Cursor cursor, Object object) {
		fromCursor(cursor, object, null);
	}

	public static void fromCursor(Cursor cursor, Object object, String tableAlias) {
		try {
			Class objClass = object.getClass();
			Print.log("class name", objClass.getName());

			ClassUri classUriAnnotation = (ClassUri) objClass.getAnnotation(ClassUri.class);
			if(classUriAnnotation == null)
				throw new IllegalArgumentException("must have a uri either via param or object");

			Uri uri = Uri.parse(classUriAnnotation.value());

			String tableName;
			if(tableAlias != null)
				tableName = tableAlias;
			else
				tableName = DatabaseProvider.getTable(uri);

			String[] columns = cursor.getColumnNames();

			ArrayList<String> annotatedColumnNames = new ArrayList<>();
			ArrayList<String> columnNames = new ArrayList<>();
			for(String columnName : columns) {
				if(columnName.contains(Sql.TABLE_COLUMN_SEPARATOR)) {
					String[] split = columnName.split(Sql.TABLE_COLUMN_SEPARATOR);
					if(split[0].equals(tableName)) {
						annotatedColumnNames.add(split[1]);
						columnNames.add(columnName);
					}
				}
				else {
					annotatedColumnNames.add(columnName);
					columnNames.add(columnName);
				}
			}

			HashMap<String, Field> fields = new HashMap<>();
			for(Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);

				ColumnName columnAnnotation = null;

				//TODO check on this, not sure what the repercussions of getting past this will be.
				try { //crashlytics issue #6605, https://code.google.com/p/android/issues/detail?id=172339
					columnAnnotation = field.getAnnotation(ColumnName.class);
				}
				catch(IncompatibleClassChangeError error) {
					Print.exception(new Exception(error.getMessage()));
				}

				if(columnAnnotation != null)
					fields.put(columnAnnotation.value(), field);
			}

			for(int i = 0; i < columnNames.size(); i++) {
				String columnNameWithTable = columnNames.get(i);
				String annotatedColumnName = annotatedColumnNames.get(i);

				Field field = fields.get(annotatedColumnName);
				if(field == null)
					continue;

				Class<?> type = field.getType();

				//TODO testing
				if(type.equals(DatabaseObject.class)) {
					Print.log("type instance of db obj");
				}


				if(type.equals(Integer.class) || type.equals(Integer.TYPE))
					field.setInt(object, cursor.getInt(cursor.getColumnIndexOrThrow(columnNameWithTable)));
				else if(type.equals(Long.class) || type.equals(Long.TYPE))
					field.setLong(object, cursor.getLong(cursor.getColumnIndexOrThrow(columnNameWithTable)));
				else if(type.equals(Double.class) || type.equals(Double.TYPE))
					field.setDouble(object, cursor.getDouble(cursor.getColumnIndexOrThrow(columnNameWithTable)));
				else if(type.equals(Short.class) || type.equals(Short.TYPE))
					field.setShort(object, cursor.getShort(cursor.getColumnIndexOrThrow(columnNameWithTable)));
				else if(type.equals(Float.class) || type.equals(Float.TYPE))
					field.setFloat(object, cursor.getFloat(cursor.getColumnIndexOrThrow(columnNameWithTable)));
				else if(type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
					//For some reason I haven't figured out, booleans can be stored as strings or integers, here is how we control for both.
					boolean stringValue = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(columnNameWithTable)));
					boolean intValue = cursor.getInt(cursor.getColumnIndexOrThrow(columnNameWithTable)) == 1;

					field.setBoolean(object, stringValue || intValue);
				}
				else if(type.equals(String.class))
					field.set(object, cursor.getString(cursor.getColumnIndexOrThrow(columnNameWithTable)));
				else
					throw new IllegalArgumentException("field type is unsupported");
			}

		} catch(Exception e) {
			Print.exception(e);
		}
	}

}
