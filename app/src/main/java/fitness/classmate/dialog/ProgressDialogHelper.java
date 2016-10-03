package fitness.classmate.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogHelper {

	/**
	 * Creates a generic progress dialog with the specified message
	 *
	 * @param activity the activity which hosts the dialog. This must be an activity, not a context.
	 * @param msgResId the resId for the message to display
	 * @return a progress dialog
	 */
	public static ProgressDialog buildDialog(Activity activity, int msgResId) {
		return buildDialog(activity, activity.getApplicationContext().getString(msgResId));
	}

	/**
	 * Creates a generic progress dialog with the specified message
	 *
	 * @param activity the activity which hosts the dialog. This must be an activity, not a context.
	 * @param msg the message to display
	 * @return a progress dialog
	 */
	public static ProgressDialog buildDialog(Activity activity, String msg) {
		ProgressDialog dialog = new ProgressDialog(activity);

		dialog.setMessage(msg);
		dialog.setCancelable(false);

		return dialog;
	}

}
