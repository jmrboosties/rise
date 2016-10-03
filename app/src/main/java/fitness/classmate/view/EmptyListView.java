package fitness.classmate.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fitness.classmate.R;

public class EmptyListView extends RelativeLayout {

    TextView mEmptyListText;
	Button mEmptyListButton;

	public EmptyListView(Context context) {
		super(context);
		initialize();
	}

	public EmptyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public EmptyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize();
	}

	private void initialize() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_empty_list_with_button, this, true);

		mEmptyListButton = (Button) findViewById(R.id.empty_button);
		mEmptyListText = (TextView) findViewById(R.id.empty_textview);
		mEmptyListButton.setVisibility(GONE);
		mEmptyListText.setVisibility(GONE);
	}

	public void setButtonText(String text) {
		mEmptyListButton.setVisibility(VISIBLE);
		mEmptyListButton.setText(text);
	}

	public void setButtonOnClickListener(OnClickListener onClickListener) {
		mEmptyListButton.setOnClickListener(onClickListener);
	}

	public void setEmptyListText(String text) {
		mEmptyListText.setVisibility(VISIBLE);
		mEmptyListText.setText(text);
	}

	public void setEmptyListText(@StringRes int id) {
		mEmptyListText.setVisibility(VISIBLE);
		mEmptyListText.setText(getResources().getString(id));
	}

	public void setHeight(int height) {
		ViewGroup.LayoutParams layoutParams = getLayoutParams();
		layoutParams.height = height;
		setLayoutParams(layoutParams);
	}
}
