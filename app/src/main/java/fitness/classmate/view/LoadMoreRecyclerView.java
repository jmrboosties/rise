package fitness.classmate.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import fitness.classmate.util.Print;

public class LoadMoreRecyclerView extends RecyclerView {

	private OnLoadMoreTriggeredListener mOnLoadMoreTriggeredListener;
	private int mBeginRefreshAt;

	public LoadMoreRecyclerView(Context context) {
		this(context, null);
	}

	public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public void initialize() {
		//Default
		mBeginRefreshAt = 5;

		addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				try {
					LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();

					int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
					if(getAdapter().getItemCount() > 0 && lastVisiblePosition >= getAdapter().getItemCount() - mBeginRefreshAt) {
						if(mOnLoadMoreTriggeredListener != null) {
							mOnLoadMoreTriggeredListener.onLoadMoreTriggered();
							mOnLoadMoreTriggeredListener = null;
						}
					}
				} catch(ClassCastException e) {
					Print.exception(e);
				}
			}

		});
	}

	public void setBeginRefreshAt(int refreshAt) {
		mBeginRefreshAt = refreshAt;
	}

	public void setOnLoadMoreTriggeredListener(OnLoadMoreTriggeredListener onLoadMoreTriggeredListener) {
		mOnLoadMoreTriggeredListener = onLoadMoreTriggeredListener;
	}

	public interface OnLoadMoreTriggeredListener {

		void onLoadMoreTriggered();

	}

}