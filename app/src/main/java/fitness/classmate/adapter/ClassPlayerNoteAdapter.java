package fitness.classmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.model.ComponentNote;
import fitness.classmate.util.Helpbot;
import fitness.classmate.util.Print;

import java.util.ArrayList;

public class ClassPlayerNoteAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
	private ArrayList<ComponentNote> mNotes = new ArrayList<>();

	private NoteViewHolder mActiveNote;
	private NoteViewHolder mNextNote;

	public ClassPlayerNoteAdapter(BaseActivity activity) {
		mActivity = activity;
	}

	public void setNotes(@NonNull ArrayList<ComponentNote> notes) {
		mNotes = notes;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new NoteViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.listitem_player_note, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof NoteViewHolder)
			((NoteViewHolder) holder).buildItem();
	}

	@Override
	public int getItemCount() {
		return mNotes.size();
	}

	private class NoteViewHolder extends RecyclerView.ViewHolder {

		private TextView mDescription;
		private TextView mTime;
		private TextView mBottomLabel;
		private TextView mTopLabel;

		public NoteViewHolder(View itemView) {
			super(itemView);

			mDescription = (TextView) itemView.findViewById(R.id.lpn_description);
			mTime = (TextView) itemView.findViewById(R.id.lpn_timestamp);
			mBottomLabel = (TextView) itemView.findViewById(R.id.lpn_bottom_label);
			mTopLabel = (TextView) itemView.findViewById(R.id.lpn_top_label);
		}

		public void buildItem() {
			ComponentNote note = mNotes.get(getAdapterPosition());

			mDescription.setText(note.getMessage());

			if(mActiveNote == null)
				showNormalNote(note);
			else {
				int displacementFromActive = getAdapterPosition() - mActiveNote.getAdapterPosition();

				if(displacementFromActive == 0) {
					//Means it's the active note, bring up bottom label
					mBottomLabel.setVisibility(View.VISIBLE);
					mBottomLabel.setText(R.string.to_go);

					mTopLabel.setVisibility(View.GONE);

					//Time value handled by rx
				}
				else if(displacementFromActive == 1) {
					//Means this is next up
					mTopLabel.setVisibility(View.VISIBLE);
					mTopLabel.setText(R.string.starts_in);

					mBottomLabel.setVisibility(View.GONE);

					//Time value handled by rx
				}
				else if(displacementFromActive > 0)
					showNormalNote(note);
				else {
					//Note is before, this shouldn't happen
					Print.exception(new Exception("note built is before active note, shouldn't happen"));
				}
			}

		}

		private void showNormalNote(ComponentNote note) {
			//Means it's further in the future
			mTopLabel.setVisibility(View.VISIBLE);
			mTopLabel.setText(R.string.starts_in);

			mBottomLabel.setVisibility(View.GONE);

			//Here we handle time ourselves
			mTime.setText(Helpbot.getDurationTimestampFromMillis(note.getTimestamp()));
		}

		public void setTime(String time) {
			mTime.setText(time);
		}

	}



}
