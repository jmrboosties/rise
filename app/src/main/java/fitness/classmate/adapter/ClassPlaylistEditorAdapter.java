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

import java.util.ArrayList;

public class ClassPlaylistEditorAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
//	private int mGraphSectionHeight;
//	private int mSpacing;
	private int mBarWidth;

	private ArrayList<String> mComponents = new ArrayList<>();

	public ClassPlaylistEditorAdapter(BaseActivity activity,int barWidth) {
		mActivity = activity;
		mBarWidth = barWidth;
	}

	public void setComponents(@NonNull ArrayList<String> components) {
		mComponents = components;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ClassPlaylistEditorAdapter.BarViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_class_graph_component, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof ClassPlaylistEditorAdapter.BarViewHolder)
			((ClassPlaylistEditorAdapter.BarViewHolder) holder).buildItem();
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
			String component = mComponents.get(getAdapterPosition());

			mComponentName.setText(component);
//			mBar.getLayoutParams().height = mHeights[component.getIntensity()];
//			mBar.invalidate();
		}

	}

}
