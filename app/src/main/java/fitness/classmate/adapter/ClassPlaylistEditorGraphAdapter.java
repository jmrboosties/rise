package fitness.classmate.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.model.ClassComponent;
import fitness.classmate.util.Print;

import java.util.ArrayList;

public class ClassPlaylistEditorGraphAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
	private int mGraphSectionHeight;
	private int mSpacing;
	private int mBarWidth;

	private int[] mHeights;

	private ArrayList<ClassComponent> mComponents = new ArrayList<>();

	public ClassPlaylistEditorGraphAdapter(BaseActivity activity, int graphSectionHeight, int barWidth, int spacing) {
		mActivity = activity;
		mGraphSectionHeight = graphSectionHeight;
		mSpacing = spacing;
		mBarWidth = barWidth;

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

	public void setComponents(@NonNull ArrayList<ClassComponent> components) {
		mComponents = components;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new BarViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_class_graph_component, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof BarViewHolder)
			((BarViewHolder) holder).buildItem();
	}

	@Override
	public int getItemCount() {
		return mComponents.size();
	}

	private class BarViewHolder extends RecyclerView.ViewHolder {

		private View mBar;
		private TextView mComponentName;

		public BarViewHolder(View itemView) {
			super(itemView);

			mBar = itemView.findViewById(R.id.icgc_bar);

			Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.class_component_background);
			drawable.setColorFilter(ContextCompat.getColor(mActivity, android.R.color.white), PorterDuff.Mode.MULTIPLY);

			mBar.setBackground(drawable);

			mComponentName = (TextView) itemView.findViewById(R.id.icgc_component_name);

			final FrameLayout dragArea = (FrameLayout) itemView.findViewById(R.id.icgc_adjust_area);
			dragArea.setVisibility(View.GONE);

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mBarWidth, ViewGroup.LayoutParams.MATCH_PARENT);
			itemView.setLayoutParams(params);
		}

		public void buildItem() {
			ClassComponent component = mComponents.get(getAdapterPosition());

			mComponentName.setText(component.getName());
			mBar.getLayoutParams().height = mHeights[component.getIntensity()];
			mBar.invalidate();
		}

	}

}
