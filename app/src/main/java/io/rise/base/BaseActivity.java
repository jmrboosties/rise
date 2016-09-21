package io.rise.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prepareActivity();

		setContentView(getLayoutResId());

		initLayout();
	}


	protected abstract void prepareActivity();

	protected abstract void initLayout();

	protected abstract int getLayoutResId();

}
