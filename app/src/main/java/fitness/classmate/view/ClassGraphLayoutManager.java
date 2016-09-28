package fitness.classmate.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class ClassGraphLayoutManager extends LinearLayoutManager {

	private boolean mScrollingDisabled;

	public ClassGraphLayoutManager(Context context) {
		super(context);
	}

	public ClassGraphLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
	}

	public ClassGraphLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public boolean canScrollHorizontally() {
		return super.canScrollHorizontally() && mScrollingDisabled;
	}

	public void setScrollingDisabled(boolean scrollingDisabled) {
		mScrollingDisabled = scrollingDisabled;
	}
}
