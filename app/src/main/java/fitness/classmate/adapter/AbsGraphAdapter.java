package fitness.classmate.adapter;


import android.support.v7.widget.RecyclerView;
import fitness.classmate.base.BaseActivity;

public abstract class AbsGraphAdapter extends RecyclerView.Adapter {

	protected BaseActivity mActivity;
	protected int mComponentWidth;
	protected int mGraphSectionHeight;
	protected int mSpacing;

	protected int[] mHeights;

	public AbsGraphAdapter(BaseActivity activity, int spacing, int childWidth, int maxHeight) {
		mActivity = activity;
		mSpacing = spacing;
		mComponentWidth = childWidth;
		mGraphSectionHeight = maxHeight;

		crunchSnapHeights();
	}

	private void crunchSnapHeights() {
		mHeights = new int[5];

		//Max is normal height minus 1/8
		int maxHeight = getMaxHeight();

		//Smallest is just enough to show the text
		mHeights[0] = maxHeight / 10;

		//Next 1/4
		mHeights[1] = (int) (maxHeight * .25f);

		//Half
		mHeights[2] = maxHeight / 2;

		//3/4
		mHeights[3] = (int) (maxHeight * .75f);

		//Full
		mHeights[4] = maxHeight;
	}

	protected int getMaxHeight() {
		return mGraphSectionHeight - (mSpacing * 2) - (mGraphSectionHeight / 16);
	}

	/**
	 * Counts all the items, multiplies them by their width, and adds the spacing from the decorator
	 *
	 * @return the full pixel width of the recycler view (will extend off screen in most cases)
	 */
	public int getEstimatedFullWidth() {
		return getItemCount() * (mComponentWidth + (mSpacing * 2));
	}

	public int getGraphSectionHeight() {
		return mGraphSectionHeight;
	}

}
