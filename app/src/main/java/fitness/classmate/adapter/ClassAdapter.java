package fitness.classmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.model.ClassmateClass;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassItemViewHolder> {

	private BaseActivity mActivity;

	private ArrayList<ClassmateClass> mClasses = new ArrayList<>();

	private View.OnClickListener mOnClickListener;

	public ClassAdapter(BaseActivity activity) {
		mActivity = activity;
	}

	public void setClasses(@NonNull ArrayList<ClassmateClass> classes) {
		mClasses = classes;
		notifyDataSetChanged();
	}

	@Override
	public ClassItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ClassItemViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.listitem_class, parent, false));
	}

	@Override
	public void onBindViewHolder(ClassItemViewHolder holder, int position) {
		holder.buildItem();
	}

	@Override
	public int getItemCount() {
		return mClasses.size();
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		mOnClickListener = onClickListener;
	}

	public class ClassItemViewHolder extends RecyclerView.ViewHolder {

		private TextView mTitle;
		private TextView mAuthor;
		private TextView mCreatedAt;
		private TextView mDuration;

		public ClassItemViewHolder(View itemView) {
			super(itemView);

			mTitle = (TextView) itemView.findViewById(R.id.lc_title);
			mAuthor = (TextView) itemView.findViewById(R.id.lc_author);
			mCreatedAt = (TextView) itemView.findViewById(R.id.lc_created_at);
			mDuration = (TextView) itemView.findViewById(R.id.lc_time);

			itemView.setOnClickListener(mOnClickListener);
		}

		public void buildItem() {
			ClassmateClass classmateClass = mClasses.get(getAdapterPosition());

			mTitle.setText(classmateClass.getTitle());
			mAuthor.setText(classmateClass.getAuthor());
			mCreatedAt.setText(classmateClass.getCreatedAt());
			mDuration.setText(classmateClass.getDuration());
		}

	}

}
