package io.rise.util;

import android.util.Log;

import io.rise.constant.Constants;

public class Print {

	private static final String DEBUG_TAG = "*** RISE APP ***";
	private static final String SHORT_TAG = "(RISE)";

	/**
	 * Used for logging whatever in log cat.
	 *
	 * @param message - log message
	 */
	public static void log(Object message) {
		if(Constants.IS_DEBUG_MODE) {
			if(message == null || message.toString() == null)
				Log.d(DEBUG_TAG, "The Message is null");
			else
				Log.d(DEBUG_TAG, message.toString());
		}
	}

	/**
	 * Used for logging whatever in log cat.
	 *
	 * @param tag - log tag
	 * @param messages - log messages, separated by commas
	 */
	public static void log(String tag, Object... messages) {
		if(Constants.IS_DEBUG_MODE) {
			String message;
			if(messages == null || messages.length == 0)
				message = "The Message is null";
			else {
				message = "";
				for(int i = 0; i < messages.length; i++) {
					if(messages[i] != null)
						message += messages[i].toString();
					else
						message += null;

					if(i != messages.length - 1)
						message += ", ";
				}
			}

			//this is so log cat can be filtered by bit tag
			try {
				Log.d(SHORT_TAG + " " + tag, message);
			}
			catch(Exception e) {
				//you can't log in unit tests so this stops it from throwing an exception
			}
		}
	}

	public static void exception(Exception e) {
		exception(e, true);
	}

	public static void exception(Exception e, boolean report) {
		if(Constants.IS_DEBUG_MODE)
			e.printStackTrace();
	}

}
