package fitness.classmate.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

public abstract class BaseActivity extends AppCompatActivity {

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

	protected abstract int getMenuResId();

	protected abstract void prepareActivity();

	protected abstract void initLayout();

	protected abstract int getLayoutResId();

}
