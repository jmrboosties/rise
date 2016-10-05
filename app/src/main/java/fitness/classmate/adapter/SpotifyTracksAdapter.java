package fitness.classmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.model.SpotifyAudioFeatures;
import fitness.classmate.model.SpotifyPlaylistTrack;
import fitness.classmate.util.Helpbot;
import fitness.classmate.util.Print;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class SpotifyTracksAdapter extends RecyclerView.Adapter {

	private Context mContext;
	private ArrayList<SpotifyPlaylistTrack> mSpotifyTracks = new ArrayList<>();

	private HashSet<Integer> mChangedPositions = new HashSet<>();

	private OnSpotifyPlaylistTrackClickListener mTrackClickListener;

	public SpotifyTracksAdapter(Context context) {
		mContext = context;
	}

	public void setSpotifyTracks(ArrayList<SpotifyPlaylistTrack> spotifyTracks) {
		mSpotifyTracks.clear();
		mSpotifyTracks.addAll(spotifyTracks);

		notifyDataSetChanged();
	}

	public void swapItems(int position1, int position2) {
		Print.log("swapping positions", position1, position2);

		Collections.swap(mSpotifyTracks, position1, position2);

		notifyItemMoved(position1, position2);

		mChangedPositions.add(position1);
		mChangedPositions.add(position2);
	}

	public void onSwapComplete() {
		for(Integer i : mChangedPositions)
			notifyItemChanged(i);

		mChangedPositions.clear();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new TrackViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_component, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof TrackViewHolder)
			((TrackViewHolder) holder).buildItem();
	}

	@Override
	public int getItemCount() {
		return mSpotifyTracks.size();
	}

	public void setTrackClickListener(OnSpotifyPlaylistTrackClickListener trackClickListener) {
		mTrackClickListener = trackClickListener;
	}

	public void itemChanged(SpotifyPlaylistTrack track) {
		notifyItemChanged(mSpotifyTracks.indexOf(track));
	}

	public class TrackViewHolder extends RecyclerView.ViewHolder {

		private TextView mTitle;
		private TextView mArtist;
		private TextView mNotesCount;
		private TextView mOrder;
		private TextView mDuration;
		private TextView mBpm;
		private ProgressBar mProgressBar;
		private View mBpmContainer;

		public TrackViewHolder(View itemView) {
			super(itemView);

			mTitle = (TextView) itemView.findViewById(R.id.lst_title);
			mArtist = (TextView) itemView.findViewById(R.id.lst_subtitle);
			mNotesCount = (TextView) itemView.findViewById(R.id.lst_notes_count);
			mOrder = (TextView) itemView.findViewById(R.id.lst_order);
			mDuration = (TextView) itemView.findViewById(R.id.lst_duration);
			mBpm = (TextView) itemView.findViewById(R.id.lst_pace);
			mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress);
			mBpmContainer = itemView.findViewById(R.id.lst_pace_container);

			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mTrackClickListener != null)
						mTrackClickListener.onSpotifyPlaylistTrackClick(mSpotifyTracks.get(getAdapterPosition()));
				}

			});

//			mProgressBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//				@Override
//				public void onGlobalLayout() {
//					mProgressBar.getLayoutParams().width = mBpmContainer.getWidth();
//					mProgressBar.getLayoutParams().height = mBpmContainer.getWidth();
//
//					Print.log("new params", mProgressBar.getLayoutParams().width, mProgressBar.getLayoutParams().height);
//
//					mProgressBar.requestLayout();
//
//					mProgressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//				}
//
//			});
		}

		public void buildItem() {
			mTitle.setText(mSpotifyTracks.get(getAdapterPosition()).getName());
			mArtist.setText(mSpotifyTracks.get(getAdapterPosition()).getArtist());
			mNotesCount.setText(mContext.getString(R.string.notes_count, mSpotifyTracks.get(getAdapterPosition()).getClassNotes().size()));
			mOrder.setText(String.valueOf(getAdapterPosition() + 1));
			mDuration.setText(Helpbot.getDurationTimestampFromMillis(mSpotifyTracks.get(getAdapterPosition()).getDuration()));

			SpotifyAudioFeatures audioFeatures = mSpotifyTracks.get(getAdapterPosition()).getAudioFeatures();
			if(audioFeatures != null) {
				mBpm.setText(String.valueOf(Math.round(audioFeatures.getBpm())));

				mProgressBar.setVisibility(View.GONE);
				mBpmContainer.setVisibility(View.VISIBLE);
			}
			else {
				mProgressBar.setVisibility(View.VISIBLE);
				mBpmContainer.setVisibility(View.GONE);
			}

//			Kahlo.with(mContext).matrixCenterTop().source(mSpotifyTracks.get(getAdapterPosition()).getImageUrl());
		}

	}

	public interface OnSpotifyPlaylistTrackClickListener {

		void onSpotifyPlaylistTrackClick(SpotifyPlaylistTrack track);

	}

}
