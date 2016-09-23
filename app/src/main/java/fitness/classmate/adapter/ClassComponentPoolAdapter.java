package fitness.classmate.adapter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;

import fitness.classmate.base.BaseActivity;
import fitness.classmate.R;

public class ClassComponentPoolAdapter extends RecyclerView.Adapter<ClassComponentPoolAdapter.ComponentViewHolder> {

	private BaseActivity mActivity;
	private ArrayList<String> mItems = new ArrayList<>(); //TODO change to real items
	private OnComponentWidthCalculatedCallback mWidthCalculatedCallback;

	public ClassComponentPoolAdapter(BaseActivity activity) {
		mActivity = activity;
	}

	public void setItems(@NonNull ArrayList<String> items) {
		mItems = items;
		notifyDataSetChanged();
	}

	@Override
	public ComponentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ComponentViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_class_component, parent, false));
	}

	@Override
	public void onBindViewHolder(ComponentViewHolder holder, int position) {
		holder.buildItem();
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	public void setWidthCalculatedCallback(OnComponentWidthCalculatedCallback widthCalculatedCallback) {
		mWidthCalculatedCallback = widthCalculatedCallback;
	}

	class ComponentViewHolder extends RecyclerView.ViewHolder {

		private TextView mTextView;

		public ComponentViewHolder(View itemView) {
			super(itemView);
			mTextView = (TextView) itemView;

			mTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {
					mTextView.setWidth(mTextView.getHeight() * 2);

					if(mWidthCalculatedCallback != null)
						mWidthCalculatedCallback.onComponentWidthCalculated(mTextView.getHeight() * 2);

					mTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}

			});

			mTextView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
					ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

					View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

					//Deprecated in 24, leave it for us
					//noinspection deprecation
					v.startDrag(dragData,  // the data to be dragged
							myShadow,  // the drag shadow builder
							v, // this should pass the dragged view right on in
							0  // flags (not currently used, set to 0)
					);

					return true;
				}

			});
		}

		public void buildItem() {
			mTextView.setText(mItems.get(getAdapterPosition()));
			mTextView.setTag(mItems.get(getAdapterPosition()));
		}

	}

	public interface OnComponentWidthCalculatedCallback {

		void onComponentWidthCalculated(int width);

	}

}
