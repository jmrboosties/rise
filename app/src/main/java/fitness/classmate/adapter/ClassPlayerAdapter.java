package fitness.classmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.item.ClassPlayerItem;
import fitness.classmate.model.ClassComponent;

import java.util.ArrayList;

public class ClassPlayerAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
	private ComponentViewHolder mActiveComponent;
	private NoteViewHolder mActiveNote;

	private ArrayList<ClassPlayerItem> mItems = new ArrayList<>();

	public ClassPlayerAdapter(BaseActivity activity) {
		mActivity = activity;
	}

	public void setItems(@NonNull ArrayList<ClassComponent> components) {
		mItems.clear();

		for(ClassComponent classComponent : components) {
			ClassPlayerItem item = new ClassPlayerItem();
			item.setType(ClassPlayerItem.COMPONENT);
			item.setComponent(classComponent);

			mItems.add(item);
		}

		//Don't notify data set changed!
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	private class ComponentViewHolder extends RecyclerView.ViewHolder {

		public ComponentViewHolder(View itemView) {
			super(itemView);
		}

		public void buildItem() {

		}

	}

	private class NoteViewHolder extends RecyclerView.ViewHolder {

		public NoteViewHolder(View itemView) {
			super(itemView);
		}

		public void buildItem() {

		}

	}

}
