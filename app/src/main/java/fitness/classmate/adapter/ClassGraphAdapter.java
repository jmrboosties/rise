package fitness.classmate.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.item.ClassGraphItem;
import fitness.classmate.util.Print;

public class ClassGraphAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
	private int mComponentWidth;
	private int mGraphSectionHeight;
	private ArrayList<ClassGraphItem> mItems = new ArrayList<>();

	public ClassGraphAdapter(BaseActivity activity) {
		mActivity = activity;
	}

	public void setComponentWidth(int componentWidth) {
		mComponentWidth = componentWidth;
	}

	private void setGraphSectionHeight(int height) {
		mGraphSectionHeight = height;
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

	private class ComponentViewHolder extends RecyclerView.ViewHolder {

		private TextView mComponentName;
		private boolean mDragAreaVisible;

		public ComponentViewHolder(View itemView) {
			super(itemView);

			mComponentName = (TextView) itemView.findViewById(R.id.icgc_component_name);

			final FrameLayout dragArea = (FrameLayout) itemView.findViewById(R.id.icgc_adjust_area);
			dragArea.setVisibility(View.INVISIBLE);

			//Get background
			Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.class_component_background);

			itemView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					mDragAreaVisible = true;
					dragArea.setVisibility(View.VISIBLE);
					return false;
				}

			});

			itemView.setOnTouchListener(new View.OnTouchListener() {

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

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Print.log("drag area touched!");
					return false;
				}

			});

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mComponentWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
			itemView.setLayoutParams(params);
		}

		public void buildItem() {
			mComponentName.setText(mItems.get(getAdapterPosition()).getTitle());
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


}
