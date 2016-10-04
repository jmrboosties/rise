package fitness.classmate.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.RxView;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.item.ClassGraphItem;
import fitness.classmate.model.ClassmateClassComponent;
import fitness.classmate.util.Print;
import rx.functions.Action1;

import java.util.ArrayList;

public class ClassGraphAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
	private int mComponentWidth;
	private int mGraphSectionHeight;
	private int mSpacing;
	private ArrayList<ClassGraphItem> mItems = new ArrayList<>();
	private OnComponentDraggingCallback mOnComponentDraggingCallback;

	private int[] mHeights;

	public ClassGraphAdapter(BaseActivity activity, int spacing, int childWidth, int maxHeight) {
		mActivity = activity;
		mSpacing = spacing;
		mComponentWidth = childWidth;
		mGraphSectionHeight = maxHeight;

		crunchSnapHeights();
	}

	private void crunchSnapHeights() {
		mHeights = new int[5];

		//Max is normal height minus 1/8
		int maxHeight = mGraphSectionHeight - (mSpacing * 2) - (mGraphSectionHeight / 16);

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

		for(int i : mHeights)
			Print.log("height", i);
	}

	public void setItems(@NonNull ArrayList<ClassmateClassComponent> items) {
		mItems.clear();

		for(ClassmateClassComponent component : items) {
			ClassGraphItem item = new ClassGraphItem();
			item.setType(ClassGraphItem.COMPONENT);
			item.setClassComponent(component);

			mItems.add(item);
		}

		notifyDataSetChanged();
	}

	public void addItem(@NonNull ClassmateClassComponent component) {
		ClassGraphItem item = new ClassGraphItem();
		item.setType(ClassGraphItem.COMPONENT);
		item.setClassComponent(component);

		//Find a placeholder if it exists
		int placeholderPosition = getIndexOfPlaceholder();
		if(placeholderPosition < 0) {
			//Will only be -1 if there is no placeholder, meaning an empty recycler view
			mItems.add(item);
			notifyItemInserted(mItems.indexOf(item));
		}
		else {
			//In the case of a placeholder existing, swap it with the new item
			mItems.remove(placeholderPosition);
			mItems.add(placeholderPosition, item);

			notifyItemChanged(placeholderPosition);
		}
	}

	/**
	 * Counts all the items, multiplies them by their width, and adds the spacing from the decorator
	 *
	 * @return the full pixel width of the recycler view (will extend off screen in most cases)
	 */
	public int getEstimatedFullWidth() {
		return mItems.size() * (mComponentWidth + (mSpacing * 2));
	}

	public void handleDragEnter(int positionInFullGraph) {
		//Don't do anything if the adapter is currently empty
		if(mItems.size() == 0)
			return;

		//Crunch a value representing the position of the dragged object relative to the indices of the items in the recycler view
		//Subtract half the width of one component to shift everything over to the left, allowing us to determine if the
		//drag occurs on the left or right half of the component, which will alter behavior
		float f = (positionInFullGraph - ((mComponentWidth + (mSpacing * 2)) / 2)) / (float) (mComponentWidth + (mSpacing * 2));

		//Round to find the index we will insert placeholder
		int insertPlaceholderAt = f > 0 ? (int) Math.ceil(f) : 0;

		boolean hasPlaceholder = getIndexOfPlaceholder() != -1;
		int maxIndex = mItems.size() - (hasPlaceholder ? 1 : 0);
		insertPlaceholderAt = Math.min(insertPlaceholderAt, maxIndex);

		insertPlaceholder(insertPlaceholderAt);
	}

	private void insertPlaceholder(int index) {
		int placeholderIndex = getIndexOfPlaceholder();

		//No need to do anything
		if(placeholderIndex == index)
			return;

		if(placeholderIndex == -1) {
			ClassGraphItem placeholder = new ClassGraphItem();
			placeholder.setType(ClassGraphItem.PLACEHOLDER);

			mItems.add(index, placeholder);
			Print.log("notify item inserted at index", index);
			notifyItemInserted(index);
		}
		else {
			ClassGraphItem placeholder = mItems.get(placeholderIndex);
			mItems.remove(placeholderIndex);
			mItems.add(index, placeholder);

			notifyItemMoved(placeholderIndex, index);
		}

		if(index == 0)
			notifyDataSetChanged();
	}

	public void onDragEnd() {
		int placeholderIndex = getIndexOfPlaceholder();
		if(placeholderIndex >= 0) {
			mItems.remove(placeholderIndex);
			notifyItemRemoved(placeholderIndex);
		}
	}

	public int getIndexOfPlaceholder() {
		for(ClassGraphItem item : mItems) {
			if(item.getType() == ClassGraphItem.PLACEHOLDER)
				return mItems.indexOf(item);
		}

		return -1;
	}

	public int getPositionOfPlaceholder(int currentDx) {
		int indexItWillBeDroppedIn = Math.max(getIndexOfPlaceholder(), 0);

		Print.log("width of a single node", (mComponentWidth + (mSpacing * 2)));
		Print.log("current dx used", currentDx);

		/*
		 * Get the x position of the placeholder relative to the screen (as opposed to the full graph view). To do this subtract current dx from
		 * calculated position in graph, accounting for horizontal spacing
		 */
		return mSpacing + (indexItWillBeDroppedIn * (mComponentWidth + (mSpacing * 2))) - currentDx;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch(viewType) {
			case ClassGraphItem.COMPONENT :
				return new ComponentViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_class_graph_component, parent, false));
			case ClassGraphItem.PLACEHOLDER :
				return new PlaceholderViewHolder(new View(mActivity));
			default :
				throw new IllegalArgumentException("no view holder for type: " + viewType);
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof ComponentViewHolder)
			((ComponentViewHolder) holder).buildItem();
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	@Override
	public int getItemViewType(int position) {
		return mItems.get(position).getType();
	}

	public int getGraphSectionHeight() {
		return mGraphSectionHeight;
	}

	public void setOnComponentDraggingCallback(OnComponentDraggingCallback onComponentDraggingCallback) {
		mOnComponentDraggingCallback = onComponentDraggingCallback;
	}

	private int getCurrentHeightIndexFromCurrentHeight(int currentHeight) {
		for(int i = 0; i < mHeights.length; i++) {
			if(currentHeight == mHeights[i])
				return i;
		}

		throw new IllegalArgumentException("fuck");
	}

	private int roundToNearestSnapHeight(int currentHeight) {
		int minIndex = 0;
		for(int i = 1; i < mHeights.length; i++) {
			if(Math.abs(currentHeight - mHeights[minIndex]) > Math.abs(currentHeight - mHeights[i]))
				minIndex = i;
		}

		return minIndex;
	}

	public ArrayList<ClassmateClassComponent> buildComponents() {
		ArrayList<ClassmateClassComponent> components = new ArrayList<>();
		for(ClassGraphItem item : mItems) {
			if(item.getType() != ClassGraphItem.COMPONENT)
				continue;

			components.add(item.getClassComponent());
		}

		return components;
	}

	private class ComponentViewHolder extends RecyclerView.ViewHolder {

		private View mBar;
		private TextView mComponentName;
		private boolean mDragAreaVisible;

		public ComponentViewHolder(View itemView) {
			super(itemView);
			mBar = itemView.findViewById(R.id.icgc_bar);

			mComponentName = (TextView) itemView.findViewById(R.id.icgc_component_name);

			final FrameLayout dragArea = (FrameLayout) itemView.findViewById(R.id.icgc_adjust_area);
			dragArea.setVisibility(View.VISIBLE);

			final int adjustAreaHeight = (int) mActivity.getResources().getDimension(R.dimen.adjust_area_height);

			//TODO set colors for each intensity level
			Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.class_component_background);
			drawable.setColorFilter(ContextCompat.getColor(mActivity, android.R.color.white), PorterDuff.Mode.MULTIPLY);

			mBar.setBackground(drawable);

			mBar.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					mDragAreaVisible = true;
					dragArea.setVisibility(View.VISIBLE);
					return false;
				}

			});

			mBar.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_UP) {
						if(mDragAreaVisible) {
							mDragAreaVisible = false;
							dragArea.setVisibility(View.INVISIBLE);
						}
					}

					return false;
				}

			});

			dragArea.setOnTouchListener(new View.OnTouchListener() {

				float initialYTouch;
				int initialHeight;
				int initialIndex;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							initialYTouch = event.getRawY();
							initialHeight = mBar.getHeight();
							initialIndex = getCurrentHeightIndexFromCurrentHeight(initialHeight);

							if(mOnComponentDraggingCallback != null)
								mOnComponentDraggingCallback.onComponentDragging(true);
							break;
						case MotionEvent.ACTION_MOVE:
							float touchDy = initialYTouch - event.getRawY();

							if(touchDy == 0)
								break;

							int minHeight = mHeights[0];
							int maxHeight = mHeights[mHeights.length - 1];

							int barHeight = (int) (initialHeight + touchDy);

							barHeight = Math.max(minHeight, barHeight);
							barHeight = Math.min(maxHeight, barHeight);

							mBar.getLayoutParams().height = barHeight;
							mBar.requestLayout();
							break;
						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
							if(mOnComponentDraggingCallback != null)
								mOnComponentDraggingCallback.onComponentDragging(false);

							int closestIndex = roundToNearestSnapHeight(mBar.getHeight());

							touchDy = initialYTouch - event.getRawY();

							//This second below might not be good, but I think it is
							if(touchDy < 0 && closestIndex > 0) {
								if(closestIndex == initialIndex)
									closestIndex--;
							}
							else if(touchDy > 0 && closestIndex < mHeights.length - 1) {
								if(closestIndex == initialIndex)
									closestIndex++;
							}

							mBar.getLayoutParams().height = mHeights[closestIndex];
							mBar.requestLayout();

							ClassmateClassComponent component = mItems.get(getAdapterPosition()).getClassComponent();
							component.setIntensity(closestIndex);
							break;
					}

					return true;
				}

			});

			RxView.layoutChanges(mBar).doOnNext(new Action1<Void>() {

				@Override
				public void call(Void aVoid) {
					int top = mBar.getTop();

					dragArea.setY(top - (adjustAreaHeight / 2));
					dragArea.invalidate();
				}

			}).subscribe();

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mComponentWidth, ViewGroup.LayoutParams.MATCH_PARENT);
			itemView.setLayoutParams(params);
		}

		public void buildItem() {
			ClassmateClassComponent component = mItems.get(getAdapterPosition()).getClassComponent();

			mComponentName.setText(component.getName());
			mBar.getLayoutParams().height = mHeights[component.getIntensity()];
			mBar.invalidate();
		}

	}

	private class PlaceholderViewHolder extends RecyclerView.ViewHolder {

		public PlaceholderViewHolder(View itemView) {
			super(itemView);

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mComponentWidth, 1);
			itemView.setLayoutParams(params);

			itemView.setVisibility(View.INVISIBLE);
		}

	}

	public interface OnComponentDraggingCallback {

		void onComponentDragging(boolean dragging);

	}

}
