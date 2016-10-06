package fitness.classmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.item.NoteEditorItem;
import fitness.classmate.model.ClassmateClass;
import fitness.classmate.model.ClassmateClassComponent;
import fitness.classmate.model.ComponentNote;
import fitness.classmate.model.SpotifyAudioFeatures;
import fitness.classmate.util.Helpbot;
import fitness.classmate.util.Print;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ClassmateNoteEditorAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
	private ClassmateClass mClassmateClass;

	private ArrayList<NoteEditorItem> mItems = new ArrayList<>();

	private HashSet<Integer> mChangedPositions = new HashSet<>();

	private ClassmateClassComponent mSelectedComponent;

	private AdapterClickListener mAdapterClickListener;

	public ClassmateNoteEditorAdapter(BaseActivity activity) {
		mActivity = activity;
	}

	public void setClassmateClass(@NonNull ClassmateClass classmateClass) {
		mClassmateClass = classmateClass;

		for(ClassmateClassComponent component : mClassmateClass.getComponents()) {
			NoteEditorItem item = new NoteEditorItem();
			item.setType(NoteEditorItem.COMPONENT);
			item.setComponent(component);

			mItems.add(item);
		}

		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch(viewType) {
			case NoteEditorItem.COMPONENT :
				return new ComponentItemHolder(LayoutInflater.from(mActivity).inflate(R.layout.listitem_component, parent, false));
			case NoteEditorItem.NOTE :
				return new NoteItemHolder(LayoutInflater.from(mActivity).inflate(R.layout.listitem_component_note, parent, false));
			case NoteEditorItem.ADD_NOTE_BUTTON :
				return new AddButtonViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.listitem_create_note, parent, false));
			default:
				throw new IllegalArgumentException("view type does not have view holder");
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof ComponentItemHolder)
			((ComponentItemHolder) holder).buildItem();
		else if(holder instanceof NoteItemHolder)
			((NoteItemHolder) holder).buildItem();
	}

	@Override
	public int getItemViewType(int position) {
		return mItems.get(position).getType();
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	private void hideNotes() {
		ArrayList<Integer> toRemoveNotify = new ArrayList<>();
		ArrayList<NoteEditorItem> toRemove = new ArrayList<>();
		for(NoteEditorItem item : mItems) {
			if(item.getType() != NoteEditorItem.COMPONENT) {
				toRemove.add(item);
				toRemoveNotify.add(mItems.indexOf(item));
			}
		}

		mSelectedComponent = null;
		mItems.removeAll(toRemove);

		for(int i : toRemoveNotify)
			notifyItemRemoved(i);
	}

	public void swapItems(int fromPosition, int toPosition) {
		Print.log("swapping positions", fromPosition, toPosition);
		hideNotes();

		Collections.swap(mItems, fromPosition, toPosition);

		//Swap the class as well
		Collections.swap(mClassmateClass.getComponents(), getComponentIndex(fromPosition), getComponentIndex(toPosition));

		notifyItemMoved(fromPosition, toPosition);

		mChangedPositions.add(fromPosition);
		mChangedPositions.add(toPosition);
	}

	public void onSwapComplete() {
		for(Integer i : mChangedPositions)
			notifyItemChanged(i);

		mChangedPositions.clear();
	}

	public void itemChanged(ClassmateClassComponent component) {
		for(int i = 0; i < mItems.size(); i++) {
			if(mItems.get(i).getType() == NoteEditorItem.COMPONENT && mItems.get(i).getComponent().equals(component)) {
				notifyItemChanged(i);
				return;
			}
		}
	}

	private int getComponentIndex(int adapterPosition) {
		ClassmateClassComponent component = mItems.get(adapterPosition).getComponent();
		return getComponentIndex(component);
	}

	private int getComponentIndex(ClassmateClassComponent component) {
		return mClassmateClass.getComponents().indexOf(component);
	}

	public void setAdapterClickListener(AdapterClickListener adapterClickListener) {
		mAdapterClickListener = adapterClickListener;
	}

	public ClassmateClassComponent getSelectedComponent() {
		return mSelectedComponent;
	}

	private class ComponentItemHolder extends RecyclerView.ViewHolder {

		private TextView mComponent;
		private TextView mTrackTitle;
		private TextView mBpm;
		private TextView mOrder;
		private TextView mDuration;
		private TextView mPace;

		public ComponentItemHolder(View itemView) {
			super(itemView);

			mComponent = (TextView) itemView.findViewById(R.id.lst_title);
			mTrackTitle = (TextView) itemView.findViewById(R.id.lst_subtitle);
			mBpm = (TextView) itemView.findViewById(R.id.lst_notes_count);
			mOrder = (TextView) itemView.findViewById(R.id.lst_order);
			mDuration = (TextView) itemView.findViewById(R.id.lst_duration);
			mPace = (TextView) itemView.findViewById(R.id.lst_pace);

			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showNotes(getAdapterPosition());

					if(mAdapterClickListener != null)
						mAdapterClickListener.onComponentSelected(mSelectedComponent);
				}

			});
		}

		public void buildItem() {
			mComponent.setText(mItems.get(getAdapterPosition()).getComponent().getName());
			mTrackTitle.setText(mItems.get(getAdapterPosition()).getComponent().getComponentTrack().getSpotifyPlaylistTrack().getName());
			mPace.setText(String.valueOf(mItems.get(getAdapterPosition()).getComponent().getIntensity()));
			mOrder.setText(String.valueOf(mClassmateClass.getComponents().indexOf(mItems.get(getAdapterPosition()).getComponent()) + 1));
			mDuration.setText(Helpbot.getDurationTimestampFromMillis(mItems.get(getAdapterPosition()).getComponent().getComponentTrack().getSpotifyPlaylistTrack().getDuration())); //TODO this will need to change

			SpotifyAudioFeatures audioFeatures = mItems.get(getAdapterPosition()).getComponent().getComponentTrack().getSpotifyPlaylistTrack().getAudioFeatures();
			if(audioFeatures != null) {
				mBpm.setText(mActivity.getString(R.string.bpm_count, Math.round(audioFeatures.getBpm())));
				mBpm.setVisibility(View.VISIBLE);
			}
			else
				mBpm.setVisibility(View.INVISIBLE);
		}

	}

	public void reloadNotes() {
		for(NoteEditorItem item : mItems) {
			if(item.getType() == NoteEditorItem.COMPONENT && item.getComponent() == mSelectedComponent) {
				showNotes(mItems.indexOf(item));
				return;
			}
		}
	}

	private void showNotes(int position) {
		//Get the selected component
		ClassmateClassComponent component = mItems.get(position).getComponent();

//		//If the next item is a note, just hide notes and do nothing else
//		if(mItems.get(position + 1).getType() == NoteEditorItem.NOTE) {
//			//Hide existing notes
//			hideNotes();
//		}

		//TODO figure this out, we need to find a way to cleanly add notes. its complicated list logic that i dont want to waste time on now
//		if(mSelectedComponent == component) {
//			//If it's the same component do nothing
//			return;
//		}
//		else {
			//Hide existing notes
			hideNotes();

			//Set the selected component here, don't do it above because hideNotes clears it
			mSelectedComponent = component;

			//Get the component based position
			position = getComponentIndex(component);

			ArrayList<NoteEditorItem> notes = new ArrayList<>();
			for(ComponentNote note : component.getComponentNotes()) {
				NoteEditorItem item = new NoteEditorItem();
				item.setType(NoteEditorItem.NOTE);
				item.setComponentNote(note);

				notes.add(item);
			}

			//Add the "add new" button
			NoteEditorItem button = new NoteEditorItem();
			button.setType(NoteEditorItem.ADD_NOTE_BUTTON);
			notes.add(button);

			mItems.addAll(position + 1, notes);

//			for(int i = position + 1; i < position + 1 + notes.size(); i++) {
//				notifyItemInserted(i);
//				Print.log("note inserted, should se this " + notes.size() + " times");
//			}

			//TODO notify correctly, its a bit tricky
			notifyDataSetChanged();
//		}
	}

	private class NoteItemHolder extends RecyclerView.ViewHolder {

		private TextView mDescription;
		private TextView mTimestamp;

		public NoteItemHolder(View itemView) {
			super(itemView);

			mDescription = (TextView) itemView.findViewById(R.id.lcn_description);
			mTimestamp = (TextView) itemView.findViewById(R.id.lcn_timestamp);
		}

		public void buildItem() {
			mDescription.setText(mItems.get(getAdapterPosition()).getComponentNote().getMessage());
			mTimestamp.setText(":30"); //TODO
		}

	}

	private class AddButtonViewHolder extends RecyclerView.ViewHolder {

		public AddButtonViewHolder(View itemView) {
			super(itemView);

			Button button = (Button) itemView.findViewById(R.id.lcn_add_note_button);
			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAdapterClickListener != null)
						mAdapterClickListener.onNewNoteClicked(mSelectedComponent);
				}

			});
		}

	}

	public interface AdapterClickListener {

		void onNewNoteClicked(ClassmateClassComponent component);

		void onComponentSelected(ClassmateClassComponent component);

	}

}
