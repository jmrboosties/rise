package fitness.classmate.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbsPreferences {

	protected Context mContext;

	public static final String PREFS_NAME = "ClassmatePrefs";

	protected AbsPreferences(Context context) {
		mContext = context;
	}

	protected void setPref(String prefName, Set<String> value) {
		SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putStringSet(prefName, value).apply();
	}

	protected void setPref(String prefName, String value)	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit(); //TODO get 1 instance of the prefs and editor objects
		editor.putString(prefName, value).apply();
	}

	protected void setPref(String prefName, int value)	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(prefName, value).apply();
	}

	protected void setPref(String prefName, boolean value)	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(prefName, value).apply();
	}

	protected void setPref(String prefName, long value)	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putLong(prefName, value).apply();
	}

	protected void setPref(String prefName, float value)	{
		SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putFloat(prefName, value).apply();
	}

	protected String getStringPref(String key) {
		return getStringPref(key, null);
	}

	protected String getStringPref(String key, String defaultValue) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getString(key, defaultValue);
	}

	protected int getIntPref(String key) {
		return getIntPref(key, 0);
	}

	protected int getIntPref(String key, int defValue) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(key, defValue);
	}

	protected boolean getBooleanPref(String key) {
		return getBooleanPref(key, false);
	}

	protected boolean getBooleanPref(String key, boolean defValue) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getBoolean(key, defValue);
	}

	protected long getLongPref(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getLong(key, 0);
	}

	protected float getFloatPref(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getFloat(key, 0);
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	protected Set<String> getStringSetPref(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Set<String> s = new LinkedHashSet<>(preferences.getStringSet(key, new HashSet<String>()));

		// according to http://stackoverflow.com/questions/14034803/misbehavior-when-trying-to-store-a-string-set-using-sharedpreferences
		// you have to create a new object or changes wont be updated.

		return s;
	}

	protected void clearPref(String pref) {
		SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.remove(pref).apply();
	}


}
