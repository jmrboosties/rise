package fitness.classmate.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import fitness.classmate.base.BaseActivity;

public class BasicAlertDialogHelper {

	public static AlertDialog.Builder createOneButtonBuilder(BaseActivity activity, int titleResId, String msg) {
		String title = null;
		if(titleResId > 0)
			title = activity.getString(titleResId);

		return createOneButtonBuilder(activity, title, msg);
	}

	public static AlertDialog.Builder createOneButtonBuilder(BaseActivity activity, String title, String msg) {
		return createOneButtonBuilder(activity, title, msg, null);
	}

	public static AlertDialog.Builder createOneButtonBuilder(BaseActivity activity, int titleResId, int msgResId) {
		return createOneButtonBuilder(activity, titleResId, msgResId, null);
	}

	public static AlertDialog.Builder createOneButtonBuilder(BaseActivity activity, int titleResId, int msgResId, DialogInterface.OnClickListener listener) {
		String title = null;
		if(titleResId > 0)
			title = activity.getString(titleResId);

		return createOneButtonBuilder(activity, title, activity.getString(msgResId), listener);
	}

	public static AlertDialog.Builder createOneButtonBuilder(BaseActivity activity, String title, String msg, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		if(msg != null)
			builder.setMessage(msg);

		if(title != null)
			builder.setTitle(title);

		builder.setNeutralButton(android.R.string.ok, listener != null ? listener : new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});

		//Only let it be cancelable if the listener is null, meaning the usage doesn't have anything happen when it is closed other than it just closing
		builder.setCancelable(listener == null);

		return builder;
	}

	public static AlertDialog.Builder createTwoButtonBuilder(BaseActivity activity, String title, String msg, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		if(title != null)
			builder.setTitle(title);

		if(msg != null)
			builder.setMessage(msg);

		builder.setPositiveButton(android.R.string.ok, listener);
		builder.setNegativeButton(android.R.string.cancel, listener);

		return builder;
	}

	public static void showTwoButtonBuilder(BaseActivity activity, String title, String msg, String positiveString, String negativeString, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		if(title != null)
			builder.setTitle(title);

		if(msg != null)
			builder.setMessage(msg);

		builder.setPositiveButton(positiveString, listener);
		builder.setNegativeButton(negativeString, listener);

		activity.showDialog(builder.create());
	}

	public static AlertDialog.Builder createNoButtonBuilder(BaseActivity activity, int titleResId, int msgResId) {
		return createNoButtonBuilder(activity, activity.getString(titleResId), activity.getString(msgResId));
	}

	public static AlertDialog.Builder createNoButtonBuilder(BaseActivity activity, String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		if(msg != null)
			builder.setMessage(msg);

		if(title != null)
			builder.setTitle(title);

		return builder;
	}

}
