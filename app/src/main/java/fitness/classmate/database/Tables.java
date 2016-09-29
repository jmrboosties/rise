package fitness.classmate.database;

import android.net.Uri;

@SuppressWarnings("WeakerAccess")
public class Tables {

	public static final String RAW = "raw";
	public static final Uri RAW_QUERY = Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/" + RAW);
	public static final String ORDER_KEY = "order_key";

	public static final class ClassmateClasses {

		public static final String TABLE_NAME = "classmate_classes";

		//uri and MIME type
		public static final Uri CONTENT_URI = Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/" + TABLE_NAME);
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.BIT." + TABLE_NAME;

		public static final String ID = "id";
		public static final String TITLE = "title";
		public static final String CREATED_AT = "created_at";
		public static final String UPDATED_AT = "updated_at";

	}

	public static final class ClassmateClassComponents {

		public static final String TABLE_NAME = "classmate_class_components";

		//uri and MIME type
		public static final Uri CONTENT_URI = Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/" + TABLE_NAME);
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.BIT." + TABLE_NAME;

		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String PACE = "pace";
		public static final String CREATED_AT = "created_at";
		public static final String UPDATED_AT = "updated_at";

	}

	public static final class ClassmateClassComponentsList {

		public static final String TABLE_NAME = "classmate_class_components_list";

		//uri and MIME type
		public static final Uri CONTENT_URI = Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/" + TABLE_NAME);
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.BIT." + TABLE_NAME;

		public static final String CLASSMATE_CLASS_ID = "classmate_class_id";
		public static final String CLASSMATE_CLASS_COMPONENT_ID = "classmate_class_component_id";

	}

}
