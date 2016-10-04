package fitness.classmate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.item.NoteEditorItem;
import fitness.classmate.model.ClassmateClass;

import java.util.ArrayList;

public class ClassmateNoteEditorAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;

	private ArrayList<NoteEditorItem> mItems = new ArrayList<>();

	public ClassmateNoteEditorAdapter(BaseActivity activity, ClassmateClass classmateClass) {
		mActivity = activity;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch(viewType) {
			case NoteEditorItem.COMPONENT :
				return new ComponentItemHolder(LayoutInflater.from(mActivity).inflate(R.layout.listitem_spotify_track, parent, false));
			case NoteEditorItem.NOTE :

				break;
			case NoteEditorItem.ADD_NOTE_BUTTON :

				break;
			default:
				throw new IllegalArgumentException("view type does not have view holder");
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemViewType(int position) {
		return mItems.get(position).getType();
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	private class ComponentItemHolder extends RecyclerView.ViewHolder {

		public ComponentItemHolder(View itemView) {
			super(itemView);
		}

		public void buildItem() {

		}

	}

	private class NoteItemHolder extends RecyclerView.ViewHolder {

		public NoteItemHolder(View itemView) {
			super(itemView);
		}

		public void buildItem() {

		}

	}

}
