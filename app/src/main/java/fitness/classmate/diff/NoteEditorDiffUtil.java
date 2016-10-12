package fitness.classmate.diff;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

import fitness.classmate.item.NoteEditorItem;
import fitness.classmate.util.Print;

public class NoteEditorDiffUtil extends DiffUtil.Callback {

	private ArrayList<NoteEditorItem> mOldItems;
	private ArrayList<NoteEditorItem> mNewItems;

	public NoteEditorDiffUtil(ArrayList<NoteEditorItem> oldItems, ArrayList<NoteEditorItem> newItems) {
		mOldItems = oldItems;
		mNewItems = newItems;
	}

	@Override
	public int getOldListSize() {
		return mOldItems.size();
	}

	@Override
	public int getNewListSize() {
		return mNewItems.size();
	}

	@Override
	public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
		boolean same = mOldItems.get(oldItemPosition).equals(mNewItems.get(newItemPosition));
		Print.log("items same?", same);
		return same;
	}

	@Override
	public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
		boolean same = mOldItems.get(oldItemPosition).equals(mNewItems.get(newItemPosition));
		Print.log("contents same?", same);
		return same;
	}

	@Nullable
	@Override
	public Object getChangePayload(int oldItemPosition, int newItemPosition) {
		return super.getChangePayload(oldItemPosition, newItemPosition);
	}
}
