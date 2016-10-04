package fitness.classmate.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.util.Print;

import java.util.ArrayList;

public class ClassPlaylistEditorAdapter extends AbsGraphAdapter {

	private ArrayList<String> mComponents = new ArrayList<>();

	public ClassPlaylistEditorAdapter(BaseActivity activity, int spacing, int childWidth, int maxHeight) {
		super(activity, spacing, childWidth, maxHeight);
	}

	public void setComponents(@NonNull ArrayList<String> components) {
		mComponents = components;
		notifyDataSetChanged();
	}

//	@Override
//	protected int getMaxHeight() {
//		//TODO
//	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new GraphItemHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_class_graph_component_with_music, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof GraphItemHolder)
			((GraphItemHolder) holder).buildItem();
	}

	@Override
	public int getItemCount() {
		return mComponents.size();
	}

	private class GraphItemHolder extends RecyclerView.ViewHolder {

		private View mBar;
		private View mMusicNode;
		private TextView mComponentName;

		public GraphItemHolder(View itemView) {
			super(itemView);

			mBar = itemView.findViewById(R.id.icgcwm_bar);
			mMusicNode = itemView.findViewById(R.id.icgcwm_track);

			mMusicNode.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Print.log("music node height", mMusicNode.getHeight());
				}

			});

			Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.class_component_background);
			drawable.setColorFilter(ContextCompat.getColor(mActivity, android.R.color.white), PorterDuff.Mode.MULTIPLY);

			mBar.setBackground(drawable);
//			mMusicNode.setBackground(drawable);

			mComponentName = (TextView) itemView.findViewById(R.id.icgc_component_name);

			Print.log("component width", mComponentWidth);

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mComponentWidth, ViewGroup.LayoutParams.MATCH_PARENT);
			itemView.setLayoutParams(params);

			Print.log("view created");
		}

		public void buildItem() {
			Print.log("build item called");
			String component = mComponents.get(getAdapterPosition());

			mComponentName.setText(component);
			mBar.getLayoutParams().height = mHeights[1];
			mBar.requestLayout();
		}

	}

}
