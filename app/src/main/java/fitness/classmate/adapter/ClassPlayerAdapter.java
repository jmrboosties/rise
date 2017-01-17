package fitness.classmate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.item.ClassPlayerItem;
import fitness.classmate.model.ClassComponent;
import fitness.classmate.model.ClassmateClass;
import fitness.classmate.model.ComponentNote;
import fitness.classmate.rx.ClassProgress;
import fitness.classmate.util.Helpbot;
import fitness.classmate.util.Print;
import fitness.classmate.view.ClassComponentView;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import java.util.ArrayList;

public class ClassPlayerAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;
	private ComponentViewHolder mActiveComponent;
	private NoteViewHolder mActiveNote;

	private PublishSubject<ClassProgress> mProgressSubject;

	private ClassmateClass mClassmateClass;
	private int mCurrentIndex = -1;

	private ArrayList<ClassPlayerItem> mVisibleItems = new ArrayList<>();

	public ClassPlayerAdapter(BaseActivity activity, ClassmateClass classmateClass) {
		mActivity = activity;
		mClassmateClass = classmateClass;

		mProgressSubject = PublishSubject.create();

		//TODO idea: big header banner as first item, so as you progress it scrolls up? idk

//		displayComponent(0);
	}

	private void displayComponent(int index) {
		mVisibleItems.clear();

		ClassComponent component = mClassmateClass.getComponents().get(index);

		ClassPlayerItem componentItem = new ClassPlayerItem();
		componentItem.setComponent(component);

		mVisibleItems.add(componentItem);

		//Display first note only to begin with
		if(component.getComponentNotes().size() > 0) {
			ClassPlayerItem noteItem = new ClassPlayerItem();
			noteItem.setNote(component.getComponentNotes().get(0));

			mVisibleItems.add(noteItem);
		}

//		for(ComponentNote note : component.getComponentNotes()) {
//			ClassPlayerItem noteItem = new ClassPlayerItem();
//			noteItem.setNote(note);
//
//			mVisibleItems.add(noteItem);
//		}

		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch(viewType) {
			case ClassPlayerItem.COMPONENT:
				return new ComponentViewHolder(new ClassComponentView(mActivity));
			case ClassPlayerItem.NOTE:
				return new NoteViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.listitem_player_note, parent, false));
			default:
				throw new RuntimeException("no view holder");
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof ComponentViewHolder)
			((ComponentViewHolder) holder).buildItem();
		else if(holder instanceof NoteViewHolder)
			((NoteViewHolder) holder).buildItem();
	}

	@Override
	public int getItemCount() {
		return mVisibleItems.size();
	}

	@Override
	public int getItemViewType(int position) {
		return mVisibleItems.get(position).getType();
	}

	public void subscribeToProgressSubject(PublishSubject<ClassProgress> progressSubject) {
		progressSubject.subscribe(mProgressSubject);
		mProgressSubject.subscribe(new Action1<ClassProgress>() {

			@Override
			public void call(ClassProgress progress) {
				if(mCurrentIndex != progress.componentIndex) {
					displayComponent(progress.componentIndex);
				}
				else {
					//TODO show the proper note
				}
			}

		});
	}

	private class ComponentViewHolder extends RecyclerView.ViewHolder {

		private ClassComponentView vClassComponentView;

		public ComponentViewHolder(View itemView) {
			super(itemView);

			vClassComponentView = (ClassComponentView) itemView;

			mProgressSubject.subscribe(new Action1<ClassProgress>() {

				@Override
				public void call(ClassProgress progress) {
					ClassComponent component = mVisibleItems.get(getAdapterPosition()).getComponent();
					vClassComponentView.setTimeLeft(Helpbot.getDurationTimestampFromMillis(component.getDuration() - progress.time));
				}

			});
		}

		public void buildItem() {
			vClassComponentView.loadComponent(mVisibleItems.get(getAdapterPosition()).getComponent());
		}

	}

	private class NoteViewHolder extends RecyclerView.ViewHolder {

		private TextView mDescription;
		private TextView mTime;
		private TextView mBottomLabel;
		private TextView mTopLabel;

		private static final float MIN_ALPHA = .25f;
		private static final int BEGIN_FADE_IN_T_MINUS = 15000;

		public NoteViewHolder(View itemView) {
			super(itemView);

			mDescription = (TextView) itemView.findViewById(R.id.lpn_description);
			mTime = (TextView) itemView.findViewById(R.id.lpn_timestamp);
			mBottomLabel = (TextView) itemView.findViewById(R.id.lpn_bottom_label);
			mTopLabel = (TextView) itemView.findViewById(R.id.lpn_top_label);

			mProgressSubject.subscribe(new Action1<ClassProgress>() {

				@Override
				public void call(ClassProgress progress) {
					ComponentNote note = mVisibleItems.get(getAdapterPosition()).getNote();

					if(note.getTimestamp() > progress.time) {
						long millisLeft = note.getTimestamp() - progress.time;
						mTime.setText(Helpbot.getDurationTimestampFromMillis(millisLeft));

						mTime.setVisibility(View.VISIBLE);
						mTopLabel.setVisibility(View.VISIBLE);
						mBottomLabel.setVisibility(View.VISIBLE);
					}
					else {
						//TODO missing duration in note which makes you just assume it goes until next note which is not the case, the "remaining" would go here

						mTime.setVisibility(View.GONE);
						mTopLabel.setVisibility(View.GONE);
						mBottomLabel.setVisibility(View.GONE);
					}

					setAlpha(note, progress.time);
				}

			});
		}

		private void setAlpha(ComponentNote note, long aLong) {
			if(note.getTimestamp() > aLong)
				itemView.setAlpha(MIN_ALPHA + (1f - MIN_ALPHA) * (1 - Math.min(1, (float) (note.getTimestamp() - aLong) / BEGIN_FADE_IN_T_MINUS)));
			else
				itemView.setAlpha(1f);
		}

		public void buildItem() {
			ComponentNote note = mVisibleItems.get(getAdapterPosition()).getNote();

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
