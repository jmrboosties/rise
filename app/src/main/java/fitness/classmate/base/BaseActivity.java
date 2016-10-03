package fitness.classmate.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import fitness.classmate.dialog.ProgressDialogHelper;
import fitness.classmate.util.Print;

public abstract class BaseActivity extends AppCompatActivity {

	private AlertDialog mBaseAlertDialog;
	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prepareActivity();

		setContentView(getLayoutResId());

		initLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//avoid adding items to the menu when the activity is recreated
		menu.clear();

		if(getMenuResId() > 0) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(getMenuResId(), menu);
		}

		return super.onCreateOptionsMenu(menu);
	}

	public void showDialog(AlertDialog dialog) {
		dismissDialog();

		mBaseAlertDialog = dialog;
		mBaseAlertDialog.show();
	}

	public void dismissDialog() {
		if(mBaseAlertDialog != null)
			mBaseAlertDialog.dismiss();
	}

	public void showProgressDialog(int msgResId) {
		showProgressDialog(getString(msgResId));
	}

	public void showProgressDialog(String msg) {
		mProgressDialog = ProgressDialogHelper.buildDialog(this, msg);
		mProgressDialog.show();
	}

	public void hideProgressDialog() {
		if(mProgressDialog != null && mProgressDialog.isShowing()) {
			try {
				mProgressDialog.dismiss();
			}
			catch(IllegalArgumentException e) {
				Print.exception(e);
			}
		}
	}

	protected abstract int getMenuResId();

	protected abstract void prepareActivity();

	protected abstract void initLayout();

	protected abstract int getLayoutResId();

}
